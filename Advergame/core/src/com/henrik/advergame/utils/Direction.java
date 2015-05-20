package com.henrik.advergame.utils;

import com.badlogic.gdx.math.Vector3;

/**
 * A class for use in uniformly determining a fake horizontal/vertical direction within 3D space, based upon a given velocity.
 */
public class Direction {

    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT= 2;
    public static final int UP = 3;
    public static final int COUNT = 4;

    private int current;

    Vector3 direction;
    Vector3 normDir;

    public Direction() {
        current = UP;

        direction = new Vector3();
        normDir = new Vector3();
    }

    public int getCurrent() { return current; }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void update(Vector3 velocity) {
        if(isMoving(velocity)) {
            direction.set(velocity);
            normDir.set(direction);
            normDir.nor();

            normDir.x = Math.abs(direction.x);
            normDir.z = Math.abs(direction.z);

            if (normDir.x > normDir.z) { // If X magnitude is higher, favour x direction for animation
                if (direction.x < 0)
                    current = LEFT;
                else
                    current = RIGHT;
            } else { // Otherwise favour Y
                if (direction.z < 0)
                    current = UP;
                else
                    current = DOWN;
            }
        }
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Direction) {
            if(object == this)
                return true;

            if(((Direction) object).getCurrent() == this.getCurrent())
                return true;
        }

        return false;
    }

    private boolean isMoving(Vector3 velocity) {
        return (velocity.x + velocity.y + velocity.z != 0);
    }

}
