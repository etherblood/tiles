package com.etherblood.rules.stats;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntSet;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.events.EntityEvent;
import com.etherblood.rules.events.VoidEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class RefreshAllStatHandler extends AbstractGameEventHandler implements EventHandler<VoidEvent> {

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

    public void handle() {
        IntSet entities = new IntSet();
        for (int entity : data.query(base).list()) {
            entities.set(entity);
        }
        for (int entity : data.query(active).list()) {
            entities.set(entity);
        }
        for (int entity : data.query(buffed).list()) {
            entities.set(entity);
        }
        for (int entity : data.query(additive).list()) {
            entities.set(data.get(entity, Components.BUFF_ON));
        }

        IntArrayList list = new IntArrayList(entities.size());
        entities.foreach(list::add);
        list.sort();

        LOG.info("refreshing {} for {}", statName, list);

        list.foreach(entity -> {
            events.sub(new EntityEvent(updateBuffedSupply, entity));
        });
        list.foreach(entity -> {
            events.sub(new EntityEvent(resetActiveSupply, entity));
        });
    }

    @Override
    public void handle(VoidEvent event) {
        handle();
    }

}
