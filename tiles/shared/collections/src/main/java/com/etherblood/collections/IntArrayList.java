package com.etherblood.collections;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Philipp
 */
public class IntArrayList implements IntList {

    private static final int DEFAULT_CAPACITY = 8;
    private int size = 0;
    private int[] data;

    public IntArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public IntArrayList(int capacity) {
        data = new int[capacity];
    }

    @Override
    public int get(int index) {
        assert index < size;
        return data[index];
    }

    @Override
    public void set(int index, int value) {
        assert index < size;
        data[index] = value;
    }

    @Override
    public void add(int value) {
        if (size == data.length) {
            grow();
        }
        data[size++] = value;
    }

    @Override
    public int removeLast() {
        return data[--size];
    }

    @Override
    public void insertAt(int index, int value) {
        if (size == data.length) {
            grow();
        }
        System.arraycopy(data, index, data, index + 1, size++ - index);
        data[index] = value;
    }

    @Override
    public void swapInsertAt(int index, int value) {
        if (size == data.length) {
            grow();
        }
        data[size++] = data[index];
        data[index] = value;
    }

    @Override
    public void removeAt(int index) {
        System.arraycopy(data, index + 1, data, index, --size - index);
    }

    @Override
    public void swapRemoveAt(int index) {
        data[index] = data[--size];
    }

    @Override
    public int indexOf(int value) {
        for (int i = 0; i < size; i++) {
            if (data[i] == value) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public void sort() {
        Arrays.sort(data, 0, size);
    }

    public void shuffle(Random random) {
        for (int i = 0; i < size; i++) {
            swap(i, i + random.nextInt(size - i));
        }
    }

    @Override
    public IntStream stream() {
        return Arrays.stream(data, 0, size);
    }

    private void grow() {
        int[] nextData = new int[Math.max(data.length * 2, DEFAULT_CAPACITY)];
        System.arraycopy(data, 0, nextData, 0, data.length);
        data = nextData;
    }

    @Override
    public int size() {
        return size;
    }

    public int[] data() {
        return data;
    }

    @Override
    public List<Integer> boxed() {
        return Arrays.stream(data, 0, size).boxed().collect(Collectors.toList());
    }

    public void foreach(IntConsumer consumer) {
        for (int i = 0; i < size; i++) {
            consumer.accept(data[i]);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(Arrays.copyOfRange(data, 0, size));
    }

}
