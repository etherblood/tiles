package com.etherblood.rules.abilities;

import com.etherblood.events.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public interface ActionGenerator<T extends Event> {

    void availableActions(int actor, Consumer<T> consumer);

    default List<Event> availableActions(int actor) {
        List<Event> events = new ArrayList<>();
        availableActions(actor, events::add);
        return events;
    }
}
