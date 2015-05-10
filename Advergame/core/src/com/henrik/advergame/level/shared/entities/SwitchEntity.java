package com.henrik.advergame.level.shared.entities;

import com.henrik.advergame.level.shared.MapEntityState;
import com.henrik.advergame.utils.Point;

import java.util.ArrayList;

/**
 * Created by Henri on 05/03/2015.
 */
public class SwitchEntity extends MapEntity {

    public static final String DOOR_LOC_DELIM = "-";

    private ArrayList<Point> doorLocations;

    public SwitchEntity(Point location) {
        super(location, MapEntityState.SWITCH.getID());
        doorLocations = new ArrayList<Point>();
    }

    public void addDoorLocation(Point location) {
        doorLocations.add(location);
    }

    public boolean removeDoorLocation(Point location) {
        return doorLocations.remove(location);
    }

    public ArrayList<Point> getDoorLocations() {
        return doorLocations;
    }

    @Override
    public String build() {
        String str = super.build(MapEntityState.SWITCH.getID());

        for(Point p : doorLocations) {
            str += String.valueOf(p.x) + "," + String.valueOf(p.y) + "-";
        }
        if(doorLocations.size() > 0) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    protected SwitchEntity(String data) {
        super(data);
        doorLocations = new ArrayList<Point>();

        String[] split = data.split(":");

        // Add waypoints if they exist
        if(split.length > 1) {
            String properties = split[1];
            String[] doorLocationsStr = properties.split(DOOR_LOC_DELIM);
            for (String point : doorLocationsStr) {
                String[] coord = point.split(POINT_DELIM);
                addDoorLocation(new Point(Integer.parseInt(coord[0]), Integer.parseInt(coord[1])));
            }
        }
    }

    public static SwitchEntity Parse(String data) {
        return new SwitchEntity(data);
    }
}
