package com.etherblood.jme.client.skills.targeting;

import com.etherblood.collections.IntHashSet;
import com.etherblood.jme.client.util.VersionedReference;
import com.etherblood.rules.GameContext;
import com.etherblood.rules.game.setup.config.CharacterConfig;
import com.etherblood.rules.game.setup.config.SkillConfig;
import com.etherblood.rules.util.Coordinates;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import java.util.OptionalInt;

public class TargetableTilesAppstate extends BaseAppState {

    private final GameContext game;
    private final VersionedReference<CharacterConfig> selectedActor;
    private final VersionedReference<SkillConfig> selectedAction;
    private final VersionedReference<IntHashSet> targetableTiles;

    public TargetableTilesAppstate(GameContext game, VersionedReference<CharacterConfig> selectedActor, VersionedReference<SkillConfig> selectedAction, VersionedReference<IntHashSet> targetableTiles) {
        this.game = game;
        this.selectedActor = selectedActor;
        this.selectedAction = selectedAction;
        this.targetableTiles = targetableTiles;
    }

    @Override
    public void update(float tpf) {
        if (selectedAction.update()) {
            if (selectedAction.get() != null) {
                IntHashSet result = new IntHashSet();
                int actor = selectedActor.get().entity;
                int skill = selectedAction.get().entity;
                int center = game.getData().get(actor, game.componentDefs.position.id);
                for (int y = 0; y < game.mapHeight(); y++) {
                    for (int x = 0; x < game.mapWidth(); x++) {
                        int target = Coordinates.of(x, y);
                        OptionalInt range = game.getData().getOptional(skill, game.componentDefs.skill.targeting.manhattanRange.id);
                        if(range.isPresent()) {
                            if(Coordinates.manhattenDistance(center, target) > range.getAsInt()) {
                                continue;
                            }
                        }
                        OptionalInt empty = game.getData().getOptional(skill, game.componentDefs.skill.targeting.empty.id);
                        if(empty.isPresent()) {
                            if(game.getData().query(game.componentDefs.blocksPath.id).exists(e -> game.getData().hasValue(e, game.componentDefs.position.id, target))) {
                                continue;
                            }
                        }
                        result.set(target);
                    }
                }
                targetableTiles.set(result);
            } else {
                targetableTiles.set(new IntHashSet());
            }
        }
    }

    @Override
    protected void initialize(Application app) {
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

}
