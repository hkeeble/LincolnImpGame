package com.henrik.advergame.leveleditor;

import com.badlogic.gdx.Gdx;

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
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.x;
        hash = 71 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if (!(obj instanceof Point)) return false;

        Point other = (Point)obj;
        return other.x == x && other.y == y;
    }
}