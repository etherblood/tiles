package com.etherblood.mods.core.game;

import com.etherblood.core.Action;
import com.etherblood.core.ActionGenerator;
import java.util.List;
import java.util.Random;

public class RandomMover {

    private final ActionGenerator actionGenerator;
    private final Random random;

    public RandomMover(ActionGenerator actionGenerator, Random random) {
        this.actionGenerator = actionGenerator;
        this.random = random;
    }

    public Action randomAction(int player) {
        List<Action> actions = actionGenerator.generatePlayerActions(player);
        return actions.get(random.nextInt(actions.size()));
    }
}
