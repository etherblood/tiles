package com.etherblood.rules.stats.types.power;

import com.etherblood.events.EventQueue;
import com.etherblood.rules.context.TypeComponentMaps;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class SetActivePowerHandler implements Consumer<SetActivePowerEvent> {

    private final Logger log;
    private final EventQueue events;
    private final TypeComponentMaps types;

    public SetActivePowerHandler(Logger log, EventQueue events, TypeComponentMaps types) {
        this.log = log;
        this.events = events;
        this.types = types;
    }

    @Override
    public void accept(SetActivePowerEvent event) {
        types.get(event.type).power.active.set(event.target, event.power);
        log.info("setting active {} power of {} to {}", event.type, event.target, event.power);
    }

}
