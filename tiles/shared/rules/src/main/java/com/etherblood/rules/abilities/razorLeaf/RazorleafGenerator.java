package com.etherblood.rules.abilities.razorLeaf;

import com.etherblood.entities.EntityData;
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

    private final EntityData data;
    private final int razorleafAction;

    public RazorleafGenerator(EntityData data, int razorleafAction) {
        this.data = data;
        this.razorleafAction = razorleafAction;
    }

    @Override
    public void availableActions(int actor, Consumer<Action> consumer) {
        int level = data.component(Components.Abilities.RAZORLEAF).getOrElse(actor, 0);
        int minRange = 0;
        int maxRange = 1 + level / 5;
        int cost = RazorleafHandler.apCost(level);
        if (level > 0 && data.component(Components.POSITION).has(actor) && data.component(Components.Stats.ActionPoints.ACTIVE).getOrElse(actor, 0) >= cost) {
            int sourcePosition = data.component(Components.POSITION).get(actor);
            for (int targetCandidate : data.component(Components.POSITION).entities()) {
                int targetPosition = data.component(Components.POSITION).get(targetCandidate);
                int distance = Coordinates.manhattenDistance(sourcePosition, targetPosition);
                if (minRange <= distance && distance <= maxRange) {
                    consumer.accept(new Action(razorleafAction, actor, targetCandidate));
                }
            }
        }
    }

}
