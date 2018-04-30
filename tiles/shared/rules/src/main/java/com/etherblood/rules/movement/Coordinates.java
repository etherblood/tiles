package com.etherblood.rules.movement;

/**
 *
 * @author Philipp
 */
public class Coordinates {

    public static int of(int x, int y) {
        assert (x & 0xffff) == x;
        assert (y & 0xffff) == y;
        return x | (y << 16);
    }

    public static int x(int position) {
        return position & 0xffff;
    }

    public static int y(int position) {
        return position >>> 16;
    }

    public static int manhattenDistance(int posA, int posB) {
        int x = x(posA) - x(posB);
        int y = y(posA) - y(posB);
        return Math.abs(x) + Math.abs(y);
    }

    public static int euclideanDistance(int posA, int posB) {
        int x = x(posA) - x(posB);
        int y = y(posA) - y(posB);
        return (int) Math.sqrt(x * x + y * y);
    }

    public static String toString(int coordinates) {
        return "(" + x(coordinates) + "," + y(coordinates) + ")";
    }

    public static boolean inBounds(int position, int size) {
        return inBounds1d(x(position), x(size)) && inBounds1d(y(position), y(size));
    }

    private static boolean inBounds1d(int x, int width) {
        return 0 <= x && x < width;
    }

}
