package com.etherblood.events;

import com.etherblood.events.handlers.EventHandler;

/**
 *
 * @author Philipp
 */
public interface EventQueue {
    
    void action(Event event);

    void fire(Event event);
    
////    void setInlineHandlers(int eventId, EventHandler[] handlers);
////
////    void setQueueHandlers(int eventId, EventHandler[] handlers);
}
