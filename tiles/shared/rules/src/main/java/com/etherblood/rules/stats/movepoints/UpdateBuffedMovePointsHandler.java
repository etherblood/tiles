package com.etherblood.rules.stats.movepoints;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedMovePointsHandler implements Consumer<UpdateBuffedMovePointsEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap baseMovePointsKey, additiveMovePointsKey, buffedMovePointsKey, buffOnKey;

    public UpdateBuffedMovePointsHandler(Logger log, EventQueue events, SimpleComponentMap baseMovePointsKey, SimpleComponentMap additiveMovePointsKey, SimpleComponentMap buffedMovePointsKey, SimpleComponentMap buffOnKey) {
        this.log = log;
        this.events = events;
        this.baseMovePointsKey = baseMovePointsKey;
        this.additiveMovePointsKey = additiveMovePointsKey;
        this.buffedMovePointsKey = buffedMovePointsKey;
        this.buffOnKey = buffOnKey;
    }

    @Override
    public void accept(UpdateBuffedMovePointsEvent event) {
        int buffedMovePoints = baseMovePointsKey.getOrElse(event.target, 0);
        log.debug("updating buffedMovePoints of {}, baseMovePoints is {}", event.target, buffedMovePoints);
        for (int buff : additiveMovePointsKey.entities(x -> buffOnKey.hasValue(x, event.target))) {
            int additiveMovePoints = additiveMovePointsKey.get(buff);
            log.debug("adding {} from additiveMovePoints-buff {}", additiveMovePointsKey, buff);
            buffedMovePoints += additiveMovePoints;
        }
        log.info("setting buffed movePoints of {} to {}", event.target, buffedMovePoints);
        buffedMovePointsKey.set(event.target, buffedMovePoints);
        event.movePoints = buffedMovePoints;
    }

}
