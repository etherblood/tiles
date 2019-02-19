package com.etherblood.jme.client.skills.targeting;

import com.etherblood.collections.IntHashSet;
import com.etherblood.jme.client.util.Cursor;
import com.etherblood.jme.client.util.VersionedReference;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

public class TargetableCursorAppstate extends BaseAppState {

    private final Cursor hoverCursor;
    private final VersionedReference<Integer> hoveredTile;
    private final VersionedReference<IntHashSet> targetableTiles;

    public TargetableCursorAppstate(Cursor hoverCursor, VersionedReference<Integer> hoveredTile, VersionedReference<IntHashSet> targetableTiles) {
        this.hoverCursor = hoverCursor;
        this.hoveredTile = hoveredTile;
        this.targetableTiles = targetableTiles;
    }

    @Override
    public void update(float tpf) {
        if(hoveredTile.update() || targetableTiles.update()) {
            if(hoveredTile.get() != null && targetableTiles.get().hasKey(hoveredTile.get())){
                getApplication().getInputManager().setMouseCursor(hoverCursor);
            } else {
                getApplication().getInputManager().setMouseCursor(null);
            }
        }
    }
    @Override
    protected void initialize(Application app) {
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

}
