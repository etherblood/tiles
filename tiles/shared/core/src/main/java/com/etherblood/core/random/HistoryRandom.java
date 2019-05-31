package com.etherblood.core.random;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntList;
import java.util.function.IntUnaryOperator;

public class HistoryRandom {

    private final IntUnaryOperator source;
    private final IntList history = new IntArrayList();
    private int nextIndex = 0;
    private boolean masterMode = false;

    public HistoryRandom(IntUnaryOperator source) {
        this.source = source;
    }

    public int next(int min, int max) {
        return next(max - min) + min;
    }

    public boolean nextBoolean() {
        return next(1) != 0;
    }

    public int next(int max) {
        if (masterMode) {
            assert nextIndex == history.size();
            history.add(source.applyAsInt(max));
        }
        return history.get(nextIndex++);
    }

    public void setMasterMode(boolean value) {
        if (nextIndex != history.size()) {
            throw new IllegalStateException();
        }
        masterMode = value;
    }

    public void enqueue(int value) {
        assert !masterMode;
        history.add(value);
    }

    public IntList getHistory() {
        return history;
    }

    public void clear() {
        history.clear();
        nextIndex = 0;
    }

    public RandomObserver createObserver() {
        return new RandomObserver(this, history.size());
    }
}
