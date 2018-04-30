package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.Event;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class StartTurnOfRandomTeamHandler<T extends Event> implements Consumer<T> {

    private final Logger log;
    private final EventQueue events;
    private final IntUnaryOperator random;
    private final SimpleComponentMap teamKey;

    public StartTurnOfRandomTeamHandler(Logger log, EventQueue events, IntUnaryOperator random, SimpleComponentMap teamKey) {
        this.log = log;
        this.events = events;
        this.teamKey = teamKey;
        this.random = random;
    }

    @Override
    public void accept(T event) {
        IntArrayList teams = teamKey.entities();
        int team = teams.get(random.applyAsInt(teams.size()));
        log.info("selected {} as starting team", team);
        events.response(new TurnStartEvent(team));
    }

}
