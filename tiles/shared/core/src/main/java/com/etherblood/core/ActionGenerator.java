package com.etherblood.core;

import java.util.List;

public interface ActionGenerator {
    
    List<Action> generatePlayerActions(int player);
    
    List<Action> generateActorActions(int actor);

    List<Action> generateSkillActions(int skill);
    
    default boolean isValidAction(Action action) {
        return generateSkillActions(action.getTargetSkill()).contains(action);
    }
}
