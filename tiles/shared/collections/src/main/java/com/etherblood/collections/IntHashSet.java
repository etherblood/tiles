package com.etherblood.collections;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

/**
 *
 * @author Philipp
 */
public final class IntHashSet implements Iterable<Integer> {

    private static final int FREE_KEY = 0;

    private int[] keys;
    private int mask;
    private int fillLimit;
    private int count = 0;
    private final float fillFactor;
    private boolean hasFreeKey;

    public IntHashSet() {
        this(8, 0.75f);
    }

    public IntHashSet(int capacity, float fillFactor) {
        assert 0 < fillFactor && fillFactor < 1;
        this.fillFactor = fillFactor;
        this.mask = capacity - 1;
        assert mask != 0;
        assert (mask & capacity) == 0;
        keys = new int[capacity];
        updateFillLimit(capacity);
    }

    public float estimateHashQuality() {
        long distanceSum = 0;
        long num = 0;
        for (int i = 0; i < keys.length; i++) {
            int key = keys[i];
            if (key != FREE_KEY) {
                int distance = 0;
                while ((i & mask) != ((key + distance) & mask)) {
                    distance++;
                }
                distanceSum += distance;
                num++;
            }
        }
        return (float) num / (distanceSum + num);
    }

    public void foreach(IntConsumer consumer) {
        if (hasFreeKey) {
            consumer.accept(FREE_KEY);
        }
        for (int i = 0; i < keys.length; i++) {
            int key = keys[i];
            if (key != FREE_KEY) {
                consumer.accept(key);
            }
        }
    }

    public boolean hasKey(int key) {
        if (key == FREE_KEY) {
            return hasFreeKey;
        }
        int index = key & mask;
        int indexKey;
        while (true) {
            indexKey = keys[index];
            if (indexKey == key) {
                return true;
            }
            if (indexKey == FREE_KEY) {
                return false;
            }
            index = (index + 1) & mask;
        }
    }

    public void set(int key) {
        assert count < keys.length;
        if (key == FREE_KEY) {
            if (!hasFreeKey) {
                hasFreeKey = true;
                count++;
            }
            return;
        }
        if (count >= fillLimit) {
            resize(2 * capacity());
        }
        if (uncheckedSet(key)) {
            count++;
        }
    }

    private void updateFillLimit(int capacity) {
        fillLimit = (int) (fillFactor * capacity) - 1;
    }

    public void remove(int key) {
        if (key == FREE_KEY) {
            if (hasFreeKey) {
                hasFreeKey = false;
                count--;
            }
            return;
        }
        int index = key & mask;
        int indexKey;
        while (true) {
            indexKey = keys[index];
            if (indexKey == key) {
                shift(index);
                count--;
                return;
            }
            if (indexKey == FREE_KEY) {
                return;
            }
            index = (index + 1) & mask;
        }
    }

    private void shift(int pos) {
        int last = pos;
        while (true) {
            pos = (pos + 1) & mask;
            int key = keys[pos];
            if (key == FREE_KEY) {
                keys[last] = FREE_KEY;
                return;
            }
            int slot = key & mask;
            if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) {
                keys[last] = key;
                last = pos;
            }
        }
    }

    private boolean uncheckedSet(int key) {
        assert key != FREE_KEY;
        int index = key & mask;
        int indexKey;
        while (true) {
            indexKey = keys[index];
            if (indexKey == key) {
                return false;
            }
            if (indexKey == FREE_KEY) {
                keys[index] = key;
                return true;
            }
            index = (index + 1) & mask;
        }
    }

    private void resize(int capacity) {
        assert count < capacity;
        mask = capacity - 1;
        assert (mask & capacity) == 0;
        int[] oldKeys = keys;
        keys = new int[capacity];
        Arrays.fill(keys, FREE_KEY);
        updateFillLimit(capacity);
        for (int index = 0; index < oldKeys.length; index++) {
            int key = oldKeys[index];
            if (key == FREE_KEY) {
                continue;
            }
            uncheckedSet(key);
        }
    }

    public void clear() {
        count = 0;
        hasFreeKey = false;
        Arrays.fill(keys, FREE_KEY);
    }

    public int[] toArray() {
        int[] array = new int[size()];
        int i = 0;
        PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            array[i++] = iterator.nextInt();
        }
        return array;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(toArray());
    }

    public int size() {
        return count;
    }
    
    public boolean isEmpty() {
        return count == 0;
    }

    public int capacity() {
        return keys.length;
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private int i;
            private int next;

            {
                i = -1;
                if (!hasFreeKey) {
                    gotoNext();
                } else {
                    next = FREE_KEY;
                }
            }

            private void gotoNext() {
                assert hasNext();
                while (true) {
                    i++;
                    if (!hasNext()) {
                        break;
                    }
                    int key = keys[i];
                    if (key != FREE_KEY) {
                        next = key;
                        break;
                    }
                }
            }

            @Override
            public int nextInt() {
                try {
                    return next;
                } finally {
                    gotoNext();
                }
            }

            @Override
            public boolean hasNext() {
                return i < keys.length;
            }
        };
    }
}
