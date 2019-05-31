package com.etherblood.mods.core.game.components.actor;

import com.etherblood.core.components.BoolComponent;
import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.IntComponent;
import com.etherblood.mods.core.game.components.ComponentsBase;

public class ActorComponents extends ComponentsBase {

    public final IntComponent controlledBy;
    public final BoolComponent isStatusOk;
    public final IntComponent memberOf;
    public final BoolComponent active;
    public final IntComponent sprite;
    public final IntComponent position;

    public ActorComponents(String name, ComponentRegistry registry) {
        controlledBy = newIntComponent(name + "ControlledBy", registry);
        isStatusOk = newBoolComponent(name + "IsStatusOk", registry);
        memberOf = newIntComponent(name + "MemberOf", registry);
        active = newBoolComponent(name + "Active", registry);
        sprite = newIntComponent(name + "Sprite", registry);
        position = newIntComponent(name + "Position", registry);
    }
}
