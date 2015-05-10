package com.henrik.advergame.leveleditor.shared;

/**
 * Created by Henri on 24/02/2015.
 */
public enum  TileState implements State {

    FLOOR(0), WALL_ONE(1), WALL_TWO(2), WALL_THREE(3), WALL_FOUR(4), EXIT(5), START(6), COUNT(7);

    private int id;

    TileState(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

}
