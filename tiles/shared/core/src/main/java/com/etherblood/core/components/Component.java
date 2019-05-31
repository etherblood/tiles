package com.etherblood.core.components;

import com.etherblood.entities.EntityQuery;

public abstract class Component<T> {

    public final int id;
    public final String name;

    public Component(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract void remove(int entity);

    public abstract boolean has(int entity);

    public abstract T getGeneric(int entity);

    public abstract void setGeneric(int entity, T value);

    public abstract EntityQuery query();

    public abstract Class<T> getComponentType();

    public abstract void clear();

    @Override
    public String toString() {
        return getComponentType().getSimpleName() + "Component#" + id + "<" + name + ">";
    }
}
