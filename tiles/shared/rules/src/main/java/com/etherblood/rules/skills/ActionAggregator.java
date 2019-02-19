package com.etherblood.rules.skills;

import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class ActionAggregator implements ActionGenerator {

    private final ActionGenerator[] generators;

    public ActionAggregator(ActionGenerator... generators) {
        this.generators = generators;
    }

    @Override
    public void availableActions(int actor, Consumer<Action> consumer) {
        for (ActionGenerator generator : generators) {
            generator.availableActions(actor, consumer);
        }
    }
}
