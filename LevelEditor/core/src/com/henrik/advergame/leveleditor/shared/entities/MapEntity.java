package com.henrik.advergame.leveleditor.shared.entities;

import com.henrik.advergame.leveleditor.Point;

/**
 * Created by Henri on 24/02/2015.
 */
public abstract class MapEntity {

    public static final String ID_DELIM = "#";
    public static final String POINT_DELIM = ",";
    public static final String POS_DELIM = ":";

    protected Point location;

    protected int id;

    public int getId() {
        return id;
    }

    public Point getLocation() {
        return location;
    }

    public MapEntity() { }

    public MapEntity(Point location, int id) {
        this.location = location;
        this.id = id;
    }

    public abstract String build();

    protected MapEntity(String data) {
        String[] exclID = data.split(ID_DELIM);
        String[] splitProperties = exclID[1].split(POS_DELIM);
        String[] position = splitProperties[0].split(POINT_DELIM);
        id = Integer.parseInt(exclID[0]);
        location = new Point(Integer.parseInt(position[0]),Integer.parseInt(position[1]));
    }

    protected String build(int id) {
        String str = "";
        str += String.valueOf(id) + "#";
        str += String.valueOf(location.x) + "," + String.valueOf(location.y);
        str += ":";
        return str;
    }
}
