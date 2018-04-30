package com.etherblood.rules.stats.types;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.Event;
import com.etherblood.events.EventQueue;
import com.etherblood.rules.context.ElementComponentMaps;
import com.etherblood.rules.context.TypeComponentMaps;
import com.etherblood.rules.stats.PokemonTypes;
import com.etherblood.rules.stats.Util;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class RefreshAllTypeStatsHandler<T extends Event> implements Consumer<T> {

    private final Logger log;
    private final EventQueue events;
    private final TypeComponentMaps types;
    private final SimpleComponentMap buffOn;

    public RefreshAllTypeStatsHandler(Logger log, EventQueue events, TypeComponentMaps types, SimpleComponentMap buffOnKey) {
        this.log = log;
        this.events = events;
        this.types = types;
        this.buffOn = buffOnKey;
    }

    @Override
    public void accept(T event) {
        log.info("refreshing all type stats...");
        for (PokemonTypes type : PokemonTypes.values()) {
            ElementComponentMaps map = types.get(type);
            Util.refreshAllForStat(map.power, buffOn);
            Util.refreshAllForStat(map.toughness, buffOn);
        }
        log.info("type stats refreshed.");
    }

}
