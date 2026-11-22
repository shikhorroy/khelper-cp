package utils.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class OutputWriterTest {

    // Small buffer (it grows to the 64-byte minimum) so tests also cross buffer flushes.
    private static String write(Consumer<OutputWriter> body) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (OutputWriter w = new OutputWriter(out, 8)) {
            body.accept(w);
        }
        return out.toString(StandardCharsets.UTF_8);
    }

    @Test
    void printsNumberLimitsWithOneMinusSign() {
        assertEquals("-2147483648\n2147483647\n-9223372036854775808\n0\n",
                write(w -> {
                    w.printLine(Integer.MIN_VALUE);
                    w.printLine(Integer.MAX_VALUE);
                    w.printLine(Long.MIN_VALUE);
                    w.printLine(0);
                }));
    }

    @Test
    void printsArraysAsSpaceSeparatedValues() {
        assertEquals("1 -2 3\n4 5\n\n", write(w -> {
            w.printLine(new int[]{1, -2, 3});
            w.printLine(new long[]{4, 5});
            w.printLine(new int[0]);
        }));
    }

    @Test
    void varargsJoinsMixedValuesWithSpaces() {
        assertEquals("a b 3 4 null 1.5 true\n", write(w -> w.printLine("a", 'b', 3, 4L, null, 1.5, true)));
    }

    @Test
    void writesNonAsciiAsUtf8() {
        String s = "x".repeat(70) + "éö";
        assertEquals(s + "\né", write(w -> {
            w.printLine(s);
            w.print('é');
        }));
    }

    @Test
    void manyNumbersSurviveBufferFlushes() {
        StringBuilder expected = new StringBuilder();
        for (int i = -5000; i <= 5000; i++) expected.append(i * 7919L).append('\n');
        assertEquals(expected.toString(), write(w -> {
            for (int i = -5000; i <= 5000; i++) w.printLine(i * 7919L);
        }));
    }

    @Test
    void closeFlushesButKeepsStreamUsable() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputWriter w = new OutputWriter(out);
        w.print(42);
        assertEquals(0, out.size());
        w.close();
        assertEquals("42", out.toString());
        w.print(7);
        w.flush();
        assertEquals("427", out.toString());
    }
}
