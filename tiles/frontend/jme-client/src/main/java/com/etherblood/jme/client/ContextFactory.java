package com.etherblood.jme.client;

import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.GameContext;
import com.etherblood.rules.game.setup.GameContextSetup;
import com.etherblood.rules.game.setup.config.CharacterConfig;
import com.etherblood.rules.game.setup.config.GameConfig;
import com.etherblood.rules.game.setup.config.MapConfig;
import com.etherblood.rules.game.setup.config.PlayerConfig;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 *
 * @author Philipp
 */
public class ContextFactory {

    private EventHandler globalHandler = e -> {
    };

    public GameContext create() {
        GameContext context = new GameContext(globalHandler);

        MapConfig map = loadJson("/test/data/maps/map0.json", MapConfig.class);

        CharacterConfig bulbasaur0 = loadJson("/test/data/characters/bulbasaur.json", CharacterConfig.class);
        bulbasaur0.id = 0;
        bulbasaur0.teamId = map.teams[0].id;
        CharacterConfig charmander0 = loadJson("/test/data/characters/charmander.json", CharacterConfig.class);
        charmander0.id = 1;
        charmander0.teamId = map.teams[0].id;
        CharacterConfig squirtle0 = loadJson("/test/data/characters/squirtle.json", CharacterConfig.class);
        squirtle0.id = 2;
        squirtle0.teamId = map.teams[0].id;

        CharacterConfig bulbasaur1 = loadJson("/test/data/characters/bulbasaur.json", CharacterConfig.class);
        bulbasaur1.id = 3;
        bulbasaur1.teamId = map.teams[1].id;
        CharacterConfig charmander1 = loadJson("/test/data/characters/charmander.json", CharacterConfig.class);
        charmander1.id = 4;
        charmander1.teamId = map.teams[1].id;
        CharacterConfig squirtle1 = loadJson("/test/data/characters/squirtle.json", CharacterConfig.class);
        squirtle1.id = 5;
        squirtle1.teamId = map.teams[1].id;

        PlayerConfig player0 = new PlayerConfig();
        player0.id = 0;
        player0.name = "player0";
        player0.characterIds = new int[]{0, 1, 2, 3, 4, 5};

        PlayerConfig player1 = new PlayerConfig();
        player1.id = 1;
        player1.name = "player1";
        player1.characterIds = new int[]{3, 4, 5};

        GameConfig config = new GameConfig();
        config.characters = Arrays.asList(bulbasaur0, charmander0, squirtle0, bulbasaur1, charmander1, squirtle1);
        config.map = map;
        config.players = Arrays.asList(player0, player1);
        new GameContextSetup().populate(context, config);
        context.startGame();
        return context;
    }

    private <T> T loadJson(String file, Class<T> clazz) {
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(file), StandardCharsets.UTF_8.name())) {
            return new Gson().fromJson(reader, clazz);
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }

    public ContextFactory withGlobalEventHandler(EventHandler globalHandler) {
        this.globalHandler = globalHandler;
        return this;
    }
}
