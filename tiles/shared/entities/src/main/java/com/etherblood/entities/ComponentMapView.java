package com.etherblood.entities;

import com.etherblood.collections.IntArrayList;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public interface ComponentMapView {

    IntArrayList entities(IntPredicate... predicates);

    boolean has(int entity);

    int get(int entity);
    
    default boolean hasValue(int entity, int value) {
        return has(entity) && get(entity) == value;
    }

    default int getOrElse(int entity, int defaultValue) {
        return has(entity) ? get(entity) : defaultValue;
    }
    
    default boolean exists(IntPredicate... predicates) {
        return entities(predicates).size() != 0;
    }
}
