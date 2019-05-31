package com.etherblood.jme3.gui;

import com.etherblood.game.client.Game;
import com.jme3.app.state.AbstractAppState;
import com.simsilica.lemur.core.VersionedHolder;
import com.simsilica.lemur.core.VersionedReference;

public class GameSelectAppstate extends AbstractAppState {

    private final VersionedHolder<Game> activeGame = new VersionedHolder<>();

    public void selectGame(Game game) {
        activeGame.updateObject(game);
    }

    public VersionedReference<Game> activeGameReference() {
        return activeGame.createReference();
    }
}
