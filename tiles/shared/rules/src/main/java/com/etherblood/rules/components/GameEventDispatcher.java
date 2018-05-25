package com.etherblood.rules.components;

import com.etherblood.entities.SimpleEntityData;
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

    public final void setHandlers(int eventId, NullaryHandler... handlers) {
        for (Object handler : handlers) {
            initGameEventHandler((GameEventHandler) handler);
        }
        queue.registerHandlers(eventId, handlers);
    }

    public final void setHandlers(int eventId, UnaryHandler... handlers) {
        for (Object handler : handlers) {
            initGameEventHandler((GameEventHandler) handler);
        }
        queue.registerHandlers(eventId, handlers);
    }

    public final void setHandlers(int eventId, BinaryHandler... handlers) {
        for (Object handler : handlers) {
            initGameEventHandler((GameEventHandler) handler);
        }
        queue.registerHandlers(eventId, handlers);
    }

    public final void setHandlers(int eventId, TernaryHandler... handlers) {
        for (Object handler : handlers) {
            initGameEventHandler((GameEventHandler) handler);
        }
        queue.registerHandlers(eventId, handlers);
    }

    public final void setHandlers(int eventId, ArrayHandler... handlers) {
        for (Object handler : handlers) {
            initGameEventHandler((GameEventHandler) handler);
        }
        queue.registerHandlers(eventId, handlers);
    }

    private void initGameEventHandler(GameEventHandler handler) {
        handler.data = data;
        handler.events = queue;
        handler.random = random;
    }

}
