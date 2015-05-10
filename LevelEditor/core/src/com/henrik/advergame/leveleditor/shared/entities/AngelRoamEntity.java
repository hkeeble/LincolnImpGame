package com.henrik.advergame.leveleditor.shared.entities;

import com.henrik.advergame.leveleditor.Point;
import com.henrik.advergame.leveleditor.shared.MapEntityState;

import java.util.ArrayList;

public class AngelRoamEntity extends MapEntity {

    public static final String WAYPOINT_DELIM = "-";

    private ArrayList<Point> wayPoints;

    public AngelRoamEntity() {
        super(new Point(0,0), MapEntityState.ANGEL_ROAM.getID());
        wayPoints = new ArrayList<Point>();
    }

    public AngelRoamEntity(Point location) {
        super(location, MapEntityState.ANGEL_ROAM.getID());
        wayPoints = new ArrayList<Point>();
    }

    public void addWayPoint(Point p) {
        wayPoints.add(p);
    }

    public ArrayList<Point> getWayPoints() {
        return wayPoints;
    }

    public void removeWayPoint(Point p) {
        wayPoints.remove(p);
    }

    public void reverseDirection() {
        ArrayList<Point> inverted = new ArrayList<Point>();
        for(int i = wayPoints.size()-1; i > -1; i--) {
            inverted.add(wayPoints.get(i));
        }
        wayPoints = inverted;
    }

    @Override
    public String build() {
        String str = super.build(MapEntityState.ANGEL_ROAM.getID());
        for(Point p : wayPoints) {
            str += String.valueOf(p.x) + "," + String.valueOf(p.y) + "-";
        }
        if(wayPoints.size() > 0) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    protected AngelRoamEntity(String data) {
        super(data);
        wayPoints = new ArrayList<Point>();

        String[] split = data.split(":");

        // Add waypoints if they exist
        if(split.length > 1) {
            String properties = split[1];
            String[] wayPoints = properties.split(WAYPOINT_DELIM);
            for (String point : wayPoints) {
                String[] coord = point.split(POINT_DELIM);
                addWayPoint(new Point(Integer.parseInt(coord[0]), Integer.parseInt(coord[1])));
            }
        }
    }

    public static AngelRoamEntity Parse(String data) {
        return new AngelRoamEntity(data);
    }

}
