package com.etherblood.rules.stats.actionpoints;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedActionPointsHandler implements Consumer<UpdateBuffedActionPointsEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap baseActionPointsKey, additiveActionPointsKey, buffedActionPointsKey, buffOnKey;

    public UpdateBuffedActionPointsHandler(Logger log, EventQueue events, SimpleComponentMap baseActionPointsKey, SimpleComponentMap additiveActionPointsKey, SimpleComponentMap buffedActionPointsKey, SimpleComponentMap buffOnKey) {
        this.log = log;
        this.events = events;
        this.baseActionPointsKey = baseActionPointsKey;
        this.additiveActionPointsKey = additiveActionPointsKey;
        this.buffedActionPointsKey = buffedActionPointsKey;
        this.buffOnKey = buffOnKey;
    }

    @Override
    public void accept(UpdateBuffedActionPointsEvent event) {
        int buffedActionPoints = baseActionPointsKey.getOrElse(event.target, 0);
        log.debug("updating buffedActionPoints of {}, baseActionPoints is {}", event.target, buffedActionPoints);
        for (int buff : additiveActionPointsKey.entities(x -> buffOnKey.hasValue(x, event.target))) {
            int additiveActionPoints = additiveActionPointsKey.get(buff);
            log.debug("adding {} from additiveActionPoints-buff {}", additiveActionPointsKey, buff);
            buffedActionPoints += additiveActionPoints;
        }
        log.info("setting buffed actionPoints of {} to {}", event.target, buffedActionPoints);
        buffedActionPointsKey.set(event.target, buffedActionPoints);
        event.actionPoints = buffedActionPoints;
    }

}
