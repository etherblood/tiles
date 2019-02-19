package com.etherblood.entities;

import java.util.OptionalInt;

/**
 *
 * @author Philipp
 */
public interface EntityData {

    int createEntity();

    void assertEmpty(int entity);

    boolean has(int entity, int component);

    int get(int entity, int component);

    default boolean hasValue(int entity, int component, int value) {
        return has(entity, component) && get(entity, component) == value;
    }

    default OptionalInt getOptional(int entity, int component) {
        return has(entity, component) ? OptionalInt.of(get(entity, component)) : OptionalInt.empty();
    }

    void set(int entity, int component, int value);

    void remove(int entity, int component);

    EntityQuery query(int component);
}
