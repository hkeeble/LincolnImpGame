package com.henrik.advergame.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Henri on 06/02/2015.
 */
public class VectorMath {

    private static Vector3 temp = new Vector3();
    private static Vector3 temp2 = new Vector3();
    private static Vector3 temp3 = new Vector3();

    /**
     * Gets the direction from point A to point B as a normalised vector.
     * @param a The first point.
     * @param b The point to get the direction to.
     * @return
     */
    public static void directionTo(Vector3 a, Vector3 b, Vector3 out) {
        temp.set(b);
        temp2.set(a);
        temp3.set(temp.sub(temp2));
        temp3.nor();
        out.set(temp3);
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
