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
        int hp = data.component(Components.Stats.Health.ACTIVE).getOrElse(target, 0);
        data.component(Components.Stats.Health.ACTIVE).set(target, hp - damage);
        LOG.info("{} took {} damage", target, damage);
    }

}
