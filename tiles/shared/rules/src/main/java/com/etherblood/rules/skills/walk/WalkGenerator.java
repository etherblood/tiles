package com.etherblood.rules.skills.walk;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.entities.EntityData;
import com.etherblood.rules.skills.Action;
import com.etherblood.rules.skills.ActionGenerator;
import com.etherblood.rules.events.EntityMoveEventMeta;
import com.etherblood.rules.util.Coordinates;
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
    private final EntityMoveEventMeta walkAction;
    private final ComponentMeta walk, position, movePoints;

    public WalkGenerator(EntityData data, IntPredicate positionAvailability, EntityMoveEventMeta walkAction, ComponentMeta walk, ComponentMeta position, ComponentMeta movePoints) {
        this.data = data;
        this.positionAvailability = positionAvailability;
        this.walkAction = walkAction;
        this.walk = walk;
        this.position = position;
        this.movePoints = movePoints;
    }

    @Override
    public void availableActions(int actor, Consumer<Action> consumer) {
        if (data.has(actor, walk.id) && data.has(actor, position.id) && data.getOptional(actor, movePoints.id).orElse(0) >= 1) {
            int from = data.get(actor, position.id);
            for (int offset : OFFSETS) {
                int to = Coordinates.sum(from, offset);
                if (positionAvailability.test(to)) {
                    consumer.accept(new Action(walkAction.create(actor, from, to)));
                }
            }
        }
    }

}
