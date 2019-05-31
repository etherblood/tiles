package com.etherblood.game.client;

import com.etherblood.core.GameContext;
import com.etherblood.core.to.GameConfig;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

    private final Logger LOG = LoggerFactory.getLogger(Game.class);
    private final UUID id;
    private final GameConfig config;
    private final GameContext context;

    public Game(UUID id, GameConfig config, GameContext context) {
        this.id = id;
        this.config = config;
        this.context = context;
    }

    public GameConfig getConfig() {
        return config;
    }

    public GameContext getContext() {
        return context;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Game && ((Game) obj).id.equals(id);
    }
}
