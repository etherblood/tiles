package com.etherblood.sandbox;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.MapBuilder;
import com.etherblood.entities.ComponentMeta;
import com.etherblood.entities.EntityData;
import com.etherblood.rules.GameContext;
import com.etherblood.rules.skills.Action;
import com.etherblood.rules.components.ComponentDefinitions;
import com.etherblood.rules.util.Coordinates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
        LOG = LoggerFactory.getLogger(Main.class);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IllegalArgumentException, IOException {
        GameContext context = new GameContext();
        EntityData data = context.getData();
        ComponentDefinitions components = context.componentDefs;

        Pokemons pokemons = new Pokemons(data, components);

        int gameState = data.createEntity();
        data.set(gameState, components.mapSize.id, Coordinates.of(20, 20));

        int teamA = data.createEntity();
        int teamB = data.createEntity();
        data.set(teamA, components.nextTeam.id, teamB);
        data.set(teamB, components.nextTeam.id, teamA);

        int bulbasaur = data.createEntity();
        pokemons.bulbasaur(bulbasaur);
        data.set(bulbasaur, components.memberOf.id, teamA);
        data.set(bulbasaur, components.position.id, Coordinates.of(1, 1));
        data.set(bulbasaur, components.walkSkill.id, 1);
        data.set(bulbasaur, components.razorleafSkill.id, 4);

        int testBuff = data.createEntity();
        data.set(testBuff, components.health.additive.id, 3);
        data.set(testBuff, components.buffOn.id, bulbasaur);

        int charmander = data.createEntity();
        pokemons.charmander(charmander);
        data.set(charmander, components.memberOf.id, teamB);
        data.set(charmander, components.position.id, Coordinates.of(5, 9));
        data.set(charmander, components.walkSkill.id, 1);

        int squirtle = data.createEntity();
        pokemons.squirtle(squirtle);
        data.set(squirtle, components.memberOf.id, teamB);
        data.set(squirtle, components.position.id, Coordinates.of(3, 5));
        data.set(squirtle, components.walkSkill.id, 1);

        for (int y = 4; y < 6; y++) {
            for (int x = 4; x < 6; x++) {
                int obstacle = data.createEntity();
                data.set(obstacle, components.position.id, Coordinates.of(x, y));
                data.set(obstacle, components.obstacle.id, 1);
            }
        }
        context.startGame();

        Map<Integer, Character> actorShortcuts = new MapBuilder<Integer, Character>()
                .with(bulbasaur, 'b')
                .with(charmander, 'c')
                .with(squirtle, 's')
                .build();

        while (true) {
            logState(context.getData(), actorShortcuts, context.mapWidth(), context.mapHeight(), components.position.id, context.componentMetaList);
            Action action = chooseAction(context, actorShortcuts);
            context.action(action.event);
        }
    }

    private static void logState(EntityData data, Map<Integer, Character> actorShortcuts, int mapWidth, int mapHeight, int position, List<ComponentMeta> componentMetaList) throws IOException {
        if (LOG.isInfoEnabled()) {
            LOG.info("{}", GSON.toJson(new EntityDebugObjectMapper().toDebugObjects(data, componentMetaList)));
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8.name())) {
                new MapPrinter().printMap(actorShortcuts, mapWidth, mapHeight, data, position, ps);
                LOG.info("{}", new String(baos.toByteArray(), StandardCharsets.UTF_8));
            }
        }
    }

    private static Action chooseAction(GameContext context, Map<Integer, Character> actorShortcuts) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            IntArrayList actors = context.getData().query(context.componentDefs.activePlayer.id).list();
            System.out.println("choose actor:");
            for (int i = 0; i < actors.size(); i++) {
                int actor = actors.get(i);
                System.out.println(i + ": " + actorShortcuts.get(actor) + "(" + actor + ")");
            }
            int actor = actors.get(scanner.nextInt());
            List<Action> availableActions = context.getActions().availableActions(actor);
            System.out.println("choose action:");
            for (int i = 0; i < availableActions.size(); i++) {
                Action action = availableActions.get(i);
                System.out.println(i + ": " + context.eventMetaList.get(action.event.getId()).toString(action.event));
            }
            int action = scanner.nextInt();
            return availableActions.get(action);
        }
    }

}
