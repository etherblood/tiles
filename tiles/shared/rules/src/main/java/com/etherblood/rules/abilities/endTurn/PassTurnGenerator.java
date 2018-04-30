package com.etherblood.rules.abilities.endTurn;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.rules.abilities.ActionGenerator;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class PassTurnGenerator implements ActionGenerator<PassTurnAction> {

    private final SimpleComponentMap passAbilityKey;

    public PassTurnGenerator(SimpleComponentMap passAbilityKey) {
        this.passAbilityKey = passAbilityKey;
    }

    @Override
    public void availableActions(int actor, Consumer<PassTurnAction> consumer) {
        if (passAbilityKey.has(actor)) {
            consumer.accept(new PassTurnAction(actor));
        }
    }

}
