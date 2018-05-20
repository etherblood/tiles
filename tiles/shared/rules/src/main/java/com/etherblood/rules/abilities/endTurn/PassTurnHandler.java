package com.etherblood.rules.abilities.endTurn;

import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.game.turns.TurnEndEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class PassTurnHandler extends GameEventHandler<PassTurnAction> {

    private static final Logger LOG = LoggerFactory.getLogger(PassTurnHandler.class);

    @Override
    public void handle(PassTurnAction event) {
        LOG.info("passed turn of {}", event.actor);
        data.component(Components.ACTIVE_TURN).remove(event.actor);
        if (!data.component(Components.ACTIVE_TURN).exists()) {
            LOG.info("all actors passed, ending turn...", event.actor);
            events.trigger(new TurnEndEvent(data.component(Components.MEMBER_OF).get(event.actor)));
        }
    }

}
