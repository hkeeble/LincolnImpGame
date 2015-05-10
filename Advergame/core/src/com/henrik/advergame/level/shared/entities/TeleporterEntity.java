package com.henrik.advergame.level.shared.entities;

import com.henrik.advergame.level.shared.MapEntityState;
import com.henrik.advergame.utils.Point;

/**
 * Created by Henri on 05/03/2015.
 */
public class TeleporterEntity extends MapEntity {

    private Point exitLocation;

    public TeleporterEntity(Point location) {
        super(location, MapEntityState.TELEPORTER_ENTRANCE.getID());
        exitLocation = new Point(0,0);
    }

    public Point getExitLocation() {
        return exitLocation;
    }

    public void setExitLocation(Point location) {
        this.exitLocation = location;
    }

    @Override
    public String build() {
        String str = super.build(MapEntityState.TELEPORTER_ENTRANCE.getID());

        str += String.valueOf(exitLocation.x) + POINT_DELIM + String.valueOf(exitLocation.y);

        return str;
    }

    protected TeleporterEntity(String data) {
        super(data);

        String[] split = data.split(":");
        String[] dirStr = split[1].split(POINT_DELIM);

        exitLocation = new Point(Integer.parseInt(dirStr[0]), Integer.parseInt(dirStr[1]));
    }

    public static TeleporterEntity Parse(String data) {
        return new TeleporterEntity(data);
    }
}
