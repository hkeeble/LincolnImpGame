package com.henrik.advergame.level.shared.entities;

import com.henrik.advergame.level.shared.MapEntityState;
import com.henrik.advergame.utils.Point;

/**
 * Created by Henri on 05/03/2015.
 */
public class DestructableBlockEntity extends MapEntity {

    public DestructableBlockEntity(Point location) {
        super(location, MapEntityState.DESTRUCTABLE_BLOCK.getID());
    }

    @Override
    public String build() {
       return super.build(MapEntityState.DESTRUCTABLE_BLOCK.getID());
    }

    protected DestructableBlockEntity(String data) {
        super(data);
    }

    public static DestructableBlockEntity Parse(String data) {
        return new DestructableBlockEntity(data);
    }
}
