package utils.algo.dp.knapsack.un_bounded_knapsack;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RodCuttingTest {

    @Test
    void classicExample() {
        List<Integer> lengths = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> prices = List.of(1, 5, 8, 9, 10, 17, 17, 20);
        assertEquals(22, new RodCutting(8, lengths, prices).iterativeDP());
    }

    @Test
    void largePricesDoNotOverflow() {
        assertEquals(3_000_000_000L, new RodCutting(3, List.of(1), List.of(1_000_000_000)).iterativeDP());
    }

    @Test
    void negativeLengthGivesZero() {
        assertEquals(0, new RodCutting(-1, List.of(1), List.of(5)).iterativeDP());
    }

    @Test
    void zeroLengthPieceIsIgnored() {
        assertEquals(4, new RodCutting(2, List.of(0, 1), List.of(100, 2)).iterativeDP());
    }

    @Test
    void randomInputsMatchRecursiveReference() {
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            int length = random.nextInt(12), pieces = random.nextInt(4);
            List<Integer> lengths = new ArrayList<>(), prices = new ArrayList<>();
            for (int i = 0; i < pieces; i++) {
                lengths.add(random.nextInt(6));
                prices.add(random.nextInt(1_000_000_000));
            }
            assertEquals(reference(length, lengths, prices), new RodCutting(length, lengths, prices).iterativeDP());
        }
    }

    private static long reference(int length, List<Integer> lengths, List<Integer> prices) {
        long best = 0;
        for (int i = 0; i < lengths.size(); i++)
            if (lengths.get(i) >= 1 && lengths.get(i) <= length)
                best = Math.max(best, prices.get(i) + reference(length - lengths.get(i), lengths, prices));
        return best;
    }
}
