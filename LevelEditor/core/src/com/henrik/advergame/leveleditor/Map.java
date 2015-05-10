package com.henrik.advergame.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.henrik.advergame.leveleditor.StateManagers.EntityStates;
import com.henrik.advergame.leveleditor.StateManagers.TileStates;
import com.henrik.advergame.leveleditor.shared.*;
import com.henrik.advergame.leveleditor.shared.entities.*;

import java.lang.Exception;
import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Henri on 24/02/2015.
 */
public class Map {

    private static final String END_OF_GRID_DELIM = "EOF";
    private static final String END_OF_ENTITY_DELIM ="EOE";

    public static final int CELL_WIDTH = 64;
    public static final int CELL_HEIGHT = 64;

    private String name;
    private int width;
    private int height;

    private TileState[][] cells;
    private HashMap<Point,MapEntity> entities;

    private Map() {
        entities = new  HashMap<Point,MapEntity>();
    }

    private MapEntity selectedEntity;

    public Map(String name, int width, int height) {
        this();

        selectedEntity = null;

        cells = new TileState[width][height];

        this.name = name;
        this.width = width;
        this.height = height;

        for(int x  = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                cells[x][y] = TileState.WALL_ONE;
            }
        }
    }

    public void setCell(int x, int y, TileState state) {
        Point pos = new Point(x,y);
        if(!tileValid(pos)) {
            Gdx.app.error("com.henrik.advergame.leveleditor.Map Error:", "The map location " + String.valueOf(pos.x) + "," + String.valueOf(pos.y) + " is invalid.");
        } else {
            cells[x][y] = state;

            if(entities.containsKey(new Point(x,y))) {
                cells[x][y] = TileState.FLOOR;
            }
        }
    }

    public void addEntity(MapEntity entity) {
        Point pos = entity.getLocation();
        if (!tileValid(pos)) {
            Gdx.app.error("com.henrik.advergame.leveleditor.Map Error:", "The map location " + String.valueOf(pos.x) + "," + String.valueOf(pos.y) + " is invalid.");
        } else {
            if(entities.get(entity.getLocation()) == null) {
                entities.put(entity.getLocation(), entity);
            }

        }
    }

    public MapEntity entityAt(Point location) {
        return entities.get(location);
    }

    public void update() {

    }

    public void render(SpriteBatch batch, OrthographicCamera camera, BitmapFont font, TileStates tileStateManager, EntityStates entityStateManager) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(int x = 0; x < cells.length; x++) {
            for(int y = 0; y < cells[x].length; y++) {
                tileStateManager.renderID(batch, cells[x][y].getID(), x * CELL_WIDTH, y * CELL_HEIGHT);
            }
        }

        Iterator iter = entities.entrySet().iterator();
        while(iter.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry)iter.next();
            MapEntity entity = (MapEntity)entry.getValue();

            if(entity == selectedEntity)
                batch.setColor(Color.BLUE);
            else
                batch.setColor(Color.WHITE);

            batch.draw(entityStateManager.getStateIDTexture(entity.getId()), entity.getLocation().x * CELL_WIDTH, entity.getLocation().y * CELL_HEIGHT);

            // Render any entity specific information
            if(entity instanceof CannonEntity) {
                CannonEntity cannon = (CannonEntity)entity;
                float rotation = 0;

                if(cannon.getDirection().x < 0) {
                    rotation = 90f;
                } else if (cannon.getDirection().x > 0) {
                    rotation = -90f;
                } else if (cannon.getDirection().y < 0) {
                    rotation = 180f;
                }

                batch.draw(new TextureRegion(EntityStates.CannonDirectionTexture), entity.getLocation().x * CELL_WIDTH, entity.getLocation().y * CELL_HEIGHT,
                        (float)EntityStates.CannonDirectionTexture.getHeight()/2, (float)EntityStates.CannonDirectionTexture.getHeight()/2,
                        (float)EntityStates.CannonDirectionTexture.getWidth(), (float)EntityStates.CannonDirectionTexture.getHeight(), 1f, 1f, rotation);

            } else if(entity instanceof SwitchEntity) {
                SwitchEntity switchEntity = (SwitchEntity) entity;
                for(Point door : switchEntity.getDoorLocations()) {
                    batch.draw(EntityStates.SwitchDoorTexture, door.x * CELL_WIDTH, door.y * CELL_HEIGHT);
                }
            } else if(entity instanceof TeleporterEntity) {
                TeleporterEntity teleporter = (TeleporterEntity) entity;
                batch.draw(EntityStates.TeleporterExitTexture, teleporter.getExitLocation().x * CELL_WIDTH, teleporter.getExitLocation().y * CELL_HEIGHT);
            } else if(entity instanceof PushBlockEntity) {
                PushBlockEntity pushBlock = (PushBlockEntity) entity;
                for(int x = 0; x < pushBlock.getWidth(); x++) {
                    for(int y = 0; y < pushBlock.getHeight(); y++) {
                        batch.draw(EntityStates.PushBlockTexture, (pushBlock.getLocation().x + x) * CELL_WIDTH, (pushBlock.getLocation().y + y) * CELL_HEIGHT);
                    }
                }
            }
        }

        // Render any selected entity information
        if(selectedEntity != null) {
            if(selectedEntity instanceof AngelRoamEntity) {
                AngelRoamEntity angel = (AngelRoamEntity)selectedEntity;
                font.setScale(2.5f, 2.5f);
                font.setColor(Color.RED);
                for(int i = 0; i < angel.getWayPoints().size(); i++) {
                    BitmapFont.TextBounds bounds = font.getBounds(String.valueOf(i));
                    font.draw(batch, String.valueOf(i), (angel.getWayPoints().get(i).x * CELL_WIDTH) + ((CELL_WIDTH/2) - (bounds.width/2)),
                            (angel.getWayPoints().get(i).y * CELL_HEIGHT)+((CELL_HEIGHT/2)+bounds.height/2));
                }
                font.setScale(1f, 1f);
                font.setColor(Color.BLUE);
            } else if(selectedEntity instanceof AngelEntity) {
                AngelEntity angel = (AngelEntity)selectedEntity;
                font.setScale(2.5f, 2.5f);
                font.setColor(Color.RED);
                for(int i = 0; i < angel.getWaypoints().size(); i++) {
                    BitmapFont.TextBounds bounds = font.getBounds(String.valueOf(i));
                    font.draw(batch, String.valueOf(i), (angel.getWaypoints().get(i).getLocation().x * CELL_WIDTH) + ((CELL_WIDTH/2) - (bounds.width/2)),
                            (angel.getWaypoints().get(i).getLocation().y * CELL_HEIGHT)+((CELL_HEIGHT/2)+bounds.height/2));
                }
                font.setScale(1f, 1f);
                font.setColor(Color.BLUE);
            } else if(selectedEntity instanceof MessageEntity) {
                MessageEntity messageEntity = (MessageEntity) selectedEntity;
                if(!messageEntity.isSelfFocus()) {
                    batch.draw(EntityStates.MessageFocusPointTexture, messageEntity.getFocusPoint().x * CELL_WIDTH, messageEntity.getFocusPoint().y * CELL_HEIGHT);
                }
            }
        }

        batch.setColor(Color.WHITE);
        batch.end();
    }

    public void save() {
        FileHandle file = Gdx.files.local(name + ".txt");
            if(file.exists()) {
            file.delete(); // clear existing file
        }

        try {
            for (int y = 0; y < height; y++) {
                String row = "";
                for (int x = 0; x < width; x++) {
                    row += String.valueOf(cells[x][y].getID());
                }
                file.writeString(row + "\n", true);
            }

            // Write entity data
            file.writeString(END_OF_GRID_DELIM + "\n", true);

            Iterator iter = entities.entrySet().iterator();
            while(iter.hasNext()) {
                HashMap.Entry entry = (HashMap.Entry)iter.next();
                MapEntity entity = (MapEntity)entry.getValue();
                file.writeString(entity.build() + "\n", true);
            }

            file.writeString(END_OF_ENTITY_DELIM, true);

        } catch (Exception e) {
            Gdx.app.debug("I/O Error:", "Could not save map " + name + ".");
        }
    }

    public static Map Load(String name, TileStates tileStateManager) {
        Map map = new Map();

        FileHandle file = Gdx.files.local(name + ".txt");
        if(file.exists()) {

            String data = file.readString();
            String[] lines = data.split(END_OF_GRID_DELIM);
            String[] rows = lines[0].split("\n");

            map.name = name;
            map.cells = new TileState[rows[0].length()][rows.length];
            map.width = map.cells.length;
            map.height = map.cells[0].length;

            for(int y = 0; y < map.height; y++) {
                for(int x = 0; x < map.width; x++) {
                    map.cells[x][y] = (TileState)tileStateManager.getState(Integer.parseInt(String.valueOf(rows[y].charAt(x))));
                }
            }

            String[] entityData = lines[1].split("\n");
            for(int i = 0; i < entityData.length; i++) {
                if(!entityData[i].equals("")) {
                    if (entityData[i].equals(END_OF_ENTITY_DELIM)) {
                        break;
                    } else {
                        int val = Integer.parseInt(entityData[i].split(MapEntity.ID_DELIM)[0]);
                        MapEntity entity;

                        if (val == MapEntityState.ANGEL_ROAM.getID()) {
                            entity = AngelRoamEntity.Parse(entityData[i]);
                        } else if (val == MapEntityState.ANGEL.getID()) {
                            entity = AngelEntity.Parse(entityData[i]);
                        } else if(val == MapEntityState.DOOR.getID()) {
                            entity = DoorEntity.Parse(entityData[i]);
                        } else if(val == MapEntityState.KEY.getID()) {
                            entity = KeyEntity.Parse(entityData[i]);
                        } else if(val == MapEntityState.SWITCH.getID()) {
                            entity = SwitchEntity.Parse(entityData[i]);
                        } else if(val == MapEntityState.DESTRUCTABLE_BLOCK.getID()) {
                            entity = DestructableBlockEntity.Parse(entityData[i]);
                        } else if(val == MapEntityState.TELEPORTER_ENTRANCE.getID()) {
                            entity = TeleporterEntity.Parse(entityData[i]);
                        } else if(val == MapEntityState.CANNON.getID()) {
                            entity = CannonEntity.Parse(entityData[i]);
                        } else if(val == MapEntityState.OBJECT.getID()) {
                            entity = ObjectEntity.Parse(entityData[i]);
                        } else if(val == MapEntityState.MESSAGE.getID()) {
                            entity = MessageEntity.Parse(entityData[i]);
                        }
                        else {
                            entity = PushBlockEntity.Parse(entityData[i]);
                        }

                        map.entities.put(entity.getLocation(), entity);
                    }
                }
            }

            return map;

        } else {
            Gdx.app.debug("I/O Error:", "Could not load map " + name + ".");
            return map;
        }
    }

    public void setSelectedEntity(Point p)  {
        selectedEntity = entityAt(p);
    }

    public MapEntity getSelectedEntity() {
        return selectedEntity;
    }

    public void deleteSelectedEntity() {
        if(selectedEntity != null) {
            entities.remove(selectedEntity.getLocation());
            clearSelectedEntity();
        }
    }

    public void clearSelectedEntity() {
        selectedEntity = null;
    }

    /**
     * Returns whether or not the given tile position is a valid one.
     * @return
     */
    public boolean tileValid(Point pos) {
        if (pos.x < 0 || pos.y < 0 || pos.x > cells.length-1 || pos.y > cells[0].length-1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Converts an unprojected mouse postion to a tile position. Returns null for invalid tile positions.
     * @param pos
     * @return
     */
    public Point mousePosToTile(Vector2 pos) {
        Vector2 tileLoc = new Vector2(pos);
        tileLoc.scl(1f / Map.CELL_WIDTH);

        Point p = new Point((int)tileLoc.x, (int)tileLoc.y);
        return p;
    }
}