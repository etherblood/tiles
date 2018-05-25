package com.etherblood.collections;

/**
 *
 * @author Philipp
 */
public interface IntObjectConsumer<T> {

    void accept(int key, T value);
}
