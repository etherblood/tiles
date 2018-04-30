package com.etherblood.rules.stats.types.toughness;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import com.etherblood.rules.context.StatComponentMaps;
import com.etherblood.rules.context.TypeComponentMaps;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class RefreshBuffedToughnessHandler implements Consumer<RefreshBuffedToughnessEvent> {

    private final Logger log;
    private final EventQueue events;
    private final TypeComponentMaps types;
    private final SimpleComponentMap buffOn;

    public RefreshBuffedToughnessHandler(Logger log, EventQueue events, TypeComponentMaps types, SimpleComponentMap buffOn) {
        this.log = log;
        this.events = events;
        this.types = types;
        this.buffOn = buffOn;
    }

    @Override
    public void accept(RefreshBuffedToughnessEvent event) {
        StatComponentMaps toughnessMap = types.get(event.type).toughness;
        int power = (toughnessMap.base).getOrElse(event.target, 0);
        for (int entity : (toughnessMap.additive).entities(x -> buffOn.hasValue(x, event.target))) {
            power += (toughnessMap.additive).get(entity);
        }

        (toughnessMap.buffed).set(event.target, power);
        log.info("setting buffed {} toughness of {} to {}", event.type, event.target, power);
    }

}
