package com.etherblood.playground;

import com.etherblood.core.to.GameConfig;
import com.etherblood.core.to.ModConfig;
import com.etherblood.core.to.PlayerConfig;
import com.etherblood.game.client.GameClient;
import com.etherblood.game.server.GameServer;
import com.etherblood.mods.core.game.DefaultMod;
import com.etherblood.network.NetworkUtil;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        NetworkUtil.initSerializers();
        GameServer server = new GameServer(NetworkUtil.DEFAULT_PORT);
        server.registerMod("default", new DefaultMod());
        server.run();

        PlayerConfig playerA = new PlayerConfig(UUID.randomUUID());
        PlayerConfig playerB = new PlayerConfig(UUID.randomUUID());
        GameConfig config = new GameConfig(new PlayerConfig[]{playerA, playerB}, new ModConfig("default"));
        UUID gameId = server.startGame(config);
        LOG.info("started game {} with config {}.", gameId, config);


        GameClient client = new GameClient("localhost", NetworkUtil.DEFAULT_PORT);
        client.registerMod("default", new DefaultMod());
        client.run();
        client.login(UUID.randomUUID());//spectator
        client.join(gameId);
    }
}
