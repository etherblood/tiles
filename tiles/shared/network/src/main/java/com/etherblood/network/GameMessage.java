package com.etherblood.network;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.UUID;

@Serializable
public class GameMessage extends AbstractMessage {

    public UUID game;
    public Object value;

    GameMessage() {
    }

    public GameMessage(UUID game, Object value) {
        this.game = game;
        this.value = value;
    }

    @Override
    public String toString() {
        return "GameMessage{" + "game=" + game + ", value=" + value + '}';
    }
}
