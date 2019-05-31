package com.etherblood.entities;

import com.etherblood.collections.IntToIntHashMap;

/**
 *
 * @author Philipp
 */
public class SimpleComponentMap implements ComponentMap {

    private final IntToIntHashMap components = new IntToIntHashMap();
    private final IntEntityQueryImpl aggregator = new IntEntityQueryImpl(components);

    @Override
    public boolean has(int entity) {
        return components.hasKey(entity);
    }

    @Override
    public boolean hasValue(int entity, int value) {
        return components.getOrElse(entity, ~value) == value;
    }

    @Override
    public int get(int entity) {
        return components.get(entity);
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
    public EntityQuery query() {
        return aggregator;
    }

    @Override
    public void clear() {
        components.clear();
    }

}
