package com.etherblood.jme3.gui;

import com.etherblood.core.util.Coordinates;
import com.etherblood.mods.core.game.components.CoreComponents;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.simsilica.lemur.core.VersionedReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GridAppstate extends AbstractAppState {

    private static final int TILE_X = 4;
    private static final int TILE_Y = 1;
    private static final float[] FLOOR_TILE_QUAD_BUFFER = new float[]{
        TILE_X / 16f, TILE_Y / 16f,
        (TILE_X + 1) / 16f, TILE_Y / 16f,
        (TILE_X + 1) / 16f, (TILE_Y + 1) / 16f,
        TILE_X / 16f, (TILE_Y + 1) / 16f};

    private Node rootNode;
    private Material defaultTileMaterial;
    private Material whiteTileMaterial;
    private Material yellowTileMaterial;
    private Material blueTileMaterial;
    private Material lightBlueTileMaterial;
    private final Map<Integer, Geometry> floorGeometries = new HashMap<>();
    private final Node floorNode = new Node("floor");
    private final Geometry grid = new Geometry("grid");
    private final CoreComponents core;
    private final VersionedReference<Integer> mapSize;
    private final VersionedReference<Integer> gameVersion;
    private final VersionedReference<Integer> selectedActor;
    private final VersionedReference<Set<Integer>> targetableTiles;
    private final VersionedReference<Set<Integer>> selectableActors;

    public GridAppstate(CoreComponents core, VersionedReference<Integer> mapSize, VersionedReference<Integer> gameVersion, VersionedReference<Integer> selectedActor, VersionedReference<Set<Integer>> targetableTiles, VersionedReference<Set<Integer>> selectableActors) {
        this.core = core;
        this.mapSize = mapSize;
        this.gameVersion = gameVersion;
        this.selectedActor = selectedActor;
        this.targetableTiles = targetableTiles;
        this.selectableActors = selectableActors;
    }
    
    @Override
    public void update(float tpf) {
        boolean mapSizeUpdated = mapSize.update();
        if (mapSizeUpdated) {
            floorNode.removeFromParent();
            grid.removeFromParent();
            Integer value = mapSize.get();
            if (value != null) {
                rootNode.attachChild(floorNode);
                int x = Coordinates.x(value);
                int y = Coordinates.y(value);
                grid.setMesh(new Grid(x + 1, y + 1, 1));
                rootNode.attachChild(grid);
                populateFloorNode(x, y);
            }
        }
        if(mapSizeUpdated | targetableTiles.update() | selectableActors.update() | selectedActor.update() | gameVersion.update()) {
            Integer selectedActorTile = selectedActor.get() == null? null: core.actor.position.get(selectedActor.get());
            Set<Integer> selectableActorTiles = selectableActors.get().stream().map(core.actor.position::get).collect(Collectors.toSet());
            Set<Integer> activeActorTiles = core.actor.active.query().list().stream().map(core.actor.position::get).boxed().collect(Collectors.toSet());
            for (Map.Entry<Integer, Geometry> entry : floorGeometries.entrySet()) {
                if(targetableTiles.get().contains(entry.getKey())) {
                    entry.getValue().setMaterial(yellowTileMaterial);
                } else if(entry.getKey().equals(selectedActorTile)) {
                    entry.getValue().setMaterial(whiteTileMaterial);
                } else if(selectableActorTiles.contains(entry.getKey())) {
                    entry.getValue().setMaterial(lightBlueTileMaterial);
                } else if(activeActorTiles.contains(entry.getKey())) {
                    entry.getValue().setMaterial(blueTileMaterial);
                } else {
                    entry.getValue().setMaterial(defaultTileMaterial);
                }
            }
        }
    }

    private void populateFloorNode(int width, int height) {
        for (Spatial child : floorNode.getChildren()) {
            child.removeFromParent();
        }
        floorGeometries.clear();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Quad quad = new Quad(1, 1);
                quad.setBuffer(VertexBuffer.Type.TexCoord, 2, FLOOR_TILE_QUAD_BUFFER);
                Node n = new Node();
                Geometry g = new Geometry("tile" + x + "/" + y, quad);
                g.setMaterial(defaultTileMaterial);
                n.attachChild(g);
                n.setLocalTranslation(new Vector3f(x, 0, y));
                n.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X));
                int tile = Coordinates.of(x, y);
                floorGeometries.put(tile, g);
                floorNode.attachChild(n);
            }
        }
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        TilesApplication tilesApp = (TilesApplication) app;
        rootNode = tilesApp.getRootNode();

        Material material = new Material(tilesApp.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Blue);
        grid.setMaterial(material);

        Texture texture = tilesApp.getAssetManager().loadTexture(new TextureKey("test/tiles/terrain.png", false));
        defaultTileMaterial = createMaterial(tilesApp, texture, ColorRGBA.Gray);
        whiteTileMaterial = createMaterial(tilesApp, texture, ColorRGBA.White);
        yellowTileMaterial = createMaterial(tilesApp, texture, ColorRGBA.Yellow);
        blueTileMaterial = createMaterial(tilesApp, texture, ColorRGBA.Blue);
        lightBlueTileMaterial = createMaterial(tilesApp, texture, new ColorRGBA(0.5f, 0.5f, 1f, 1f));

        super.initialize(stateManager, app);
    }

    private Material createMaterial(TilesApplication tilesApp, Texture texture, ColorRGBA color) {
        Material mat = new Material(tilesApp.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", texture);
        mat.setColor("Color", color);
        mat.setFloat("AlphaDiscardThreshold", 0.001f);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        return mat;
    }

}
