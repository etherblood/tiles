package com.etherblood.rules;

import com.etherblood.events.Event;
import com.etherblood.events.EventDefinition;
import com.etherblood.rules.events.EntityValueEvent;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class EventDefGenerator {

    public Map<String, EventDefinition> generate() {
        Map result = new HashMap<>();
        stat("Health", result);
        stat("ActionPoints", result);
        stat("MovePoints", result);
        elementStat("Toughness", result);
        elementStat("Power", result);
        set("ActivePlayer", result);
        set("ActiveTeam", result);
        event("SetPosition", result);
        event("WalkAction", result);
        event("PassTurnAction", result);
        event("RazorleafAction", result);
        event("GameStart", result);
        event("TurnStart", result);
        event("TurnEnd", result);
        event("GameOver", result);
        element("Damage", result);
        return result;
    }

    private void element(String elementName, Map out) {
        event("Fire" + elementName, out);
        event("Water" + elementName, out);
        event("Air" + elementName, out);
        event("Earth" + elementName, out);
    }

    private void elementStat(String elementName, Map out) {
        stat("Fire" + elementName, out);
        stat("Water" + elementName, out);
        stat("Air" + elementName, out);
        stat("Earth" + elementName, out);
    }

    private void stat(String statName, Map out) {
        set("Active" + statName, out);
        set("Base" + statName, out);
        set("Buffed" + statName, out);
        set("Additive" + statName, out);
        event("UpdateBuffed" + statName, out);
        event("ResetActive" + statName, out);
    }

    private void set(String valueName, Map<String, EventDefinition<EntityValueEvent>> out) {
        event("Set" + valueName, out);
    }

    private <T extends Event> void event(String name, Map<String, EventDefinition<T>> out) {
        EventDefinition<T> eventDefinition = new EventDefinition<>(out.size(), name, Object::toString);
        out.put(name, eventDefinition);
        LoggerFactory.getLogger(EventDefGenerator.class).debug("registered {}", eventDefinition);
    }
}
