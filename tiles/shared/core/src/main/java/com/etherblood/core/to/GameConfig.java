package com.etherblood.core.to;

import java.util.Arrays;

public class GameConfig {

    private PlayerConfig[] players;
    private ModConfig mod;

    public GameConfig() {
    }

    public GameConfig(PlayerConfig[] players, ModConfig mod) {
        this.players = players;
        this.mod = mod;
    }

    public PlayerConfig[] getPlayers() {
        return players;
    }

    public ModConfig getMod() {
        return mod;
    }

    @Override
    public String toString() {
        return "GameConfig{" + "players=" + Arrays.toString(players) + ", mod=" + mod + '}';
    }

}
