package com.etherblood.rules.stats.types.power;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import com.etherblood.rules.context.TypeComponentMaps;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class SetAdditivePowerHandler implements Consumer<SetAdditivePowerEvent> {

    private final Logger log;
    private final EventQueue events;
    private final TypeComponentMaps types;
    private final SimpleComponentMap buffOn;

    public SetAdditivePowerHandler(Logger log, EventQueue events, TypeComponentMaps types, SimpleComponentMap buffOn) {
        this.log = log;
        this.events = events;
        this.types = types;
        this.buffOn = buffOn;
    }

    @Override
    public void accept(SetAdditivePowerEvent event) {
        types.get(event.type).power.additive.set(event.target, event.power);
        log.info("setting additive {} power of {} to {}", event.type, event.target, event.power);

        events.response(new RefreshBuffedPowerEvent(buffOn.get(event.target), event.type));
    }

}
