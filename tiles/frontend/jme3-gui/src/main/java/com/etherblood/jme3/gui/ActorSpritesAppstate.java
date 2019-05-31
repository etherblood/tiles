package com.etherblood.jme3.gui;

import com.etherblood.collections.KeyValuePair;
import com.etherblood.collections.MapBuilder;
import com.etherblood.core.util.Coordinates;
import com.etherblood.jme3.gui.sprites.Sprite;
import com.etherblood.jme3.gui.sprites.SpritesheetInstance;
import com.etherblood.jme3.gui.sprites.data.FrameType;
import com.etherblood.jme3.gui.sprites.data.SpritesheetData;
import com.etherblood.jme3.gui.util.Utils;
import com.etherblood.jme3.gui.util.VersionedMapReference;
import com.google.gson.Gson;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.simsilica.lemur.core.VersionedReference;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActorSpritesAppstate extends AbstractAppState {

    private static final Logger LOG = LoggerFactory.getLogger(ActorSpritesAppstate.class);
    private Node rootNode;
    private final Map<Integer, Sprite> sprites = new HashMap<>();
    private final Map<Integer, SpritesheetInstance> spritesheets = new HashMap<>();
    private final VersionedMapReference<Integer, ActorDetails> actors;

    public ActorSpritesAppstate(VersionedMapReference<Integer, ActorDetails> actors) {
        this.actors = actors;
    }

    public Sprite getActorSprite(int actor) {
        return sprites.get(actor);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        TilesApplication tilesApp = (TilesApplication) app;
        rootNode = tilesApp.getRootNode();

        long startMillis = System.currentTimeMillis();
        Map<Integer, String> assetMap = new MapBuilder<Integer, String>()
                .with(0, "spritesheet.json")
                .with(3, "spritesheet2.json")
                .with(6, "spritesheet3.json")
                .build();

        Map<Integer, SpritesheetInstance> loadedAssets = assetMap.entrySet().parallelStream()
                .map(entry -> new KeyValuePair<>(entry.getKey(), loadSpritesheet(tilesApp.getAssetManager(), "/test/isometric_heroine/", entry.getValue())))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
        spritesheets.putAll(loadedAssets);
        LOG.debug("Loading sprites took {}ms.", System.currentTimeMillis() - startMillis);
    }

    @Override
    public void update(float elapsedSeconds) {
        updateSprites(elapsedSeconds);
    }

    private void updateSprites(float elapsedSeconds) {
        if (actors.update()) {
            Utils.updateEntries(actors.getReferences(), sprites, this::createSprite, this::cleanupSprite);
        }
        for (Sprite sprite : sprites.values()) {
            sprite.update(elapsedSeconds);
        }
    }

    private Sprite createSprite(VersionedReference<ActorDetails> ref) {
        return createSprite(ref.get());
    }

    private Sprite createSprite(ActorDetails actorDetails) {
        Sprite sprite = createSprite(actorDetails.sprite);
        Vector3f translation = getTranslation(Coordinates.x(actorDetails.coordinates), Coordinates.y(actorDetails.coordinates));
        sprite.getNode().setLocalTranslation(translation);
        return sprite;
    }

    private Sprite createSprite(int spriteId) {
        SpritesheetInstance spritesheet = spritesheets.get(spriteId);
        Sprite sprite = spritesheet.createSprite(FrameType.SOUTH_WEST);
        rootNode.attachChild(sprite.getNode());
        return sprite;
    }

    private void cleanupSprite(Sprite sprite) {
        sprite.getNode().removeFromParent();
    }

    private SpritesheetInstance loadSpritesheet(AssetManager assetManager, String path, String json) {
        SpritesheetData data = loadSpritesheetData(path, json);
        return loadSpritesheetInstance(assetManager, path, data);
    }

    private SpritesheetInstance loadSpritesheetInstance(AssetManager assetManager, String path, SpritesheetData data) {
        Texture texture = assetManager.loadTexture(new TextureKey(path + data.spritesheetPath, false));
        Material spriteMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        spriteMaterial.setTexture("ColorMap", texture);
        spriteMaterial.setFloat("AlphaDiscardThreshold", 0.001f);
        spriteMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        spriteMaterial.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        return new SpritesheetInstance(data, spriteMaterial);
    }

    private SpritesheetData loadSpritesheetData(String path, String json) throws RuntimeException {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(path + json))) {
            return new Gson().fromJson(reader, SpritesheetData.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Vector3f getTranslation(int x, int y) {
        return new Vector3f(x + 0.5f, 0, y + 0.5f);
    }

}
