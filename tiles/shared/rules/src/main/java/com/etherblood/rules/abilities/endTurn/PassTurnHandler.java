package com.etherblood.rules.abilities.endTurn;

import com.etherblood.events.EventDefinition;
import com.etherblood.events.handlers.UnaryHandler;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class PassTurnHandler extends GameEventHandler implements UnaryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PassTurnHandler.class);
    private final EventDefinition endTurn;

    public PassTurnHandler(EventDefinition endTurn) {
        this.endTurn = endTurn;
    }

    @Override
    public void handle(int actor) {
        LOG.info("passed turn of {}", actor);
        data.component(Components.ACTIVE_TURN).remove(actor);
        if (!data.component(Components.ACTIVE_TURN).exists()) {
            LOG.info("all actors passed, ending turn...", actor);
            events.trigger(endTurn.id(), data.component(Components.MEMBER_OF).get(actor));
        }
    }

}
