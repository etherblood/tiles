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
        int baseValue = data.getOptional(entity, base).orElse(0);
        int additiveValue = data.query(additive).compute(Integer::sum, x -> data.hasValue(x, Components.BUFF_ON, entity)).orElse(0);
        LOG.info("updating buffed {} of {} to {}, base is {}, additive is {}", statName, entity, baseValue + additiveValue, baseValue, additiveValue);
        events.response(setBuffedSupply, entity, baseValue + additiveValue);
    }

}
