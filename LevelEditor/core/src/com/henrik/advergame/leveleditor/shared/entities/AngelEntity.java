package com.henrik.advergame.leveleditor.shared.entities;

import com.badlogic.gdx.utils.Array;
import com.henrik.advergame.leveleditor.Point;
import com.henrik.advergame.leveleditor.shared.MapEntityState;

import java.util.ArrayList;

/**
 * Created by Henri on 05/03/2015.
 */
public class AngelEntity extends MapEntity {

    private static final String WAYPOINT_DELIM = "~";
    private static final String WAYPOINT_INNER_DELIM = "%";
    private static final String INSTRUCTION_DELIM = "z";
    private static final String INSTRUCTION_INNER_DELIM = "@";

    public static class Instruction {
        private Point direction;
        private int waitTimeSeconds;

        public Instruction(Point direction, int timeInSeconds) {
            this.direction = direction;
            this.waitTimeSeconds = timeInSeconds;
        }

        public int getWaitTimeSeconds() {
            return waitTimeSeconds;
        }

        public Point getDirection() {
            return direction;
        }

        public String build() {
            return String.valueOf(direction.x) + POINT_DELIM + String.valueOf(direction.y) + INSTRUCTION_INNER_DELIM + String.valueOf(waitTimeSeconds);
        }

        public Instruction(String data) {
            String[] split = data.split(INSTRUCTION_INNER_DELIM);
            String[] dir = split[0].split(POINT_DELIM);
            this.direction = new Point(Integer.parseInt(dir[0]), Integer.parseInt(dir[1]));
            this.waitTimeSeconds = Integer.parseInt(split[1]);
        }
    }

    public static class Waypoint {

        private Point location;
        private ArrayList<Instruction> instructions;

        public Waypoint(Point location) {
            this.location = location;
            this.instructions = new ArrayList<Instruction>();
        }

        public ArrayList<Instruction> getInstructions() {
            return instructions;
        }

        public void setInstructions(ArrayList<Instruction> instructions) {
            this.instructions = instructions;
        }

        public String build() {
            String str = "";
            str += String.valueOf(location.x) + POINT_DELIM + String.valueOf(location.y) + WAYPOINT_INNER_DELIM;

            for(int i = 0; i < instructions.size(); i++) {
                str += instructions.get(i).build();
                if (!(i == instructions.size() - 1)) {
                    str += INSTRUCTION_DELIM;
                }
            }

            return str;
        }

        public Waypoint(String data) {
            String[] split = data.split(WAYPOINT_INNER_DELIM);
            String[] loc = split[0].split(POINT_DELIM);
            location = new Point(Integer.parseInt(loc[0]), Integer.parseInt(loc[1]));

            instructions = new ArrayList<Instruction>();
            String[] inst = split[1].split(INSTRUCTION_DELIM);
            for(String instruction : inst) {
                instructions.add(new Instruction(instruction));
            }
        }

        public Point getLocation() {
            return location;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof  Waypoint))
                return false;
            else if(obj == this)
                return  true;

            Waypoint point = (Waypoint) obj;
            if(point.location.x == location.x && point.location.y == location.y)
                return true;

            return false;
        }
    }

    private ArrayList<Waypoint> waypoints;

    public AngelEntity(Point location) {
        super(location, MapEntityState.ANGEL.getID());
        waypoints = new ArrayList<Waypoint>();
    }

    public void addWaypoint(Point location) {
        waypoints.add(new Waypoint(location));
    }

    public boolean removeWaypoint(Waypoint waypoint) {
        return waypoints.remove(waypoint);
    }

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }

    public Waypoint getWaypoint(Point location) {
        for(Waypoint waypoint : waypoints) {
            if(waypoint.location.equals(location)) {
                return waypoint;
            }
        }

        return null;
    }

    @Override
    public String build() {
        String str = super.build(MapEntityState.ANGEL.getID());

        str += WAYPOINT_DELIM;

        for(int i = 0; i < waypoints.size(); i++) {
            str += waypoints.get(i).build();
            if(!(i == waypoints.size()-1)) {
                str += WAYPOINT_DELIM;
            }
        }

        return str;
    }

    protected AngelEntity(String data) {
        super(data);

        String[] split = data.split(POS_DELIM);
        String[] waypointSplit = split[1].split(WAYPOINT_DELIM);

        waypoints = new ArrayList<Waypoint>();
        for(String waypoint : waypointSplit) {
            if(!waypoint.equals("")) {
                waypoints.add(new Waypoint(waypoint));
            }
        }
    }

    public static AngelEntity Parse(String data) {
        return new AngelEntity(data);
    }
}
