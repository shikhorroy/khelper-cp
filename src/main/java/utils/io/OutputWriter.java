package utils.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Fast writer for competitive programming. Writes bytes into its own buffer, with no lock per call.
 * Use the typed overloads ({@code printLine(int)} and so on) in hot loops: they do not box.
 * {@link #close()} flushes but does not close the stream, so System.out stays usable.
 */
public class OutputWriter implements AutoCloseable {

    private final OutputStream out;
    //~ At least 64 bytes, so the longest number (20 bytes) always fits after one flush.
    private final byte[] buf;
    private int pos;

    public OutputWriter(OutputStream out) {
        this(out, 1 << 16);
    }

    public OutputWriter(OutputStream out, int bufSize) {
        this.out = out;
        this.buf = new byte[Math.max(bufSize, 64)];
    }

    private void drain() {
        try {
            out.write(buf, 0, pos);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        pos = 0;
    }

    private void ensure(int n) {
        if (pos + n > buf.length) drain();
    }

    private void writeByte(int b) {
        if (pos == buf.length) drain();
        buf[pos++] = (byte) b;
    }

    private void writeString(String s) {
        for (int i = 0, n = s.length(); i < n; i++) {
            char c = s.charAt(i);
            if (c >= 128) {
                writeUtf8(s.substring(i));
                return;
            }
            writeByte(c);
        }
    }

    private void writeUtf8(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > buf.length) {
            drain();
            try {
                out.write(bytes);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return;
        }
        ensure(bytes.length);
        System.arraycopy(bytes, 0, buf, pos, bytes.length);
        pos += bytes.length;
    }

    private void writeLong(long x) {
        ensure(20); // longest Long with sign
        if (x == 0) {
            buf[pos++] = '0';
            return;
        }
        // work with the negative value, so Long.MIN_VALUE needs no special case
        long n = x < 0 ? x : -x;
        if (x < 0) buf[pos++] = '-';
        // write digits backwards, then reverse them in place
        int start = pos;
        while (n != 0) {
            long q = n / 10;
            buf[pos++] = (byte) ('0' - (n - q * 10));
            n = q;
        }
        for (int l = start, r = pos - 1; l < r; l++, r--) {
            byte t = buf[l];
            buf[l] = buf[r];
            buf[r] = t;
        }
    }

    private void writeObject(Object o) {
        if (o instanceof Integer i) writeLong(i);
        else if (o instanceof Long l) writeLong(l);
        else if (o instanceof Character c) print((char) c);
        else if (o instanceof int[] a) printArray(a);
        else if (o instanceof long[] a) printArray(a);
        else writeString(String.valueOf(o));
    }

    private void printArray(int[] a) {
        for (int i = 0; i < a.length; i++) {
            if (i != 0) writeByte(' ');
            writeLong(a[i]);
        }
    }

    private void printArray(long[] a) {
        for (int i = 0; i < a.length; i++) {
            if (i != 0) writeByte(' ');
            writeLong(a[i]);
        }
    }

    public void print(int x) {
        writeLong(x);
    }

    public void print(long x) {
        writeLong(x);
    }

    public void print(char c) {
        if (c < 128) writeByte(c);
        else writeUtf8(String.valueOf(c));
    }

    public void print(String s) {
        writeString(s);
    }

    /** Values split by a space. int[] and long[] print as space-separated values. */
    public void print(Object... objects) {
        for (int i = 0; i < objects.length; i++) {
            if (i != 0) writeByte(' ');
            writeObject(objects[i]);
        }
    }

    public void printLine() {
        writeByte('\n');
    }

    public void printLine(int x) {
        writeLong(x);
        writeByte('\n');
    }

    public void printLine(long x) {
        writeLong(x);
        writeByte('\n');
    }

    public void printLine(char c) {
        print(c);
        writeByte('\n');
    }

    public void printLine(String s) {
        writeString(s);
        writeByte('\n');
    }

    public void printLine(int[] a) {
        printArray(a);
        writeByte('\n');
    }

    public void printLine(long[] a) {
        printArray(a);
        writeByte('\n');
    }

    public void printLine(Object... objects) {
        print(objects);
        writeByte('\n');
    }

    public void flush() {
        drain();
        try {
            out.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() {
        flush();
    }
}
