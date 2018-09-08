package com.etherblood.rules.battle;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityValueEventMeta;
import com.etherblood.rules.events.SourceTargetValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class ElementalAttackHandler extends AbstractGameEventHandler implements EventHandler<SourceTargetValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ElementalAttackHandler.class);

    private final String elementName;
    private final ComponentMeta power, toughness;
    private final EntityValueEventMeta elementalDamage;

    public ElementalAttackHandler(String elementName, ComponentMeta power, ComponentMeta toughness, EntityValueEventMeta elementalDamage) {
        this.elementName = elementName;
        this.power = power;
        this.toughness = toughness;
        this.elementalDamage = elementalDamage;
    }

    public void handle(int source, int target, int damage) {
        int pow = data.getOptional(target, power.id).orElse(0);
        int tough = data.getOptional(target, toughness.id).orElse(0);
        int dividend;
        int divisor;
        
        if (pow < 0) {
            dividend = 100;
            divisor = 100 - pow;
        } else {
            dividend = 100 + pow;
            divisor = 100;
        }
        
        if (tough < 0) {
            dividend *= 100 - tough;
            divisor *= 100;
        } else {
            dividend *= 100;
            divisor *= 100 + tough;
        }
        int finalDamage = damage * dividend / divisor;
        LOG.info("#{} attacks #{} with a strength {} {} attack, it deals {} damage", source, target, damage, elementName, finalDamage);
        events.fire(elementalDamage.create(target, finalDamage));
    }

    @Override
    public void handle(SourceTargetValueEvent event) {
        handle(event.source, event.target, event.value);
    }

}
