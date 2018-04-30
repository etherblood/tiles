package com.etherblood.rules.context;

import com.etherblood.rules.stats.PokemonTypes;

/**
 *
 * @author Philipp
 */
public class TypeComponentMaps {

    public final ElementComponentMaps normal;
    public final ElementComponentMaps fight;
    public final ElementComponentMaps flying;
    public final ElementComponentMaps poison;
    public final ElementComponentMaps ground;
    public final ElementComponentMaps rock;
    public final ElementComponentMaps bug;
    public final ElementComponentMaps ghost;
    public final ElementComponentMaps steel;
    public final ElementComponentMaps fire;
    public final ElementComponentMaps water;
    public final ElementComponentMaps grass;
    public final ElementComponentMaps electric;
    public final ElementComponentMaps psychic;
    public final ElementComponentMaps ice;
    public final ElementComponentMaps dragon;
    public final ElementComponentMaps dark;
    public final ElementComponentMaps fairy;

    public TypeComponentMaps(DebugEntityBuilder builder) {
        normal = builder.element("normal");
        fight = builder.element("fight");
        flying = builder.element("flying");
        poison = builder.element("poison");
        ground = builder.element("ground");
        rock = builder.element("rock");
        bug = builder.element("bug");
        ghost = builder.element("ghost");
        steel = builder.element("steel");
        fire = builder.element("fire");
        water = builder.element("water");
        grass = builder.element("grass");
        electric = builder.element("electric");
        psychic = builder.element("psychic");
        ice = builder.element("ice");
        dragon = builder.element("dragon");
        dark = builder.element("dark");
        fairy = builder.element("fairy");
    }

    public ElementComponentMaps get(PokemonTypes type) {
        switch (type) {
            case NORMAL:
                return normal;
            case FIGHT:
                return fight;
            case FLYING:
                return flying;
            case POISON:
                return poison;
            case GROUND:
                return ground;
            case ROCK:
                return rock;
            case BUG:
                return bug;
            case GHOST:
                return ghost;
            case STEEL:
                return steel;
            case FIRE:
                return fire;
            case WATER:
                return water;
            case GRASS:
                return grass;
            case ELECTRIC:
                return electric;
            case PSYCHIC:
                return psychic;
            case ICE:
                return ice;
            case DRAGON:
                return dragon;
            case DARK:
                return dark;
            case FAIRY:
                return fairy;
            default:
                throw new AssertionError(type.name());

        }
    }
//
//    public ElementComponentMaps normal() {
//        return normal;
//    }
//
//    public ElementComponentMaps fight() {
//        return fight;
//    }
//
//    public ElementComponentMaps flying() {
//        return flying;
//    }
//
//    public ElementComponentMaps poison() {
//        return poison;
//    }
//
//    public ElementComponentMaps ground() {
//        return ground;
//    }
//
//    public ElementComponentMaps rock() {
//        return rock;
//    }
//
//    public ElementComponentMaps bug() {
//        return bug;
//    }
//
//    public ElementComponentMaps ghost() {
//        return ghost;
//    }
//
//    public ElementComponentMaps steel() {
//        return steel;
//    }
//
//    public ElementComponentMaps fire() {
//        return fire;
//    }
//
//    public ElementComponentMaps water() {
//        return water;
//    }
//
//    public ElementComponentMaps grass() {
//        return grass;
//    }
//
//    public ElementComponentMaps electric() {
//        return electric;
//    }
//
//    public ElementComponentMaps psychic() {
//        return psychic;
//    }
//
//    public ElementComponentMaps ice() {
//        return ice;
//    }
//
//    public ElementComponentMaps dragon() {
//        return dragon;
//    }
//
//    public ElementComponentMaps dark() {
//        return dark;
//    }
//
//    public ElementComponentMaps fairy() {
//        return fairy;
//    }
}
