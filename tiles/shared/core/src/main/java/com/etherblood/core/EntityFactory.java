package com.etherblood.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityFactory {

    private final Logger LOG = LoggerFactory.getLogger(EntityFactory.class);

    private int next;

    public EntityFactory() {
        this(1);
    }

    public EntityFactory(int next) {
        this.next = next;
    }

    public int create() {
        LOG.debug("Created entity #{}.", next);
        return next++;
    }

    public int peek() {
        return next;
    }
}
