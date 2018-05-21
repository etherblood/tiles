package com.etherblood.collections;

/**
 *
 * @author Philipp
 */
public class IntArrayQueue {

    private static final int DEFAULT_CAPACITY = 8;
    private int size = 0, start = 0, mask;
    private int[] data;

    public IntArrayQueue() {
        data = new int[DEFAULT_CAPACITY];
        mask = DEFAULT_CAPACITY - 1;
    }

    public void push(int value) {
        if (size == data.length) {
            grow();
        }
        data[(start + size) & mask] = value;
        size++;
    }

    public int pop() {
        assert hasNext();
        int value = data[start];
        start = (start + 1) & mask;
        size--;
        return value;
    }

    public boolean hasNext() {
        return size != 0;
    }

    public void clear() {
        size = 0;
    }

    private void grow() {
        int[] nextData = new int[data.length * 2];
        System.arraycopy(data, 0, nextData, 0, data.length);
        System.arraycopy(data, 0, nextData, data.length, data.length);
        data = nextData;
        mask = nextData.length - 1;
    }

    public int size() {
        return size;
    }

}
