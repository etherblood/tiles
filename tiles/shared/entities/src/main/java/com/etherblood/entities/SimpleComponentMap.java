package com.etherblood.entities;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntToIntMap;
import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class SimpleComponentMap implements ComponentMap {

    private final IntToIntMap components = new IntToIntMap();

    @Override
    public boolean has(int entity) {
        return components.hasKey(entity);
    }

    @Override
    public boolean hasValue(int entity, int value) {
        return getOrElse(entity, ~value) == value;
    }

    @Override
    public int getOrElse(int entity, int defaultValue) {
        return components.getOrElse(entity, defaultValue);
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
    public IntArrayList entities(IntPredicate... predicates) {
        IntArrayList list = new IntArrayList(components.size());
        components.foreachKey(entity -> {
            for (IntPredicate predicate : predicates) {
                if (!predicate.test(entity)) {
                    return;
                }
            }
            list.add(entity);
        });
        list.sort();
        return list;
    }

    @Override
    public boolean exists(IntPredicate... predicates) {
        for (PrimitiveIterator.OfInt iterator = components.iterator(); iterator.hasNext();) {
            int entity = iterator.nextInt();
            if (Util.all(entity, predicates)) {
                return true;
            }
        }
        return false;
    }

}
