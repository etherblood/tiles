package com.etherblood.jme.client;

import com.etherblood.collections.IntArrayList;
import com.etherblood.events.Event;
import com.etherblood.jme.client.sprites.Sprite;
import com.etherblood.jme.client.sprites.SpritesheetInstance;
import com.etherblood.jme.client.sprites.data.FrameType;
import com.etherblood.jme.client.sprites.data.SpritesheetData;
import com.etherblood.jme.client.state.StateTransitionManager;
import com.etherblood.jme.client.state.transitions.SpriteAnimationTransition;
import com.etherblood.jme.client.util.VersionedReference;
import com.etherblood.rules.GameContext;
import com.etherblood.rules.events.EntityMoveEvent;
import com.etherblood.rules.events.EntityValueEvent;
import com.etherblood.rules.events.EventDefinitions;
import com.etherblood.rules.game.setup.config.CharacterConfig;
import com.etherblood.rules.util.Coordinates;
import com.google.gson.Gson;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.simsilica.lemur.Container;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Philipp
 */
public class Testi {

    private final GameContext context;
    private final AssetManager assetManager;
    private final Node root, selectedActorNode = new Node("selected-actor-highlight");
    private final ActorDetailsContainer selectedActorDetails = new ActorDetailsContainer();
    private final ActorDetailsContainer hoveredActorDetails = new ActorDetailsContainer();
    private final Map<Integer, Sprite> sprites = new HashMap<>();
    private Integer hoveredActor = null;
    private final Map<Integer, SpritesheetInstance> spritesheets = new HashMap<>();
    private final Queue<Event> eventHistory;
    private final EventDefinitions eventDefs;
    private final StateTransitionManager transitionManager = new StateTransitionManager();
    private final VersionedReference<CharacterConfig> selectedActor;

