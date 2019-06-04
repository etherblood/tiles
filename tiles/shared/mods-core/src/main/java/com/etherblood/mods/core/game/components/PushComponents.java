package com.etherblood.mods.core.game.components;

import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.IntComponent;

public class PushComponents extends ComponentsBase {

    public final IntComponent direction;
    public final IntComponent amount;
    public final IntComponent collisionDamage;

    public PushComponents(String name, ComponentRegistry registry) {
        direction = newIntComponent(name + "Direction", registry);
        amount = newIntComponent(name + "Amount", registry);
        collisionDamage = newIntComponent(name + "CollisionDamage", registry);
    }

}
