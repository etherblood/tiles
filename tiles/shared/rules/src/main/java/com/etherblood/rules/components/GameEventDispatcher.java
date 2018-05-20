package com.etherblood.rules.components;

import com.etherblood.entities.EntityData;
import com.etherblood.events.Event;
import com.etherblood.events.EventQueue;
import com.etherblood.rules.GameEventHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class GameEventDispatcher implements Consumer<Event> {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<Class<? extends Event>, List<GameEventHandler<? extends Event>>> handlers = new HashMap<>();
    private final EntityData data;
    private EventQueue queue;
    private final IntUnaryOperator random;

    public GameEventDispatcher(EntityData data, IntUnaryOperator random) {
        this.data = data;
        this.random = random;
    }

    public void setQueue(EventQueue queue) {
        this.queue = queue;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void accept(Event event) {
        List<GameEventHandler<? extends Event>> list = handlers.get(event.getClass());
        assert list != null : "no handler for " + event;
        for (GameEventHandler handler : list) {
            if (event.isCancelled()) {
                break;
            }
            handler.handle(event);
        }
    }

    public void addHandler(Class<? extends Event> eventType, GameEventHandler<? extends Event> handler) {
        List<GameEventHandler<?>> list = handlers.computeIfAbsent(eventType, t -> new ArrayList<>());
        list.add(handler);
        handler.data = data;
        handler.events = queue;
        handler.random = random;
    }

    @SafeVarargs
    public final void addHandlers(Class<? extends Event> eventType, GameEventHandler<? extends Event>... handlers) {
        for (GameEventHandler<? extends Event> handler : handlers) {
            addHandler(eventType, handler);
        }
    }

}
