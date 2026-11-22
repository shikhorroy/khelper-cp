package utils.algo.math.counting;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EulerTotientTest {

    private static int brute(int n) {
        int cnt = 0;
        for (int k = 1; k <= n; k++) if (BigInteger.valueOf(n).gcd(BigInteger.valueOf(k)).intValue() == 1) cnt++;
        return cnt;
    }

    @Test
    void listMatchesBruteForceUpTo500() {
        List<Integer> phi = EulerTotient.calculate(500);
        assertEquals(501, phi.size());
        assertEquals(0, phi.get(0));
        for (int n = 1; n <= 500; n++) assertEquals(brute(n), phi.get(n), "phi(" + n + ")");
    }

    @Test
    void arrayAndSingleValueAgree() {
        int[] phi = EulerTotient.calculateArray(3000);
        for (int n = 0; n <= 3000; n++) assertEquals(phi[n], EulerTotient.of(n), "phi(" + n + ")");
    }

    @Test
    void singleValueForLargeInputs() {
        assertEquals(1_000_000_006L, EulerTotient.of(1_000_000_007L));
        assertEquals(1L << 39, EulerTotient.of(1L << 40));
        assertEquals(0, EulerTotient.of(-5));
    }

    @Test
    void negativeNGivesEmptyResult() {
        assertTrue(EulerTotient.calculate(-1).isEmpty());
        assertEquals(0, EulerTotient.calculateArray(-1).length);
    }
}
