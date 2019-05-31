package com.etherblood.mods.core.game;

import com.etherblood.collections.IntList;
import com.etherblood.core.Action;
import com.etherblood.core.util.Coordinates;
import com.etherblood.core.util.Flags;
import com.etherblood.mods.core.game.components.CoreComponents;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomMover {

    private final CoreComponents core;
    private final Random random;

    public RandomMover(CoreComponents core, Random random) {
        this.core = core;
        this.random = random;
    }

    public Action randomAction(int player) {
        return randomAction(Arrays.asList(player));
    }

    public Action randomAction(List<Integer> players) {
        int playerFlags = toFlags(players);
        List<Action> result = new ArrayList<>();
        for (int actor : core.actor.active.query().list(x -> !Flags.isEmpty(Flags.intersection(playerFlags, core.actor.controlledBy.getOptional(x).orElse(0))))) {
            System.out.println("generating actions for actor #" + actor + "(" + core.name.getGeneric(actor) + ")");
            generateActorActions(actor, result);
        }
        System.out.println("Generated actions " + result);
        return result.get(random.nextInt(result.size()));
    }

    private void generateActorActions(int actor, List<Action> out) {
        for (int skill : core.skill.ofActor.query().list(x -> core.skill.ofActor.hasValue(x, actor))) {
            System.out.println("generating actions for #" + skill + "(" + core.name.getGeneric(skill) + ")");
            if (core.skill.cooldown.active.has(skill)) {
                continue;
            }
            if (core.skill.cost.actionPoints.getOptional(skill).orElse(0) > core.stats.actionPoints.active.getOptional(actor).orElse(0)) {
                continue;
            }
            if (core.skill.cost.movePoints.getOptional(skill).orElse(0) > core.stats.movePoints.active.getOptional(actor).orElse(0)) {
                continue;
            }
            if (core.skill.cost.health.getOptional(skill).orElse(0) > core.stats.health.active.getOptional(actor).orElse(0)) {
                continue;
            }

            if (core.skill.targeting.position.required.has(skill)) {
                int manhattanRange = core.skill.targeting.position.manhattanRange.getOptional(skill).orElse(Integer.MAX_VALUE);
                int actorPosition = core.actor.position.get(actor);
                int mapSize = core.mapSize.query().uniqueValue().getAsInt();
                int width = Coordinates.x(mapSize);
                int height = Coordinates.y(mapSize);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int targetPosition = Coordinates.of(x, y);
                        if (Coordinates.manhattenDistance(actorPosition, targetPosition) > manhattanRange) {
                            continue;
                        }
                        IntList targetActors = core.actor.position.query().list(z -> core.actor.position.hasValue(z, targetPosition));
                        if (targetActors.isEmpty()) {
                            if (core.skill.targeting.actor.required.has(skill)) {
                                continue;
                            }
                        } else {
                            if (core.skill.targeting.position.empty.has(skill)) {
                                continue;
                            }
                        }
                        if ((targetActors.size() != 1 || targetActors.get(0) != actor) && core.skill.targeting.actor.self.has(skill)) {
                            continue;
                        }
                        for (int i = targetActors.size() - 1; i >= 0; i--) {
                            if (targetActors.get(i) == actor) {
                                targetActors.swapRemoveAt(i);
                            }
                        }
                        int team = core.actor.memberOf.get(actor);
                        if(core.skill.targeting.actor.ally.has(skill) && targetActors.stream().filter(core.actor.memberOf::has).map(core.actor.memberOf::get).noneMatch(targetTeam -> targetTeam == team)) {
                            continue;
                        }
                        if(core.skill.targeting.actor.enemy.has(skill) && targetActors.stream().filter(core.actor.memberOf::has).map(core.actor.memberOf::get).noneMatch(targetTeam -> targetTeam != team)) {
                            continue;
                        }
                        out.add(Action.builder(skill).withTargetPosition(targetPosition).build());
                    }
                }
            } else {
                out.add(Action.builder(skill).build());
            }
        }
    }

    private int toFlags(List<Integer> indices) {
        int flags = 0;
        for (int index : indices) {
            flags = Flags.union(flags, Flags.toFlag(index));
        }
        return flags;
    }
}
