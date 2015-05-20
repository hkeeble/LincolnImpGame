package com.henrik.advergame.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.henrik.advergame.level.shared.*;
import com.henrik.advergame.level.shared.entities.*;
import com.henrik.advergame.utils.Point;

import java.util.ArrayList;

/**
 * A class used to store and parse map files. Data is then used to build levels from maps.
 */
public class Map {

    private static final String END_OF_GRID_DELIM = "EOF";
    private static final String END_OF_ENTITY_DELIM = "EOE";

    private int width, height;
    private TileState[][] grid;
    private Point exitLocation;
    private ArrayList<Point> objectLocations;
    private ArrayList<Point> keyLocations;
    private ArrayList<Point> doorLocations;
    private ArrayList<MapEntity> entities;
    private Point playerStart;

    public Map() {
        objectLocations = new ArrayList<Point>();
        doorLocations = new ArrayList<Point>();
        entities = new ArrayList<MapEntity>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TileState[][] getGrid() {
        return grid;
    }

    public Point getExitLocation() {
        return exitLocation;
    }

    public ArrayList<Point> getObjectLocations() {
        return objectLocations;
    }

    public ArrayList<Point> getDoorLocations() { return doorLocations; }

    public ArrayList<Point> getKeyLocations() {
        return keyLocations;
    }

    public Point getPlayerStart() {
        return playerStart;
    }

    public ArrayList<MapEntity> getEntities() {
        return entities;
    }

    private Map(FileHandle file) {
        this();

        String data = file.readString();
        String[] lines = data.split(END_OF_GRID_DELIM);
        String[] rows = lines[0].split("\r\n");

        this.grid = new TileState[rows[0].length()][rows.length];
        this.width = this.grid.length;
        this.height = this.grid[0].length;

        // Parse the grid
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {

                int val = Integer.parseInt(String.valueOf(rows[y].charAt(x)));

                if(val == TileState.FLOOR.getID()) {
                    this.grid[x][y] = TileState.FLOOR;
                }
                else if(val == TileState.WALL_ONE.getID()) {
                    this.grid[x][y] = TileState.WALL_ONE;
                }
                else if(val == TileState.WALL_TWO.getID()) {
                    this.grid[x][y] = TileState.WALL_TWO;
                }
                else if(val == TileState.WALL_THREE.getID()) {
                    this.grid[x][y] = TileState.WALL_THREE;
                }
                else if(val == TileState.WALL_FOUR.getID()) {
                    this.grid[x][y] = TileState.WALL_FOUR;
                }
                else if(val == TileState.START.getID()) {
                    this.playerStart = new Point(x,y);
                }
                else if(val == TileState.EXIT.getID()) {
                    this.exitLocation = new Point(x, y);
                }
            }
        }

        // Parse entities
        String[] entityData = lines[1].split("\r\n");
        for (int i = 0; i < entityData.length; i++) {
            if (!entityData[i].equals("")) {
                if (entityData[i].equals(END_OF_ENTITY_DELIM)) {
                    break;
                } else {

                    // Get entity ID
                    int val = Integer.parseInt(entityData[i].split(MapEntity.ID_DELIM)[0]);

                    // Parse dependent on ID
                    if (val == MapEntityState.ANGEL_ROAM.getID()) {
                        this.entities.add(AngelRoamEntity.Parse(entityData[i]));
                    } else if (val == MapEntityState.ANGEL.getID()) {
                        this.entities.add(AngelEntity.Parse(entityData[i]));
                    } else if(val == MapEntityState.DOOR.getID()) {
                        this.entities.add(DoorEntity.Parse(entityData[i]));
                    } else if(val == MapEntityState.KEY.getID()) {
                        this.entities.add(KeyEntity.Parse(entityData[i]));
                    } else if(val == MapEntityState.SWITCH.getID()) {
                        this.entities.add(SwitchEntity.Parse(entityData[i]));
                    } else if(val == MapEntityState.DESTRUCTABLE_BLOCK.getID()) {
                        this.entities.add(DestructableBlockEntity.Parse(entityData[i]));
                    } else if(val == MapEntityState.TELEPORTER_ENTRANCE.getID()) {
                        this.entities.add(TeleporterEntity.Parse(entityData[i]));
                    } else if(val == MapEntityState.CANNON.getID()) {
                        this.entities.add(CannonEntity.Parse(entityData[i]));
                    } else if(val == MapEntityState.OBJECT.getID()) {
                        this.entities.add(ObjectEntity.Parse(entityData[i]));
                    } else if(val == MapEntityState.MESSAGE.getID()) {
                        this.entities.add(MessageEntity.Parse(entityData[i]));
                    } else {
                        this.entities.add(PushBlockEntity.Parse(entityData[i]));
                    }
                }
            }
        }
    }

    /**
     * Parse a map file.
     *
     * @param name The name of the map to parse.
     * @return
     */
    public static Map Parse(String name) {
        FileHandle file = Gdx.files.internal("levels/" + name + ".txt");

        if (file.exists()) {
            return new Map(file);
        } else {
            Gdx.app.log("I/O Error:", "Error loading map file: " + file.path());
            return null;
        }
    }
}

