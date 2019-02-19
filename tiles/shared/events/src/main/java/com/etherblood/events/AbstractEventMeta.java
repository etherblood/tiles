package com.etherblood.events;

public abstract class AbstractEventMeta<T> implements EventMeta<T> {

    private final int id;
    private final String name;

    public AbstractEventMeta(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "id=" + id + ", name=" + name + '}';
    }

}
