package com.etherblood.rules.game.setup;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntToIntHashMap;
import com.etherblood.collections.IntToIntMap;
import com.etherblood.entities.EntityData;
import com.etherblood.rules.game.setup.config.GameConfig;
import com.etherblood.rules.GameContext;
import com.etherblood.rules.components.ComponentDefinitions;
import com.etherblood.rules.game.setup.config.CharacterConfig;
import com.etherblood.rules.game.setup.config.ObjectConfig;
import com.etherblood.rules.game.setup.config.PlayerConfig;
import com.etherblood.rules.game.setup.config.SkillConfig;
import com.etherblood.rules.game.setup.config.TeamConfig;
import com.etherblood.rules.util.Coordinates;
import com.etherblood.rules.util.Flags;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameContextSetup {

    public void populate(GameContext context, GameConfig config) {
        EntityData data = context.getData();
        ComponentDefinitions components = context.componentDefs;
        Map<String, Integer> componentIdMap = context.componentMetaList.stream().collect(Collectors.toMap(x -> x.name, x -> x.id));

        int gameState = data.createEntity();
        data.set(gameState, components.mapSize.id, Coordinates.of(config.map.width, config.map.height));

        IntToIntMap teams = new IntToIntHashMap();
        for (TeamConfig team : config.map.teams) {
            teams.set(team.id, data.createEntity());
        }
        for (int i = 0; i < config.map.teams.length; i++) {
            int j = (i + 1) % config.map.teams.length;
            data.set(teams.get(config.map.teams[i].id), components.nextTeam.id, teams.get(config.map.teams[j].id));
        }

        IntToIntMap characters = new IntToIntHashMap();
        for (CharacterConfig character : config.characters) {
            character.entity = data.createEntity();
            for (Map.Entry<String, Integer> entry : character.components.entrySet()) {
                data.set(character.entity, componentIdMap.get(entry.getKey()), entry.getValue());
            }
            data.set(character.entity, components.memberOf.id, teams.get(character.teamId));
            characters.set(character.id, character.entity);
            int controlFlags = 0;
            for (PlayerConfig player : config.players) {
                if (Arrays.stream(player.characterIds).anyMatch(x -> x == character.id)) {
                    controlFlags = Flags.union(controlFlags, Flags.toFlag(player.id));
                }
            }
            if (Flags.isEmpty(controlFlags)) {
                throw new IllegalStateException();
            }
            data.set(character.entity, components.controlledBy.id, controlFlags);
            data.set(character.entity, components.alive.id, 1);
            for (SkillConfig skill : character.skills) {
                initSkill(skill, data, componentIdMap, components, character);
            }
            if (character.walkSkill != null) {
                initSkill(character.walkSkill, data, componentIdMap, components, character);
            }
            character.passTurnSkill = new SkillConfig();
            character.passTurnSkill.effectPassTurn = 1;
            initSkill(character.passTurnSkill, data, componentIdMap, components, character);
        }

        for (TeamConfig team : config.map.teams) {
            int teamEntity = teams.get(team.id);
            List<Integer> positions = new ArrayList<>(Arrays.stream(team.startCoordinates).boxed().collect(Collectors.toList()));
            IntArrayList teamMembers = data.query(components.memberOf.id).list(x -> data.get(x, components.memberOf.id) == teamEntity);
            for (int i = 0; i < teamMembers.size(); i++) {
                data.set(teamMembers.get(i), components.position.id, positions.get(i));
            }
        }

        for (ObjectConfig object : config.map.objects) {
            int entity = data.createEntity();
            for (Map.Entry<String, Integer> entry : object.components.entrySet()) {
                data.set(entity, componentIdMap.get(entry.getKey()), entry.getValue());
            }
        }

//        int bulbasaur = data.createEntity();
//        pokemons.bulbasaur(bulbasaur);
//        data.set(bulbasaur, components.memberOf.id, teamA);
//        data.set(bulbasaur, components.position.id, Coordinates.of(1, 1));
//        data.set(bulbasaur, components.walkAbility.id, 1);
//        data.set(bulbasaur, components.razorleafAbility.id, 4);
//
////        int testBuff = data.createEntity();
////        data.set(testBuff, components.health.additive.id, 3);
////        data.set(testBuff, components.buffOn.id, bulbasaur);
//
//        int charmander = data.createEntity();
//        pokemons.charmander(charmander);
//        data.set(charmander, components.memberOf.id, teamB);
//        data.set(charmander, components.position.id, Coordinates.of(5, 9));
//        data.set(charmander, components.walkAbility.id, 1);
//
//        int squirtle = data.createEntity();
//        pokemons.squirtle(squirtle);
//        data.set(squirtle, components.memberOf.id, teamB);
//        data.set(squirtle, components.position.id, Coordinates.of(3, 5));
//        data.set(squirtle, components.walkAbility.id, 1);
//
//        for (int y = 4; y < 6; y++) {
//            for (int x = 4; x < 6; x++) {
//                int obstacle = data.createEntity();
//                data.set(obstacle, components.position.id, Coordinates.of(x, y));
//                data.set(obstacle, components.arenaObstacle.id, 1);
//            }
//        }
//        context.startGame();
        context.config = config;
    }

    private void initSkill(SkillConfig skill, EntityData data, Map<String, Integer> componentIdMap, ComponentDefinitions components, CharacterConfig character) {
        skill.entity = data.createEntity();
        for (Map.Entry<String, Integer> entry : skill.components.entrySet()) {
            data.set(skill.entity, componentIdMap.get(entry.getKey()), entry.getValue());
        }
        data.set(skill.entity, components.skill.owner.id, character.entity);

        setOptional(data, skill.entity, components.skill.cooldown.base.id, skill.cooldown);
        setOptional(data, skill.entity, components.skill.actionPointsCost.base.id, skill.actionPointsCost);
        setOptional(data, skill.entity, components.skill.movePointsCost.base.id, skill.movePointsCost);
        setOptional(data, skill.entity, components.skill.healthCost.base.id, skill.healthCost);

        setOptional(data, skill.entity, components.skill.targeting.self.id, skill.targetingSelf);
        setOptional(data, skill.entity, components.skill.targeting.ally.id, skill.targetingAlly);
        setOptional(data, skill.entity, components.skill.targeting.enemy.id, skill.targetingEnemy);
        setOptional(data, skill.entity, components.skill.targeting.empty.id, skill.targetingEmpty);

        setOptional(data, skill.entity, components.skill.targeting.manhattanRange.id, skill.targetingManhattanRange);
        setOptional(data, skill.entity, components.skill.targeting.requiresPath.id, skill.targetingRequiresPath);
        setOptional(data, skill.entity, components.skill.targeting.requiresSight.id, skill.targetingRequiresSight);

        setOptional(data, skill.entity, components.skill.effect.passTurn.id, skill.effectPassTurn);
        setOptional(data, skill.entity, components.skill.effect.walkToTarget.id, skill.effectWalkToTarget);
        setOptional(data, skill.entity, components.skill.effect.damageToTarget.air.id, skill.effectAirDamageToTarget);
        setOptional(data, skill.entity, components.skill.effect.damageToTarget.earth.id, skill.effectEarthDamageToTarget);
        setOptional(data, skill.entity, components.skill.effect.damageToTarget.fire.id, skill.effectFireDamageToTarget);
        setOptional(data, skill.entity, components.skill.effect.damageToTarget.water.id, skill.effectWaterDamageToTarget);
    }

    private static void setOptional(EntityData data, int entity, int component, Integer value) {
        if (value != null) {
            data.set(entity, component, value);
        }
    }
}
