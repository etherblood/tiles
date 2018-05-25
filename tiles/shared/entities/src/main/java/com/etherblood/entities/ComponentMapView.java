package com.etherblood.entities;

import java.util.OptionalInt;

/**
 *
 * @author Philipp
 */
public interface ComponentMapView {

    boolean has(int entity);

    int get(int entity);

    default OptionalInt getOptional(int entity) {
        return has(entity) ? OptionalInt.of(get(entity)) : OptionalInt.empty();
    }

    default boolean hasValue(int entity, int value) {
        return has(entity) && get(entity) == value;
    }

    EntityQuery query();
    
}
