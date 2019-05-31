package com.etherblood.mods.core.game;

import com.etherblood.mods.core.game.components.CoreComponents;
import com.etherblood.core.ActionSystem;
import com.etherblood.core.AnimationsController;
import com.etherblood.core.EntityFactory;
import com.etherblood.core.GameContext;
import com.etherblood.core.GameSystem;
import com.etherblood.core.Mod;
import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.random.HistoryRandom;
import com.etherblood.core.to.GameConfig;
import com.etherblood.core.util.Coordinates;
import com.etherblood.core.util.Flags;
import com.etherblood.mods.core.game.systems.core.iteration.ConvertTriggerToEffectSystem;
import com.etherblood.mods.core.game.systems.CooldownUpdateSystem;
import com.etherblood.mods.core.game.systems.ResetActivatedActorActionPointsSystem;
import com.etherblood.mods.core.game.systems.ResetActivatedActorMovePointsSystem;
import com.etherblood.mods.core.game.systems.attack.animation.AttackAnimationSystem;
import com.etherblood.mods.core.game.systems.walk.animation.WalkAnimationSystem;
import com.etherblood.mods.core.game.systems.attack.AttackSkillTriggerSystem;
import com.etherblood.mods.core.game.systems.death.DeathSystem;
import com.etherblood.mods.core.game.systems.death.DieWithoutHealthSystem;
import com.etherblood.mods.core.game.systems.attack.EarthDamageToTargetSystem;
import com.etherblood.mods.core.game.systems.core.cost.MovePointsCostSystem;
import com.etherblood.mods.core.game.systems.core.iteration.EffectPhaseCleanupSystem;
import com.etherblood.mods.core.game.systems.core.turn.ActorActivationSystem;
import com.etherblood.mods.core.game.systems.core.turn.NextTeamsTurnSystem;
import com.etherblood.mods.core.game.systems.pass.PassTurnOfTargetSystem;
import com.etherblood.mods.core.game.systems.pass.PassTurnSkillTriggerSystem;
import com.etherblood.mods.core.game.systems.walk.WalkToTargetPositionSystem;
import com.etherblood.mods.core.game.systems.core.condition.EmptyTargetRequiredSystem;
import com.etherblood.mods.core.game.systems.core.condition.ManhattanRangeRequiredSystem;
import com.etherblood.mods.core.game.systems.core.cost.ActionPointsCostSystem;
import com.etherblood.mods.core.game.systems.core.cost.CooldownCostSystem;
import com.etherblood.mods.core.game.systems.walk.WalkSkillTriggerSystem;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

public class DefaultMod implements Mod {

    @Override
    public GameContext createContext() {
        EntityFactory entityFactory = new EntityFactory();
        ComponentRegistry registry = new ComponentRegistry();
        CoreComponents core = new CoreComponents(registry);
        AnimationsController animations = new AnimationsController();
        HistoryRandom random = new HistoryRandom(new SecureRandom()::nextInt);
        List<GameSystem> systems = initSystems(entityFactory, core, animations, random);
        List<ActionSystem> actionSystems = initActionSystems(core, animations);
        TurnsGameController controller = new TurnsGameController(systems, entityFactory, core, actionSystems);
        List<Object> components = Arrays.asList(core);
        return new GameContext(
                entityFactory,
                registry,
                systems,
                components,
                controller,
                new SingleTeamRemaining(core),
                random,
                animations);
    }

    @Override
    public void populateContext(GameContext context, GameConfig config) {
        EntityFactory entityFactory = context.getEntityFactory();
        CoreComponents core = context.getComponents(CoreComponents.class);
        int player0 = 0;
        int player1 = 1;

        int meta = entityFactory.create();
        core.name.set(meta, "GameMeta");
        int width = 10;
        int height = 10;
        core.mapSize.set(meta, Coordinates.of(width, height));

        int teamA = entityFactory.create();
        core.name.set(teamA, "TeamA");
        int teamB = entityFactory.create();
        core.name.set(teamB, "TeamB");
        core.nextTeam.set(teamA, teamB);
        core.nextTeam.set(teamB, teamA);

        int actorA = entityFactory.create();
        core.name.set(actorA, "ActorA");
        core.actor.isStatusOk.set(actorA);
        core.actor.controlledBy.set(actorA, Flags.toFlag(player0));
        core.actor.memberOf.set(actorA, teamA);
        core.actor.sprite.set(actorA, 0);
        core.actor.position.set(actorA, Coordinates.of(2, 2));
        core.stats.health.active.set(actorA, 5);
        core.stats.movePoints.buffed.set(actorA, 3);
        core.stats.actionPoints.buffed.set(actorA, 6);
        passTurnSkill(context, actorA);
        walkSkill(context, actorA);
        razorleafSkill(context, actorA);

        int actorB = entityFactory.create();
        core.name.set(actorB, "ActorB");
        core.actor.isStatusOk.set(actorB);
        core.actor.controlledBy.set(actorB, Flags.toFlag(player1));
        core.actor.memberOf.set(actorB, teamB);
        core.actor.sprite.set(actorB, 3);
        core.actor.position.set(actorB, Coordinates.of(7, 7));
        core.stats.health.active.set(actorB, 5);
        core.stats.movePoints.buffed.set(actorB, 3);
        core.stats.actionPoints.buffed.set(actorB, 6);
        passTurnSkill(context, actorB);
        walkSkill(context, actorB);

        boolean teamAFirst = context.getRandom().nextBoolean();
        core.activeTeam.set(teamAFirst ? teamA : teamB);
        core.actor.active.set(teamAFirst ? actorA : actorB);
    }

