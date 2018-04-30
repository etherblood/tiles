package com.etherblood.entities;

import com.etherblood.collections.IntIntConsumer;

/**
 *
 * @author Philipp
 */
public class UniqueComponentMap implements ComponentMap {

    private boolean exists;
    private int entity, value;

    UniqueComponentMap() {
    }

    @Override
    public boolean has(int entity) {
        return exists && this.entity == entity;
    }

    @Override
    public boolean hasValue(int entity, int value) {
        return exists && this.entity == entity && this.value == value;
    }

    @Override
    public int getOrElse(int entity, int defaultValue) {
        if (has(entity)) {
            return value;
        }
        return defaultValue;
    }

    @Override
    public int get(int entity) {
        assert has(entity);
        return value;
    }

    @Override
    public void set(int entity, int value) {
        exists = true;
        this.entity = entity;
        this.value = value;
    }

    @Override
    public void remove(int entity) {
        assert has(entity);
        exists = false;
    }

    public void foreachEntity(IntIntConsumer consumer) {
        if (exists) {
            consumer.accept(entity, value);
        }
    }

    public boolean exists() {
        return exists;
    }

    public int uniqueEntity() {
        assert exists;
        return entity;
    }

}
