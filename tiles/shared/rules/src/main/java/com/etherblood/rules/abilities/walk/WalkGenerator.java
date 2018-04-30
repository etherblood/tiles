package com.etherblood.rules.abilities.walk;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.movement.Coordinates;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class WalkGenerator implements ActionGenerator<WalkAction> {

    private static final int[] OFFSETS = {Coordinates.of(1, 0), Coordinates.of(-1, 0), Coordinates.of(0, 1), Coordinates.of(0, -1)};
    private final SimpleComponentMap walkAbilityKey, positionKey, movePointsKey;
    private final IntPredicate positionAvailability;

    public WalkGenerator(SimpleComponentMap walkAbilityKey, SimpleComponentMap positionKey, SimpleComponentMap movePointsKey, IntPredicate positionAvailability) {
        this.walkAbilityKey = walkAbilityKey;
        this.positionKey = positionKey;
        this.movePointsKey = movePointsKey;
        this.positionAvailability = positionAvailability;
    }
    
    @Override
    public void availableActions(int actor, Consumer<WalkAction> consumer) {
        if(walkAbilityKey.has(actor) && positionKey.has(actor) && movePointsKey.getOrElse(actor, 0) >= 1) {
            int from = positionKey.get(actor);
            for (int offset : OFFSETS) {
                int to = from + offset;
                if(positionAvailability.test(to)) {
                    consumer.accept(new WalkAction(actor, from, to));
                }
            }
        }
    }

}
