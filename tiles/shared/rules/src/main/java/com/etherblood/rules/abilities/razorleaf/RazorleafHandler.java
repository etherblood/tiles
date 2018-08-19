package com.etherblood.rules.abilities.razorleaf;

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
public class RazorleafHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RazorleafHandler.class);

    private final int earthDamageEvent;

    public RazorleafHandler(int earthDamageEvent) {
        this.earthDamageEvent = earthDamageEvent;
    }

    public void handle(int actor, int target) {
        int level = data.get(actor, Components.Abilities.RAZORLEAF);
        int cost = apCost(level);
        int ap = data.getOptional(actor, Components.Stats.ActionPoints.ACTIVE).orElse(0);
        assert ap >= cost;
        LOG.info("used {} ap of {}", cost, actor);
        data.set(actor, Components.Stats.ActionPoints.ACTIVE, ap - cost);
        events.response(new EntityValueEvent(earthDamageEvent, target, attack(level)));
    }

    public static int apCost(int level) {
        return 3;
    }

    public static int attack(int level) {
        return level;
    }

    @Override
    public void handle(EntityValueEvent event) {
        handle(event.entity, event.value);
    }

}
