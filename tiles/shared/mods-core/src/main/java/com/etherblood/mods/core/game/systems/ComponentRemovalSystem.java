package com.etherblood.mods.core.game.systems;

import com.etherblood.core.GameSystem;
import com.etherblood.core.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ComponentRemovalSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentRemovalSystem.class);
    private final Component<?> component;

    public ComponentRemovalSystem(Component<?> component) {
        this.component = component;
    }

    @Override
    public void update() {
        for (int entity : component.query().list()) {
            if (LOG.isDebugEnabled() && component.has(entity)) {
                LOG.debug("Removed {}={} from #{}.", component.name, component.getGeneric(entity), entity);
            }
            component.remove(entity);
        }
    }
}
