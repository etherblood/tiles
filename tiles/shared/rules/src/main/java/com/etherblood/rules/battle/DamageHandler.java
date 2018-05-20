package com.etherblood.rules.battle;

import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class DamageHandler extends GameEventHandler<DamageEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DamageHandler.class);

    @Override
    public void handle(DamageEvent event) {
        int hp = data.component(Components.Stats.Health.ACTIVE).getOrElse(event.defender, 0);
        data.component(Components.Stats.Health.ACTIVE).set(event.defender, hp - event.damage);
        LOG.info("{} dealt {} {} damage to {}", event.attacker, event.damage, event.type, event.defender);
    }

}
