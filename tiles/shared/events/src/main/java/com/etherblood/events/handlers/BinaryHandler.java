package com.etherblood.events.handlers;

/**
 *
 * @author Philipp
 */
public interface BinaryHandler extends TernaryHandler {

    void handle(int arg0, int arg1);

    @Override
    default void handle(int arg0, int arg1, int arg2) {
        handle(arg0, arg1);
    }
}
