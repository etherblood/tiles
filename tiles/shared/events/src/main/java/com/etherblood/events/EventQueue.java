package com.etherblood.events;

import com.etherblood.events.handlers.EventHandler;

/**
 *
 * @author Philipp
 */
public interface EventQueue {
    
    void action(Event event);

    void response(Event event);

    void sub(Event event);
    
    void setHandlers(int eventId, EventHandler[] handlers);

}
