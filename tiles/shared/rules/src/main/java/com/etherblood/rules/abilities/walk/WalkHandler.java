package com.etherblood.rules.abilities.walk;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import com.etherblood.rules.movement.SetPositionEvent;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class WalkHandler implements Consumer<WalkAction> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap activeTurnKey, movePointsKey;

    public WalkHandler(Logger log, EventQueue events, SimpleComponentMap activeTurnKey, SimpleComponentMap movePointsKey) {
        this.log = log;
        this.events = events;
        this.activeTurnKey = activeTurnKey;
        this.movePointsKey = movePointsKey;
    }

    @Override
    public void accept(WalkAction event) {
        assert activeTurnKey.has(event.actor);
        int movePoints = movePointsKey.getOrElse(event.actor, 0);
        assert movePoints >= 1;
        log.info("used 1 mp of {}", event.actor);
        movePointsKey.set(event.actor, movePoints - 1);
        events.response(new SetPositionEvent(event.actor, event.to));
    }

}
