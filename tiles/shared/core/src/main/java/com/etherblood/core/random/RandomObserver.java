package com.etherblood.core.random;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntList;

public class RandomObserver {

    private final HistoryRandom random;
    private int next;

    RandomObserver(HistoryRandom random, int start) {
        this.random = random;
        this.next = start;
    }

    public IntList getNext() {
        IntList result = new IntArrayList();
        IntList history = random.getHistory();
        while (next < history.size()) {
            result.add(history.get(next++));
        }
        return result;
    }
}
