package com.etherblood.jme3.gui;

import com.etherblood.collections.IntList;
import com.etherblood.core.GameContext;
import com.etherblood.core.util.Flags;
import com.etherblood.jme3.gui.util.VersionedMap;
import com.etherblood.jme3.gui.util.VersionedMapReference;
import com.etherblood.mods.core.game.components.CoreComponents;
import com.jme3.app.state.AbstractAppState;
import com.simsilica.lemur.core.VersionedHolder;
import com.simsilica.lemur.core.VersionedReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;

public class GameStateAppstate extends AbstractAppState {

    private final GameContext gameContext;
    private final OptionalInt playerIndex;
    private final VersionedHolder<Integer> gameVersion = new VersionedHolder<>();
    private final VersionedHolder<Integer> mapSize = new VersionedHolder<>();
    private final VersionedMap<Integer, ActorDetails> actors = new VersionedMap<>();
    private final VersionedHolder<Set<Integer>> selectableActors = new VersionedHolder<>(Collections.emptySet());

    public GameStateAppstate(GameContext gameContext, OptionalInt playerIndex) {
        this.gameContext = gameContext;
        this.playerIndex = playerIndex;
    }

    @Override
    public void update(float tpf) {
        if (!gameVersion.updateObject(gameContext.getVersion())) {
            return;
        }
        CoreComponents core = gameContext.getComponents(CoreComponents.class);
        IntList list = core.actor.sprite.query().list(core.actor.position::has);
        for (int actor : list) {
            if (!actors.getObject().containsKey(actor)) {
                actors.getObject().put(actor, new VersionedHolder<>(new ActorDetails(actor, core.actor.sprite.get(actor), core.actor.position.get(actor))));
            }
        }
        for (int actor : actors.getObject().keySet()) {
            if (!list.contains(actor)) {
                actors.getObject().remove(actor);
            }
        }
        OptionalInt mapSizeValue = core.mapSize.query().uniqueValue();
        if (mapSizeValue.isPresent()) {
            mapSize.updateObject(mapSizeValue.getAsInt());
        } else {
            mapSize.updateObject(null);
        }

        if (playerIndex.isPresent()) {
            IntList nextSelectableActors = core.actor.active.query().list(x -> Flags.containsIndex(core.actor.controlledBy.get(x), playerIndex.getAsInt()));
            selectableActors.updateObject(new HashSet<>(nextSelectableActors.boxed()));
        } else {
            selectableActors.updateObject(Collections.emptySet());
        }
    }

    public VersionedReference<Integer> mapSizeReference() {
        return mapSize.createReference();
    }

    public VersionedReference<Integer> gameVersionReference() {
        return gameVersion.createReference();
    }

    public VersionedMapReference<Integer, ActorDetails> actorsReference() {
        return actors.createReference();
    }

    public VersionedReference<Set<Integer>> selectableActorsReference() {
        return selectableActors.createReference();
    }
}
