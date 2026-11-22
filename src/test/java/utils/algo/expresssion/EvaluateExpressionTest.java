package utils.algo.expresssion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EvaluateExpressionTest {

    @ParameterizedTest(name = "{0} = {1}")
    @CsvSource(delimiter = '|', value = {
            "2 + 3 * (4 - 1) / 3 | 5",
            "8-3-2               | 3",
            "100/10/5            | 2",
            "7/2                 | 3",
            "-7/2                | -3",
            "-3+5                | 2",
            "2*-3                | -6",
            "-(2+3)*2            | -10",
            "2-(-3)              | 5",
            "--4                 | 4",
            "+4                  | 4",
            "((((1))))           | 1",
            "2147483647          | 2147483647",
    })
    void evaluatesValidExpressions(String expression, int expected) {
        assertEquals(expected, EvaluateExpression.evaluate(expression));
    }

    @ParameterizedTest(name = "\"{0}\" is malformed")
    @ValueSource(strings = {"2^3", "(2+3", "2+3)", "  ", "", "2 3", "2+", "*2", "()", "2(3)", "1+*2"})
    void malformedExpressionThrows(String expression) {
        assertThrows(IllegalArgumentException.class, () -> EvaluateExpression.evaluate(expression));
    }

    @Test
    void tabsAndLineEndingsAreWhitespace() {
        assertEquals(5, EvaluateExpression.evaluate("2 +\t3\r\n"));
    }

    @Test
    void literalAboveIntRangeThrowsNumberFormatException() {
        assertThrows(NumberFormatException.class, () -> EvaluateExpression.evaluate("99999999999"));
    }

    @Test
    void divisionByZeroThrows() {
        assertThrows(UnsupportedOperationException.class, () -> EvaluateExpression.evaluate("1/(2-2)"));
    }

    @Test
    void applyOpRejectsUnknownOperator() {
        assertThrows(IllegalArgumentException.class, () -> EvaluateExpression.applyOp('%', 2, 3));
    }

    @Test
    void randomExpressionsMatchReferenceParser() {
        Random rnd = new Random(42);
        for (int it = 0; it < 20_000; it++) {
            String e = generate(rnd, 3);
            Integer expected;
            try {
                expected = new Reference(e).parse();
            } catch (ArithmeticException ex) {
                expected = null;
            }
            Integer actual;
            try {
                actual = EvaluateExpression.evaluate(e);
            } catch (UnsupportedOperationException ex) {
                actual = null;
            }
            assertEquals(expected, actual, e);
        }
    }

    private static String generate(Random rnd, int depth) {
        int kind = depth == 0 ? 0 : rnd.nextInt(4);
        String sign = rnd.nextInt(5) == 0 ? "-" : "";
        if (kind == 0) return sign + rnd.nextInt(20);
        if (kind == 1) return sign + "(" + generate(rnd, depth - 1) + ")";
        char op = "+-*/".charAt(rnd.nextInt(4));
        return generate(rnd, depth - 1) + " " + op + " " + generate(rnd, depth - 1);
    }

    /**
     * Recursive-descent reference: expr = term (+|- term)*, term = unary (*|/ unary)*, unary = -unary | atom.
     */
    private static final class Reference {
        private final String s;
        private int i;

        Reference(String s) {
            this.s = s.replace(" ", "");
        }

        int parse() {
            int v = expr();
            assertEquals(s.length(), i);
            return v;
        }

        private int expr() {
            int v = term();
            while (i < s.length() && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
                char c = s.charAt(i++);
                int r = term();
                v = c == '+' ? v + r : v - r;
            }
            return v;
        }

        private int term() {
            int v = unary();
            while (i < s.length() && (s.charAt(i) == '*' || s.charAt(i) == '/')) {
                char c = s.charAt(i++);
                int r = unary();
                if (c == '*') v *= r;
                else {
                    if (r == 0) throw new ArithmeticException();
                    v /= r;
                }
            }
            return v;
        }

        private int unary() {
            if (s.charAt(i) == '-') {
                i++;
                return -unary();
            }
            if (s.charAt(i) == '(') {
                i++;
                int v = expr();
                i++;
                return v;
            }
            int v = 0;
            while (i < s.length() && Character.isDigit(s.charAt(i))) v = v * 10 + (s.charAt(i++) - '0');
            return v;
        }
    }
}
