package com.etherblood.rules;

import com.etherblood.collections.IntArrayList;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class RandomTracker {

    private final IntArrayList list = new IntArrayList();
    private final Random random;

    public RandomTracker(Random random) {
        this.random = random;
    }

    public int nextInt() {
        int value = random.nextInt();
        list.add(value);
        return value;
    }

    public int nextInt(int bound) {
        int value = random.nextInt(bound);
        list.add(value);
        return value;
    }

    public IntArrayList getList() {
        return list;
    }

    public Random getRandom() {
        return random;
    }

}
