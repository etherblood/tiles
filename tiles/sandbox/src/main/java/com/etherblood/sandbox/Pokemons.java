package com.etherblood.sandbox;

import com.etherblood.entities.SimpleEntityData;
import com.etherblood.rules.components.Components;

/**
 *
 * @author Philipp
 */
public class Pokemons {

    private final SimpleEntityData data;

    public Pokemons(SimpleEntityData data) {
        this.data = data;
    }

    public void bulbasaur(int entity) {
        data.set(entity, Components.SPRITE, 0);
        data.set(entity, Components.Stats.Health.BASE, 20);
        data.set(entity, Components.Stats.ActionPoints.BASE, 4);
        data.set(entity, Components.Stats.MovePoints.BASE, 2);
        data.set(entity, Components.Stats.Power.Earth.BASE, 50);
        data.set(entity, Components.Stats.Toughness.Earth.BASE, 50);
        data.set(entity, Components.Stats.Toughness.Fire.BASE, -100);
    }

    public void charmander(int entity) {
        data.set(entity, Components.SPRITE, 3);
        data.set(entity, Components.Stats.Health.BASE, 20);
        data.set(entity, Components.Stats.ActionPoints.BASE, 4);
        data.set(entity, Components.Stats.MovePoints.BASE, 2);
        data.set(entity, Components.Stats.Power.Fire.BASE, 50);
        data.set(entity, Components.Stats.Toughness.Fire.BASE, 50);
        data.set(entity, Components.Stats.Toughness.Water.BASE, -100);
    }

    public void squirtle(int entity) {
        data.set(entity, Components.SPRITE, 6);
        data.set(entity, Components.Stats.Health.BASE, 20);
        data.set(entity, Components.Stats.ActionPoints.BASE, 4);
        data.set(entity, Components.Stats.MovePoints.BASE, 2);
        data.set(entity, Components.Stats.Power.Water.BASE, 50);
        data.set(entity, Components.Stats.Toughness.Water.BASE, 50);
        data.set(entity, Components.Stats.Toughness.Earth.BASE, -100);
    }

}
