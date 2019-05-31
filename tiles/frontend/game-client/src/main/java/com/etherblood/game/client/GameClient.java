package com.etherblood.game.client;

import com.etherblood.core.Action;
import com.etherblood.core.Mod;
import com.etherblood.core.to.GameConfig;
import com.etherblood.core.to.HistoryAction;
import com.etherblood.core.to.RegistryState;
import com.etherblood.network.GameMessage;
import com.etherblood.network.GameRequest;
import com.etherblood.network.LoginMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameClient implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(GameClient.class);
    private UUID playerId;
    private final Client client;
    private final Map<String, Mod> mods = new ConcurrentHashMap<>();
    private final Map<UUID, Game> games = new ConcurrentHashMap<>();
    private final boolean headless;

    public GameClient(String host, int port) throws IOException {
        this(host, port, false);
    }

    public GameClient(String host, int port, boolean headless) throws IOException {
        this.client = Network.connectToServer(host, port);
        this.headless = headless;
    }

    public void registerMod(String key, Mod mod) {
        mods.put(key, mod);
    }

    public void run() {
        client.addErrorListener(this::handleError);
        client.addMessageListener(this::gameMessageReceived, GameMessage.class);
        client.start();
    }

    public void login(UUID playerId) {
        this.playerId = playerId;
        client.send(new LoginMessage(playerId));
    }

    public void act(UUID gameId, Action action) {
        client.send(new GameMessage(gameId, action));
    }

    public void join(UUID gameId) {
        client.send(new GameMessage(gameId, GameRequest.SPECTATE));
    }

    public void leave(UUID gameId) {
        client.send(new GameMessage(gameId, GameRequest.DISCONNECT));
    }

    private void handleError(Client source, Throwable t) {
        LOG.error("Network error.", t);
    }

    private void gameMessageReceived(Client source, Message m) {
        GameMessage message = (GameMessage) m;
        LOG.debug("{}", message);
        Object value = message.value;
        if (value instanceof GameConfig) {
            GameConfig config = (GameConfig) value;
            Mod mod = mods.get(config.getMod().getModName());
            Game game = new Game(message.game, config, mod.createContext());
            if(!headless) {
                game.getContext().getAnimationsController().enable();
            }
            games.put(message.game, game);
            LOG.info("Created game {}.", message.game);
        } else if (value instanceof RegistryState) {
            RegistryState state = (RegistryState) value;
            Game game = games.get(message.game);
            state.toRegistry(game.getContext().getRegistry());
            LOG.info("Initialized state for game {}.", message.game);
        } else if (value instanceof HistoryAction) {
            HistoryAction action = (HistoryAction) value;
            Game game = games.get(message.game);
            game.getContext().applyHistoryAction(action);
            LOG.info("Applied action {} to game {}.", action, message.game);
        } else {
            LOG.warn("Message could not be parsed: {}", message);
        }
    }

    @Override
    public void close() {
        client.close();
    }

    public Map<UUID, Game> getGames() {
        return games;
    }

    public Game getGame(UUID id) {
        return games.get(id);
    }

    public Client getClient() {
        return client;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
