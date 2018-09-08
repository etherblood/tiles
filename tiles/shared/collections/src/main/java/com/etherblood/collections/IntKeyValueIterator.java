package com.etherblood.collections;

/**
 *
 * @author Philipp
 */
public interface IntKeyValueIterator {

    boolean hasItem();

    void next();

    int key();

    int value();
}
