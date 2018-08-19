package com.etherblood.events.handlers;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public interface EventHandler<T extends Event> {

    void handle(T event);
}
