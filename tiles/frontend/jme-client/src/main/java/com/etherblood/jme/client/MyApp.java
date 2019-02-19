package com.etherblood.jme.client;

import com.etherblood.jme.client.skills.targeting.HoveredTileAppstate;
import com.etherblood.collections.IntHashSet;
import com.etherblood.events.Event;
import com.etherblood.jme.client.actor.SelectedActorAppsate;
import com.etherblood.jme.client.skills.SkillAppstate;
import com.etherblood.jme.client.skills.targeting.TargetableCursorAppstate;
import com.etherblood.jme.client.skills.targeting.TargetableTilesAppstate;
import com.etherblood.jme.client.util.Cursor;
import com.etherblood.jme.client.util.VersionedModel;
import com.etherblood.rules.GameContext;
import com.etherblood.rules.game.setup.config.CharacterConfig;
import com.etherblood.rules.game.setup.config.SkillConfig;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Philipp
 */
public class MyApp extends SimpleApplication {

    private Testi testi;
    private final VersionedModel<CharacterConfig> selectedActor = new VersionedModel<>();
    private final VersionedModel<SkillConfig> selectedAction = new VersionedModel<>();
    private final VersionedModel<Integer> hoveredTile = new VersionedModel<>();
    private final VersionedModel<IntHashSet> targetableTiles = new VersionedModel<>(new IntHashSet());
    private final VersionedModel<IntHashSet> targetedTiles = new VersionedModel<>(new IntHashSet());
    private final int playerId = 0;
    private GameContext gameContext;

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
//        stateManager.attach(new DetailedProfilerState());

        Container guiContainer = new Container();
        guiNode.attachChild(guiContainer);
        guiContainer.setLocalTranslation(0, getCamera().getHeight(), 0);

        stateManager.detach(stateManager.getState(FlyCamAppState.class));
        setPauseOnLostFocus(true);

        Queue<Event> eventHistory = new LinkedList<>();
        gameContext = new ContextFactory().withGlobalEventHandler(eventHistory::add).create();
        testi = new Testi(gameContext, assetManager, rootNode, guiContainer, eventHistory, selectedActor.createReference());

        initSkillbar();
        stateManager.attach(new SelectedActorAppsate(gameContext, selectedActor.createReference(), playerId));
        stateManager.attach(new TargetableTilesAppstate(gameContext, selectedActor.createReference(), selectedAction.createReference(), targetableTiles.createReference()));
        stateManager.attach(new HoveredTileAppstate(hoveredTile, gameContext.mapSize()));
        stateManager.attach(new TargetableCursorAppstate(new Cursor(assetManager.loadAsset(new TextureKey("test/pointer_smol.png")), 14, 1), hoveredTile.createReference(), targetableTiles.createReference()));
        initCamera();
    }

    private void initCamera() {
        Camera camera = getCamera();
        camera.setParallelProjection(true);
        camera.setRotation(new Quaternion().fromAngles(FastMath.QUARTER_PI / 3 * 2, FastMath.QUARTER_PI, 0));

        //settings which seemed to work at the time...
        float zoom = 8;
        float aspect = (float) camera.getWidth() / camera.getHeight();
        camera.setLocation(new Vector3f(0, zoom, 0));
        camera.setFrustum(0f, 100, zoom * aspect, -zoom * aspect, zoom, -zoom);

//        stateManager.attach(new CameraAppstate());
    }

    private void initSkillbar() {
        Container skillbarContainer = new Container();
        skillbarContainer.setLocalTranslation(getCamera().getWidth() - 640, 64, 0);
        skillbarContainer.setPreferredSize(new Vector3f(640, 64, 0));
        guiNode.attachChild(skillbarContainer);
        stateManager.attach(new SkillAppstate(gameContext, skillbarContainer, selectedActor.createReference(), selectedAction.createReference(), targetableTiles.createReference(), "test/icons/perspective-dice-six-faces-random.png", "test/icons/circle.png"));
    }

    @Override
    public void simpleUpdate(float elapsedSeconds) {
        testi.update(elapsedSeconds);

    }

}
