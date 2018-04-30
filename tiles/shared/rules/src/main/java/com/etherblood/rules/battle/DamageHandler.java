package com.etherblood.rules.battle;

import com.etherblood.rules.stats.health.*;
import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class DamageHandler implements Consumer<DamageEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap health;

    public DamageHandler(Logger log, EventQueue events, SimpleComponentMap activeHealthKey) {
        this.log = log;
        this.events = events;
        this.health = activeHealthKey;
    }

    @Override
    public void accept(DamageEvent event) {
        int hp = health.getOrElse(event.defender, 0);
        health.set(event.defender, hp - event.damage);
        log.info("{} dealt {} {} damage to {}", event.attacker, event.damage, event.type, event.defender);
    }

}
