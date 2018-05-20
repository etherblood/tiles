package com.etherblood.rules.abilities.razorLeaf;

import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.battle.DamageEvent;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.stats.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class RazorLeafHandler extends GameEventHandler<RazorLeafAction> {

    private static final Logger LOG = LoggerFactory.getLogger(RazorLeafHandler.class);

    @Override
    public void handle(RazorLeafAction event) {
        int level = data.component(Components.Abilities.RAZORLEAF).get(event.actor);
        int cost = RazorLeafAction.cost(level);
        int ap = data.component(Components.Stats.ActionPoints.ACTIVE).getOrElse(event.actor, 0);
        assert ap >= cost;
        LOG.info("used {} ap of {}", cost, event.actor);
        data.component(Components.Stats.ActionPoints.ACTIVE).set(event.actor, ap - cost);
        events.response(new DamageEvent(event.actor, event.target, RazorLeafAction.attack(level), Elements.EARTH));
    }

}
