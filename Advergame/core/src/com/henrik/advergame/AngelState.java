package com.henrik.advergame;

/**
 * Created by Henri on 31/01/2015.
 */
public enum AngelState implements State {
    ATTACK(0),
    RETREAT(1),
    DISPERSE(2),
    ROAM(3);

    private final int id;

    AngelState(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
