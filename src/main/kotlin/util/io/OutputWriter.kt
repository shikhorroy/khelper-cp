package util.io

import java.io.OutputStream

class OutputWriter(
    private val out: OutputStream,
    bufSize: Int = 1 shl 16 // 64 KiB
) : AutoCloseable {
    //~ Own buffer: no lock and no method call per byte, unlike BufferedOutputStream.write(int)
    private val buf = ByteArray(maxOf(bufSize, 64)) // must hold the longest number (20 bytes)
    private var pos = 0

    fun flush() {
        if (pos > 0) {
            out.write(buf, 0, pos)
            pos = 0
        }
        out.flush()
    }

    override fun close() = flush()

    private fun ensure(n: Int) {
        if (pos + n > buf.size) {
            out.write(buf, 0, pos)
            pos = 0
        }
    }

    private fun writeByte(b: Int) {
        if (pos == buf.size) {
            out.write(buf, 0, pos)
            pos = 0
        }
        buf[pos++] = b.toByte()
    }

    private fun writeString(s: String) {
        val n = s.length
        var i = 0
        // ASCII fast path: copy chars straight into the buffer, no byte array
        while (i < n) {
            val c = s[i].code
            if (c >= 128) {
                writeUtf8(s.substring(i))
                return
            }
            if (pos == buf.size) {
                out.write(buf, 0, pos)
                pos = 0
            }
            buf[pos++] = c.toByte()
            i++
        }
    }

    private fun writeUtf8(s: String) {
        val bytes = s.toByteArray(Charsets.UTF_8)
        if (bytes.size > buf.size) {
            out.write(buf, 0, pos)
            pos = 0
            out.write(bytes)
            return
        }
        ensure(bytes.size)
        System.arraycopy(bytes, 0, buf, pos, bytes.size)
        pos += bytes.size
    }

    private fun writeChar(c: Char) {
        if (c.code < 128) writeByte(c.code) else writeUtf8(c.toString())
    }

    private fun writeInt(x: Int) = writeLong(x.toLong())

    private fun writeLong(x: Long) {
        ensure(20) // max length of a Long with sign
        if (x == 0L) {
            buf[pos++] = '0'.code.toByte(); return
        }
        // work with the negative value, so Long.MIN_VALUE needs no special case
        var n = if (x < 0) x else -x
        if (x < 0) buf[pos++] = '-'.code.toByte()
        // write digits backwards, then reverse them in place
        val start = pos
        while (n != 0L) {
            val q = n / 10
            buf[pos++] = ('0'.code - (n - q * 10).toInt()).toByte()
            n = q
        }
        var l = start
        var r = pos - 1
        while (l < r) {
            val t = buf[l]; buf[l++] = buf[r]; buf[r--] = t
        }
    }

    // Typed overloads: no vararg array and no boxing on the hot path.
    fun print(x: Int) = writeInt(x)
    fun print(x: Long) = writeLong(x)
    fun print(x: Char) = writeChar(x)
    fun print(x: String) = writeString(x)
    fun print(x: Boolean) = writeString(if (x) "true" else "false")

    fun printLine() = writeByte('\n'.code)
    fun printLine(x: Int) { writeInt(x); writeByte('\n'.code) }
    fun printLine(x: Long) { writeLong(x); writeByte('\n'.code) }
    fun printLine(x: Char) { writeChar(x); writeByte('\n'.code) }
    fun printLine(x: String) { writeString(x); writeByte('\n'.code) }
    fun printLine(x: Boolean) { print(x); writeByte('\n'.code) }

    /** Prints the array on one line, values split by [sep]. */
    fun printLine(a: IntArray, sep: Char = ' ') {
        for (i in a.indices) {
            if (i != 0) writeByte(sep.code)
            writeInt(a[i])
        }
        writeByte('\n'.code)
    }

    fun printLine(a: LongArray, sep: Char = ' ') {
        for (i in a.indices) {
            if (i != 0) writeByte(sep.code)
            writeLong(a[i])
        }
        writeByte('\n'.code)
    }

    /** General form, values split by a space. Slower: it boxes and builds an array. */
    fun print(vararg objects: Any?) {
        for (idx in objects.indices) {
            if (idx != 0) writeByte(' '.code)
            when (val o = objects[idx]) {
                null -> writeString("null")
                is String -> writeString(o)
                is Char -> writeChar(o)
                is Int -> writeInt(o)
                is Long -> writeLong(o)
                else -> writeString(o.toString())
            }
        }
    }

    fun printLine(vararg objects: Any?) {
        print(*objects)
        writeByte('\n'.code)
    }
}
