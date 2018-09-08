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
    private final EventHandler[][] inlineHandlers, queueHandlers;

    public SimpleEventQueue(int eventCount) {
        this.inlineHandlers = new EventHandler[eventCount][0];
        this.queueHandlers = new EventHandler[eventCount][0];
    }

    public void setInlineHandlers(int eventId, EventHandler[] handlers) {
        assert this.inlineHandlers[eventId].length == 0;
        this.inlineHandlers[eventId] = handlers;
    }

    public void setQueueHandlers(int eventId, EventHandler[] handlers) {
        assert this.queueHandlers[eventId].length == 0;
        this.queueHandlers[eventId] = handlers;
    }

    @Override
    public void action(Event event) {
        if (!eventQueue.isEmpty()) {
            throw new IllegalStateException();
        }
        if (activeEvent != null) {
            throw new IllegalStateException();
        }
        fire(event);
        while (!eventQueue.isEmpty()) {
            Event response = eventQueue.removeFirst();
            activeEvent = response;
            handleEvent(response, queueHandlers[response.id]);
        }
        activeEvent = null;
    }

    @Override
    public void fire(Event event) {
        event.setParent(activeEvent);
        eventQueue.addLast(event);
        LOG.debug("enqueued response {}", event);
        activeEvent = event;
        handleEvent(event, inlineHandlers[event.id]);
        activeEvent = event.getParent();
    }

    private void handleEvent(Event event, EventHandler[] handlers) {
        for (EventHandler handler : handlers) {
            if (event.isCancelled()) {
                return;
            }
            LOG.debug("handling {} with handler {}", event, handler);
            handler.handle(event);
        }
    }

}
