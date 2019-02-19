package com.etherblood.jme.client.skills.targeting;

import com.etherblood.jme.client.util.VersionedModel;
import com.etherblood.rules.util.Coordinates;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class HoveredTileAppstate extends BaseAppState {

    private final VersionedModel<Integer> hoveredTile;
    private final int mapSize;

    public HoveredTileAppstate(VersionedModel<Integer> hoveredTile, int mapSize) {
        this.hoveredTile = hoveredTile;
        this.mapSize = mapSize;
    }

    @Override
    public void update(float tpf) {
        Vector2f cursor = getApplication().getInputManager().getCursorPosition();
        Plane floor = new Plane(Vector3f.UNIT_Y, 0);
        Ray ray = new Ray(getApplication().getCamera().getWorldCoordinates(cursor, 0), getApplication().getCamera().getDirection());
        Vector3f result = new Vector3f();
        boolean intersectsPlane = ray.intersectsWherePlane(floor, result);
        int tile = Coordinates.of((int) result.getX(), (int) result.getZ());
        if (intersectsPlane && Coordinates.inBounds(tile, mapSize)) {
            hoveredTile.set(tile);
        } else {
            hoveredTile.set(null);
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
