package com.henrik.advergame.utils;

import com.henrik.gdxFramework.core.CollisionTag;

/**
 * Collision tags applied to physics components to differentiate them when deciding upon a collision response.
 */
public enum CollisionTags implements CollisionTag {
    PLAYER(0),
    STATIC_SOLID(1),
    ANGEL(2),
    GROUND(3),
    OBJECT(4),
    PLAYER_ACTION_BOX(5),
    EXIT(6),
    KEY(7),
    DOOR(8),
    SWITCH_DOOR(9),
    SWITCH(10),
    PUSH_BLOCK(11),
    TELEPORTER(12),
    CANNON_BALL(13),
    CANNON(14),
    ANGEL_SIGHT_BOX(15),
    DESTRUCTABLE_BLOCK(16),
    MESSAGE_TRIGGER(17),
    ANGEL_DOG(18);

    private final int id;

    CollisionTags(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
