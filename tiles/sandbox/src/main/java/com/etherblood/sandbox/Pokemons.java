package com.etherblood.sandbox;

import com.etherblood.entities.EntityData;
import com.etherblood.rules.components.ComponentDefinitions;

/**
 *
 * @author Philipp
 */
public class Pokemons {

    private final EntityData data;
    private final ComponentDefinitions components;

    public Pokemons(EntityData data, ComponentDefinitions components) {
        this.data = data;
        this.components = components;
    }

    public void bulbasaur(int entity) {
        data.set(entity, components.sprite.id, 0);
        data.set(entity, components.health.base.id, 20);
        data.set(entity, components.actionPoints.base.id, 4);
        data.set(entity, components.movePoints.base.id, 2);
        data.set(entity, components.power.earth.base.id, 50);
        data.set(entity, components.toughness.earth.base.id, 50);
        data.set(entity, components.toughness.fire.base.id, -100);
    }

    public void charmander(int entity) {
        data.set(entity, components.sprite.id, 3);
        data.set(entity, components.health.base.id, 20);
        data.set(entity, components.actionPoints.base.id, 4);
        data.set(entity, components.movePoints.base.id, 2);
        data.set(entity, components.power.fire.base.id, 50);
        data.set(entity, components.toughness.fire.base.id, 50);
        data.set(entity, components.toughness.water.base.id, -100);
    }

    public void squirtle(int entity) {
        data.set(entity, components.sprite.id, 6);
        data.set(entity, components.health.base.id, 20);
        data.set(entity, components.actionPoints.base.id, 4);
        data.set(entity, components.movePoints.base.id, 2);
        data.set(entity, components.power.water.base.id, 50);
        data.set(entity, components.toughness.water.base.id, 50);
        data.set(entity, components.toughness.earth.base.id, -100);
    }

}
