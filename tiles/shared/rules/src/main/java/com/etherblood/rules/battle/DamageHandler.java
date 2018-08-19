package com.etherblood.rules.battle;

import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.events.EntityValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class DamageHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DamageHandler.class);

    public void handle(int target, int damage) {
        int hp = data.getOptional(target, Components.Stats.Health.ACTIVE).orElse(0);
        data.set(target, Components.Stats.Health.ACTIVE, hp - damage);
        LOG.info("{} took {} damage", target, damage);
    }

    @Override
    public void handle(EntityValueEvent event) {
        handle(event.entity, event.value);
    }

}
