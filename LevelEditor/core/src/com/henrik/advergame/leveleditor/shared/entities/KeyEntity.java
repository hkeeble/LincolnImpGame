package com.henrik.advergame.leveleditor.shared.entities;

import com.henrik.advergame.leveleditor.Point;
import com.henrik.advergame.leveleditor.shared.MapEntityState;

/**
 * Created by Henri on 27/02/2015.
 */
public class KeyEntity extends MapEntity {

    public KeyEntity(Point location) {
        super(location, MapEntityState.KEY.getID());
    }

    protected KeyEntity(String data) {
        super(data);
    }

    public static KeyEntity Parse(String data) {
        return new KeyEntity(data);
    }

    @Override
    public String build() {
        String str = super.build(MapEntityState.KEY.getID());
        return str;
    }
}
