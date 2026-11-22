package utils.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Fast token reader for competitive programming. Reads raw bytes into one 64 KiB buffer.
 * Tokens are split by any byte in 0..32 (space, tab, CR, LF and other control bytes).
 */
public class InputReader {

    private static final int EOF = -1;

    private final InputStream stream;
    private final byte[] buf;
    private int len;
    private int ptr;
    //~ Reused scratch space for next() and nextLine(), so a token costs no StringBuilder.
    private byte[] tok = new byte[64];
    //~ The byte that ended the last token. nextLine() uses it to finish that line first.
    private int lastEnd = '\n';

    public InputReader(InputStream stream) {
        this(stream, 1 << 16);
    }

    public InputReader(InputStream stream, int bufSize) {
        this.stream = stream;
        this.buf = new byte[bufSize];
    }

    private int read() {
        if (ptr == len) {
            try {
                len = stream.read(buf, 0, buf.length);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            ptr = 0;
            if (len <= 0) {
                len = 0;
                return EOF;
            }
        }
        // `& 0xFF`: a non-ASCII byte must not look like EOF (-1)
        return buf[ptr++] & 0xFF;
    }

    private static boolean isBlank(int c) {
        return c >= 0 && c <= ' ';
    }

    private int skipBlanks() {
        int c = read();
        while (isBlank(c)) c = read();
        return c;
    }

    /**
     * When the last token ended inside a line, eats the trailing spaces, tabs and CR of that line and
     * its line break, so {@link #nextLine()} starts on the next line even with trailing spaces.
     * Stops before the next non-blank byte. Done here, not after each token, to keep tokens fast.
     */
    private void finishTokenLine() {
        if (lastEnd == '\n' || lastEnd == EOF) return;
        lastEnd = '\n';
        int c = read();
        while (c == ' ' || c == '\t' || c == '\r') c = read();
        if (c != '\n' && c != EOF) ptr--; // safe: read() just returned a byte
    }

    private void putTok(int n, int c) {
        if (n == tok.length) tok = Arrays.copyOf(tok, n << 1);
        tok[n] = (byte) c;
    }

    /** True if one more token is left. Does not consume it. */
    public boolean hasNext() {
        int c = skipBlanks();
        if (c == EOF) return false;
        ptr--; // safe: read() just returned a byte, so ptr >= 1
        return true;
    }

    public int nextInt() {
        int c = skipBlanks();
        boolean neg = c == '-';
        if (neg || c == '+') c = read();
        // accumulate as a negative number, so Integer.MIN_VALUE also fits
        int res = 0;
        while (c >= '0' && c <= '9') {
            res = res * 10 - (c - '0');
            c = read();
        }
        lastEnd = c;
        return neg ? res : -res;
    }

    public long nextLong() {
        int c = skipBlanks();
        boolean neg = c == '-';
        if (neg || c == '+') c = read();
        long res = 0;
        while (c >= '0' && c <= '9') {
            res = res * 10 - (c - '0');
            c = read();
        }
        lastEnd = c;
        return neg ? res : -res;
    }

    /** Exact parse (handles exponents too). Hand-made digit loops lose precision. */
    public double nextDouble() {
        return Double.parseDouble(next());
    }

    /** Next non-blank character (ASCII). */
    public char nextCharacter() {
        return (char) skipBlanks();
    }

    public String next() {
        int c = skipBlanks();
        int n = 0;
        while (c > ' ') {
            putTok(n++, c);
            c = read();
        }
        lastEnd = c;
        return new String(tok, 0, n, StandardCharsets.UTF_8);
    }

    /** Same as {@link #next()}. */
    public String nextString() {
        return next();
    }

    /**
     * Reads the rest of the current line, without the line break ({@code \n} or {@code \r\n}).
     * A token read eats the line break that ends it, so after {@code nextInt()} at the end of a line,
     * this returns the whole next line. After a token in the middle of a line, it returns the rest.
     */
    public String nextLine() {
        finishTokenLine();
        int c = read();
        int n = 0;
        while (c != EOF && c != '\n') {
            if (c != '\r') putTok(n++, c);
            c = read();
        }
        return new String(tok, 0, n, StandardCharsets.UTF_8);
    }

    public int[] nextIntArray(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = nextInt();
        return a;
    }

    public long[] nextLongArray(int n) {
        long[] a = new long[n];
        for (int i = 0; i < n; i++) a[i] = nextLong();
        return a;
    }
}
