package com.etherblood.jme3.gui;

import com.jme3.app.state.AbstractAppState;
import com.simsilica.lemur.core.VersionedHolder;
import com.simsilica.lemur.core.VersionedReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GuiStateAppstate extends AbstractAppState {

    private final VersionedReference<Set<Integer>> selectableActors;
    private final VersionedHolder<Integer> selectedActor = new VersionedHolder<>();

    public GuiStateAppstate(VersionedReference<Set<Integer>> selectableActors) {
        this.selectableActors = selectableActors;
    }

    @Override
    public void update(float tpf) {
        if (selectableActors.update()) {
            if (!selectableActors.get().contains(selectedActor.getObject())) {
                selectNextActor();
            }
        }
    }

    public void selectNextActor() {
        Integer nextActor;
        if (selectableActors.get().isEmpty()) {
            nextActor = null;
        } else {
            List<Integer> actors = new ArrayList<>(selectableActors.get());
            Collections.sort(actors);
            Integer previousActor = selectedActor.getObject();
            int previousIndex = actors.indexOf(previousActor);//null is handled implicitly
            int index = (previousIndex + 1) % actors.size();
            nextActor = actors.get(index);
        }
        selectedActor.updateObject(nextActor);
    }

    public VersionedReference<Integer> selectedActorReference() {
        return selectedActor.createReference();
    }

}
