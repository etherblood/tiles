package com.etherblood.collections;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

/**
 *
 * @author Philipp
 */
public class IntToObjectMap<T> implements Iterable<Integer> {

    private static final int FREE_KEY = 0;

    private int[] keys;
    private T[] values;
    private int mask;
    private T freeValue;
    private int count;
    private int fillLimit;
    private final float fillFactor;
    private boolean hasFreeKey;

    public IntToObjectMap() {
        this(8, 0.75f);
    }

    @SuppressWarnings("unchecked")
    public IntToObjectMap(int capacity, float fillFactor) {
        this.fillFactor = fillFactor;
        this.mask = capacity - 1;
        assert mask != 0;
        assert (mask & capacity) == 0;
        keys = new int[capacity];
        values = (T[]) new Object[capacity];
        updateFillLimit(capacity);
    }

    public void foreach(IntObjectConsumer<T> consumer) {
        if (hasFreeKey) {
            consumer.accept(FREE_KEY, freeValue);
        }
        for (int i = 0; i < keys.length; i++) {
            int key = keys[i];
            if (key != FREE_KEY) {
                consumer.accept(key, values[i]);
            }
        }
    }

    public void foreachKey(IntConsumer consumer) {
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

    public T computeIfAbsent(int key, IntFunction<T> func) {
        if (key == FREE_KEY) {
            if (!hasFreeKey) {
                freeValue = func.apply(key);
            }
            return freeValue;
        }
        int index = key & mask;
        int indexKey;
        while (true) {
            indexKey = keys[index];
            if (indexKey == key) {
                return values[index];
            }
            if (indexKey == FREE_KEY) {
                T value = func.apply(key);
                keys[index] = key;
                values[index] = value;
                return value;
            }
            index = (index + 1) & mask;
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

    public T get(int key) {
        if (key == FREE_KEY) {
            if (hasFreeKey) {
                return freeValue;
            }
            throw new NullPointerException();
        }
        int index = key & mask;
        int indexKey;
        while (true) {
            indexKey = keys[index];
            if (indexKey == key) {
                return values[index];
            }
            if (indexKey == FREE_KEY) {
                throw new NullPointerException();
            }
            index = (index + 1) & mask;
        }
    }

    public T getOrElse(int key, T defaultValue) {
        if (key == FREE_KEY) {
            return hasFreeKey ? freeValue : defaultValue;
        }
        int index = key & mask;
        int indexKey;
        while (true) {
            indexKey = keys[index];
            if (indexKey == key) {
                return values[index];
            }
            if (indexKey == FREE_KEY) {
                return defaultValue;
            }
            index = (index + 1) & mask;
        }
    }

    public void set(int key, T value) {
        assert count < keys.length;
        if (key == FREE_KEY) {
            if (!hasFreeKey) {
                freeValue = value;
                hasFreeKey = true;
                count++;
            }
            return;
        }
        if (count >= fillLimit) {
            resize(capacity() << 1);
        }
        if (uncheckedSet(key, value)) {
            count++;
        }
    }

    private boolean uncheckedSet(int key, T shiftedValue) {
        assert key != FREE_KEY;
        int index = key & mask;
        int indexKey;
        while (true) {
            indexKey = keys[index];
            if (indexKey == key) {
                values[index] = shiftedValue;
                return false;
            }
            if (indexKey == FREE_KEY) {
                keys[index] = indexKey;
                values[index] = shiftedValue;
                return true;
            }
            index = (index + 1) & mask;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        assert count < capacity;
        mask = capacity - 1;
        assert (mask & capacity) == 0;
        int[] oldKeys = keys;
        T[] oldValues = values;
        keys = new int[capacity];
        values = (T[]) new Object[capacity];
        Arrays.fill(keys, FREE_KEY);
        updateFillLimit(capacity);
        for (int index = 0; index < oldKeys.length; index++) {
            int key = oldKeys[index];
            if (key == FREE_KEY) {
                continue;
            }
            uncheckedSet(key, oldValues[index]);
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
        // Shift entries with the same hash.
        int last, slot;
        int key;
        while (true) {
            pos = ((last = pos) + 1) & mask;
            while (true) {
                key = keys[pos];
                if (key == FREE_KEY) {
                    keys[last] = FREE_KEY;
                    return;
                }
                slot = key & mask; //calculate the starting slot for the current key
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) {
                    break;
                }
                pos = (pos + 1) & mask; //go to the next entry
            }
            keys[last] = key;
            values[last] = values[pos];
        }
    }

    private static int key(long keyValue) {
        return (int) keyValue;
    }

    private static int value(long keyValue) {
        return (int) (keyValue >>> 32);
    }

    private static long dataValue(int value) {
        return ((long) value) << 32;
    }

    private static long dataKey(int key) {
        return Integer.toUnsignedLong(key);
    }

    public int size() {
        return count;
    }

    public int capacity() {
        return keys.length;
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            int i;
            int next;

            {
                i = -1;
                next = FREE_KEY;
                if (!hasFreeKey) {
                    gotoNext();
                }
            }

            private void gotoNext() {
                do {
                    i++;
                } while (hasNext() && keys[i] == FREE_KEY);
                if (hasNext()) {
                    next = keys[i];
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
