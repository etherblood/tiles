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
public class UpdateBuffedStatHandler extends AbstractGameEventHandler implements EventHandler<EntityEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateBuffedStatHandler.class);
    private final String statName;
    private final ComponentMeta base, additive, buffOn;
    private final EntityValueEventMeta setBuffedSupply;

    public UpdateBuffedStatHandler(String statName, ComponentMeta base, ComponentMeta additive, ComponentMeta buffOn, EntityValueEventMeta setBuffedSupply) {
        this.statName = statName;
        this.base = base;
        this.additive = additive;
        this.buffOn = buffOn;
        this.setBuffedSupply = setBuffedSupply;
    }

    public void handle(int entity) {
        int baseValue = data.getOptional(entity, base.id).orElse(0);
        int additiveValue = data.query(additive.id).sum(hasValue(buffOn.id, entity));
        LOG.info("updating buffed {} of {} to {}, base is {}, additive is {}", statName, entity, baseValue + additiveValue, baseValue, additiveValue);
        events.sub(setBuffedSupply.create(entity, baseValue + additiveValue));
    }

    @Override
    public void handle(EntityEvent event) {
        handle(event.entity);
    }

}
