package com.etherblood.events;

import com.etherblood.collections.IntArrayQueue;
import com.etherblood.events.handlers.ArrayHandler;
import com.etherblood.events.handlers.BinaryHandler;
import com.etherblood.events.handlers.NullaryHandler;
import com.etherblood.events.handlers.TernaryHandler;
import com.etherblood.events.handlers.UnaryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class EventQueueImpl implements EventQueue {

    private static final int DEPTH_LIMIT = 64;
    private static final int ROOT_QUEUE = 0;
    private static final Logger LOG = LoggerFactory.getLogger(EventQueueImpl.class);

    private final EventDefinition[] eventDefinitions;
    private final IntArrayQueue[] eventStack;
    private final Object[][] handlers;
    private int depth = 0;
    private boolean cancelled;

    public EventQueueImpl(EventDefinition[] eventDefinitions) {
        this.eventDefinitions = eventDefinitions;
        eventStack = new IntArrayQueue[DEPTH_LIMIT];
        for (int i = 0; i < DEPTH_LIMIT; i++) {
            eventStack[i] = new IntArrayQueue();
        }
        handlers = new Object[eventDefinitions.length][];
    }

    public void registerHandlers(int eventId, Object[] handlers) {
        this.handlers[EventDefinition.eventIndex(eventId)] = handlers;
    }

    @SuppressWarnings("unchecked")
    private void processEvents() {
        IntArrayQueue queue = getQueue(depth);
        depth++;
        while (queue.hasNext()) {
            cancelled = false;
            int eventId = queue.pop();
            int index = EventDefinition.eventIndex(eventId);
            int argumentCount = EventDefinition.eventArgumentCount(eventId);
            Object[] currentHandlers = handlers[index];
            if (currentHandlers == null) {
                LOG.warn("no handlers found for {}", eventDefinitions[index].getName());
                for (int i = 0; i < argumentCount; i++) {
                    queue.pop();
                }
                cancelEvent();
            } else {
                switch (argumentCount) {
                    case 0: {
                        LOG.debug("handling {}", eventDefinitions[index].lazyString());
                        for (NullaryHandler handler : (NullaryHandler[]) currentHandlers) {
                            handler.handle();
                            if (cancelled) {
                                break;
                            }
                        }
                        break;
                    }
                    case 1: {
                        int arg0 = queue.pop();
                        LOG.debug("handling {}", eventDefinitions[index].lazyString(arg0));
                        for (UnaryHandler handler : (UnaryHandler[]) currentHandlers) {
                            handler.handle(arg0);
                            if (cancelled) {
                                break;
                            }
                        }
                        break;
                    }
                    case 2: {
                        int arg0 = queue.pop();
                        int arg1 = queue.pop();
                        LOG.debug("handling {}", eventDefinitions[index].lazyString(arg0, arg1));
                        for (BinaryHandler handler : (BinaryHandler[]) currentHandlers) {
                            handler.handle(arg0, arg1);
                            if (cancelled) {
                                break;
                            }
                        }
                        break;
                    }
                    case 3: {
                        int arg0 = queue.pop();
                        int arg1 = queue.pop();
                        int arg2 = queue.pop();
                        LOG.debug("handling {}", eventDefinitions[index].lazyString(arg0, arg1, arg2));
                        for (TernaryHandler handler : (TernaryHandler[]) currentHandlers) {
                            handler.handle(arg0, arg1, arg2);
                            if (cancelled) {
                                break;
                            }
                        }
                        break;
                    }
                    default: {
                        int[] args = new int[argumentCount];
                        for (int i = 0; i < argumentCount; i++) {
                            args[i] = queue.pop();
                        }
                        LOG.debug("handling {}", eventDefinitions[index].lazyString(args));
                        for (ArrayHandler handler : (ArrayHandler[]) currentHandlers) {
                            handler.handle(args);
                            if (cancelled) {
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            if (cancelled) {
                LOG.debug("{} was cancelled", eventDefinitions[index].getName());
                getQueue(depth).clear();
            } else {
                processEvents();
            }
        }
        depth--;
    }

    @Override
    public void cancelEvent() {
        cancelled = true;
    }

    private IntArrayQueue getQueue(int depth) {
        return eventStack[depth];
    }

    @Override
    public void action(int eventId) {
        if (depth != 0) {
            throw new IllegalStateException("actions may not be enqueued during event handling");
        }
        enqueue(getQueue(ROOT_QUEUE), eventId);
        processEvents();
    }

    @Override
    public void action(int eventId, int arg0) {
        if (depth != 0) {
            throw new IllegalStateException("actions may not be enqueued during event handling");
        }
        enqueue(getQueue(ROOT_QUEUE), eventId, arg0);
        processEvents();
    }

    @Override
    public void action(int eventId, int arg0, int arg1) {
        if (depth != 0) {
            throw new IllegalStateException("actions may not be enqueued during event handling");
        }
        enqueue(getQueue(ROOT_QUEUE), eventId, arg0, arg1);
        processEvents();
    }

    @Override
    public void action(int eventId, int arg0, int arg1, int arg2) {
        if (depth != 0) {
            throw new IllegalStateException("actions may not be enqueued during event handling");
        }
        enqueue(getQueue(ROOT_QUEUE), eventId, arg0, arg1, arg2);
        processEvents();
    }

    @Override
    public void action(int eventId, int... args) {
        if (depth != 0) {
            throw new IllegalStateException("actions may not be enqueued during event handling");
        }
        enqueue(getQueue(ROOT_QUEUE), eventId, args);
        processEvents();
    }

    @Override
    public void trigger(int eventId) {
        assert depth > 0;
        enqueue(getQueue(depth - 1), eventId);
    }

    @Override
    public void trigger(int eventId, int arg0) {
        assert depth > 0;
        enqueue(getQueue(depth - 1), eventId, arg0);
    }

    @Override
    public void trigger(int eventId, int arg0, int arg1) {
        assert depth > 0;
        enqueue(getQueue(depth - 1), eventId, arg0, arg1);
    }

    @Override
    public void trigger(int eventId, int arg0, int arg1, int arg2) {
        assert depth > 0;
        enqueue(getQueue(depth - 1), eventId, arg0, arg1, arg2);
    }

    @Override
    public void trigger(int eventId, int... args) {
        assert depth > 0;
        enqueue(getQueue(depth - 1), eventId, args);
    }

    @Override
    public void response(int eventId) {
        assert depth > 0;
        enqueue(getQueue(depth), eventId);
    }

    @Override
    public void response(int eventId, int arg0) {
        assert depth > 0;
        enqueue(getQueue(depth), eventId, arg0);
    }

    @Override
    public void response(int eventId, int arg0, int arg1) {
        assert depth > 0;
        enqueue(getQueue(depth), eventId, arg0, arg1);
    }

    @Override
    public void response(int eventId, int arg0, int arg1, int arg2) {
        assert depth > 0;
        enqueue(getQueue(depth), eventId, arg0, arg1, arg2);
    }

    @Override
    public void response(int eventId, int... args) {
        assert depth > 0;
        enqueue(getQueue(depth), eventId, args);
    }

    private void enqueue(IntArrayQueue queue, int eventId) {
        assert EventDefinition.eventIndex(eventId) < eventDefinitions.length;
        assert EventDefinition.eventArgumentCount(eventId) == 0;
        queue.push(eventId);
        LOG.debug("enqueued {}", eventDefinitions[EventDefinition.eventIndex(eventId)].lazyString());
    }

    private void enqueue(IntArrayQueue queue, int eventId, int arg0) {
        assert EventDefinition.eventIndex(eventId) < eventDefinitions.length;
        assert EventDefinition.eventArgumentCount(eventId) == 1;
        queue.push(eventId);
        queue.push(arg0);
        LOG.debug("enqueued {}", eventDefinitions[EventDefinition.eventIndex(eventId)].lazyString(arg0));
    }

    private void enqueue(IntArrayQueue queue, int eventId, int arg0, int arg1) {
        assert EventDefinition.eventIndex(eventId) < eventDefinitions.length;
        assert EventDefinition.eventArgumentCount(eventId) == 2;
        queue.push(eventId);
        queue.push(arg0);
        queue.push(arg1);
        LOG.debug("enqueued {}", eventDefinitions[EventDefinition.eventIndex(eventId)].lazyString(arg0, arg1));
    }

    private void enqueue(IntArrayQueue queue, int eventId, int arg0, int arg1, int arg2) {
        assert EventDefinition.eventIndex(eventId) < eventDefinitions.length;
        assert EventDefinition.eventArgumentCount(eventId) == 3;
        queue.push(eventId);
        queue.push(arg0);
        queue.push(arg1);
        queue.push(arg2);
        LOG.debug("enqueued {}", eventDefinitions[EventDefinition.eventIndex(eventId)].lazyString(arg0, arg1, arg2));
    }

    private void enqueue(IntArrayQueue queue, int eventId, int... args) {
        assert EventDefinition.eventIndex(eventId) < eventDefinitions.length;
        assert EventDefinition.eventArgumentCount(eventId) == args.length;
        queue.push(eventId);
        for (int arg : args) {
            queue.push(arg);
        }
        LOG.debug("enqueued {}", eventDefinitions[EventDefinition.eventIndex(eventId)].lazyString(args));
    }

}
