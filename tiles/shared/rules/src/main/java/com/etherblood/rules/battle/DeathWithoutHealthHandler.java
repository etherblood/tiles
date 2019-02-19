package com.etherblood.rules.battle;

import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityValueEvent;
import com.etherblood.rules.events.EntityValueEventMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathWithoutHealthHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DeathWithoutHealthHandler.class);
    
    private final EntityValueEventMeta setAliveEvent;
    private final int alive;

    public DeathWithoutHealthHandler(EntityValueEventMeta setAliveEvent, int alive) {
        this.setAliveEvent = setAliveEvent;
        this.alive = alive;
    }

    @Override
    public void handle(EntityValueEvent event) {
        if (event.value <= 0 && data.has(event.entity, alive)) {
            LOG.info("#{} has no health left, removing 'alive'.", event.entity);
            events.fire(setAliveEvent.create(event.entity, 0));
        }
    }

}
