package com.etherblood.rules.stats;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntSet;
import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityEventMeta;
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
    private final ComponentMeta base, active, buffed, additive, buffOn;
    private final EntityEventMeta updateBuffedSupply, resetActiveSupply;

    public RefreshAllStatHandler(String statName, ComponentMeta base, ComponentMeta active, ComponentMeta buffed, ComponentMeta additive, ComponentMeta buffOn, EntityEventMeta updateBuffedSupply, EntityEventMeta resetActiveSupply) {
        this.statName = statName;
        this.base = base;
        this.active = active;
        this.buffed = buffed;
        this.additive = additive;
        this.buffOn = buffOn;
        this.updateBuffedSupply = updateBuffedSupply;
        this.resetActiveSupply = resetActiveSupply;
    }

    public void handle() {
        IntSet entities = new IntSet();
        for (int entity : data.query(base.id).list()) {
            entities.set(entity);
        }
        for (int entity : data.query(active.id).list()) {
            entities.set(entity);
        }
        for (int entity : data.query(buffed.id).list()) {
            entities.set(entity);
        }
        for (int entity : data.query(additive.id).list()) {
            entities.set(data.get(entity, buffOn.id));
        }

        IntArrayList list = new IntArrayList(entities.size());
        entities.foreach(list::add);
        list.sort();

        LOG.info("refreshing {} for {}", statName, list);

        list.foreach(entity -> {
            events.sub(updateBuffedSupply.create(entity));
        });
        list.foreach(entity -> {
            events.sub(resetActiveSupply.create(entity));
        });
    }

    @Override
    public void handle(VoidEvent event) {
        handle();
    }

}
