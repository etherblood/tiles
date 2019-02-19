package com.etherblood.rules.skills;

import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SkillCostHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SkillCostHandler.class);

    private final boolean validate;
    private final int skillOwner;
    private final SkillCostMeta[] skillCosts;

    public SkillCostHandler(boolean validate, int skillOwner, SkillCostMeta... skillCosts) {
        this.validate = validate;
        this.skillOwner = skillOwner;
        this.skillCosts = skillCosts;
    }

    @Override
    public void handle(EntityValueEvent event) {
        int skill = event.entity;
        int actor = data.get(skill, skillOwner);
        if (validate) {
            for (SkillCostMeta skillCost : skillCosts) {
                int cost = data.getOptional(skill, skillCost.cost).orElse(0);
                if (cost > 0) {
                    int value = data.getOptional(actor, skillCost.stat).orElse(0);
                    if (cost > value) {
                        event.cancel();
                        LOG.warn("Skill cancelled, cannot pay {}.", skillCost.statName);
                        return;
                    }
                }
            }
        }
        for (SkillCostMeta skillCost : skillCosts) {
            int cost = data.getOptional(skill, skillCost.cost).orElse(0);
            if (cost > 0) {
                int value = data.getOptional(actor, skillCost.stat).orElse(0);
                data.set(actor, skillCost.stat, value - cost);
                LOG.debug("Paid {} {}. ({} -> {})", cost, skillCost.statName, value, value - cost);
            }
        }
    }

}
