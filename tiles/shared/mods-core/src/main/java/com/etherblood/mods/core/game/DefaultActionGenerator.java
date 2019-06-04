package com.etherblood.mods.core.game;

import com.etherblood.collections.IntList;
import com.etherblood.core.Action;
import com.etherblood.core.ActionGenerator;
import com.etherblood.core.util.Coordinates;
import com.etherblood.core.util.Flags;
import com.etherblood.mods.core.game.components.CoreComponents;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultActionGenerator implements ActionGenerator {

    private final CoreComponents core;

    public DefaultActionGenerator(CoreComponents core) {
        this.core = core;
    }

    @Override
    public List<Action> generatePlayerActions(int player) {
        int playerFlags = Flags.toFlag(player);
        List<Action> result = new ArrayList<>();
        for (int actor : core.actor.active.query().list(x -> !Flags.isEmpty(Flags.intersection(playerFlags, core.actor.controlledBy.getOptional(x).orElse(0))))) {
            result.addAll(generateActorActions(actor));
        }
        return result;
    }

    @Override
    public List<Action> generateActorActions(int actor) {
        if (!core.actor.active.has(actor)) {
            return Collections.emptyList();
        }
        List<Action> result = new ArrayList<>();
        for (int skill : core.skill.ofActor.query().list(x -> core.skill.ofActor.hasValue(x, actor))) {
            result.addAll(generateSkillActions(skill));
        }
        return result;
    }

    @Override
    public List<Action> generateSkillActions(int skill) {
        int actor = core.skill.ofActor.get(skill);
        if (core.skill.cooldown.active.has(skill)) {
            return Collections.emptyList();
        }
        if (core.skill.cost.actionPoints.getOptional(skill).orElse(0) > core.stats.actionPoints.active.getOptional(actor).orElse(0)) {
            return Collections.emptyList();
        }
        if (core.skill.cost.movePoints.getOptional(skill).orElse(0) > core.stats.movePoints.active.getOptional(actor).orElse(0)) {
            return Collections.emptyList();
        }
        if (core.skill.cost.health.getOptional(skill).orElse(0) > core.stats.health.active.getOptional(actor).orElse(0)) {
            return Collections.emptyList();
        }
        List<Action> result = new ArrayList<>();
        if (core.skill.targeting.position.required.has(skill)) {
            int manhattanRange = core.skill.targeting.position.manhattanRange.getOptional(skill).orElse(Integer.MAX_VALUE);
            int actorPosition = core.actor.position.get(actor);
            int actorX = Coordinates.x(actorPosition);
            int actorY = Coordinates.y(actorPosition);
            int mapSize = core.mapSize.query().uniqueValue().getAsInt();
            int width = Coordinates.x(mapSize);
            int height = Coordinates.y(mapSize);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int targetPosition = Coordinates.of(x, y);
                    if (Coordinates.manhattenDistance(actorPosition, targetPosition) > manhattanRange) {
                        continue;
                    }
                    if(actorX != x && actorY != y && core.skill.targeting.position.requiresLine.has(skill)) {
                        continue;
                    }
                    IntList targetActors = core.actor.position.query().list(z -> core.actor.position.hasValue(z, targetPosition));
                    if (targetActors.isEmpty()) {
                        if (!core.skill.targeting.actor.none.has(skill)) {
                            continue;
                        }
                    } else {
                        if (targetActors.size() != 1) {
                            throw new UnsupportedOperationException();
                        }
                        int targetActor = targetActors.get(0);
                        if (targetActor == actor && !core.skill.targeting.actor.self.has(skill)) {
                            continue;
                        }
                        int team = core.actor.memberOf.get(actor);
                        int targetTeam = core.actor.memberOf.get(targetActor);
                        if (team == targetTeam) {
                            if (!core.skill.targeting.actor.ally.has(skill)) {
                                continue;
                            }
                        } else {
                            if (!core.skill.targeting.actor.enemy.has(skill)) {
                                continue;
                            }
                        }
                    }
                    result.add(Action.builder(skill).withTargetPosition(targetPosition).build());
                }
            }
        } else {
            result.add(Action.builder(skill).build());
        }
        return result;
    }

}
