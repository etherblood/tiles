package com.etherblood.collections;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

/**
 *
 * @author Philipp
 */
public class IntToIntHashMap implements IntToIntMap {

    private static final long VALUE_MASK = 0xffffffff00000000L;
    private static final int FREE_KEY = 0;
    private static final long FREE_KEYVALUE = dataKey(FREE_KEY);

    private long[] data;
    private int mask;
    private int freeValue;
    private int count;
    private int fillLimit;
    private final float fillFactor;
    private boolean hasFreeKey;

    public IntToIntHashMap() {
        this(8, 0.75f);
    }

    public IntToIntHashMap(int capacity, float fillFactor) {
        this.fillFactor = fillFactor;
        this.mask = capacity - 1;
        assert mask != 0;
        assert (mask & VALUE_MASK) == 0;
        assert (mask & capacity) == 0;
        data = new long[capacity];
        updateFillLimit(capacity);
    }

    public void foreach(IntIntConsumer consumer) {
        if (hasFreeKey) {
            consumer.accept(FREE_KEY, freeValue);
        }
        for (int i = 0; i < data.length; i++) {
            long keyValue = data[i];
            if (keyValue != FREE_KEYVALUE) {
                consumer.accept(key(keyValue), value(keyValue));
            }
        }
    }

    public void foreachKey(IntConsumer consumer) {
        if (hasFreeKey) {
            consumer.accept(FREE_KEY);
        }
        for (int i = 0; i < data.length; i++) {
            long keyValue = data[i];
            if (keyValue != FREE_KEYVALUE) {
                consumer.accept(key(keyValue));
            }
        }
    }

    @Override
    public boolean hasKey(int key) {
        if (key == FREE_KEY) {
            return hasFreeKey;
        }
        int index = key & mask;
        while (true) {
            long keyValue = data[index];
            if (keyValue == FREE_KEYVALUE) {
                return false;
            }
            if (key(keyValue) == key) {
                return true;
            }
            index = (index + 1) & mask;
        }
    }

    @Override
    public int get(int key) {
        if (key == FREE_KEY) {
            if (hasFreeKey) {
                return freeValue;
            }
            throw new NullPointerException();
        }
        int index = key & mask;
        while (true) {
            long keyValue = data[index];
            int keyCandidate = key(keyValue);
            if (keyCandidate == key) {
                return value(keyValue);
            }
            if (keyCandidate == FREE_KEY) {
                throw new NullPointerException();
            }
            index = (index + 1) & mask;
        }
    }

    @Override
    public int getOrElse(int key, int defaultValue) {
        if (key == FREE_KEY) {
            return hasFreeKey ? freeValue : defaultValue;
        }
        int index = key & mask;
        while (true) {
            long keyValue = data[index];
            if (keyValue == FREE_KEYVALUE) {
                return defaultValue;
            }
            if (key(keyValue) == key) {
                return value(keyValue);
            }
            index = (index + 1) & mask;
        }
    }

    @Override
    public void set(int key, int value) {
        assert count < data.length;
        if (key == FREE_KEY) {
            if (!hasFreeKey) {
                freeValue = value;
                hasFreeKey = true;
                count++;
            }
            return;
        }
        if (count >= fillLimit) {
            resize(2 * capacity());
        }
        if (uncheckedSet(key, dataValue(value))) {
            count++;
        }
    }

    private boolean uncheckedSet(int key, long shiftedValue) {
        assert key != FREE_KEY;
        int index = key & mask;
        while (true) {
            long keyValue = data[index];
            if (keyValue == FREE_KEYVALUE) {
                data[index] = dataKey(key) | shiftedValue;
                return true;
            }
            if (key(keyValue) == key) {
                data[index] = dataKey(key) | shiftedValue;
                return false;
            }
            index = (index + 1) & mask;
        }
    }

    private void resize(int capacity) {
        assert count < capacity;
        mask = capacity - 1;
        assert (mask & capacity) == 0;
        long[] oldData = data;
        data = new long[capacity];
        Arrays.fill(data, FREE_KEY);
        updateFillLimit(capacity);
        for (int index = 0; index < oldData.length; index++) {
            long keyValue = oldData[index];
            int key = key(keyValue);
            if (key == FREE_KEY) {
                continue;
            }
            uncheckedSet(key, keyValue & VALUE_MASK);
        }
    }

    private void updateFillLimit(int capacity) {
        fillLimit = (int) (fillFactor * capacity) - 1;
    }

    @Override
    public void remove(int key) {
        if (key == FREE_KEY) {
            if (hasFreeKey) {
                hasFreeKey = false;
                count--;
            }
            return;
        }
        int index = key & mask;
        while (true) {
            long keyValue = data[index];
            if (keyValue == FREE_KEYVALUE) {
                return;
            }
            if (key(keyValue) == key) {
                shift(index);
                count--;
                return;
            }
            index = (index + 1) & mask;
        }
    }

    private void shift(int pos) {
        int last = pos;
        while (true) {
            pos = (pos + 1) & mask;
            long keyValue = data[pos];
            if (keyValue == FREE_KEYVALUE) {
                data[last] = FREE_KEYVALUE;
                return;
            }
            int key = key(keyValue);
            int slot = key & mask;
            if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) {
                data[last] = dataKey(key) | (keyValue & VALUE_MASK);
                last = pos;
            }
        }
    }

    @Override
    public int size() {
        return count;
    }

    public int capacity() {
        return data.length;
    }

    @Override
    public IntKeyValueIterator keyValueIterator() {
        return new IntKeyValueIterator() {
            private int i;
            private int key, value;

            {
                i = -1;
                if (hasFreeKey) {
                    key = FREE_KEY;
                    value = freeValue;
                } else {
                    next();
                }
            }

            @Override
            public boolean hasItem() {
                return i < data.length;
            }

            @Override
            public void next() {
                assert hasItem();
                while (true) {
                    i++;
                    if (!hasItem()) {
                        break;
                    }
                    long keyValue = data[i];
                    if (keyValue != FREE_KEYVALUE) {
                        key = IntToIntHashMap.key(keyValue);
                        value = IntToIntHashMap.value(keyValue);
                        break;
                    }
                }
            }

            @Override
            public int key() {
                assert hasItem();
                return key;
            }

            @Override
            public int value() {
                assert hasItem();
                return value;
            }
        };
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
}
