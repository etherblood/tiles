package com.etherblood.rules.abilities.razorLeaf;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.movement.Coordinates;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class RazorLeafGenerator implements ActionGenerator<RazorLeafAction> {

    private final SimpleComponentMap razorLeafAbility, position, actionPoints;

    public RazorLeafGenerator(SimpleComponentMap razorLeafAbility, SimpleComponentMap positionKey, SimpleComponentMap actionPoints) {
        this.razorLeafAbility = razorLeafAbility;
        this.position = positionKey;
        this.actionPoints = actionPoints;
    }

    @Override
    public void availableActions(int actor, Consumer<RazorLeafAction> consumer) {
        int level = razorLeafAbility.getOrElse(actor, 0);
        int minRange = 0;
        int maxRange = 1 + level / 5;
        int cost = RazorLeafAction.cost(level);
        if (level > 0 && position.has(actor) && actionPoints.getOrElse(actor, 0) >= cost) {
            int sourcePosition = position.get(actor);
            for (int targetCandidate : position.entities()) {
                int targetPosition = position.get(targetCandidate);
                int distance = Coordinates.manhattenDistance(sourcePosition, targetPosition);
                if (minRange <= distance && distance <= maxRange) {
                    consumer.accept(new RazorLeafAction(actor, targetCandidate));
                }
            }
        }
    }

}
