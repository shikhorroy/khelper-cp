package util.io

import java.io.InputStream

class InputReader(
    private val input: InputStream,
    bufSize: Int = 1 shl 16 // 64 KiB
) {
    //~ One buffer only. A BufferedInputStream on top would copy every byte twice.
    private val buffer = ByteArray(bufSize)
    private var len = 0
    private var ptr = 0
    //~ Reused scratch space for next(), so a token costs no StringBuilder.
    private var tok = ByteArray(64)
    //~ The byte that ended the last token. nextLine() uses it to finish that line first.
    private var lastEnd = '\n'.code

    private fun read(): Int {
        if (ptr == len) {
            len = input.read(buffer, 0, buffer.size)
            ptr = 0
            if (len <= 0) {
                len = 0
                return -1
            }
        }
        // `and 0xFF`: a non-ASCII byte must not look like EOF (-1)
        return buffer[ptr++].toInt() and 0xFF
    }

    /**
     * When the last token ended inside a line, eats the trailing spaces, tabs and CR of that line and
     * its line break, so [nextLine] starts on the next line even with trailing spaces.
     * Stops before the next non-blank byte. Done here, not after each token, to keep tokens fast.
     */
    private fun finishTokenLine() {
        if (lastEnd == '\n'.code || lastEnd == -1) return
        lastEnd = '\n'.code
        var c = read()
        while (c == ' '.code || c == '\t'.code || c == '\r'.code) c = read()
        if (c != '\n'.code && c != -1) ptr-- // safe: read() just returned a byte
    }

    private fun skipBlanks(): Int {
        var c = read()
        while (c in 0..32) c = read()
        return c
    }

    fun hasNext(): Boolean {
        val c = skipBlanks()
        if (c < 0) return false
        ptr-- // safe: read() just returned a byte, so ptr >= 1
        return true
    }

    fun next(): String {
        var c = skipBlanks()
        var n = 0
        while (c > 32) {
            if (n == tok.size) tok = tok.copyOf(n shl 1)
            tok[n++] = c.toByte()
            c = read()
        }
        lastEnd = c
        return String(tok, 0, n, Charsets.UTF_8)
    }

    fun nextInt(): Int {
        var c = skipBlanks()
        val neg = c == '-'.code
        if (neg || c == '+'.code) c = read()
        // accumulate as a negative number, so Int.MIN_VALUE also fits
        var res = 0
        while (c >= '0'.code) {
            res = res * 10 - (c - '0'.code)
            c = read()
        }
        lastEnd = c
        return if (neg) res else -res
    }

    fun nextLong(): Long {
        var c = skipBlanks()
        val neg = c == '-'.code
        if (neg || c == '+'.code) c = read()
        var res = 0L
        while (c >= '0'.code) {
            res = res * 10 - (c - '0'.code)
            c = read()
        }
        lastEnd = c
        return if (neg) res else -res
    }

    fun nextDouble(): Double = next().toDouble()

    fun nextIntArray(n: Int): IntArray = IntArray(n) { nextInt() }
    fun nextLongArray(n: Int): LongArray = LongArray(n) { nextLong() }

    /**
     * Reads the rest of the current line, without the line break (`\n` or `\r\n`).
     * A token read eats the line break that ends it, so after [nextInt] at the end of a line,
     * this returns the whole next line. After a token in the middle of a line, it returns the rest.
     */
    fun nextLine(): String {
        finishTokenLine()
        var c = read()
        var n = 0
        while (c >= 0 && c != '\n'.code) {
            if (c != '\r'.code) {
                if (n == tok.size) tok = tok.copyOf(n shl 1)
                tok[n++] = c.toByte()
            }
            c = read()
        }
        return String(tok, 0, n, Charsets.UTF_8)
    }
}
