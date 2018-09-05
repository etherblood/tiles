package com.etherblood.rules.abilities.passTurn;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.entities.EntityData;
import com.etherblood.rules.abilities.Action;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.events.EntityEventMeta;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class PassTurnGenerator implements ActionGenerator {

    private final EntityData data;
    private final EntityEventMeta passTurnAction;
    private final ComponentMeta passTurnAbility;

    public PassTurnGenerator(EntityData data, EntityEventMeta passTurnAction, ComponentMeta passTurnAbility) {
        this.data = data;
        this.passTurnAction = passTurnAction;
        this.passTurnAbility = passTurnAbility;
    }

    @Override
    public void availableActions(int actor, Consumer<Action> consumer) {
        if (data.has(actor, passTurnAbility.id)) {
            consumer.accept(new Action(passTurnAction.create(actor)));
        }
    }

}
