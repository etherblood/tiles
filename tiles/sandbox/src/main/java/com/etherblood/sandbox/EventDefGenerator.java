package com.etherblood.sandbox;

import com.etherblood.events.ArgumentDefinition;
import com.etherblood.events.EventDefinition;
import com.etherblood.events.EventDefinition.EventDefinitionBuilder;
import com.etherblood.rules.movement.Coordinates;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Philipp
 */
public class EventDefGenerator {

    public Map<String, EventDefinition> generate() {
        Map<String, EventDefinition> result = new HashMap<>();
        stat("Health", result);
        stat("ActionPoints", result);
        stat("MovePoints", result);
        elementStat("Toughness", result);
        elementStat("Power", result);
        set("Active", result);
        event("SetPosition", result, entity("Target"), position("Position"));
        event("WalkAction", result, entity("Actor"), position("To"), position("From"));
        event("PassTurnAction", result, entity("Actor"));
        event("RazorleafAction", result, entity("Actor"), simple("Target"));
        event("GameStart", result);
        event("TurnStart", result, entity("Team"));
        event("TurnEnd", result, entity("Team"));
        event("GameOver", result);
        element("Damage", result, entity("Target"), simple("Damage"));
        return result;
    }

    private void element(String elementName, Map<String, EventDefinition> out, ArgumentDefinition... args) {
        event("Fire" + elementName, out, args);
        event("Water" + elementName, out, args);
        event("Air" + elementName, out, args);
        event("Earth" + elementName, out, args);
    }

    private void elementStat(String elementName, Map<String, EventDefinition> out) {
        stat("Fire" + elementName, out);
        stat("Water" + elementName, out);
        stat("Air" + elementName, out);
        stat("Earth" + elementName, out);
    }

    private void stat(String statName, Map<String, EventDefinition> out) {
        set("Active" + statName, out);
        set("Base" + statName, out);
        set("Buffed" + statName, out);
        set("Additive" + statName, out);
        event("UpdateBuffed" + statName, out, entity("Target"));
        event("ResetActive" + statName, out, entity("Target"));
    }

    private void set(String valueName, Map<String, EventDefinition> out) {
        event("Set" + valueName, out, entity("Target"), simple("Value"));
    }

    private void event(String name, Map<String, EventDefinition> out, ArgumentDefinition... args) {
        EventDefinitionBuilder builder = EventDefinition.builder();
        for (ArgumentDefinition arg : args) {
            builder.withArgument(arg);
        }
        EventDefinition event = builder
                .name(name)
                .index(out.size())
                .build();
        out.put(name, event);
    }

    private ArgumentDefinition simple(String name) {
        return new ArgumentDefinition(name, false);
    }

    private ArgumentDefinition position(String name) {
        return new ArgumentDefinition(name, false, x -> "(" + Coordinates.x(x) + ", " + Coordinates.y(x) + ")");
    }

    private ArgumentDefinition entity(String name) {
        return new ArgumentDefinition(name, true);
    }
}
