package com.etherblood.mods.core.game.components;

import com.etherblood.mods.core.game.components.skill.SkillComponents;
import com.etherblood.core.components.BoolComponent;
import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.GeneralComponent;
import com.etherblood.core.components.IntComponent;
import com.etherblood.mods.core.game.components.actor.ActorComponents;

public class CoreComponents extends ComponentsBase {

    public final GeneralComponent<String> name;
    public final IntComponent mapSize;
    public final IntComponent buffOn;
    public final IntComponent nextTeam;
    public final BoolComponent activeTeam;
    public final SkillComponents skill;
    public final EffectComponents effect;
    public final StatsComponents stats;
    public final ActorComponents actor;

    public CoreComponents(ComponentRegistry registry) {
        name = newGeneralComponent("Name", registry, String.class);
        mapSize = newIntComponent("MapSize", registry);
        buffOn = newIntComponent("BuffOn", registry);
        nextTeam = newIntComponent("NextTeam", registry);
        activeTeam = newBoolComponent("ActiveTeam", registry);
        skill = new SkillComponents("Skill", registry);
        effect = new EffectComponents("Effect", registry);
        stats = new StatsComponents("Stats", registry);
        actor = new ActorComponents("Actor", registry);
    }
}
