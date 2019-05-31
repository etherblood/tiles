package com.etherblood.core.util;

/**
 *
 * @author Philipp
 */
public class Coordinates {

    public static int of(int x, int y) {
        assert (short) x == x;
        assert (short) y == y;
        return of((short) x, (short) y);
    }

    public static int sum(int a, int b) {
        return of(x(a) + x(b), y(a) + y(b));
    }

    public static int of(short x, short y) {
        return Short.toUnsignedInt(x) | (y << 16);
    }

    public static short x(int position) {
        return (short) position;
    }

    public static short y(int position) {
        return (short) (position >>> 16);
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
