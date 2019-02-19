package com.etherblood.collections;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

/**
 *
 * @author Philipp
 */
public interface IntToIntMap extends Iterable<Integer> {

    boolean hasKey(int key);

    int get(int key) throws NoSuchElementException;

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
            private final IntKeyValueIterator keyValueIterator = keyValueIterator();

            @Override
            public int nextInt() {
                try {
                    return keyValueIterator.key();
                } finally {
                    keyValueIterator.next();
                }
            }

            @Override
            public boolean hasNext() {
                return keyValueIterator.hasItem();
            }
        };
    }

    void clear();
}