    public Testi(GameContext context, AssetManager assetManager, Node root, Container guiContainer, Queue<Event> eventHistory, VersionedReference<CharacterConfig> selectedActor) {
        this.context = context;
        this.assetManager = assetManager;
        this.root = root;
        this.eventHistory = eventHistory;
        this.eventDefs = context.eventDefs;
        this.selectedActor = selectedActor;
        guiContainer.addChild(selectedActorDetails);
//        selectedActorDetails.setInsetsComponent(new DynamicInsetsComponent(0, 0, 1, 1));
        guiContainer.addChild(hoveredActorDetails);
//        hoveredActorDetails.setInsetsComponent(new DynamicInsetsComponent(0, 1, 1, 0));

        Geometry tiles = new Geometry("tiles", new Grid(context.mapWidth() + 1, context.mapHeight() + 1, 1));
        tiles.setMaterial(colorMaterial(ColorRGBA.Blue));
        root.attachChild(tiles);
        selectedActorNode.attachChild(selectionCircleGeometry());
        root.attachChild(selectedActorNode);

        spritesheets.put(0, loadSpritesheet("/test/isometric_heroine/", "spritesheet.json"));
        spritesheets.put(3, loadSpritesheet("/test/isometric_heroine/", "spritesheet2.json"));
        spritesheets.put(6, loadSpritesheet("/test/isometric_heroine/", "spritesheet3.json"));
//        s2 = new Sprite2(heroineSpritesheet, FrameType.SOUTH_WEST);
//        s2.getNode().setLocalTranslation(getTranslation(5, 2));
//        s2.getNode().attachChild(selectionCircleGeometry());
//        root.attachChild(s2.getNode());

        Material tileMaterial = tileMaterial("test/tiles/terrain.png");
        SpritesheetInstance grasslandSpritesheet = loadSpritesheet("/test/tiles/", "spritesheet.json");
        String[] animations = grasslandSpritesheet.getAnimations().toArray(new String[grasslandSpritesheet.getAnimations().size()]);
        for (int x = 0; x < context.mapWidth(); x++) {
            for (int y = 0; y < context.mapHeight(); y++) {
                Quad quad = new Quad(1, 1);
                quad.setBuffer(VertexBuffer.Type.TexCoord, 2, new float[]{
                    0, 0,
                    1 / 16f, 0,
                    1 / 16f, 1 / 16f,
                    0, 1 / 16f});
                Node n = new Node();
                Geometry g = new Geometry("tile" + x + "/" + y, quad);
                g.setMaterial(tileMaterial);
                n.attachChild(g);
                n.setLocalTranslation(getTranslation(x, y).add(0, -0.5f, 0));
                n.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X));
                root.attachChild(n);
//                Sprite tile = new Sprite(grasslandSpritesheet, FrameType.NONE);
//                tile.getNode().setLocalTranslation(getTranslation(x, y));
//                String animation;
//                do {
//                    animation = animations[ThreadLocalRandom.current().nextInt(animations.length)];
//                } while (!animation.startsWith("grass"));
//                tile.setAnimation(animation);
//                root.attachChild(tile.getNode());
            }
        }

        IntArrayList obstacles = context.getData().query(context.componentDefs.obstacle.id).list();
        for (int obstacle : obstacles) {
            int position = context.getData().get(obstacle, context.componentDefs.position.id);
            Sprite tree = new Sprite(grasslandSpritesheet, FrameType.NONE);
            int x = Coordinates.x(position);
            int y = Coordinates.y(position);
            tree.getNode().setLocalTranslation(getTranslation(x, y));
            String animation;
            do {
                animation = animations[ThreadLocalRandom.current().nextInt(animations.length)];
            } while (!animation.startsWith("obstacle"));
            tree.setAnimation(animation);
            root.attachChild(tree.getNode());
        }

        update();

        IntArrayList list = context.getData().query(context.componentDefs.sprite.id).list();
        for (int entity : list) {
            sprites.get(entity).getNode().setLocalTranslation(getEntityTranslation(entity));
        }
    }

    private SpritesheetInstance loadSpritesheet(String path, String json) {
        SpritesheetData data;
        try (InputStreamReader reader = new InputStreamReader(Testi.class.getResourceAsStream(path + json))) {
            data = new Gson().fromJson(reader, SpritesheetData.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Texture texture = assetManager.loadTexture(new TextureKey(path + data.spritesheetPath, false));
        Material spriteMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        spriteMaterial.setTexture("ColorMap", texture);
        spriteMaterial.setFloat("AlphaDiscardThreshold", 0.001f);
        spriteMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        spriteMaterial.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        return new SpritesheetInstance(data, spriteMaterial);
    }

    private Material tileMaterial(String atlas) {
        Texture texture = assetManager.loadTexture(new TextureKey(atlas, false));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", texture);
        mat.setColor("Color", ColorRGBA.Orange);
        mat.setFloat("AlphaDiscardThreshold", 0.001f);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        return mat;
    }

    private Geometry selectionCircleGeometry() {
        Cylinder cylinder = new Cylinder(2, 16, 0.55f, 0.001f, false);
        cylinder.setMode(Mesh.Mode.Lines);
        Geometry test = new Geometry("selection", cylinder);
        test.setMaterial(colorMaterial(ColorRGBA.Yellow));
        test.rotate(FastMath.HALF_PI, 0, 0);
        test.setLocalTranslation(0, 0.02f, 0);
        return test;
    }

    private void update() {
        updateSprites();
        updateSelectedActorCircle();
        updateActorDetails(selectedActorDetails, selectedActor.get() == null? null: selectedActor.get().entity);
        updateActorDetails(hoveredActorDetails, hoveredActor);
    }

    private void updateSprites() {
        IntArrayList list = context.getData().query(context.componentDefs.sprite.id).list();
        for (int entity : list) {
            if (sprites.containsKey(entity)) {
                continue;
            }
            Sprite sprite = new Sprite(spritesheets.get(context.getData().get(entity, context.componentDefs.sprite.id)), FrameType.SOUTH_WEST);
            sprites.put(entity, sprite);

//            float halfBoxHeight = 1.3f;
//            WireBox wireBox = new WireBox(0.4f, halfBoxHeight, 0.4f);
//            Geometry geo = new Geometry("boxy", wireBox);
//            geo.setMaterial(colorMaterial(ColorRGBA.Green));
//            geo.setLocalTranslation(0, halfBoxHeight, 0);
//            sprite.getNode().attachChild(geo);
            root.attachChild(sprite.getNode());
        }
        Iterator<Integer> iterator = sprites.keySet().iterator();
        while (iterator.hasNext()) {
            int entity = iterator.next();
            if (list.indexOf(entity) == -1) {
                root.detachChild(sprites.get(entity).getNode());
                iterator.remove();
            }
        }
    }

    private void updateSelectedActorCircle() {
        if (selectedActor.get() != null) {
            selectedActorNode.setLocalTranslation(getEntityTranslation(selectedActor.get().entity));
            selectedActorNode.setCullHint(Spatial.CullHint.Dynamic);
        } else {
            selectedActorNode.setCullHint(Spatial.CullHint.Always);
        }
    }

    private void updateActorDetails(ActorDetailsContainer container, Integer actor) {
        if (actor != null) {
            container.setCullHint(Spatial.CullHint.Dynamic);
            container.setActorName("#" + actor);
            container.setHealth(context.getData().getOptional(actor, context.componentDefs.health.active.id).orElse(0), context.getData().getOptional(actor, context.componentDefs.health.buffed.id).orElse(0));
            container.setActionPoints(context.getData().getOptional(actor, context.componentDefs.actionPoints.active.id).orElse(0), context.getData().getOptional(actor, context.componentDefs.actionPoints.buffed.id).orElse(0));
            container.setMovePoints(context.getData().getOptional(actor, context.componentDefs.movePoints.active.id).orElse(0), context.getData().getOptional(actor, context.componentDefs.movePoints.buffed.id).orElse(0));
        } else {
            container.setCullHint(Spatial.CullHint.Always);
        }
    }

    private Vector3f getEntityTranslation(int entity) {
        int coordinates = context.getData().get(entity, context.componentDefs.position.id);
        return getTranslation(Coordinates.x(coordinates), Coordinates.y(coordinates));
    }

    private Vector3f getTranslation(int x, int y) {
        return new Vector3f(x + 0.5f, 0, y + 0.5f);
    }

    public void hover(int coordinates) {
        OptionalInt entity = entityByCoordinates(coordinates);
        if (entity.isPresent()) {
            if (hoveredActor == null || hoveredActor != entity.getAsInt()) {
                hoveredActor = entity.getAsInt();
                update();
            }
        } else if (hoveredActor != null) {
            hoveredActor = null;
            update();
        }
    }

    private OptionalInt entityByCoordinates(int coordinates) {
        return context.getData().query(context.componentDefs.position.id).unique(x -> context.getData().hasValue(x, context.componentDefs.position.id, coordinates));
    }

    private Material colorMaterial(ColorRGBA color) {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", color);
        return material;
    }

    public Integer getHoveredActor() {
        return hoveredActor;
    }

    public GameContext getContext() {
        return context;
    }

    public void update(float elapsedSeconds) {
        if (selectedActor.update()) {
            update();
        }
        Event e;
        while ((e = eventHistory.poll()) != null) {
            if (e.getId() == eventDefs.walkToTargetEffect.id()) {
                EntityMoveEvent event = (EntityMoveEvent) e;
                Sprite sprite = sprites.get(event.entity);
                String animation;
                if (sprite.isAnimationSupported("walk0") && sprite.isAnimationSupported("walk1")) {
                    animation = "walk" + ((Coordinates.x(event.to) + Coordinates.y(event.to)) & 1);
                } else {
                    animation = "walk";
                }
                transitionManager.enqueue(new SpriteAnimationTransition(event.from, event.to, sprite, animation));
            }
            if (e.getId() == eventDefs.setAlive.id()) {
                EntityValueEvent event = (EntityValueEvent) e;
                if (event.value == 0) {
                    Sprite sprite = sprites.get(event.entity);
                    if (sprite.isAnimationSupported("death")) {
                        int coordinates = context.getData().get(event.entity, context.componentDefs.position.id);//TODO: coordinates shouldn't matter
                        transitionManager.enqueue(new SpriteAnimationTransition(coordinates, coordinates, sprite, "death"));
                    }
                }
            }
        }
        transitionManager.update(elapsedSeconds);
        for (Sprite sprite : sprites.values()) {
            sprite.update(elapsedSeconds);
        }
//        s2.setFrameType(new FrameType[]{FrameType.NORTH_EAST, FrameType.NORTH_WEST, FrameType.SOUTH_WEST, FrameType.SOUTH_EAST}[(int) (System.currentTimeMillis() / 7237) % 4]);
//        s2.update(elapsedSeconds);
    }

}
