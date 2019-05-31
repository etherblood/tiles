package com.etherblood.entities.util;

import com.etherblood.collections.IntToIntHashMap;
import com.etherblood.entities.IntEntityQuery;
import com.etherblood.entities.IntEntityQueryImpl;
import java.util.OptionalInt;

public class IntComponentMapImpl implements IntComponentMap {

    private final IntToIntHashMap components = new IntToIntHashMap();
    private final IntEntityQueryImpl aggregator = new IntEntityQueryImpl(components);

    @Override
    public int get(int entity) {
        return components.get(entity);
    }

    @Override
    public OptionalInt getOptional(int entity) {
        return components.getOptional(entity);
    }

    @Override
    public void set(int entity, int value) {
        components.set(entity, value);
    }

    @Override
    public void remove(int entity) {
        components.remove(entity);
    }

    @Override
    public boolean has(int entity) {
        return components.hasKey(entity);
    }

    @Override
    public IntEntityQuery query() {
        return aggregator;
    }

    @Override
    public void clear() {
        components.clear();
    }

}
