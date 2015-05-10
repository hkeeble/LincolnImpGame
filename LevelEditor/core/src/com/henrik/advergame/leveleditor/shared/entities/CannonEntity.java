package com.henrik.advergame.leveleditor.shared.entities;

import com.badlogic.gdx.math.Vector2;
import com.henrik.advergame.leveleditor.Point;
import com.henrik.advergame.leveleditor.shared.MapEntityState;

import java.util.ArrayList;

/**
 * Created by Henri on 05/03/2015.
 */
public class CannonEntity extends MapEntity {

    private Point direction;

    public CannonEntity(Point location) {
        super(location, MapEntityState.CANNON.getID());
        setDirection(new Point(0,-1));
    }

    public void setDirection(Point direction) {
        this.direction = direction;
    }

    public Point getDirection() {
        return direction;
    }

    @Override
    public String build() {
        String str = super.build(MapEntityState.CANNON.getID());

        str += String.valueOf(direction.x) + POINT_DELIM + String.valueOf(direction.y);

        return str;
    }

    protected CannonEntity(String data) {
        super(data);

        String[] split = data.split(":");
        String[] dirStr = split[1].split(POINT_DELIM);

        direction = new Point(Integer.parseInt(dirStr[0]), Integer.parseInt(dirStr[1]));
    }

    public static CannonEntity Parse(String data) {
        return new CannonEntity(data);
    }
}
