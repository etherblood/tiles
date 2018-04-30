package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class TurnStartHandler implements Consumer<TurnStartEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap activeTurnKey, memberOf;

    public TurnStartHandler(Logger log, EventQueue events, SimpleComponentMap activeTurnKey, SimpleComponentMap memberOf) {
        this.log = log;
        this.events = events;
        this.activeTurnKey = activeTurnKey;
        this.memberOf = memberOf;
    }

    @Override
    public void accept(TurnStartEvent event) {
        IntArrayList actors = memberOf.entities(x -> memberOf.hasValue(x, event.team));
        log.info("setting activeTurn for members of team {}: {}", event.team, actors);
        actors.forEach(x -> activeTurnKey.set(x, 0));
    }

}
