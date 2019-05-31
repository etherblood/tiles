package com.etherblood.jme3.gui;

import com.etherblood.jme3.gui.util.Cursor;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.simsilica.lemur.core.VersionedReference;
import java.util.Set;

public class TargetableCursorAppstate extends AbstractAppState {

    private final Cursor hoverCursor;
    private final VersionedReference<Integer> hoveredTile;
    private final VersionedReference<Set<Integer>> targetableTiles;
    private InputManager inputManager;

    public TargetableCursorAppstate(Cursor hoverCursor, VersionedReference<Integer> hoveredTile, VersionedReference<Set<Integer>> targetableTiles) {
        this.hoverCursor = hoverCursor;
        this.hoveredTile = hoveredTile;
        this.targetableTiles = targetableTiles;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        inputManager = app.getInputManager();
        super.initialize(stateManager, app);
    }

    @Override
    public void update(float tpf) {
        if (hoveredTile.update() || targetableTiles.update()) {
            if (targetableTiles.get().contains(hoveredTile.get())) {
                inputManager.setMouseCursor(hoverCursor);
            } else {
                inputManager.setMouseCursor(null);
            }
        }
    }

}
