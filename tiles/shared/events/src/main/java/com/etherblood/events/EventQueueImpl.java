package com.etherblood.events;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;

/**
 *
 * @author Philipp
 */
public class EventQueueImpl implements EventQueue {

    private static final int SUCCESSIVE_EVENTS_LIMIT = 1000;
    private static final int ROOT_QUEUE = 0;

    private final List<Queue<Event>> eventStack = new ArrayList<>();
    private final Consumer<Event> eventConsumer;
    private final Logger log;
    private int depth = 0;
    private int successiveEventsCount;
    private Event activeEvent = null;

    public EventQueueImpl(Consumer<Event> eventConsumer) {
        this(eventConsumer, NOPLogger.NOP_LOGGER);
    }

    public EventQueueImpl(Consumer<Event> eventConsumer, Logger log) {
        this.eventConsumer = eventConsumer;
        this.log = log;
    }

    @Override
    public void action(Event action) {
        if (depth != 0) {
            throw new IllegalStateException("actions may not be enqueued during event handling");
        }
        enqueue(getQueue(ROOT_QUEUE), action);
        successiveEventsCount = 0;
        processEvents();
    }

    private void processEvents() {
        Queue<Event> queue = getQueue(depth);
        depth++;
        while ((activeEvent = queue.poll()) != null) {
            successiveEventsCount++;
            if (successiveEventsCount > SUCCESSIVE_EVENTS_LIMIT) {
                throw new IllegalStateException("successive events limit reached");
            }
            log.debug("handling {}", activeEvent);
            eventConsumer.accept(activeEvent);
            if (activeEvent.isCancelled()) {
                log.debug("{} was cancelled", activeEvent);
                getQueue(depth).clear();
            } else {
                processEvents();
            }
        }
        depth--;
    }

    @Override
    public void trigger(Event event) {
        assert depth > 0;
        enqueue(getQueue(depth - 1), event);
    }

    @Override
    public void response(Event event) {
        assert depth > 0;
        enqueue(getQueue(depth), event);
    }

    private void enqueue(Queue<Event> queue, Event event) {
        event.setParent(activeEvent);
        queue.offer(event);
    }

    private Queue<Event> getQueue(int depth) {
        while (eventStack.size() <= depth) {
            eventStack.add(new ArrayDeque<>());
        }
        return eventStack.get(depth);
    }

}
