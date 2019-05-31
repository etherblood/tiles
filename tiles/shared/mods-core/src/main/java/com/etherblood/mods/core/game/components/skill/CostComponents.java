package com.etherblood.mods.core.game.components.skill;

import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.IntComponent;
import com.etherblood.mods.core.game.components.ComponentsBase;

public class CostComponents extends ComponentsBase {

    public final IntComponent actionPoints;
    public final IntComponent movePoints;
    public final IntComponent health;

    public CostComponents(String name, ComponentRegistry registry) {
        actionPoints = newIntComponent(name + "ActionPoints", registry);
        movePoints = newIntComponent(name + "MovePoints", registry);
        health = newIntComponent(name + "Health", registry);
    }
}
