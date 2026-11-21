package util.io

import java.io.BufferedOutputStream
import java.io.OutputStream

class OutputWriter(
    out: OutputStream,
    bufSize: Int = 1 shl 16 // 64 KiB
) : AutoCloseable {
    private val bos = BufferedOutputStream(out, bufSize)
    private val tmp = ByteArray(20) // for numbers

    fun flush() = bos.flush()
    override fun close() = bos.close()

    private fun writeByte(b: Int) = bos.write(b)
    private fun writeBytes(bytes: ByteArray, off: Int = 0, len: Int = bytes.size) = bos.write(bytes, off, len)

    private fun writeString(s: String) {
        // bulk write UTF-8 bytes
        val bytes = s.toByteArray(Charsets.UTF_8)
        writeBytes(bytes)
    }

    private fun writeChar(c: Char) {
        // fast path for ASCII
        if (c.code < 128) {
            writeByte(c.code)
        } else {
            writeString(c.toString())
        }
    }

    private fun writeInt(x: Int) {
        var n = x
        var i = 0
        if (n == 0) {
            writeByte('0'.code); return
        }
        if (n < 0) {
            writeByte('-'.code)
            if (n == Int.MIN_VALUE) {
                writeString("-2147483648"); return
            }
            n = -n
        }
        while (n > 0) {
            val q = n / 10
            tmp[i++] = ((n - q * 10) + '0'.code).toByte()
            n = q
        }
        for (j in i - 1 downTo 0) writeByte(tmp[j].toInt())
    }

    private fun writeLong(x: Long) {
        var n = x
        var i = 0
        if (n == 0L) {
            writeByte('0'.code); return
        }
        if (n < 0L) {
            writeByte('-'.code)
            if (n == Long.MIN_VALUE) {
                writeString("-9223372036854775808"); return
            }
            n = -n
        }
        while (n > 0L) {
            val q = n / 10L
            tmp[i++] = ((n - q * 10L).toInt() + '0'.code).toByte()
            n = q
        }
        for (j in i - 1 downTo 0) writeByte(tmp[j].toInt())
    }

    fun print(vararg objects: Any?) {
        for (idx in objects.indices) {
            if (idx != 0) writeByte(' '.code)
            when (val o = objects[idx]) {
                null -> writeString("null")
                is String -> writeString(o)
                is Char -> writeChar(o)
                is Int -> writeInt(o)
                is Long -> writeLong(o)
                is Boolean -> writeString(o.toString())
                else -> writeString(o.toString())
            }
        }
    }

    fun printLine(vararg objects: Any?) {
        print(*objects)
        writeByte('\n'.code)
    }
}
