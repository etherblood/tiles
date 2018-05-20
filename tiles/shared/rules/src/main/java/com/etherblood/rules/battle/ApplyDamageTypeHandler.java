//package com.etherblood.rules.battle;
//
//import com.etherblood.events.EventQueue;
//import com.etherblood.rules.GameEventHandler;
//import com.etherblood.rules.components.ComponentDefinitions;
//import com.etherblood.rules.components.ComponentDefinitions.ElementsComponents;
//import com.etherblood.rules.components.Components;
//import java.util.function.Consumer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * @author Philipp
// */
//public class ApplyDamageTypeHandler extends GameEventHandler<DamageEvent> {
//
//    private static final Logger LOG = LoggerFactory.getLogger(ApplyDamageTypeHandler.class);
//
//    @Override
//    public void handle(DamageEvent event) {
//        LOG.debug("applying types to {}", event);
//        int power = data.component(Components.Stats.Power.get(event.type).active).getOrElse(event.attacker, 0);
//        int toughness = components.STATS.toughness.get(event.type).active.getOrElse(event.defender, 0);
//        float effectiveDamage = event.damage * (100 + power) / 100f;
//        float multiplier;
//        if (toughness >= 0) {
//            multiplier = 100f / (100f + toughness);
//        } else {
//            multiplier = 2f - (100f / (100f - toughness));
//        }
//
//        event.damage = (int) (effectiveDamage * multiplier);
//        LOG.debug("adjusted event is {}", event);
//    }
//
//}
