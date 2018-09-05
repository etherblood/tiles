package com.etherblood.events;

/**
 *
 * @author Philipp
 */
public interface EventMeta<T> {

    int id();
    
    String name();
    
    default String toString(T event) {
        return name() + event;
    }

}
