package com.henrik.advergame.leveleditor.shared.entities;

import com.henrik.advergame.leveleditor.Point;
import com.henrik.advergame.leveleditor.shared.MapEntityState;

/**
 * Created by Henri on 05/03/2015.
 */
public class ObjectEntity extends MapEntity {

    public ObjectEntity(Point location) {
        super(location, MapEntityState.OBJECT.getID());
    }

    protected ObjectEntity(String data) {
        super(data);
    }

    public static ObjectEntity Parse(String data) {
        return new ObjectEntity(data);
    }

    @Override
    public String build() {
        String str = super.build(MapEntityState.OBJECT.getID());
        return str;
    }

}
