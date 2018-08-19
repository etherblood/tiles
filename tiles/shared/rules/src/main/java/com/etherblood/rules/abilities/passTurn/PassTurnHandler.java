package com.etherblood.rules.abilities.passTurn;

import com.etherblood.events.EventDefinition;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.events.EntityEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class PassTurnHandler extends AbstractGameEventHandler implements EventHandler<EntityEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PassTurnHandler.class);
    private final EventDefinition endTurn;

    public PassTurnHandler(EventDefinition endTurn) {
        this.endTurn = endTurn;
    }

    public void handle(int actor) {
        LOG.info("passed turn of {}", actor);
        data.remove(actor, Components.ACTIVE_PLAYER);
//        if (!data.query(Components.ACTIVE_PLAYER).exists()) {
//            LOG.info("all actors passed, ending turn...", actor);
//            events.trigger(new EntityEvent(endTurn.id(), data.get(actor, Components.MEMBER_OF)));
//        }
    }

    @Override
    public void handle(EntityEvent event) {
        handle(event.entity);
    }

}
