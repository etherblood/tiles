package com.etherblood.jme3.gui.util;

import com.etherblood.core.to.PlayerConfig;
import java.util.Iterator;
import java.util.Map;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class Utils {

    public static OptionalInt playerIndex(PlayerConfig[] players, UUID playerId) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getId().equals(playerId)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public static <K, V> void updateEntries(Map<K, V> source, Map<K, V> destination) {
        updateEntries(source, destination, x -> x);
    }

    public static <K, S, D> void updateEntries(Map<K, S> source, Map<K, D> destination, Function<S, D> onAdd) {
        updateEntries(source, destination, onAdd, x -> {
        });
    }

    public static <K, S, D> void updateEntries(Map<K, S> source, Map<K, D> destination, Function<S, D> onAdd, Consumer<D> onRemove) {
        Iterator<Map.Entry<K, D>> iterator = destination.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, D> entry = iterator.next();
            if (!source.containsKey(entry.getKey())) {
                onRemove.accept(entry.getValue());
                iterator.remove();
            }
        }
        for (Map.Entry<K, S> entry : source.entrySet()) {
            destination.computeIfAbsent(entry.getKey(), x -> onAdd.apply(entry.getValue()));
        }
    }

}
