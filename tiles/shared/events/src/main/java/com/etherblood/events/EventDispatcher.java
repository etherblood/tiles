package com.etherblood.events;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class EventDispatcher {

    private final HashMap<Class<? extends Event>, Consumer<? extends Event>[]> listeners = new HashMap<>();

    @SafeVarargs
    public final <T extends Event> void setListeners(Class<T> eventType, Consumer<T>... listeners) {
        if (this.listeners.put(eventType, listeners) != null) {
            throw new IllegalStateException("added listeners for " + eventType + " multiple times");
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void fire(Event event) {
        Consumer<? extends Event>[] handlers = listeners.get(event.getClass());
        assert handlers != null : "no handlers for " + event;
        for (Consumer handler : handlers) {
            handler.accept(event);
            if (event.isCancelled()) {
                break;
            }
        }
    }

}
