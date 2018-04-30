package com.etherblood.test.client;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class CharacterDetails {

    public int characterId;
    public String name;
    public Integer spriteId;
    public int health, ap, mp;
    public int x, y;
    public List<AbilityDetails> abilities = new ArrayList<>();

    public CharacterDetails(int characterId, String name, Integer spriteId, int health, int ap, int mp, int x, int y) {
        this.characterId = characterId;
        this.name = name;
        this.spriteId = spriteId;
        this.health = health;
        this.ap = ap;
        this.mp = mp;
        this.x = x;
        this.y = y;
    }
}
