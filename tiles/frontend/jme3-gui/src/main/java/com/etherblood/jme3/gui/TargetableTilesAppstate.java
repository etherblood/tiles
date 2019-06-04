package com.etherblood.jme3.gui;

import com.etherblood.core.util.Coordinates;
import com.etherblood.mods.core.game.components.CoreComponents;
import com.jme3.app.state.AbstractAppState;
import com.simsilica.lemur.core.VersionedHolder;
import com.simsilica.lemur.core.VersionedReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TargetableTilesAppstate extends AbstractAppState {

    private static final Logger LOG = LoggerFactory.getLogger(TargetableTilesAppstate.class);
    private final CoreComponents core;
    private final VersionedReference<Integer> mapSize;
    private final VersionedReference<Integer> gameVersion;
    private final VersionedReference<Integer> selectedAction;
    private final VersionedHolder<Set<Integer>> targetableTiles = new VersionedHolder<>(Collections.emptySet());

    public TargetableTilesAppstate(CoreComponents core, VersionedReference<Integer> mapSize, VersionedReference<Integer> gameVersion, VersionedReference<Integer> selectedAction) {
        this.core = core;
        this.mapSize = mapSize;
        this.gameVersion = gameVersion;
        this.selectedAction = selectedAction;
    }

    @Override
    public void update(float tpf) {
        if (selectedAction.update() | mapSize.update() | gameVersion.update()) {
            if (selectedAction.get() != null && mapSize.get() != null) {
                int size = mapSize.get();
                Set<Integer> result = new HashSet<>();
                int skill = selectedAction.get();
                int actor = core.skill.ofActor.get(skill);
                int center = core.actor.position.get(actor);
                int maxManhattanDistance = core.skill.targeting.position.manhattanRange.getOptional(skill).orElse(Integer.MAX_VALUE);
                boolean requiresTargetEmpty = core.skill.targeting.actor.none.has(skill);
                for (int y = 0; y < Coordinates.y(size); y++) {
                    for (int x = 0; x < Coordinates.x(size); x++) {
                        int target = Coordinates.of(x, y);
                        if (Coordinates.manhattenDistance(center, target) > maxManhattanDistance) {
                            continue;
                        }
                        if (requiresTargetEmpty && core.actor.position.query().exists(entity -> core.actor.position.hasValue(entity, target))) {
                            continue;
                        }
                        result.add(target);
                    }
                }
                LOG.debug("Targetable tiles for action #{} are {}.", skill, result);
                targetableTiles.updateObject(result);
            } else {
                targetableTiles.updateObject(Collections.emptySet());
            }
        }
    }

    public VersionedReference<Set<Integer>> targetableTilesReference() {
        return targetableTiles.createReference();
    }

}
