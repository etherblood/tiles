package com.etherblood.rules.stats.types.toughness;

import com.etherblood.events.EventQueue;
import com.etherblood.rules.context.TypeComponentMaps;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class SetActiveToughnessHandler implements Consumer<SetActiveToughnessEvent> {

    private final Logger log;
    private final EventQueue events;
    private final TypeComponentMaps types;

    public SetActiveToughnessHandler(Logger log, EventQueue events, TypeComponentMaps types) {
        this.log = log;
        this.events = events;
        this.types = types;
    }

    @Override
    public void accept(SetActiveToughnessEvent event) {
        types.get(event.type).toughness.active.set(event.target, event.power);
        log.info("setting active {} toughness of {} to {}", event.type, event.target, event.power);
    }

}
