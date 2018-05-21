package com.etherblood.rules.abilities.walk;

import com.etherblood.entities.EntityData;
import com.etherblood.rules.abilities.Action;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.movement.Coordinates;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class WalkGenerator implements ActionGenerator {

    private static final int[] OFFSETS = {Coordinates.of(1, 0), Coordinates.of(-1, 0), Coordinates.of(0, 1), Coordinates.of(0, -1)};
    private final EntityData data;
    private final IntPredicate positionAvailability;
    private final int walkAction;

    public WalkGenerator(EntityData data, IntPredicate positionAvailability, int walkAction) {
        this.data = data;
        this.positionAvailability = positionAvailability;
        this.walkAction = walkAction;
    }
    @Override
    public void availableActions(int actor, Consumer<Action> consumer) {
        if (data.component(Components.Abilities.WALK).has(actor) && data.component(Components.POSITION).has(actor) && data.component(Components.Stats.MovePoints.ACTIVE).getOrElse(actor, 0) >= 1) {
            int from = data.component(Components.POSITION).get(actor);
            for (int offset : OFFSETS) {
                int to = from + offset;
                if (positionAvailability.test(to)) {
                    consumer.accept(new Action(walkAction, actor, from, to));
                }
            }
        }
    }

}
