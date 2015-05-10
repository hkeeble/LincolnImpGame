package com.henrik.advergame.utils;

/**
 * Created by Henri on 10/12/2014.
 */
public class Point {
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if (!(obj instanceof Point)) return false;

        Point other = (Point)obj;
        return other.x == x && other.y == y;
    }
}