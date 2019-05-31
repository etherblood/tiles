package com.etherblood.network;

import com.jme3.network.AbstractMessage;
import java.util.UUID;

public class LoginMessage extends AbstractMessage {

    public UUID playerId;

    public LoginMessage() {
    }

    public LoginMessage(UUID playerId) {
        this.playerId = playerId;
    }
}
