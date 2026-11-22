package utils.utility;

import java.util.*;

public class ArrayUtility {

    private static <T> void safetyCheck(List<T> list) {
        Objects.requireNonNull(list);
        //~ if list is empty, then it can't be determined whether the list items are evens or odds.
        if (list.isEmpty()) throw new IllegalArgumentException("Provided list can't be empty");
    }

    public static <T> boolean areAllEvens(List<T> list) {
        safetyCheck(list);
        return list.stream().allMatch(MathUtility::isEven);
    }

    public static <T> boolean areAllOdds(List<T> list) {
        safetyCheck(list);
        return list.stream().allMatch(MathUtility::isOdd);
    }

    public static <T> long countEvens(List<T> list) {
        return list.stream()
                .filter(MathUtility::isEven)
                .count();
    }

    public static <T> long countOdds(List<T> list) {
        return list.stream()
                .filter(MathUtility::isOdd)
                .count();
    }

    /**
     * Count number frequency
     * Complexity: O(n) expected (hash map), n = length of itemList.
     *
     * @param itemList list of items of type T.
     * @param <T>      of any type.
     * @return Map of item count.
     */
    public static <T> Map<T, Integer> countFrequency(List<T> itemList) {
        Map<T, Integer> freq = new HashMap<>();
        for (T item : itemList) freq.merge(item, 1, Integer::sum);
        return freq;
    }

    /**
     * Set default value to an array (with any dimensions).
     *
     * @param array        default value will be set to this array.
     * @param defaultValue this value will be set the to array.
     */
    public static void fill(Object array, Object defaultValue) {
        Deque<Object> q = new ArrayDeque<>();
        q.add(array);
        while (!q.isEmpty()) {
            array = q.poll();
            Class<?> eClass = array.getClass();
            if (eClass == byte[].class) Arrays.fill((byte[]) array, (Byte) defaultValue);
            else if (eClass == short[].class) Arrays.fill((short[]) array, (Short) defaultValue);
            else if (eClass == int[].class) Arrays.fill((int[]) array, (Integer) defaultValue);
            else if (eClass == long[].class) Arrays.fill((long[]) array, (Long) defaultValue);
            else if (eClass == char[].class) Arrays.fill((char[]) array, (Character) defaultValue);
            else if (eClass == float[].class) Arrays.fill((float[]) array, (Float) defaultValue);
            else if (eClass == double[].class) Arrays.fill((double[]) array, (Double) defaultValue);
            else if (eClass == boolean[].class) Arrays.fill((boolean[]) array, (Boolean) defaultValue);
            else if (eClass.isArray()) Collections.addAll(q, (Object[]) array);
        }
    }
}
