package com.etherblood.rules.abilities.razorleaf;

import com.etherblood.entities.SimpleEntityData;
import com.etherblood.rules.abilities.Action;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.movement.Coordinates;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class RazorleafGenerator implements ActionGenerator {

    private final SimpleEntityData data;
    private final int razorleafAction;

    public RazorleafGenerator(SimpleEntityData data, int razorleafAction) {
        this.data = data;
        this.razorleafAction = razorleafAction;
    }

    @Override
    public void availableActions(int actor, Consumer<Action> consumer) {
        int level = data.getOptional(actor, Components.Abilities.RAZORLEAF).orElse(0);
        int minRange = 0;
        int maxRange = 1 + level / 5;
        int cost = RazorleafHandler.apCost(level);
        if (level > 0 && data.has(actor, Components.POSITION) && data.getOptional(actor, Components.Stats.ActionPoints.ACTIVE).orElse(0) >= cost) {
            int sourcePosition = data.get(actor, Components.POSITION);
            for (int targetCandidate : data.query(Components.POSITION).list()) {
                int targetPosition = data.get(targetCandidate, Components.POSITION);
                int distance = Coordinates.manhattenDistance(sourcePosition, targetPosition);
                if (minRange <= distance && distance <= maxRange) {
                    consumer.accept(new Action(razorleafAction, actor, targetCandidate));
                }
            }
        }
    }

}
