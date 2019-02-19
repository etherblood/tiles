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
    private final EventHandler globalHandler;

    public SimpleEventQueue(int eventCount) {
        this(eventCount, e -> {
        });
    }

    public SimpleEventQueue(int eventCount, EventHandler globalHandler) {
        this.inlineHandlers = new EventHandler[eventCount][0];
        this.queueHandlers = new EventHandler[eventCount][0];
        this.globalHandler = globalHandler;
    }

    public void addInlineHandlers(int eventId, EventHandler[] handlers) {
        this.inlineHandlers[eventId] = Util.append(this.inlineHandlers[eventId], handlers);
    }

    public void addQueueHandlers(int eventId, EventHandler[] handlers) {
        this.queueHandlers[eventId] = Util.append(this.queueHandlers[eventId], handlers);
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
        LOG.debug("enqueued {}", event);
        activeEvent = event;
        handleEvent(event, inlineHandlers[event.id]);
        activeEvent = event.getParent();
        if (!event.isCancelled()) {
            globalHandler.handle(event);
        }
    }

    private void handleEvent(Event event, EventHandler[] handlers) {
        if(handlers.length == 0) {
            LOG.warn("No handlers found for {}.", event);
        }
        for (EventHandler handler : handlers) {
            if (event.isCancelled()) {
                return;
            }
            LOG.debug("handling {} with handler {}", event, handler);
            handler.handle(event);
        }
    }

}
