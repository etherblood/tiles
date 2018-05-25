package com.etherblood.rules.abilities.endTurn;

import com.etherblood.entities.EntityData;
import com.etherblood.rules.abilities.Action;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.components.Components;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class PassTurnGenerator implements ActionGenerator {

    private final EntityData data;
    private final int passTurnAction;

    public PassTurnGenerator(EntityData data, int passTurnAction) {
        this.data = data;
        this.passTurnAction = passTurnAction;
    }

    @Override
    public void availableActions(int actor, Consumer<Action> consumer) {
        if (data.has(actor, Components.Abilities.PASS_TURN)) {
            consumer.accept(new Action(passTurnAction, actor));
        }
    }

}
