package com.etherblood.rules.abilities.endTurn;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import com.etherblood.rules.game.turns.TurnEndEvent;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class PassTurnHandler implements Consumer<PassTurnAction> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap activeTurnKey, memberOf;

    public PassTurnHandler(Logger log, EventQueue events, SimpleComponentMap activeTurnKey, SimpleComponentMap memberOf) {
        this.log = log;
        this.events = events;
        this.activeTurnKey = activeTurnKey;
        this.memberOf = memberOf;
    }

    @Override
    public void accept(PassTurnAction event) {
        log.info("passed turn of {}", event.actor);
        activeTurnKey.remove(event.actor);
        if (!activeTurnKey.exists()) {
            log.info("all actors passed, ending turn...", event.actor);
            events.trigger(new TurnEndEvent(memberOf.get(event.actor)));
        }
    }

}
