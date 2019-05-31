package com.etherblood.jme3.gui;

import com.etherblood.core.GameContext;
import com.etherblood.game.client.Game;
import com.etherblood.game.client.GameClient;
import com.etherblood.jme3.gui.skillbar.SkillbarAppstate;
import com.etherblood.jme3.gui.state.StateTransitionAppstate;
import com.etherblood.jme3.gui.util.Cursor;
import com.etherblood.jme3.gui.util.Utils;
import com.etherblood.mods.core.game.components.CoreComponents;
import com.jme3.app.DetailedProfilerState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.asset.TextureKey;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.style.BaseStyles;
import java.util.OptionalInt;

public class TilesApplication extends SimpleApplication {

    private final GameClient client;
    private VersionedReference<Game> activeGame;

    public TilesApplication(GameClient client) {
        this.client = client;
    }

    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        initCamera();

        stateManager.detach(stateManager.getState(FlyCamAppState.class));
        stateManager.attach(new DetailedProfilerState());

        GameSelectAppstate gameSelectAppstate = new GameSelectAppstate();
        activeGame = gameSelectAppstate.activeGameReference();
        stateManager.attach(gameSelectAppstate);
        stateManager.attach(new StateTransitionAppstate());
    }

    @Override
    public void simpleUpdate(float elapsedSeconds) {
        if (activeGame.update()) {
            detachAll(
                    GameStateAppstate.class,
                    GuiStateAppstate.class,
                    GridAppstate.class,
                    ActorSpritesAppstate.class,
                    HoveredTileAppstate.class,
                    SelectableActionsAppstate.class,
                    SkillbarAppstate.class,
                    TargetableTilesAppstate.class,
                    TargetableCursorAppstate.class,
                    ActorAnimationsAppstate.class);

            Game game = activeGame.get();
            GameContext gameContext = game.getContext();
            CoreComponents core = gameContext.getComponents(CoreComponents.class);
            OptionalInt playerIndex = Utils.playerIndex(game.getConfig().getPlayers(), client.getPlayerId());
            GameStateAppstate gameStateAppstate = new GameStateAppstate(gameContext, playerIndex);
            GuiStateAppstate guiStateAppstate = new GuiStateAppstate(gameStateAppstate.selectableActorsReference());
            ActorSpritesAppstate actorSpritesAppstate = new ActorSpritesAppstate(gameStateAppstate.actorsReference());
            SelectableActionsAppstate selectableActionsAppstate = new SelectableActionsAppstate(core, guiStateAppstate.selectedActorReference(), gameStateAppstate.gameVersionReference());
            SkillbarAppstate skillbarAppstate = new SkillbarAppstate(game.getId(), client, core, gameStateAppstate.gameVersionReference(), guiStateAppstate.selectedActorReference(), selectableActionsAppstate.selectableActionsReference());
            TargetableTilesAppstate targetableTilesAppstate = new TargetableTilesAppstate(core, gameStateAppstate.mapSizeReference(), gameStateAppstate.gameVersionReference(), skillbarAppstate.selectedActionReference());
            HoveredTileAppstate hoveredTileAppstate = new HoveredTileAppstate(gameStateAppstate.mapSizeReference());
            stateManager.attach(gameStateAppstate);
            stateManager.attach(guiStateAppstate);
            stateManager.attach(targetableTilesAppstate);
            stateManager.attach(new GridAppstate(core, gameStateAppstate.mapSizeReference(), gameStateAppstate.gameVersionReference(), guiStateAppstate.selectedActorReference(), targetableTilesAppstate.targetableTilesReference(), gameStateAppstate.selectableActorsReference()));
            stateManager.attach(actorSpritesAppstate);
            stateManager.attach(selectableActionsAppstate);
            stateManager.attach(skillbarAppstate);
            stateManager.attach(hoveredTileAppstate);
            stateManager.attach(new TargetableCursorAppstate(new Cursor(assetManager.loadAsset(new TextureKey("test/pointer_smol.png")), 14, 1), hoveredTileAppstate.hoveredTileReference(), targetableTilesAppstate.targetableTilesReference()));
            stateManager.attach(new ActorAnimationsAppstate(game.getContext().getAnimationsController()));
            stateManager.attach(new GameOverAppstate(gameContext));
        }
    }

    @SafeVarargs
    private final void detachAll(Class<? extends AppState>... stateClasses) {
        for (Class<? extends AppState> stateClass : stateClasses) {
            AppState state = stateManager.getState(stateClass);
            if (state != null) {
                stateManager.detach(state);
            }
        }
    }

    private void initCamera() {
        Camera camera = getCamera();
        camera.setParallelProjection(true);
        camera.setRotation(new Quaternion().fromAngles(FastMath.QUARTER_PI / 3 * 2, FastMath.QUARTER_PI, 0));

        //settings which seemed to work at the time...
        float zoom = 4;
        float aspect = (float) camera.getWidth() / camera.getHeight();
        camera.setLocation(new Vector3f(0, zoom + 0.5f, 0));
        camera.setFrustum(0f, 100, zoom * aspect, -zoom * aspect, zoom, -zoom);
    }

    @Override
    public void destroy() {
        super.destroy();
        client.close();
    }

}
