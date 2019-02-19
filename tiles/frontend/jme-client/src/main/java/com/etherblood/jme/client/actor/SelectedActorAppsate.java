package com.etherblood.jme.client.actor;

import com.etherblood.collections.IntArrayList;
import com.etherblood.entities.EntityData;
import com.etherblood.jme.client.util.VersionedReference;
import com.etherblood.rules.GameContext;
import com.etherblood.rules.game.setup.config.CharacterConfig;
import com.etherblood.rules.util.Flags;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public class SelectedActorAppsate extends BaseAppState {

    private final GameContext context;
    private final VersionedReference<CharacterConfig> selectedActor;
    private final int playerId;

    public SelectedActorAppsate(GameContext context, VersionedReference<CharacterConfig> selectedActor, int playerId) {
        this.context = context;
        this.selectedActor = selectedActor;
        this.playerId = playerId;
    }

    @Override
    public void update(float tpf) {
        CharacterConfig actor = selectedActor.get();
        IntArrayList selectable = selectableActors();
        if (actor == null || selectable.stream().noneMatch(x -> x == actor.entity)) {
            selectedActor.set(context.config.characters.stream().filter(x -> selectable.contains(x.entity)).findFirst().orElse(null));
        }
    }

    private IntArrayList selectableActors() {
        EntityData data = context.getData();
        return data.query(context.componentDefs.activePlayer.id)
                .list(x -> Flags.containsIndex(data.get(x, context.componentDefs.controlledBy.id), playerId));
    }

    private void selectNextActor() {
        IntArrayList actors = selectableActors();
        if (actors.isEmpty()) {
            return;
        }
        int currentIndex = actors.indexOf(selectedActor.get().entity);
        int nextIndex = (currentIndex + 1) % actors.size();
        int nextActor = actors.get(nextIndex);
        CharacterConfig nextActorConfig = context.config.characters.stream().filter(x -> x.entity == nextActor).findFirst().orElse(null);
        selectedActor.set(nextActorConfig);
    }

    @Override
    protected void initialize(Application app) {
        keyMapping("selectNextActor", KeyInput.KEY_TAB, this::selectNextActor);
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

    private void keyMapping(String name, int key, Runnable listener) {
        getApplication().getInputManager().addMapping(name, new KeyTrigger(key));
        getApplication().getInputManager().addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (!isPressed) {
                    listener.run();
                }
            }
        }, name);
    }

}
