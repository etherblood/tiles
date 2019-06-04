package com.etherblood.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityFactory {

    private final Logger LOG = LoggerFactory.getLogger(EntityFactory.class);

    private int next;

    public EntityFactory() {
        reset();
    }

    public EntityFactory(int next) {
    }

    public int create() {
        LOG.debug("Created entity #{}.", next);
        return next++;
    }

    public int peek() {
        return next;
    }
    
    public final void reset() {
        next = 1;
    }
}
