package com.etherblood.rules.stats.types.toughness;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import com.etherblood.rules.context.TypeComponentMaps;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class SetAdditiveToughnessHandler implements Consumer<SetAdditiveToughnessEvent> {

    private final Logger log;
    private final EventQueue events;
    private final TypeComponentMaps types;
    private final SimpleComponentMap buffOn;

    public SetAdditiveToughnessHandler(Logger log, EventQueue events, TypeComponentMaps types, SimpleComponentMap buffOn) {
        this.log = log;
        this.events = events;
        this.types = types;
        this.buffOn = buffOn;
    }

    @Override
    public void accept(SetAdditiveToughnessEvent event) {
        types.get(event.type).toughness.additive.set(event.target, event.power);
        log.info("setting additive {} toughness of {} to {}", event.type, event.target, event.power);

        events.response(new RefreshBuffedToughnessEvent(buffOn.get(event.target), event.type));
    }

}
