package com.etherblood.rules.skills.passTurn;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.entities.EntityData;
import com.etherblood.rules.skills.Action;
import com.etherblood.rules.skills.ActionGenerator;
import com.etherblood.rules.events.EntityEventMeta;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class PassTurnGenerator implements ActionGenerator {

    private final EntityData data;
    private final EntityEventMeta passTurnAction;
    private final ComponentMeta passTurnSkill;

    public PassTurnGenerator(EntityData data, EntityEventMeta passTurnAction, ComponentMeta passTurnSkill) {
        this.data = data;
        this.passTurnAction = passTurnAction;
        this.passTurnSkill = passTurnSkill;
    }

    @Override
    public void availableActions(int actor, Consumer<Action> consumer) {
        if (data.has(actor, passTurnSkill.id)) {
            consumer.accept(new Action(passTurnAction.create(actor)));
        }
    }

}
