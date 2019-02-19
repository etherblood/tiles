package com.etherblood.rules.skills;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public interface ActionGenerator {

    void availableActions(int actor, Consumer<Action> consumer);

    default List<Action> availableActions(int actor) {
        List<Action> events = new ArrayList<>();
        availableActions(actor, events::add);
        return events;
    }
}
