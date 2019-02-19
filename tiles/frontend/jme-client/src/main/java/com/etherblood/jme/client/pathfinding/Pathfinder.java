package com.etherblood.jme.client.pathfinding;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntHashSet;
import com.etherblood.collections.IntToIntHashMap;
import com.etherblood.collections.IntToIntMap;
import com.etherblood.rules.util.Coordinates;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Pathfinder {

    private final int width, height;
    private final IntToIntMap indexCost = new IntToIntHashMap();
    private final Queue<IndexPriority> open = new PriorityQueue<>(Comparator.comparing(IndexPriority::getPriority));
    private final IntToIntMap parent = new IntToIntHashMap();

    public Pathfinder(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public IntArrayList findPath(int from, int to, IntHashSet obstacles) {
        from = fromCoordinates(from);
        to = fromCoordinates(to);
        reset();
        indexCost.set(from, 0);
        open.add(new IndexPriority(from, 0));

        while (!open.isEmpty()) {
            IndexPriority current = open.poll();
            int index = current.getIndex();
            int cost = indexCost.get(index);

            if (index == to) {
                break;
            }
            for (int neighbor : neighbors(index)) {
                if (obstacles.hasKey(toCoordinates(neighbor))) {
                    continue;
                }
                int newCost = cost + 1;
                if (newCost < indexCost.getOrElse(neighbor, Integer.MAX_VALUE)) {
                    indexCost.set(neighbor, newCost);
                    open.add(new IndexPriority(neighbor, newCost + manhattenDistance(neighbor, to)));
                    parent.set(neighbor, index);
                }
            }
        }
        IntArrayList path = new IntArrayList();
        if(!parent.hasKey(to)) {
            return path;
        }
        for (int current = to; current != from; current = parent.get(current)) {
            path.insertAt(0, toCoordinates(current));
        }
        return path;
    }

    private IntArrayList neighbors(int index) {
        int x = x(index);
        int y = y(index);
        IntArrayList list = new IntArrayList();
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    if (x > 0) {
                        list.add(index(x - 1, y));
                    }
                    break;
                case 1:
                    if (x + 1 < width) {
                        list.add(index(x + 1, y));
                    }
                    break;
                case 2:
                    if (y > 0) {
                        list.add(index(x, y - 1));
                    }
                    break;
                case 3:
                    if (y + 1 < height) {
                        list.add(index(x, y + 1));
                    }
                    break;
            }
        }
        return list;
    }

    private void reset() {
        open.clear();
        parent.clear();
        indexCost.clear();
    }

    private int fromCoordinates(int coordinates) {
        return index(Coordinates.x(coordinates), Coordinates.y(coordinates));
    }

    private int toCoordinates(int index) {
        return Coordinates.of(x(index), y(index));
    }

    private int index(int x, int y) {
        return x + y * width;
    }

    private int x(int index) {
        return index % width;
    }

    private int y(int index) {
        return index / width;
    }

    public int manhattenDistance(int indexA, int indexB) {
        int x = x(indexA) - x(indexB);
        int y = y(indexA) - y(indexB);
        return Math.abs(x) + Math.abs(y);
    }
}
