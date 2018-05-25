package com.etherblood.entities;

import com.etherblood.collections.IntToIntMap;
import java.util.OptionalInt;

/**
 *
 * @author Philipp
 */
public class SimpleComponentMap implements ComponentMap {

    private final IntToIntMap components = new IntToIntMap();
    private final SimpleEntityQuery aggregator = new SimpleEntityQuery(components);

    @Override
    public boolean has(int entity) {
        return components.hasKey(entity);
    }

    @Override
    public boolean hasValue(int entity, int value) {
        OptionalInt optional = getOptional(entity);
        return optional.isPresent() && optional.getAsInt() == value;
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
