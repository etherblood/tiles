package com.etherblood.rules.abilities.endTurn;

import com.etherblood.entities.EntityData;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.components.Components;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class PassTurnGenerator implements ActionGenerator<PassTurnAction> {

    private final EntityData data;

    public PassTurnGenerator(EntityData data) {
        this.data = data;
    }

    @Override
    public void availableActions(int actor, Consumer<PassTurnAction> consumer) {
        if (data.component(Components.Abilities.PASS_TURN).has(actor)) {
            consumer.accept(new PassTurnAction(actor));
        }
    }

}
