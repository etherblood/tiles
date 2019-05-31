package com.etherblood.collections;

import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;

/**
 *
 * @author Philipp
 */
public interface IntToIntMap extends Iterable<Integer> {

    boolean hasKey(int key);

    int get(int key) throws NoSuchElementException;

    default OptionalInt getOptional(int key) {
        return hasKey(key) ? OptionalInt.of(get(key)) : OptionalInt.empty();
    }

    default int getOrElse(int key, int defaultValue) {
        return hasKey(key) ? get(key) : defaultValue;
    }

    void set(int key, int value);

    void remove(int key);

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    IntKeyValueIterator keyValueIterator();

    @Override
    default PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private final IntKeyValueIterator keyValueIterator;
            private Integer nextKey;

            {
                keyValueIterator = keyValueIterator();
                updateNext();
            }

            @Override
            public int nextInt() {
                try {
                    return nextKey;
                } finally {
                    updateNext();
                }
            }

            @Override
            public boolean hasNext() {
                return nextKey != null;
            }

            private void updateNext() {
                if (keyValueIterator.next()) {
                    nextKey = keyValueIterator.key();
                } else {
                    nextKey = null;
                }
            }
        };
    }

    void clear();
}
