package com.etherblood.templates;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.rules.context.StatComponentMaps;
import com.etherblood.rules.context.TypeComponentMaps;

/**
 *
 * @author Philipp
 */
public class Pokemons {

    private final SimpleComponentMap spriteId;
    private final StatComponentMaps health;
    private final StatComponentMaps actionPoints;
    private final StatComponentMaps movePoints;
    private final TypeComponentMaps types;

    public Pokemons(SimpleComponentMap spriteId, StatComponentMaps health, StatComponentMaps actionPoints, StatComponentMaps movePoints, TypeComponentMaps types) {
        this.spriteId = spriteId;
        this.health = health;
        this.actionPoints = actionPoints;
        this.movePoints = movePoints;
        this.types = types;
    }

    public void bulbasaur(int entity) {
        spriteId.set(entity, 0);
        health.base.set(entity, 20);
        actionPoints.base.set(entity, 4);
        movePoints.base.set(entity, 2);
        types.grass.power.base.set(entity, 50);
        types.grass.toughness.base.set(entity, 50);
        types.fire.toughness.base.set(entity, -100);
    }

    public void charmander(int entity) {
        spriteId.set(entity, 3);
        health.base.set(entity, 20);
        actionPoints.base.set(entity, 4);
        movePoints.base.set(entity, 2);
        types.fire.power.base.set(entity, 50);
        types.fire.toughness.base.set(entity, 50);
        types.water.toughness.base.set(entity, -100);
    }

    public void squirtle(int entity) {
        spriteId.set(entity, 6);
        health.base.set(entity, 20);
        actionPoints.base.set(entity, 4);
        movePoints.base.set(entity, 2);
        types.water.power.base.set(entity, 50);
        types.water.toughness.base.set(entity, 50);
        types.grass.toughness.base.set(entity, -100);
    }

}
