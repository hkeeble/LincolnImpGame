package com.henrik.advergame.level.shared.entities;

import com.henrik.advergame.level.shared.MapEntityState;
import com.henrik.advergame.utils.Point;

/**
 * Created by Henri on 27/02/2015.
 */
public class DoorEntity extends MapEntity {

    public DoorEntity(Point location) {
        super(location, MapEntityState.DOOR.getID());
    }

    protected DoorEntity(String data) {
        super(data);
    }

    public static DoorEntity Parse(String data) {
        return new DoorEntity(data);
    }

    @Override
    public String build() {
        String str = super.build(MapEntityState.DOOR.getID());
        return str;
    }
}
