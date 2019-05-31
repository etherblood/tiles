package com.etherblood.collections;

import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

public interface IntList extends Iterable<Integer> {

    int get(int index);

    void set(int index, int value);

    void add(int value);

    int removeLast();

    void insertAt(int index, int value);

    void swapInsertAt(int index, int value);

    default void swap(int index1, int index2) {
        int tmp = get(index1);
        set(index1, get(index2));
        set(index2, tmp);
    }

    void removeAt(int index);

    void swapRemoveAt(int index);

    default boolean contains(int value) {
        return indexOf(value) != -1;
    }

    int indexOf(int value);

    void clear();

    IntStream stream();

    default boolean isEmpty() {
        return size() == 0;
    }

    int size();

    @Override
    default PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private int i = 0;

            @Override
            public int nextInt() {
                return get(i++);
            }

            @Override
            public boolean hasNext() {
                return i < size();
            }
        };
    }

    void sort();
    
    List<Integer> boxed();
}
