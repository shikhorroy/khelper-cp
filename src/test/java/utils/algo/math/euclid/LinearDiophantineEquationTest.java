package utils.algo.math.euclid;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class LinearDiophantineEquationTest {

    @Test
    void largeRightSideIsSolvedExactlyWithoutDoublePrecisionLoss() {
        long c = (1L << 53) + 1;
        LinearDiophantineEquation.Solution s = LinearDiophantineEquation.findAnySolution(3, 5, c);
        assertTrue(s.hasSolution);
        BigInteger lhs = BigInteger.valueOf(3).multiply(BigInteger.valueOf(s.x))
                .add(BigInteger.valueOf(5).multiply(BigInteger.valueOf(s.y)));
        assertEquals(BigInteger.valueOf(c), lhs);
    }

    @Test
    void matchesBruteForceForAllSignsAndZeros() {
        for (long a = -6; a <= 6; a++)
            for (long b = -6; b <= 6; b++)
                for (long c = -10; c <= 10; c++) {
                    if (a == 0 && b == 0) continue;
                    LinearDiophantineEquation.Solution s = LinearDiophantineEquation.findAnySolution(a, b, c);
                    boolean exists = c % Euclid.gcd(a, b) == 0;
                    String msg = a + "x + " + b + "y = " + c;
                    assertEquals(exists, s.hasSolution, msg);
                    if (exists) assertEquals(c, a * s.x + b * s.y, msg);
                }
    }

    @Test
    void bothCoefficientsZeroAndRightSideZeroHasSolution() {
        LinearDiophantineEquation.Solution s = LinearDiophantineEquation.findAnySolution(0, 0, 0);
        assertTrue(s.hasSolution);
        assertEquals(0, s.x);
        assertEquals(0, s.y);
    }

    @Test
    void bothCoefficientsZeroAndRightSideNonZeroHasNoSolution() {
        assertFalse(LinearDiophantineEquation.findAnySolution(0, 0, 5).hasSolution);
    }

    @Test
    void noSolutionWhenGcdDoesNotDivideRightSide() {
        assertFalse(LinearDiophantineEquation.findAnySolution(4, 6, 5).hasSolution);
    }
}
