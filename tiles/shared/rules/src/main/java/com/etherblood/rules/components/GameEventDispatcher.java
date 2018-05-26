package com.etherblood.rules.components;

import com.etherblood.entities.SimpleEntityData;
import com.etherblood.events.EventDefinition;
import com.etherblood.events.EventQueueImpl;
import com.etherblood.events.handlers.ArrayHandler;
import com.etherblood.events.handlers.BinaryHandler;
import com.etherblood.events.handlers.NullaryHandler;
import com.etherblood.events.handlers.TernaryHandler;
import com.etherblood.events.handlers.UnaryHandler;
import com.etherblood.rules.GameEventHandler;
import java.util.function.IntUnaryOperator;

/**
 *
 * @author Philipp
 */
public class GameEventDispatcher {

    private final SimpleEntityData data;
    private final EventQueueImpl queue;
    private final IntUnaryOperator random;

    public GameEventDispatcher(SimpleEntityData data, EventQueueImpl queue, IntUnaryOperator random) {
        this.data = data;
        this.queue = queue;
        this.random = random;
    }

    public <T extends GameEventHandler> T init(T handler) {
        handler.data = data;
        handler.events = queue;
        handler.random = random;
        return handler;
    }

    public final void setNullaryHandlers(EventDefinition event, NullaryHandler... handlers) {
        int eventId = event.id();
        if (event.argumentCount() != 0) {
            throw new IllegalArgumentException("tried to register nullary handlers for event " + event.getName() + " which has " + event.argumentCount() + " arguments");
        }
        queue.registerHandlers(eventId, handlers);
    }

    public final void setUnaryHandlers(EventDefinition event, UnaryHandler... handlers) {
        int eventId = event.id();
        if (event.argumentCount() != 1) {
            throw new IllegalArgumentException("tried to register unary handlers for event " + event.getName() + " which has " + event.argumentCount() + " arguments");
        }
        queue.registerHandlers(eventId, handlers);
    }

    public final void setBinaryHandlers(EventDefinition event, BinaryHandler... handlers) {
        int eventId = event.id();
        if (event.argumentCount() != 2) {
            throw new IllegalArgumentException("tried to register binary handlers for event " + event.getName() + " which has " + event.argumentCount() + " arguments");
        }
        queue.registerHandlers(eventId, handlers);
    }

    public final void setTernaryHandlers(EventDefinition event, TernaryHandler... handlers) {
        int eventId = event.id();
        if (event.argumentCount() != 3) {
            throw new IllegalArgumentException("tried to register ternary handlers for event " + event.getName() + " which has " + event.argumentCount() + " arguments");
        }
        queue.registerHandlers(eventId, handlers);
    }

    public final void setArrayHandlers(EventDefinition event, ArrayHandler... handlers) {
        queue.registerHandlers(event.id(), handlers);
    }

}
