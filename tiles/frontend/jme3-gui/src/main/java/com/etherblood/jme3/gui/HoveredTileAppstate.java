package com.etherblood.jme3.gui;

import com.etherblood.core.util.Coordinates;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.core.VersionedHolder;
import com.simsilica.lemur.core.VersionedReference;

public class HoveredTileAppstate extends AbstractAppState {

    private final VersionedHolder<Integer> hoveredTile = new VersionedHolder<>();
    private final VersionedReference<Integer> mapSize;
    private InputManager inputManager;
    private Camera camera;

    public HoveredTileAppstate(VersionedReference<Integer> mapSize) {
        this.mapSize = mapSize;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        inputManager = app.getInputManager();
        camera = app.getCamera();
        super.initialize(stateManager, app);
    }

    @Override
    public void update(float tpf) {
        if (mapSize.get() == null) {
            hoveredTile.updateObject(null);
            return;
        }
        Vector2f cursor = inputManager.getCursorPosition();
        Plane floor = new Plane(Vector3f.UNIT_Y, 0);
        Ray ray = new Ray(camera.getWorldCoordinates(cursor, 0), camera.getDirection());
        Vector3f result = new Vector3f();
        boolean intersectsPlane = ray.intersectsWherePlane(floor, result);
        int tile = Coordinates.of((int) result.getX(), (int) result.getZ());
        if (intersectsPlane && Coordinates.inBounds(tile, mapSize.get())) {
            hoveredTile.updateObject(tile);
        } else {
            hoveredTile.updateObject(null);
        }
    }

    public VersionedReference<Integer> hoveredTileReference() {
        return hoveredTile.createReference();
    }

}
