package com.etherblood.entities;

import com.etherblood.collections.IntToIntHashMap;

/**
 *
 * @author Philipp
 */
public class SimpleComponentMap implements ComponentMap {

    private final IntToIntHashMap components = new IntToIntHashMap();
    private final SimpleEntityQuery aggregator = new SimpleEntityQuery(components);

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

//    @Deprecated
//    public IntArrayList entities(IntPredicate predicate) {
//        IntArrayList list = new IntArrayList(components.size());
//        components.foreachKey(entity -> {
//            if (predicate.test(entity)) {
//                list.add(entity);
//            }
//        });
//        list.sort();
//        return list;
//    }

    @Override
    public EntityQuery query() {
        return aggregator;
    }

}
