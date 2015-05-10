package com.henrik.advergame.leveleditor.shared;

/**
 * Created by Henri on 27/02/2015.
 */
public enum MapEntityState implements State {
    ANGEL_ROAM(0), ANGEL(1), KEY(2), DOOR(3), SWITCH(4), PUSH_BLOCK(5), CANNON(6), DESTRUCTABLE_BLOCK(7), TELEPORTER_ENTRANCE(8), OBJECT(9), MESSAGE(10), COUNT(11);

    private int id;

    MapEntityState(int id) {
        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }
}