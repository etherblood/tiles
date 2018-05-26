package com.etherblood.rules.abilities.endTurn;

import com.etherblood.events.EventDefinition;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class PassTurnHandler extends GameEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PassTurnHandler.class);
    private final EventDefinition endTurn;

    public PassTurnHandler(EventDefinition endTurn) {
        this.endTurn = endTurn;
    }

    public void handle(int actor) {
        LOG.info("passed turn of {}", actor);
        data.remove(actor, Components.ACTIVE_TURN);
        if (!data.query(Components.ACTIVE_TURN).exists()) {
            LOG.info("all actors passed, ending turn...", actor);
            events.trigger(endTurn.id(), data.get(actor, Components.MEMBER_OF));
        }
    }

}
