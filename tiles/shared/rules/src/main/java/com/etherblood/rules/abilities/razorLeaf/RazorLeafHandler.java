package com.etherblood.rules.abilities.razorLeaf;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import com.etherblood.rules.battle.DamageEvent;
import com.etherblood.rules.movement.SetPositionEvent;
import com.etherblood.rules.stats.PokemonTypes;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class RazorLeafHandler implements Consumer<RazorLeafAction> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap razorLeafAbility, actionPoints;

    public RazorLeafHandler(Logger log, EventQueue events, SimpleComponentMap razorLeafAbility, SimpleComponentMap actionPoints) {
        this.log = log;
        this.events = events;
        this.razorLeafAbility = razorLeafAbility;
        this.actionPoints = actionPoints;
    }

    @Override
    public void accept(RazorLeafAction event) {
        int level = razorLeafAbility.get(event.actor);
        int cost = RazorLeafAction.cost(level);
        int ap = actionPoints.getOrElse(event.actor, 0);
        assert ap >= cost;
        log.info("used {} ap of {}", cost, event.actor);
        actionPoints.set(event.actor, ap - cost);
        events.response(new DamageEvent(event.actor, event.target, RazorLeafAction.attack(level), PokemonTypes.GRASS));
    }

}
