package com.etherblood.events.handlers;

/**
 *
 * @author Philipp
 */
public interface TernaryHandler extends ArrayHandler {

    void handle(int arg0, int arg1, int arg2);

    @Override
    default void handle(int... args) {
        handle(args[0], args[1], args[2]);
    }
}
