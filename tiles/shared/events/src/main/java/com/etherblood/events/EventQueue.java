package com.etherblood.events;

/**
 *
 * @author Philipp
 */
public interface EventQueue {

    void action(Event event);

    void trigger(Event event);

    void response(Event event);

}
