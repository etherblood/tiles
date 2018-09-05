package com.etherblood.events;

import java.util.function.Function;

/**
 *
 * @author Philipp
 */
public class EventDefinition<T extends Event> implements EventMeta<T> {

    private final int id;
    private final String name;
    private final Function<T, String> stringify;

    public EventDefinition(int id, String name) {
        this(id, name, event -> "{" + name + "=" + event + "}");
    }

    public EventDefinition(int id, String name, Function<T, String> stringify) {
        this.name = name;
        this.id = id;
        this.stringify = stringify;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int id() {
        return id;
    }

    public String toString(T event) {
        assert id == event.id;
        return stringify.apply(event);
    }

    @Override
    public String toString() {
        return EventDefinition.class.getSimpleName() + "{id=" + id + ", name=" + name + ", stringify=" + stringify + '}';
    }

}
