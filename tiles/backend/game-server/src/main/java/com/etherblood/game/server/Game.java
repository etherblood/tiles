package com.etherblood.game.server;

import com.etherblood.core.Action;
import com.etherblood.core.EntityDebugMap;
import com.etherblood.core.GameContext;
import com.etherblood.core.components.ComponentDebugMapExtractor;
import com.etherblood.core.to.GameConfig;
import com.etherblood.core.to.HistoryAction;
import com.etherblood.core.to.PlayerConfig;
import com.etherblood.core.to.RegistryState;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

    private final Logger LOG = LoggerFactory.getLogger(Game.class);
    private final UUID id = UUID.randomUUID();
    private final GameContext context;
    private final GameConfig config;
    private final RegistryState initialData = new RegistryState();
    private final List<GameEvent<HistoryAction>> actionHistory = new ArrayList<>();
    private final Map<UUID, GameListener> spectators = new HashMap<>();

    public Game(GameContext context, GameConfig config) {
        this.context = context;
        this.config = config;
        initialData.fromRegistry(context.getRegistry());
    }

    public synchronized void connect(UUID playerId, GameListener listener) {
        LOG.debug("Added connection for player {}.", playerId);
        GameListener previous = spectators.put(playerId, listener);
        if (previous != null) {
            LOG.debug("Removed previous connection of player {}.", playerId);
        }
        listener.onUpdate(new GameEvent<>(config));
        listener.onUpdate(new GameEvent<>(initialData));
        for (GameEvent<HistoryAction> gameEvent : actionHistory) {
            listener.onUpdate(gameEvent);
        }
    }

    public synchronized void disconnect(UUID playerId) {
        spectators.remove(playerId);
        LOG.debug("Disconnected player {}.", playerId);
    }

    public synchronized void action(UUID playerId, Action action) {
        LOG.info("Player {} requested action {}.", playerId, action);
        if (context.getResult().isGameOver()) {
            throw new IllegalStateException();
        }
        int playerIndex = getPlayerIndex(playerId);
        HistoryAction historyAction;
        try {
            historyAction = context.applyAction(playerIndex, action);
        } catch (Exception ex) {
            LOG.error("Rolling back game state due to exception...", ex);
            rollback();
            throw ex;
        }
        GameEvent<HistoryAction> event = new GameEvent<>(historyAction);
        actionHistory.add(event);
        for (GameListener listener : spectators.values()) {
            listener.onUpdate(event);
        }
        List<EntityDebugMap> list = new ComponentDebugMapExtractor().extract(context.getRegistry());
        LOG.debug(new GsonBuilder().setPrettyPrinting().create().toJson(list));
    }

    private void rollback() {
        context.clear();
        initialData.toRegistry(context.getRegistry());
        LOG.info("Replaying game up to newest state.");
        context.getRandom().setMasterMode(false);
        for (GameEvent<HistoryAction> gameEvent : actionHistory) {
            context.applyHistoryAction(gameEvent.getValue());
        }
        context.getRandom().setMasterMode(true);
        LOG.info("Game state was rolled back and replayed.");
    }

    private int getPlayerIndex(UUID playerId) {
        PlayerConfig[] players = config.getPlayers();
        for (int i = 0; i < players.length; i++) {
            if (players[i].getId().equals(playerId)) {
                return i;
            }
        }
        throw new IllegalStateException("Player is not on the players list of this game.");
    }

    public UUID getId() {
        return id;
    }

    public GameConfig getConfig() {
        return config;
    }
}
