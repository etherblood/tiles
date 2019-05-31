package com.etherblood.playground;

import com.etherblood.core.Action;
import com.etherblood.core.to.GameConfig;
import com.etherblood.core.to.ModConfig;
import com.etherblood.core.to.PlayerConfig;
import com.etherblood.core.util.Flags;
import com.etherblood.game.client.GameClient;
import com.etherblood.game.server.GameServer;
import com.etherblood.mods.core.game.DefaultMod;
import com.etherblood.mods.core.game.RandomMover;
import com.etherblood.mods.core.game.components.CoreComponents;
import com.etherblood.network.NetworkUtil;
import java.io.IOException;
import java.security.SecureRandom;
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
        PlayerConfig[] players = new PlayerConfig[]{playerA, playerB};
        GameConfig config = new GameConfig(players, new ModConfig("default"));
        UUID gameId = server.startGame(config);
        LOG.info("started game {} with config {}.", gameId, config);

        GameClient client = new GameClient("localhost", NetworkUtil.DEFAULT_PORT);
        client.registerMod("default", new DefaultMod());
        client.run();
        client.login(UUID.randomUUID());//spectator
        client.join(gameId);

        int player = 1;
        GameClient client2 = new GameClient("localhost", NetworkUtil.DEFAULT_PORT);
        client2.registerMod("default", new DefaultMod());
        client2.run();
        client2.login(players[player].getId());
        client2.join(gameId);

        Thread.sleep(1000);
        SecureRandom random = new SecureRandom();
        while (true) {
            Thread.sleep(1000);
            CoreComponents components = client2.getGame(gameId).getContext().getComponents(CoreComponents.class);
            while (components.actor.active.query().exists(x -> isControlledByPlayer(components.actor.controlledBy.get(x), player))) {
                Action action = new RandomMover(components, random).randomAction(player);
                LOG.debug("Chose action {} randomly.", action);
                client2.act(gameId, action);
                Thread.sleep(200);
            }
        }
    }

    private static boolean isControlledByPlayer(int controlledByFlags, int player) {
        return !Flags.isEmpty(Flags.intersection(controlledByFlags, Flags.toFlag(player)));
    }
}
