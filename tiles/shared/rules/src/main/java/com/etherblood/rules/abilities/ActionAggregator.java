package com.etherblood.rules.abilities;

import com.etherblood.events.Event;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class ActionAggregator implements ActionGenerator<Event> {

    private final ActionGenerator<?>[] generators;

    public ActionAggregator(ActionGenerator<?>... generators) {
        this.generators = generators;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void availableActions(int actor, Consumer<Event> consumer) {
        for (ActionGenerator generator : generators) {
            generator.availableActions(actor, consumer);
        }
    }
}
