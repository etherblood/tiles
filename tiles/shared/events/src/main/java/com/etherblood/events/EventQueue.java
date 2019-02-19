package com.etherblood.events;

/**
 *
 * @author Philipp
 */
public interface EventQueue {

    void action(Event event);

    void fire(Event event);
}
