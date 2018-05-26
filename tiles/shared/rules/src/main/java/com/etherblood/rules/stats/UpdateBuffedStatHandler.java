package com.etherblood.rules.stats;

import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedStatHandler extends GameEventHandler {

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

    public void handle(int entity) {
        int baseValue = data.getOptional(entity, base).orElse(0);
        int additiveValue = data.query(additive).aggregate(Integer::sum, hasValue(Components.BUFF_ON, entity)).orElse(0);
        LOG.info("updating buffed {} of {} to {}, base is {}, additive is {}", statName, entity, baseValue + additiveValue, baseValue, additiveValue);
        events.response(setBuffedSupply, entity, baseValue + additiveValue);
    }

}
