package com.etherblood.rules.battle;

import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityValueEvent;
import com.etherblood.rules.events.EntityValueEventMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndTurnOnDeathHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(EndTurnOnDeathHandler.class);
    
    private final EntityValueEventMeta setActivePlayerEvent;
    private final int activePlayer;

    public EndTurnOnDeathHandler(EntityValueEventMeta setActivePlayerEvent, int activePlayer) {
        this.setActivePlayerEvent = setActivePlayerEvent;
        this.activePlayer = activePlayer;
    }

    @Override
    public void handle(EntityValueEvent event) {
        if (event.value <= 0 && data.has(event.entity, activePlayer)) {
            LOG.info("#{} died, removing 'activePlayer'.", event.entity);
            events.fire(setActivePlayerEvent.create(event.entity, 0));
        }
    }

}
