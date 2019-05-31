package com.etherblood.jme3.gui;

import com.etherblood.core.GameContext;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.core.VersionedHolder;

public class GameOverAppstate extends AbstractAppState {

    private final GameContext gameContext;
    private final VersionedHolder<Boolean> isGameOver = new VersionedHolder<>(false);
    private Node guiNode;
    private Container gameOverContainer;

    public GameOverAppstate(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void update(float tpf) {
        if (isGameOver.updateObject(gameContext.getResult().isGameOver())) {
            if (isGameOver.getObject()) {
                guiNode.attachChild(gameOverContainer);
            } else {
                guiNode.detachChild(gameOverContainer);
            }
        }
    }

    @Override
    public void initialize(AppStateManager appStateManager, Application app) {
        guiNode = ((SimpleApplication) app).getGuiNode();
        gameOverContainer = new Container();
        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setFontSize(100);
        gameOverContainer.addChild(gameOverLabel);
        gameOverContainer.setLocalTranslation(0, app.getCamera().getHeight(), 0);
        gameOverContainer.setPreferredSize(new Vector3f(app.getCamera().getWidth(), app.getCamera().getHeight(), 0));
    }

}
