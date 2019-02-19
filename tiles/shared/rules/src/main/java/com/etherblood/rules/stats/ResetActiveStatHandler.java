package com.etherblood.rules.stats;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityEvent;
import com.etherblood.rules.events.EntityValueEventMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class ResetActiveStatHandler extends AbstractGameEventHandler implements EventHandler<EntityEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ResetActiveStatHandler.class);

    private final String statName;
    private final ComponentMeta buffed;
    private final EntityValueEventMeta setActiveSupply;

    public ResetActiveStatHandler(String statName, ComponentMeta buffed, EntityValueEventMeta setActiveEvent) {
        this.statName = statName;
        this.buffed = buffed;
        this.setActiveSupply = setActiveEvent;
    }

    public void handle(int entity) {
        LOG.debug("resetting {} of #{}", statName, entity);
        events.fire(setActiveSupply.create(entity, data.getOptional(entity, buffed.id).orElse(0)));
    }

    @Override
    public void handle(EntityEvent event) {
        handle(event.entity);
    }
}
