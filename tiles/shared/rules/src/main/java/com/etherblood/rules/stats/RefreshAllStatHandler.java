package com.etherblood.rules.stats;

import com.etherblood.collections.IntSet;
import com.etherblood.events.Event;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import java.util.function.IntFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class RefreshAllStatHandler<T extends Event> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(RefreshAllStatHandler.class);

    private final String statName;
    private final int base, active, buffed, additive;
    private final IntFunction<Event> updateBuffedSupply, resetActiveSupply;

    public RefreshAllStatHandler(String statName, int base, int active, int buffed, int additive, IntFunction<Event> updateBuffedSupply, IntFunction<Event> resetActiveSupply) {
        this.statName = statName;
        this.base = base;
        this.active = active;
        this.buffed = buffed;
        this.additive = additive;
        this.updateBuffedSupply = updateBuffedSupply;
        this.resetActiveSupply = resetActiveSupply;
    }
    
    @Override
    public void handle(T event) {
        IntSet entities = new IntSet();
        for (int entity : data.component(base).entities()) {
            entities.set(entity);
        }
        for (int entity : data.component(active).entities()) {
            entities.set(entity);
        }
        for (int entity : data.component(buffed).entities()) {
            entities.set(entity);
        }
        for (int entity : data.component(additive).entities()) {
            entities.set(data.component(Components.BUFF_ON).get(entity));
        }

        LOG.info("refreshing {} for {}", statName, entities);

        entities.foreach(entity -> {
            events.trigger(updateBuffedSupply.apply(entity));
        });
        entities.foreach(entity -> {
            events.trigger(resetActiveSupply.apply(entity));
        });
    }

}
