package com.etherblood.network;

import com.etherblood.core.to.GameConfig;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.Map;
import java.util.UUID;

@Serializable
public class GamesListMessage extends AbstractMessage {

    private Map<UUID, GameConfig> games;

    public GamesListMessage() {
    }

    public GamesListMessage(Map<UUID, GameConfig> games) {
        this.games = games;
    }

    public Map<UUID, GameConfig> getGames() {
        return games;
    }
}
