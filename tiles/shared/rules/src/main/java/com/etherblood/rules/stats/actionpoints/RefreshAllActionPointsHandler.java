package com.etherblood.rules.stats.actionpoints;

import com.etherblood.collections.IntSet;
import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.Event;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class RefreshAllActionPointsHandler<T extends Event> implements Consumer<T> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap activeActionPointsKey, baseActionPointsKey, buffedActionPointsKey, additiveActionPointsKey, buffOnKey;

    public RefreshAllActionPointsHandler(Logger log, EventQueue events, SimpleComponentMap activeActionPointsKey, SimpleComponentMap baseActionPointsKey, SimpleComponentMap buffedActionPointsKey, SimpleComponentMap additiveActionPointsKey, SimpleComponentMap buffOnKey) {
        this.log = log;
        this.events = events;
        this.activeActionPointsKey = activeActionPointsKey;
        this.baseActionPointsKey = baseActionPointsKey;
        this.buffedActionPointsKey = buffedActionPointsKey;
        this.additiveActionPointsKey = additiveActionPointsKey;
        this.buffOnKey = buffOnKey;
    }

    @Override
    public void accept(T event) {
        IntSet actionPointsEntities = new IntSet();
        for (int entity : baseActionPointsKey.entities()) {
            actionPointsEntities.set(entity);
        }
        for (int entity : activeActionPointsKey.entities()) {
            actionPointsEntities.set(entity);
        }
        for (int entity : buffedActionPointsKey.entities()) {
            actionPointsEntities.set(entity);
        }
        for (int entity : additiveActionPointsKey.entities()) {
            actionPointsEntities.set(buffOnKey.get(entity));
        }

        log.info("refreshing actionPoints for {}", actionPointsEntities);

        actionPointsEntities.foreach(actionPointsEntity -> {
            events.trigger(new UpdateBuffedActionPointsEvent(actionPointsEntity));
        });
        actionPointsEntities.foreach(actionPointsEntity -> {
            events.trigger(new ResetActiveActionPointsEvent(actionPointsEntity));
        });
    }

}
