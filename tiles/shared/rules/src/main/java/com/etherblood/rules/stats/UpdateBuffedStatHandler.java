package com.etherblood.rules.stats;

import com.etherblood.events.handlers.UnaryHandler;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedStatHandler extends GameEventHandler implements UnaryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateBuffedStatHandler.class);
    private final String statName;
    private final int base, additive;
    private final int setBuffedSupply;

    public UpdateBuffedStatHandler(String statName, int base, int additive, int setBuffedSupply) {
        this.statName = statName;
        this.base = base;
        this.additive = additive;
        this.setBuffedSupply = setBuffedSupply;
    }

    @Override
    public void handle(int entity) {
        int buffedStat = data.component(base).getOrElse(entity, 0);
        LOG.debug("updating buffed {} of {}, base is {}", statName, entity, buffedStat);
        for (int buff : data.component(additive).entities(x -> data.component(Components.BUFF_ON).hasValue(x, entity))) {
            int additiveStat = data.component(additive).get(buff);
            LOG.debug("adding {} for additive-{}-buff: {}", additiveStat, statName, buff);
            buffedStat += additiveStat;
        }
        LOG.info("setting buffed {} of {} to {}", statName, entity, buffedStat);
        events.response(setBuffedSupply, entity, buffedStat);
    }

}
