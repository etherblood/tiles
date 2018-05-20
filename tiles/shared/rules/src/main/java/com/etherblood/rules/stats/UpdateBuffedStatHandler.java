package com.etherblood.rules.stats;

import com.etherblood.events.Event;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.stats.Util.IntIntFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedStatHandler<T extends Event & HasEntity> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateBuffedStatHandler.class);
    private final String statName;
    private final int base, additive;
    private final IntIntFunction<Event> setBuffedSupply;

    public UpdateBuffedStatHandler(String statName, int base, int additive, IntIntFunction<Event> setBuffedSupply) {
        this.statName = statName;
        this.base = base;
        this.additive = additive;
        this.setBuffedSupply = setBuffedSupply;
    }

    @Override
    public void handle(T event) {
        int buffedStat = data.component(base).getOrElse(event.entity(), 0);
        LOG.debug("updating buffed {} of {}, base is {}", statName, event.entity(), buffedStat);
        for (int buff : data.component(additive).entities(x -> data.component(Components.BUFF_ON).hasValue(x, event.entity()))) {
            int additiveStat = data.component(additive).get(buff);
            LOG.debug("adding {} for additive-{}-buff: {}", additiveStat, statName, buff);
            buffedStat += additiveStat;
        }
        LOG.info("setting buffed actionPoints of {} to {}", event.entity(), buffedStat);
        events.response(setBuffedSupply.apply(event.entity(), buffedStat));
    }

}
