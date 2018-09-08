package com.etherblood.rules.components;

import com.etherblood.entities.EntityData;
import com.etherblood.events.Event;
import com.etherblood.events.EventMeta;
import com.etherblood.events.SimpleEventQueue;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import java.util.function.IntUnaryOperator;

/**
 *
 * @author Philipp
 */
public class GameEventDispatcher {

    private final EntityData data;
    private final SimpleEventQueue queue;
    private final IntUnaryOperator random;

    public GameEventDispatcher(EntityData data, SimpleEventQueue queue, IntUnaryOperator random) {
        this.data = data;
        this.queue = queue;
        this.random = random;
    }

    public <T extends AbstractGameEventHandler> T init(T handler) {
        handler.data = data;
        handler.events = queue;
        handler.random = random;
        return handler;
    }

    @SafeVarargs
    public final <T extends Event> void setInlineHandlers(EventMeta<T> event, EventHandler<T>... handlers) {
        queue.setInlineHandlers(event.id(), handlers);
    }

    @SafeVarargs
    public final <T extends Event> void setQueueHandlers(EventMeta<T> event, EventHandler<T>... handlers) {
        queue.setQueueHandlers(event.id(), handlers);
    }

    public EntityData getData() {
        return data;
    }

    public SimpleEventQueue getQueue() {
        return queue;
    }

    public IntUnaryOperator getRandom() {
        return random;
    }

}