    private static int passTurnSkill(GameContext context, int actor) {
        EntityFactory entityFactory = context.getEntityFactory();
        CoreComponents core = context.getComponents(CoreComponents.class);
        int passTurn = entityFactory.create();
        core.name.set(passTurn, "PassTurn");
        core.skill.ofActor.set(passTurn, actor);
        core.skill.effect.passTurnOfActor.set(passTurn);
        core.skill.id.set(passTurn, 1);
        return passTurn;
    }

    private static int walkSkill(GameContext context, int actor) {
        EntityFactory entityFactory = context.getEntityFactory();
        CoreComponents core = context.getComponents(CoreComponents.class);
        int walk = entityFactory.create();
        core.name.set(walk, "Walk");
        core.skill.ofActor.set(walk, actor);
        core.skill.effect.walkToTargetPosition.set(walk);
        core.skill.targeting.position.empty.set(walk);
        core.skill.targeting.position.manhattanRange.set(walk, 1);
        core.skill.targeting.position.required.set(walk);
        core.skill.cost.movePoints.set(walk, 1);
        core.skill.id.set(walk, 2);
        core.skill.effect.animation.walk.set(walk);
        return walk;
    }

    private static int razorleafSkill(GameContext context, int actor) {
        EntityFactory entityFactory = context.getEntityFactory();
        CoreComponents core = context.getComponents(CoreComponents.class);
        int razorleaf = entityFactory.create();
        core.name.set(razorleaf, "Razorleaf");
        core.skill.ofActor.set(razorleaf, actor);
        core.skill.effect.damageToTarget.earth.set(razorleaf, 1);
        core.skill.effect.randomDamageToTarget.earth.set(razorleaf, 4);
        core.skill.effect.animation.attack.set(razorleaf);
        core.skill.targeting.position.manhattanRange.set(razorleaf, 4);
        core.skill.targeting.position.required.set(razorleaf);
        core.skill.targeting.actor.enemy.set(razorleaf);
        core.skill.targeting.actor.required.set(razorleaf);
        core.skill.cost.actionPoints.set(razorleaf, 4);
        core.skill.id.set(razorleaf, 3);
        return razorleaf;
    }

    private static List<GameSystem> initSystems(EntityFactory entityFactory, CoreComponents core, AnimationsController animations, HistoryRandom random) {
        return Arrays.asList(
                //
                new ConvertTriggerToEffectSystem(core),
                //
                new AttackAnimationSystem(core, animations),
                new WalkAnimationSystem(core, animations),
                new DeathSystem(core, animations),
                new WalkToTargetPositionSystem(core),
                new PassTurnOfTargetSystem(core),
                new EarthDamageToTargetSystem(core, random),
                new DieWithoutHealthSystem(core, entityFactory, animations),
                //
                new EffectPhaseCleanupSystem(core),
                //
                new NextTeamsTurnSystem(core),
                new CooldownUpdateSystem(core),
                new ResetActivatedActorActionPointsSystem(core),
                new ResetActivatedActorMovePointsSystem(core),
                new ActorActivationSystem(core)
        );
    }

    private static List<ActionSystem> initActionSystems(CoreComponents core, AnimationsController animations) {
        return Arrays.asList(
                //
                new EmptyTargetRequiredSystem(core),
                new ManhattanRangeRequiredSystem(core),
                //
                new ActionPointsCostSystem(core),
                new MovePointsCostSystem(core),
                new CooldownCostSystem(core),
                //
                new PassTurnSkillTriggerSystem(core),
                new AttackSkillTriggerSystem(core, animations),
                new WalkSkillTriggerSystem(core, animations));
    }
}
