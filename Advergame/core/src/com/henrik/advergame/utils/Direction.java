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

    public Direction() {
        current = UP;
    }

    public int getCurrent() { return current; }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void update(Vector3 velocity) {
        if(isMoving(velocity)) {
            Vector3 direction = new Vector3(velocity);
            Vector3 normDir = new Vector3();
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
