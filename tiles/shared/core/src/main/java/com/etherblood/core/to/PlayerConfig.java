package com.etherblood.core.to;

import java.util.UUID;

public class PlayerConfig {

    private UUID id;

    public PlayerConfig() {
    }

    public PlayerConfig(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "PlayerConfig{" + "id=" + id + '}';
    }
}
