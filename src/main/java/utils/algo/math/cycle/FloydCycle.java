package utils.algo.math.cycle;

import utils.utility.Pair;

/**
 * Floyd's tortoise and hare cycle detection for the sequence x0, f(x0), f(f(x0)), ...
 * Override f, then call detect. Complexity: O(mu + lambda) calls of f, O(1) memory.
 * Source: CP3, page 225.
 */
public abstract class FloydCycle {

    public abstract int f(int x);

    /**
     * @return Pair(mu, lambda): mu is the index of the first element of the cycle, lambda is the cycle length.
     */
    public Pair<Integer, Integer> detect(int x0) {
        //~ 1st part: finding k*mu, hare's speed is 2x tortoise's
        //~ f(x0) is the node next to x0
        int tortoise = f(x0), hare = f(f(x0));
        while (tortoise != hare) {
            tortoise = f(tortoise);
            hare = f(f(hare));
        }
        //~ 2nd part: finding mu, hare and tortoise move at the same speed
        int mu = 0;
        hare = x0;
        while (tortoise != hare) {
            tortoise = f(tortoise);
            hare = f(hare);
            mu++;
        }
        //~ 3rd part: finding lambda, hare moves, tortoise stays
        int lambda = 1;
        hare = f(tortoise);
        while (tortoise != hare) {
            hare = f(hare);
            lambda++;
        }
        return Pair.of(mu, lambda);
    }
}
