package com.etherblood.rules.stats.movepoints;

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
public class RefreshAllMovePointsHandler<T extends Event> implements Consumer<T> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap activeMovePointsKey, baseMovePointsKey, buffedMovePointsKey, additiveMovePointsKey, buffOnKey;

    public RefreshAllMovePointsHandler(Logger log, EventQueue events, SimpleComponentMap activeMovePointsKey, SimpleComponentMap baseMovePointsKey, SimpleComponentMap buffedMovePointsKey, SimpleComponentMap additiveMovePointsKey, SimpleComponentMap buffOnKey) {
        this.log = log;
        this.events = events;
        this.activeMovePointsKey = activeMovePointsKey;
        this.baseMovePointsKey = baseMovePointsKey;
        this.buffedMovePointsKey = buffedMovePointsKey;
        this.additiveMovePointsKey = additiveMovePointsKey;
        this.buffOnKey = buffOnKey;
    }

    @Override
    public void accept(T event) {
        IntSet movePointsEntities = new IntSet();
        for (int entity : baseMovePointsKey.entities()) {
            movePointsEntities.set(entity);
        }
        for (int entity : activeMovePointsKey.entities()) {
            movePointsEntities.set(entity);
        }
        for (int entity : buffedMovePointsKey.entities()) {
            movePointsEntities.set(entity);
        }
        for (int entity : additiveMovePointsKey.entities()) {
            movePointsEntities.set(buffOnKey.get(entity));
        }

        log.info("refreshing movePoints for {}", movePointsEntities);

        movePointsEntities.foreach(movePointsEntity -> {
            events.trigger(new UpdateBuffedMovePointsEvent(movePointsEntity));
        });
        movePointsEntities.foreach(movePointsEntity -> {
            events.trigger(new ResetActiveMovePointsEvent(movePointsEntity));
        });
    }

}
