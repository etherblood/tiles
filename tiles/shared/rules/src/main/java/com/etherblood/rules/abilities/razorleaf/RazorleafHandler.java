package com.etherblood.rules.abilities.razorleaf;

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
public class RazorleafHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RazorleafHandler.class);

    private final EntityValueEventMeta earthDamageEvent;
    private final ComponentMeta razorleaf, actionPoints;

    public RazorleafHandler(EntityValueEventMeta earthDamageEvent, ComponentMeta razorleaf, ComponentMeta actionPoints) {
        this.earthDamageEvent = earthDamageEvent;
        this.razorleaf = razorleaf;
        this.actionPoints = actionPoints;
    }

    public void handle(int actor, int target) {
        int level = data.get(actor, razorleaf.id);
        int cost = apCost(level);
        int ap = data.getOptional(actor, actionPoints.id).orElse(0);
        assert ap >= cost;
        LOG.info("used {} ap of {}", cost, actor);
        data.set(actor, actionPoints.id, ap - cost);
        events.response(earthDamageEvent.create(target, attack(level)));
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
