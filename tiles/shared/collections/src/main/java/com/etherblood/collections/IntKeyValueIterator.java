package com.etherblood.collections;

/**
 *
 * @author Philipp
 */
public interface IntKeyValueIterator {

    boolean next();

    int key();

    int value();
}
