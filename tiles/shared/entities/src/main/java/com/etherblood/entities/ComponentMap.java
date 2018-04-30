package com.etherblood.entities;

/**
 *
 * @author Philipp
 */
public interface ComponentMap {

    boolean has(int entity);

    int getOrElse(int entity, int defaultValue);

    int get(int entity);

    void set(int entity, int value);

    void remove(int entity);

    default boolean hasValue(int entity, int value) {
        return getOrElse(entity, ~value) == value;
    }

    default void setWithDefault(int entity, int value, int defaultValue) {
        if (value == defaultValue) {
            remove(entity);
        } else {
            set(entity, value);
        }
    }
}
