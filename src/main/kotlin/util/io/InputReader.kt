package util.io

import java.io.BufferedInputStream
import java.io.InputStream

class InputReader(
    input: InputStream,
    bufSize: Int = 1 shl 16 // 64 KiB; for small size input 1 shl 13 = 8 KiB
) {
    private val inStream = BufferedInputStream(input, bufSize)
    private val buffer = ByteArray(bufSize)
    private var len = 0
    private var ptr = 0

    private fun read(): Int {
        if (ptr >= len) {
            len = inStream.read(buffer)
            ptr = 0
            if (len <= 0) return -1
        }
        return buffer[ptr++].toInt()
    }

    private fun skipBlanks(): Int {
        var c = read()
        while (c in 0..32) c = read()
        return c
    }

    fun hasNext(): Boolean {
        var c = read()
        while (c in 0..32) c = read()
        if (c < 0) return false
        ptr--
        return true
    }

    fun next(): String {
        val sb = StringBuilder()
        var c = skipBlanks()
        while (c > 32) {
            sb.append(c.toChar())
            c = read()
        }
        return sb.toString()
    }

    fun nextInt(): Int {
        var c = skipBlanks()
        var sgn = 1
        if (c == '-'.code) {
            sgn = -1; c = read()
        }
        var res = 0
        while (c > 32) {
            res = res * 10 + (c - '0'.code)
            c = read()
        }
        return res * sgn
    }

    fun nextLong(): Long {
        var c = skipBlanks()
        var sgn = 1
        if (c == '-'.code) {
            sgn = -1; c = read()
        }
        var res = 0L
        while (c > 32) {
            res = res * 10 + (c - '0'.code)
            c = read()
        }
        return if (sgn == 1) res else -res
    }

    fun nextDouble(): Double = next().toDouble()
    fun nextLine(): String {
        val sb = StringBuilder()
        var c = read()
        if (c == '\n'.code) c = read()
        while (c >= 0 && c != '\n'.code) {
            if (c != '\r'.code) sb.append(c.toChar())
            c = read()
        }
        return sb.toString()
    }
}