package com.etherblood.rules.stats;

import com.etherblood.collections.IntSet;
import com.etherblood.events.handlers.NullaryHandler;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class RefreshAllStatHandler extends GameEventHandler implements NullaryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RefreshAllStatHandler.class);

    private final String statName;
    private final int base, active, buffed, additive;
    private final int updateBuffedSupply, resetActiveSupply;

    public RefreshAllStatHandler(String statName, int base, int active, int buffed, int additive, int updateBuffedSupply, int resetActiveSupply) {
        this.statName = statName;
        this.base = base;
        this.active = active;
        this.buffed = buffed;
        this.additive = additive;
        this.updateBuffedSupply = updateBuffedSupply;
        this.resetActiveSupply = resetActiveSupply;
    }
    
    @Override
    public void handle() {
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
            events.trigger(updateBuffedSupply, entity);
        });
        entities.foreach(entity -> {
            events.trigger(resetActiveSupply, entity);
        });
    }

}
