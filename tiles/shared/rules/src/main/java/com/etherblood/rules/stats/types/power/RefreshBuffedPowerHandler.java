package com.etherblood.rules.stats.types.power;

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
public class RefreshBuffedPowerHandler implements Consumer<RefreshBuffedPowerEvent> {

    private final Logger log;
    private final EventQueue events;
    private final TypeComponentMaps types;
    private final SimpleComponentMap buffOn;

    public RefreshBuffedPowerHandler(Logger log, EventQueue events, TypeComponentMaps types, SimpleComponentMap buffOn) {
        this.log = log;
        this.events = events;
        this.types = types;
        this.buffOn = buffOn;
    }

    @Override
    public void accept(RefreshBuffedPowerEvent event) {
        StatComponentMaps powerMap = types.get(event.type).power;
        int power = (powerMap.base).getOrElse(event.target, 0);
        for (int entity : (powerMap.additive).entities(x -> buffOn.hasValue(x, event.target))) {
            power += (powerMap.additive).get(entity);
        }

        (powerMap.buffed).set(event.target, power);
        log.info("setting buffed {} power of {} to {}", event.type, event.target, power);
    }

}
