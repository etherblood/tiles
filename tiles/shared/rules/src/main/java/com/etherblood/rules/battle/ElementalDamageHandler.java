package com.etherblood.rules.battle;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityValueEvent;
import com.etherblood.rules.events.EntityValueEventMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class ElementalDamageHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ElementalDamageHandler.class);

    private final String elementName;
    private final ComponentMeta health;
    private final EntityValueEventMeta setHealth;

    public ElementalDamageHandler(String elementName, ComponentMeta health, EntityValueEventMeta setHealth) {
        this.elementName = elementName;
        this.health = health;
        this.setHealth = setHealth;
    }

    public void handle(int target, int elementDamage) {
        LOG.debug("dealing {} {} damage to #{}", elementDamage, elementName, target);
        int hp = data.getOptional(target, health.id).orElse(0);
        events.fire(setHealth.create(target, hp - elementDamage));
    }

    @Override
    public void handle(EntityValueEvent event) {
        handle(event.entity, event.value);
    }

}
