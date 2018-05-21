package com.etherblood.rules.abilities.razorLeaf;

import com.etherblood.events.handlers.BinaryHandler;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class RazorleafHandler extends GameEventHandler implements BinaryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RazorleafHandler.class);

    private final int earthDamageEvent;

    public RazorleafHandler(int earthDamageEvent) {
        this.earthDamageEvent = earthDamageEvent;
    }

    @Override
    public void handle(int actor, int target) {
        int level = data.component(Components.Abilities.RAZORLEAF).get(actor);
        int cost = apCost(level);
        int ap = data.component(Components.Stats.ActionPoints.ACTIVE).getOrElse(actor, 0);
        assert ap >= cost;
        LOG.info("used {} ap of {}", cost, actor);
        data.component(Components.Stats.ActionPoints.ACTIVE).set(actor, ap - cost);
        events.response(earthDamageEvent, actor, target, attack(level));
    }
    
    public static int apCost(int level) {
        return 3;
    }
    
    public static int attack(int level) {
        return level;
    }

}
