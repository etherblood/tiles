package com.etherblood.rules.battle;

import com.etherblood.events.handlers.BinaryHandler;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class DamageHandler extends GameEventHandler implements BinaryHandler{

    private static final Logger LOG = LoggerFactory.getLogger(DamageHandler.class);

    @Override
    public void handle(int target, int damage) {
        int hp = data.getOptional(target, Components.Stats.Health.ACTIVE).orElse(0);
        data.set(target, Components.Stats.Health.ACTIVE, hp - damage);
        LOG.info("{} took {} damage", target, damage);
    }

}
