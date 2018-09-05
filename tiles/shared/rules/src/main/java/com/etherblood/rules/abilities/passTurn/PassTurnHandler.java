package com.etherblood.rules.abilities.passTurn;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class PassTurnHandler extends AbstractGameEventHandler implements EventHandler<EntityEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PassTurnHandler.class);
    
    private final ComponentMeta activePlayer;

    public PassTurnHandler(ComponentMeta activePlayer) {
        this.activePlayer = activePlayer;
    }

    public void handle(int actor) {
        LOG.info("passed turn of {}", actor);
        data.remove(actor, activePlayer.id);
    }

    @Override
    public void handle(EntityEvent event) {
        handle(event.entity);
    }

}
