package com.etherblood.jme3.gui;

import com.etherblood.mods.core.game.components.CoreComponents;
import com.jme3.app.state.AbstractAppState;
import com.simsilica.lemur.core.VersionedHolder;
import com.simsilica.lemur.core.VersionedReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;

public class SelectableActionsAppstate extends AbstractAppState {

    private final CoreComponents core;
    private final VersionedReference<Integer> selectedActor;
    private final VersionedReference<Integer> gameVersion;
    private final VersionedHolder<Set<Integer>> selectableActions = new VersionedHolder<>(Collections.emptySet());

    public SelectableActionsAppstate(CoreComponents core, VersionedReference<Integer> selectedActor, VersionedReference<Integer> gameVersion) {
        this.core = core;
        this.selectedActor = selectedActor;
        this.gameVersion = gameVersion;
    }

    @Override
    public void update(float tpf) {
        if (selectedActor.update() | gameVersion.update()) {
            Integer actor = selectedActor.get();
            if (actor != null) {
                Set<Integer> actions = new HashSet<>();
                for (int skill : core.skill.ofActor.query().list(skill -> core.skill.ofActor.hasValue(skill, actor))) {
                    if (core.skill.cooldown.active.has(skill)) {
                        continue;
                    }
                    OptionalInt movePointCost = core.skill.cost.movePoints.getOptional(skill);
                    if (movePointCost.isPresent() && core.stats.movePoints.active.getOptional(actor).orElse(0) < movePointCost.getAsInt()) {
                        continue;
                    }
                    OptionalInt actionPointCost = core.skill.cost.actionPoints.getOptional(skill);
                    if (actionPointCost.isPresent() && core.stats.actionPoints.active.getOptional(actor).orElse(0) < actionPointCost.getAsInt()) {
                        continue;
                    }
                    actions.add(skill);
                }
                selectableActions.updateObject(actions);
            } else {
                selectableActions.updateObject(Collections.emptySet());
            }
        }
    }

    public VersionedReference<Set<Integer>> selectableActionsReference() {
        return selectableActions.createReference();
    }

}
