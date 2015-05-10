package com.henrik.advergame.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Henri on 06/02/2015.
 */
public class VectorMath {

    /**
     * Gets the direction from point A to point B as a normalised vector.
     * @param a The first point.
     * @param b The point to get the direction to.
     * @return
     */
    public static Vector3 directionTo(Vector3 a, Vector3 b) {
        Vector3 target = new Vector3(b);
        Vector3 position = new Vector3(a);
        Vector3 direction = new Vector3(target.sub(position));
        direction.nor();
        return direction;
    }

    /**
     * Gets the direction from point A to point B as a normalised vector.
     * @param a The first point.
     * @param b The point to get the direction to.
     * @return
     */
    public static Vector2 directionTo(Vector2 a, Vector2 b) {
        Vector2 target = new Vector2(b);
        Vector2 position = new Vector2(a);
        Vector2 direction = new Vector2(target.sub(position));
        direction.nor();
        return direction;
    }

    /**
     * Whether or not the given velocity is a stationary one.
     * @param velocity The velocity to test.
     * @return
     */
    public static boolean isMoving(Vector3 velocity) {
        return (velocity.x + velocity.y + velocity.z != 0);
    }

}
