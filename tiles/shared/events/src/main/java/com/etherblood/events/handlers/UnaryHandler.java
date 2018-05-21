package com.etherblood.events.handlers;

/**
 *
 * @author Philipp
 */
public interface UnaryHandler extends BinaryHandler {

    void handle(int arg0);

    @Override
    default void handle(int arg0, int arg1) {
        handle(arg0);
    }
}
