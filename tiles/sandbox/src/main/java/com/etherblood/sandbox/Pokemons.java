package com.etherblood.sandbox;

import com.etherblood.entities.EntityData;
import com.etherblood.rules.components.Components;

/**
 *
 * @author Philipp
 */
public class Pokemons {

    private final EntityData data;

    public Pokemons(EntityData data) {
        this.data = data;
    }

    public void bulbasaur(int entity) {
        data.component(Components.SPRITE).set(entity, 0);
        data.component(Components.Stats.Health.BASE).set(entity, 20);
        data.component(Components.Stats.ActionPoints.BASE).set(entity, 4);
        data.component(Components.Stats.MovePoints.BASE).set(entity, 2);
        data.component(Components.Stats.Power.Earth.BASE).set(entity, 50);
        data.component(Components.Stats.Toughness.Earth.BASE).set(entity, 50);
        data.component(Components.Stats.Toughness.Fire.BASE).set(entity, -100);
    }

    public void charmander(int entity) {
        data.component(Components.SPRITE).set(entity, 3);
        data.component(Components.Stats.Health.BASE).set(entity, 20);
        data.component(Components.Stats.ActionPoints.BASE).set(entity, 4);
        data.component(Components.Stats.MovePoints.BASE).set(entity, 2);
        data.component(Components.Stats.Power.Fire.BASE).set(entity, 50);
        data.component(Components.Stats.Toughness.Fire.BASE).set(entity, 50);
        data.component(Components.Stats.Toughness.Water.BASE).set(entity, -100);
    }

    public void squirtle(int entity) {
        data.component(Components.SPRITE).set(entity, 6);
        data.component(Components.Stats.Health.BASE).set(entity, 20);
        data.component(Components.Stats.ActionPoints.BASE).set(entity, 4);
        data.component(Components.Stats.MovePoints.BASE).set(entity, 2);
        data.component(Components.Stats.Power.Water.BASE).set(entity, 50);
        data.component(Components.Stats.Toughness.Water.BASE).set(entity, 50);
        data.component(Components.Stats.Toughness.Earth.BASE).set(entity, -100);
    }

}
