package com.etherblood.entities.util;

import com.etherblood.collections.IntHashSet;
import com.etherblood.entities.BoolEntityQueryImpl;
import com.etherblood.entities.EntityQuery;

public class BoolComponentMapImpl implements BoolComponentMap {

    private final IntHashSet components = new IntHashSet();
    private final BoolEntityQueryImpl aggregator = new BoolEntityQueryImpl(components);

    @Override
    public void set(int entity) {
        components.set(entity);
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
    public EntityQuery query() {
        return aggregator;
    }

    @Override
    public void clear() {
        components.clear();
    }

}
