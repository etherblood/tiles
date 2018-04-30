package com.etherblood.entities;

import com.etherblood.collections.IntSet;
import java.util.function.IntSupplier;

/**
 *
 * @author Philipp
 */
public class EntityPool {

    private final IntSupplier random;
    private final IntSet entities = new IntSet();

    public EntityPool(IntSupplier random) {
        this.random = random;
    }

    public int create() {
        int entityKey;
        do {
            entityKey = random.getAsInt();
        } while (entities.hasKey(entityKey));
        entities.set(entityKey);
        return entityKey;
    }

    public IntSet getEntities() {
        return entities;
    }

}
