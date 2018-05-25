package com.etherblood.sandbox;

import com.etherblood.entities.EntityData;
import com.etherblood.rules.movement.Coordinates;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author Philipp
 */
public class MapPrinter {

    public void printMap(Map<Integer, Character> characterShortcuts, int width, int height, EntityData data, int positionKey, PrintStream out) {
        char[] map = new char[width * height];
        Arrays.fill(map, ' ');
        for (int entity : data.query(positionKey).list()) {
            int pos = data.get(entity, positionKey);
            int x = Coordinates.x(pos);
            int y = Coordinates.y(pos);
            int index = x + width * y;
            map[index] = characterShortcuts.getOrDefault(entity, 'X');
        }
        for (int y = 0; y < height; y++) {
            out.println();
            for (int x = 0; x < width; x++) {
                int index = x + width * y;
                out.print('[');
                out.print(map[index]);
                out.print(']');
            }
        }
    }
}
