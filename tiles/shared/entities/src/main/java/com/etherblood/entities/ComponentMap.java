package com.etherblood.entities;

/**
 *
 * @author Philipp
 */
public interface ComponentMap extends ComponentMapView {

    void set(int entity, int value);

    void remove(int entity);

    default void setWithDefault(int entity, int value, int defaultValue) {
        if (value == defaultValue) {
            remove(entity);
        } else {
            set(entity, value);
        }
    }
}
