package utils.utility;

import java.util.Objects;

public class Pair<F, S> implements Comparable<Pair<F, S>> {

    public F first;
    public S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return "<" + first + ", " + second + ">";
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(first) + Objects.hashCode(second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof Pair<?, ?> p && Objects.equals(first, p.first) && Objects.equals(second, p.second);
    }

    /**
     * Orders by first, then by second, using their natural order.
     * Both types must be Comparable; otherwise this throws ClassCastException.
     */
    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(Pair<F, S> o) {
        int c = ((Comparable<F>) first).compareTo(o.first);
        return c != 0 ? c : ((Comparable<S>) second).compareTo(o.second);
    }
}
