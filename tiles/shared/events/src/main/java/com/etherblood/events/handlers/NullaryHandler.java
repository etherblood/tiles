package com.etherblood.events.handlers;

/**
 *
 * @author Philipp
 */
public interface NullaryHandler extends UnaryHandler {

    void handle();

    @Override
    default void handle(int arg0) {
        handle();
    }
}
