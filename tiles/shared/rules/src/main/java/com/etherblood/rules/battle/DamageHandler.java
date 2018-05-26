package com.etherblood.rules.battle;

import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class DamageHandler extends GameEventHandler{

    private static final Logger LOG = LoggerFactory.getLogger(DamageHandler.class);

    public void handle(int target, int damage) {
        int hp = data.getOptional(target, Components.Stats.Health.ACTIVE).orElse(0);
        data.set(target, Components.Stats.Health.ACTIVE, hp - damage);
        LOG.info("{} took {} damage", target, damage);
    }

}
