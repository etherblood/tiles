package com.etherblood.rules.skills.razorleaf;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.entities.EntityData;
import com.etherblood.rules.skills.Action;
import com.etherblood.rules.skills.ActionGenerator;
import com.etherblood.rules.events.EntityValueEventMeta;
import com.etherblood.rules.util.Coordinates;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class RazorleafGenerator implements ActionGenerator {

    private final EntityData data;
    private final EntityValueEventMeta razorleafAction;
    private final ComponentMeta razorleaf, position, actionPoints;

    public RazorleafGenerator(EntityData data, EntityValueEventMeta razorleafAction, ComponentMeta razorleaf, ComponentMeta position, ComponentMeta actionPoints) {
        this.data = data;
        this.razorleafAction = razorleafAction;
        this.razorleaf = razorleaf;
        this.position = position;
        this.actionPoints = actionPoints;
    }

    @Override
    public void availableActions(int actor, Consumer<Action> consumer) {
        int level = data.getOptional(actor, razorleaf.id).orElse(0);
        int minRange = 0;
        int maxRange = 1 + level / 5;
        int cost = RazorleafHandler.apCost(level);
        if (level > 0 && data.has(actor, position.id) && data.getOptional(actor, actionPoints.id).orElse(0) >= cost) {
            int sourcePosition = data.get(actor, position.id);
            for (int targetCandidate : data.query(position.id).list()) {
                int targetPosition = data.get(targetCandidate, position.id);
                int distance = Coordinates.manhattenDistance(sourcePosition, targetPosition);
                if (minRange <= distance && distance <= maxRange) {
                    consumer.accept(new Action(razorleafAction.create(actor, targetCandidate)));
                }
            }
        }
    }

}
