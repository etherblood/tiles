package com.etherblood.entities.util;

import com.etherblood.entities.IntEntityQuery;
import java.util.OptionalInt;

public interface IntComponentMap {

    int get(int entity);

    OptionalInt getOptional(int entity);

    void set(int entity, int value);

    void remove(int entity);

    boolean has(int entity);

    default boolean hasValue(int entity, int value) {
        return getOptional(entity).orElse(~value) == value;
    }

    IntEntityQuery query();
    
    void clear();

}
