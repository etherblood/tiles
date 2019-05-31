package com.etherblood.core;

import com.etherblood.core.to.GameConfig;

public interface Mod {

    GameContext createContext();

    void populateContext(GameContext context, GameConfig config);
}
