package com.etherblood.rules.context;

import com.etherblood.rules.stats.PokemonTypes;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Philipp
 */
public class PokemonTemplates {
    
    public final int spriteId, health, actionPoints, movePoints;
    public final Map<PokemonTypes, Integer> power = new EnumMap<>(PokemonTypes.class);
    public final Map<PokemonTypes, Integer> toughness = new EnumMap<>(PokemonTypes.class);

    public PokemonTemplates(int spriteId, int health, int actionPoints, int movePoints) {
        this.spriteId = spriteId;
        this.health = health;
        this.actionPoints = actionPoints;
        this.movePoints = movePoints;
    }
}
