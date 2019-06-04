package com.etherblood.mods.core.game.components.skill;

import com.etherblood.core.components.BoolComponent;
import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.IntComponent;
import com.etherblood.mods.core.game.components.ComponentsBase;

public class PositionTargetingComponents extends ComponentsBase {

    public final IntComponent manhattanRange;
    public final BoolComponent requiresSight;
    public final BoolComponent requiresLine;
    public final BoolComponent required;

    public PositionTargetingComponents(String name, ComponentRegistry registry) {
        manhattanRange = newIntComponent(name + "ManhattanRange", registry);
        requiresSight = newBoolComponent(name + "RequiresSight", registry);
        requiresLine = newBoolComponent(name + "RequiresLine", registry);
        required = newBoolComponent(name + "Required", registry);
    }
}
