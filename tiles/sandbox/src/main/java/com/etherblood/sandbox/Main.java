package com.etherblood.sandbox;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.MapBuilder;
import com.etherblood.entities.EntityData;
import com.etherblood.events.EventDefinition;
import com.etherblood.rules.GameContext;
import com.etherblood.rules.abilities.Action;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.movement.Coordinates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.IntFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class Main {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOG;

    static {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
        LOG = LoggerFactory.getLogger(Main.class);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IllegalArgumentException, IOException {
        GameContext context = new GameContext();
        EntityData data = context.getData();

        Pokemons pokemons = new Pokemons(data);

        int gameState = data.createEntity();
        data.set(gameState, Components.Arena.SIZE, Coordinates.of(context.mapWidth, context.mapHeight));

        int teamA = data.createEntity();
        int teamB = data.createEntity();
        data.set(teamA, Components.NEXT_TEAM, teamB);
        data.set(teamB, Components.NEXT_TEAM, teamA);

        int human1 = 0;
        int human2 = 1;

        int bulbasaur = data.createEntity();
        pokemons.bulbasaur(bulbasaur);
        data.set(bulbasaur, Components.CONTROLLED_BY, human1);
        data.set(bulbasaur, Components.MEMBER_OF, teamA);
        data.set(bulbasaur, Components.POSITION, Coordinates.of(1, 1));
        data.set(bulbasaur, Components.Abilities.WALK, 0);
        data.set(bulbasaur, Components.Abilities.PASS_TURN, 0);
        data.set(bulbasaur, Components.Abilities.RAZORLEAF, 4);

        int testBuff = data.createEntity();
        data.set(testBuff, Components.Stats.Health.ADDITIVE, 700);
        data.set(testBuff, Components.BUFF_ON, bulbasaur);

        int charmander = data.createEntity();
        pokemons.charmander(charmander);
        data.set(charmander, Components.CONTROLLED_BY, human2);
        data.set(charmander, Components.MEMBER_OF, teamB);
        data.set(charmander, Components.POSITION, Coordinates.of(5, 9));
        data.set(charmander, Components.Abilities.WALK, 0);
        data.set(charmander, Components.Abilities.PASS_TURN, 0);

        int squirtle = data.createEntity();
        pokemons.squirtle(squirtle);
        data.set(squirtle, Components.CONTROLLED_BY, human1);
        data.set(squirtle, Components.MEMBER_OF, teamB);
        data.set(squirtle, Components.POSITION, Coordinates.of(3, 5));
        data.set(squirtle, Components.Abilities.WALK, 0);
        data.set(squirtle, Components.Abilities.PASS_TURN, 0);

        for (int y = 4; y < 6; y++) {
            for (int x = 4; x < 6; x++) {
                int obstacle = data.createEntity();
                data.set(obstacle, Components.POSITION, Coordinates.of(x, y));
                data.set(obstacle, Components.Arena.OBSTACLE, 0);
            }
        }

        Map<Integer, Character> actorShortcuts = new MapBuilder<Integer, Character>()
                .with(bulbasaur, 'b')
                .with(charmander, 'c')
                .with(squirtle, 's')
                .build();

        context.startGame();
        while (true) {
            logState(context.getData(), actorShortcuts, context.mapWidth, context.mapHeight);
            Action action = chooseAction(data, Components.ACTIVE_PLAYER, context.getActions(), context::getEventDefinition, actorShortcuts);
            context.action(action.event);
        }

    }

    private static void logState(EntityData data, Map<Integer, Character> actorShortcuts, int mapWidth, int mapHeight) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}", GSON.toJson(new EntityDebugObjectMapper().toDebugObjects(data, Components.DEFINITIONS)));
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8.name())) {
                new MapPrinter().printMap(actorShortcuts, mapWidth, mapHeight, data, Components.POSITION, ps);
                LOG.debug("{}", new String(baos.toByteArray(), StandardCharsets.UTF_8));
            }
        }
    }

    private static Action chooseAction(EntityData data, int active, ActionGenerator actions, IntFunction<EventDefinition> events, Map<Integer, Character> actorShortcuts) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            IntArrayList actors = data.query(active).list();
            System.out.println("choose actor:");
            for (int i = 0; i < actors.size(); i++) {
                int actor = actors.get(i);
                System.out.println(i + ": " + actorShortcuts.get(actor) + "(" + actor + ")");
            }
            int actor = actors.get(scanner.nextInt());
            List<Action> availableActions = actions.availableActions(actor);
            System.out.println("choose action:");
            for (int i = 0; i < availableActions.size(); i++) {
                Action action = availableActions.get(i);
                System.out.println(i + ": " + events.apply(action.event.getId()).toString(action.event));
            }
            int action = scanner.nextInt();
            return availableActions.get(action);
        }
    }

}
