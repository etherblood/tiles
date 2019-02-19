package com.etherblood.jme.client.pathfinding;

public class IndexPriority {

    private final int index;
    private final int priority;

    public IndexPriority(int index, int priority) {
        this.index = index;
        this.priority = priority;
    }

    public int getIndex() {
        return index;
    }

    public int getPriority() {
        return priority;
    }

}
