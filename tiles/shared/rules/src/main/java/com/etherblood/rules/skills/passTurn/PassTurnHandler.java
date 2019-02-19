package com.etherblood.rules.skills.passTurn;

import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class PassTurnHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PassTurnHandler.class);
    
    private final int activePlayer, skillOwner;

    public PassTurnHandler(int activePlayer, int skillOwner) {
        this.activePlayer = activePlayer;
        this.skillOwner = skillOwner;
    }

    public void handle(int skill) {
        LOG.info("passed turn of #{}", skill);
        data.remove(data.get(skill, skillOwner), activePlayer);
    }

    @Override
    public void handle(EntityValueEvent event) {
        handle(event.entity);
    }

}
