package com.etherblood.game.server;

import com.etherblood.core.Action;
import com.etherblood.core.GameContext;
import com.etherblood.core.IllegalActionException;
import com.etherblood.core.Mod;
import com.etherblood.core.to.GameConfig;
import com.etherblood.network.GameMessage;
import com.etherblood.network.GameRequest;
import com.etherblood.network.GamesListMessage;
import com.etherblood.network.LoginMessage;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.Server;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class GameServer implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(GameServer.class);
    private static final String ATTRIBUTE_PLAYER_ID = "PLAYER_ID";
    private static final String MDC_GAME_ID = "GAME_ID";
    private static final String MDC_PLAYER_ID = "PLAYER_ID";
    private static final String MDC_REQUEST_ID = "REQUEST_ID";

    private final Server server;
    private final Map<String, Mod> mods = new ConcurrentHashMap<>();
    private final Map<UUID, Game> games = new ConcurrentHashMap<>();

    public GameServer(int port) throws IOException {
        this.server = Network.createServer(port);
    }

    public void run() {
        server.addConnectionListener(new ConnectionListener() {
            @Override
            public void connectionAdded(Server server, HostedConnection conn) {
                LOG.info("Connection added.");
                conn.send(new GamesListMessage(games.entrySet().stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue().getConfig()))));
            }

            @Override
            public void connectionRemoved(Server server, HostedConnection conn) {
                Object playerId = conn.getAttribute(ATTRIBUTE_PLAYER_ID);
                try {
                    mdcPutOptional(MDC_PLAYER_ID, playerId);
                    LOG.info("Disconnected.");
                } finally {
                    MDC.remove(MDC_PLAYER_ID);
                }
            }
        });
        server.addMessageListener(this::onLoginMessage, LoginMessage.class);
        server.addMessageListener(this::onGameMessage, GameMessage.class);
        server.start();
    }

    private void onLoginMessage(HostedConnection source, Message m) {
        try (MDC.MDCCloseable mdcRequest = MDC.putCloseable(MDC_REQUEST_ID, UUID.randomUUID().toString())) {
            LoginMessage message = (LoginMessage) m;
            source.setAttribute(ATTRIBUTE_PLAYER_ID, message.playerId);
        }
    }

    private void onGameMessage(HostedConnection source, Message m) {
        try (MDC.MDCCloseable mdcRequest = MDC.putCloseable(MDC_REQUEST_ID, UUID.randomUUID().toString())) {
            GameMessage message = (GameMessage) m;
            UUID player = source.getAttribute(ATTRIBUTE_PLAYER_ID);
            UUID game = message.game;

            try (MDC.MDCCloseable mdcGame = MDC.putCloseable(MDC_GAME_ID, game.toString()); MDC.MDCCloseable mdcPlayer = MDC.putCloseable(MDC_PLAYER_ID, player.toString())) {
                handleRequest(message, player, game, source);
            } catch (IllegalActionException ex) {
                LOG.error("Exception ocurred during request.", ex);
            }
        }
    }

    private static void mdcPutOptional(String key, Object value) {
        if (value != null) {
            MDC.put(key, value.toString());
        } else {
            MDC.remove(key);
        }
    }

    private void handleRequest(GameMessage message, UUID player, UUID game, HostedConnection source) {
        Object value = message.value;
        if (value instanceof GameRequest) {
            GameRequest request = (GameRequest) value;
            switch (request) {
                case SPECTATE:
                    connect(player, game, source);
                    break;
                case DISCONNECT:
                    disconnect(player, game);
                    break;
                default:
                    throw new AssertionError(request.name());

            }
        } else if (value instanceof Action) {
            applyAction(player, game, (Action) value);
        } else {
            throw new AssertionError();
        }
    }

    public void registerMod(String key, Mod mod) {
        mods.put(key, mod);
    }

    public UUID startGame(GameConfig config) {
        Mod mod = mods.get(config.getMod().getModName());
        GameContext context = mod.createContext();
        context.getRandom().setMasterMode(true);
        mod.populateContext(context, config);
        Game game = new Game(context, config);
        games.put(game.getId(), game);
        return game.getId();
    }

    private void connect(UUID player, UUID game, HostedConnection connection) {
        games.get(game).connect(player, (GameEvent<?> update) -> {
            GameMessage message = new GameMessage(game, update.getValue());
            LOG.debug("Sending message {}.", message);
            connection.send(message);
        });
    }

    private void disconnect(UUID player, UUID game) {
        games.get(game).disconnect(player);
    }

    private void applyAction(UUID player, UUID game, Action action) {
        games.get(game).action(player, action);
    }

    @Override
    public void close() {
        server.close();
    }
}
