package com.etherblood.core.to;

import com.etherblood.core.Action;
import java.util.Arrays;

public class HistoryAction {

    private final int gameVersion;
    private final int playerIndex;
    private final Action action;
    private final int[] randomHistory;

    public HistoryAction(int gameVersion, int playerIndex, Action action, int[] randomHistory) {
        this.gameVersion = gameVersion;
        this.playerIndex = playerIndex;
        this.action = action;
        this.randomHistory = randomHistory;
    }

    public int getGameVersion() {
        return gameVersion;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public Action getAction() {
        return action;
    }

    public int[] getRandomHistory() {
        return randomHistory;
    }

    @Override
    public String toString() {
        return "HistoryAction{" + "gameVersion=" + gameVersion + ", playerIndex=" + playerIndex + ", action=" + action + ", randomHistory=" + Arrays.toString(randomHistory) + '}';
    }

}
