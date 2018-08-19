package com.etherblood.events;

import com.etherblood.events.handlers.EventHandler;
import java.util.ArrayDeque;
import java.util.Deque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleEventQueue implements EventQueue {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleEventQueue.class);

    private Event activeEvent = null;
    private final Deque<Event> eventQueue = new ArrayDeque<>();
    private final EventHandler[][] handlers;
    private int depth = 0;

    public SimpleEventQueue(int eventCount) {
        this.handlers = new EventHandler[eventCount][];
    }

    @Override
    public void setHandlers(int eventId, EventHandler[] handlers) {
        assert this.handlers[eventId] == null;
        this.handlers[eventId] = handlers;
    }

    private void handleEvent(Event event) {
        for (EventHandler handler : handlers[event.getId()]) {
            LOG.debug("handling {} with handler {}", event, handler);
            handler.handle(event);
            if (event.isCancelled()) {
                return;
            }
        }
    }

    @Override
    public void action(Event event) {
        if (!eventQueue.isEmpty()) {
            throw new IllegalStateException();
        }
        if (activeEvent != null) {
            throw new IllegalStateException();
        }
        sub(event);
        while (!eventQueue.isEmpty()) {
            Event response = eventQueue.removeFirst();
            activeEvent = response;
            handleEvent(response);
            activeEvent = null;
        }
    }

    @Override
    public void response(Event event) {
        event.setParent(activeEvent);
        eventQueue.addLast(event);
        LOG.debug("enqueued response {}", event);
    }

    @Override
    public void sub(Event event) {
        event.setParent(activeEvent);
        activeEvent = event;
        handleEvent(event);
        activeEvent = event.getParent();
    }

}
