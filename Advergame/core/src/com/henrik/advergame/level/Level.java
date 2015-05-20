package com.henrik.advergame.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Pool;
import com.henrik.advergame.Game;
import com.henrik.advergame.ModelFactory;
import com.henrik.advergame.entities.LevelEntity;
import com.henrik.advergame.level.shared.*;
import com.henrik.advergame.level.shared.entities.*;
import com.henrik.advergame.systems.MessageSequence;
import com.henrik.advergame.systems.TriggeredMessageSequence;
import com.henrik.advergame.systems.mapentities.*;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.advergame.systems.DestructableObject;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.utils.Point;
import com.henrik.advergame.entities.DecalEntity;
import com.henrik.advergame.pathfinding.NavigationMesh;
import com.henrik.gdxFramework.core.InputHandler;
import com.henrik.gdxFramework.core.Renderer;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.ModelGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;
import com.henrik.advergame.entities.ModelObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class used to represent an individual level. Contains all functionality to generate new levels.
 */
public class Level {

    /**
     * A segment represents an individual wall model, with x, and y location and height and width. Used for rendering optimization.
     */
    private class Segment {
        TileState wallType;
        private int width, height;
        private int x, y;

        public Segment(int x, int y, int width, int height, TileState wallType) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.wallType = wallType;
        }

        public TileState getWallType() {
            return wallType;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    /**
     * Represents an individual chunk. A chunk contains a number of {@link Level.Segment}s. Chunks are used for frustum culling.
     */
    private class Chunk {
        int x, y;
        TileState[][] grid;
        boolean[][] used;
        ArrayList<Segment> segments;

        public Chunk(int x, int y, TileState[][] grid) {
            this.x = x;
            this.y = y;
            this.grid = grid;
            this.used = new boolean[grid.length][grid[0].length];
            this.segments = new ArrayList<Segment>();

            calculateSegments();
        }

        private void calculateSegments() {
            // Calculate all segments that make up this chunk
            for(int x = 0; x < CHUNK_SIZE; x++) {
                for (int y = 0; y < CHUNK_SIZE; y++) {
                    if ((grid[x][y] == TileState.WALL_ONE || grid[x][y] == TileState.WALL_TWO ||
                            grid[x][y] == TileState.WALL_THREE || grid[x][y] == TileState.WALL_FOUR) && !used[x][y]) {

                        // Get the wall type of the first cell
                        TileState wallType = grid[x][y];

                        // Calculate segment height
                        int segHeight = calculateSegHeight(x, y, wallType);

                        // Calculate segment width
                        int segWidth = calculateSegWidth(x+1, y, segHeight, wallType);

                        // Add the new segment
                        segments.add(new Segment(x, y, segWidth, segHeight, wallType));
                        updateUsed(segments.get(segments.size()-1));
                        y += segHeight;
                    }
                }
            }

            // Fill in unused spaces
            for(int x = 0; x < CHUNK_SIZE; x++) {
                for (int y = 0; y < CHUNK_SIZE; y++) {
                    if(!used[x][y] && (grid[x][y] == TileState.WALL_ONE || grid[x][y] == TileState.WALL_TWO ||
                            grid[x][y] == TileState.WALL_THREE || grid[x][y] == TileState.WALL_FOUR)) {
                        segments.add(new Segment(x, y, 1, 1, grid[x][y]));
                    }
                }
            }
        }

        private void updateUsed(Segment segment) {
            for(int x = segment.getX(); x <= segment.getX() + (segment.getWidth()-1); x++) {
                for(int y = segment.getY(); y <= segment.getY() + (segment.getHeight()-1); y++) {
                    used[x][y] = true;
                }
            }
        }

        private int calculateSegHeight(int x, int y, TileState wallType) {
            int height = 1;
            while (y + 1 < CHUNK_SIZE && grid[x][y+1] == wallType && !used[x][y+1]) {
                y++;
                height++;
            }
            return height;
        }

        private int calculateSegWidth(int x, int y, int segHeight, TileState wallType) {
            int width = 1;
            while(x + 1 < CHUNK_SIZE && grid[x][y] == wallType && calculateSegHeight(x, y, wallType) == segHeight) {
                x++;
                width++;
            }

            return width;
        }

        public ArrayList<Segment> getSegments() {
            return segments;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    // Size of an individual chunk.
    private int CHUNK_SIZE = 5;

    // Textures and model builders
    private ModelBuilder builder;

    // Wall models and chunked entities
    private ArrayList<Model> models;
    private ArrayList<ModelObject> modelInstances;
    private ArrayList<btBoxShape> wallBoxes;

    // Grid data
    private TileState[][] grid;
    private int cellSize;
    private int width, height, chunkWidth, chunkHeight, chunkCount;

    // Asset manager reference
    private AssetManager assetManager;

    // Object data
    private DecalEntity exitEntity;
    private Texture stairsTexture;

    // Textures
    private Texture wall1Texture, wall2Texture, wall3Texture, wall4Texture;
    private Texture crackedWallTexture;
    private Texture floorTexture;
    private Texture pushBlockTexture;
    private Texture blackTexture;
    private Texture doorTexture;
    private Texture doorLockTexture;

    // Models (used by entities)
    private Model acrossDoorModelLock;
    private Model downDoorModelLock;
    private Model acrossDoorModel;
    private Model downDoorModel;
    private Model destructableBlockModel;

    // Push block models
    private HashMap<Point, Model> pushBlockModels;

    // Level navigation mesh
    private NavigationMesh navMesh;

    // Entities
    private ArrayList<LevelEntity> staticEntities;
    private ArrayList<LevelEntity> dynamicEntities;
    private ArrayList<MessageSequence> messageSequences;

    // The object count for this level
    private int objectCount;

    // The map for this level
    private Map map;

    // Font used by messages
    private BitmapFont messageFont;

    // Sounds
    private Sound destroy, doorOpen;

    /**
     * Creates a new blank level.
     * @param cellSize The cell size to use.
     * @param assetManager The game's asset manager.
     */
    private Level(int cellSize, AssetManager assetManager, BitmapFont messageFont) {

        models = new ArrayList<Model>();
        modelInstances = new ArrayList<ModelObject>();
        wallBoxes = new ArrayList<btBoxShape>();
        builder = new ModelBuilder();

        pushBlockModels = new HashMap<Point, Model>();

        staticEntities = new ArrayList<LevelEntity>();
        dynamicEntities = new ArrayList<LevelEntity>();
        messageSequences = new ArrayList<MessageSequence>();

        this.assetManager = assetManager;
        this.cellSize = cellSize;

        this.stairsTexture = assetManager.get("sprites/steps.png", Texture.class);

        // Get textures
        this.floorTexture =  assetManager.get("textures/floorBase.png", Texture.class);
        this.wall1Texture = assetManager.get("textures/wall1Texture.png", Texture.class);
        this.wall2Texture = assetManager.get("textures/wall2Texture.png", Texture.class);
        this.wall3Texture = assetManager.get("textures/wall3Texture.png", Texture.class);
        this.wall4Texture = assetManager.get("textures/wall4Texture.png", Texture.class);
        this.doorTexture = assetManager.get("textures/doorTexture.png", Texture.class);
        this.pushBlockTexture = assetManager.get("textures/pushBlockTexture.png", Texture.class);
        this.doorTexture = assetManager.get("textures/doorTexture.png", Texture.class);
        this.doorLockTexture = assetManager.get("textures/doorLockTexture.png", Texture.class);
        this.blackTexture = assetManager.get("textures/black.png", Texture.class);
        this.crackedWallTexture = assetManager.get("textures/crackedWallTexture.png", Texture.class);

        this.floorTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.pushBlockTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.doorTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.wall1Texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.wall2Texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.wall3Texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.wall4Texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // Get sounds
        doorOpen = assetManager.get("sounds/hit2.wav", Sound.class);
        destroy = assetManager.get("sounds/destroy.wav", Sound.class);

        this.messageFont = messageFont;
    }

    /**
     * Creates a new level and loads one from a file.
     * @param name The map to loadGame.
     */
    public Level(String name, int cellSize, AssetManager assetManager, BitmapFont messageFont, World world) {
        this(cellSize, assetManager, messageFont);

        this.cellSize = cellSize;

        Load(world, name);
    }

    /**
     * Disposes of the existing level, and loads a new one into this object.
     * @param name
     */
    public void Load(World world, String name) {

        dispose();

        map = Map.Parse(name);

        objectCount = 0;

        // Convert the tile state grid for the level
        this.grid = new TileState[map.getWidth()][map.getHeight()];
        TileState[][] mapGrid = map.getGrid();
        for(int x = 0; x < map.getWidth(); x++) {
            for(int y = 0; y < map.getHeight(); y++) {
                if(mapGrid[x][y] == TileState.FLOOR) {
                    grid[x][y] = TileState.FLOOR;
                } else if(mapGrid[x][y] == TileState.WALL_ONE) {
                    grid[x][y] = TileState.WALL_ONE;
                } else if(mapGrid[x][y] == TileState.WALL_TWO) {
                    grid[x][y] = TileState.WALL_TWO;
                } else if(mapGrid[x][y] == TileState.WALL_THREE) {
                    grid[x][y] = TileState.WALL_THREE;
                } else if(mapGrid[x][y] == TileState.WALL_FOUR) {
                    grid[x][y] = TileState.WALL_FOUR;
                }
            }
        }

        this.width = map.getWidth();
        this.height = map.getHeight();
        this.chunkHeight = map.getHeight()/CHUNK_SIZE;
        this.chunkWidth = map.getWidth()/CHUNK_SIZE;
        this.chunkCount = chunkHeight + chunkWidth;

        build(world.getRenderer());
    }

    /**
     * Gets the number of objects in the given level.
     */
    public static int GetObjectCount(String name) {
        ArrayList<MapEntity> entities = Map.Parse(name).getEntities();
        int objCount = 0;
        for(MapEntity entity : entities) {
            if(entity instanceof ObjectEntity) {
                objCount++;
            }
        }
        return objCount;
    }

    private void build(Renderer renderer) {
        buildWalls(); // Build wall models
        buildBoundaryWalls(); // Build boundaries
        buildEntities(renderer);

        // Create the exit entity
        exitEntity = new DecalEntity(new DecalGraphicsComponent(2, 4, new TextureRegion(stairsTexture), false),
                new PhysicsComponent(new btBoxShape(new Vector3(0.8f, 1.5f, 0.4f)), CollisionTags.EXIT));
        exitEntity.setPosition(map.getExitLocation().x * cellSize, 0.5f, map.getExitLocation().y * cellSize);
        exitEntity.getGraphicsComponent().setXRotation(-35);

        // Change tile state to exclude the exit from navmesh
        grid[map.getExitLocation().x][map.getExitLocation().y] = TileState.EXIT;

        navMesh = new NavigationMesh(this, cellSize); // Create navigation mesh
    }

    private void buildEntityModels() {
        // Down Door
        downDoorModel = ModelFactory.BuildDoorDown(builder, doorTexture, blackTexture, cellSize, new Vector3(0, 0, 0), new Vector3(cellSize, cellSize, 0.5f));
        acrossDoorModel = ModelFactory.BuildDoorAcross(builder, doorTexture, blackTexture, cellSize, new Vector3(0,0,0), new Vector3(0.5f, cellSize, cellSize));
        downDoorModelLock = ModelFactory.BuildDoorDown(builder, doorLockTexture, blackTexture, cellSize, new Vector3(0,0,0), new Vector3(cellSize, cellSize, 0.5f));
        acrossDoorModelLock = ModelFactory.BuildDoorAcross(builder, doorLockTexture, blackTexture, cellSize, new Vector3(0,0,0), new Vector3(0.5f, cellSize, cellSize));

        // Destructable block
        builder.begin();;
        MeshPartBuilder mpb = builder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(crackedWallTexture)));
        createBox(mpb, new Vector3(), new Vector3(cellSize, cellSize, cellSize));
        destructableBlockModel = builder.end();
    }

    private void buildEntities(Renderer renderer) {

        // Build entity models of appropriate sizes
       buildEntityModels();

        for(MapEntity entity : map.getEntities()) {

            // Get initial position
            Vector3 initialPos = new Vector3(entity.getLocation().x * cellSize, 0, entity.getLocation().y * cellSize);

            if(entity.getId() == MapEntityState.ANGEL_ROAM.getID()) {
                AngelRoamEntity angel = (AngelRoamEntity)entity;

                // Scale all waypoints
                ArrayList<Point> wayPoints = new ArrayList<Point>();
                for(Point p : angel.getWayPoints()) {
                    wayPoints.add(new Point(p.x * cellSize, p.y * cellSize));
                }

                dynamicEntities.add(new AngelDog(assetManager, initialPos, wayPoints, renderer));
            } else if(entity.getId() == MapEntityState.DOOR.getID()) {
                if(grid[entity.getLocation().x + 1][entity.getLocation().y] != TileState.FLOOR && grid[entity.getLocation().x - 1][entity.getLocation().y] != TileState.FLOOR) {
                    staticEntities.add(new Door(initialPos, downDoorModelLock, true));
                } else {
                    staticEntities.add(new Door(initialPos, acrossDoorModelLock, true));
                }

            } else if(entity.getId() == MapEntityState.KEY.getID()) {
                dynamicEntities.add(new Key(initialPos, assetManager));
            } else if(entity.getId() == MapEntityState.SWITCH.getID()) {
                SwitchEntity switchEntity = (SwitchEntity)entity;

                // Create doors
                ArrayList<Point> doorLocations = switchEntity.getDoorLocations();
                ArrayList<Door> doors = new ArrayList<Door>();
                for(Point p : doorLocations) {
                    if(grid[p.x + 1][p.y] != TileState.FLOOR && grid[p.x - 1][p.y] != TileState.FLOOR) {
                        doors.add(new Door(new Vector3(p.x * cellSize, 0, p.y * cellSize), downDoorModel, false));
                    } else {
                        doors.add(new Door(new Vector3(p.x * cellSize, 0, p.y * cellSize), acrossDoorModel, false));
                    }
                }
                staticEntities.addAll(doors);

                // Create switch
                staticEntities.add(new Switch(initialPos, doors, assetManager));
            } else if(entity.getId() == MapEntityState.PUSH_BLOCK.getID()) {
                PushBlockEntity pushBlock = (PushBlockEntity) entity;

                // Convert initial position to world position, not editor position
                Vector2 worldPos = pushBlock.EditorToWorld(pushBlock.getLocation().x, pushBlock.getLocation().y);
                initialPos.set(new Vector3(worldPos.x * cellSize, 0, worldPos.y * cellSize));

                dynamicEntities.add(new PushBlock(initialPos, getPushBlockModel(new Point(pushBlock.getWidth(), pushBlock.getHeight())),
                        new Vector3(pushBlock.getWidth() * cellSize, cellSize, pushBlock.getHeight() * cellSize)));

            } else if(entity.getId() == MapEntityState.TELEPORTER_ENTRANCE.getID()) {

                TeleporterEntity teleporter = (TeleporterEntity) entity;
                Teleporter entrance = new Teleporter(initialPos, assetManager);
                Teleporter exit = new Teleporter(new Vector3(teleporter.getExitLocation().x * cellSize, 0, teleporter.getExitLocation().y * cellSize), assetManager);

                entrance.setPartner(exit);
                exit.setPartner(entrance);

                staticEntities.add(entrance);
                staticEntities.add(exit);
            } else if(entity.getId() == MapEntityState.CANNON.getID()) {
                CannonEntity cannon = (CannonEntity) entity;
                staticEntities.add(new Cannon(initialPos, cannon.getDirection(), assetManager));
            } else if(entity.getId() == MapEntityState.ANGEL.getID()) {
                AngelEntity angel = (AngelEntity) entity;
                dynamicEntities.add(new Angel(assetManager, initialPos, angel.getWaypoints()));
            } else if(entity.getId() == MapEntityState.OBJECT.getID()) {
                dynamicEntities.add(new DestructableObject(assetManager, initialPos));
                objectCount++;
            } else if(entity.getId() == MapEntityState.DESTRUCTABLE_BLOCK.getID()) {
                staticEntities.add(new DestructableBlock(initialPos, destructableBlockModel, assetManager));
            } else if(entity.getId() == MapEntityState.MESSAGE.getID()) { // Construct a triggered message sequence here
                MessageEntity messageEntity = (MessageEntity) entity;

                String[] msgs = new String[messageEntity.getMessages().size()];
                for(int i = 0; i < msgs.length; i++) {
                    msgs[i] = messageEntity.getMessages().get(i);
                }

                messageSequences.add(new TriggeredMessageSequence(initialPos, cellSize, msgs, messageFont, Color.BLACK,
                        Game.getUISkin().getRegion("speechBubble"), new Vector3(messageEntity.getFocusPoint().x * cellSize, 2f, messageEntity.getFocusPoint().y * cellSize), renderer));
            }
        }
    }

    /**
     * Gets a push block model of the given size, creating one if it does not already exist.
     */
    private Model getPushBlockModel(Point size) {
        if(pushBlockModels.containsKey(size)) {
            return pushBlockModels.get(size);
        } else {
            builder.begin();
            MeshPartBuilder mpb = builder.part("block", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                    new Material(TextureAttribute.createDiffuse(pushBlockTexture)));
            createWallSegment(mpb, new Vector3(0, 0, 0), new Vector3(cellSize * size.x, cellSize, cellSize * size.y));
            pushBlockModels.put(size, builder.end());
        }

        return pushBlockModels.get(size);
    }

    // TODO Optimize this!
    private void buildBoundaryWalls() {
        int bWallHeight = height+2;
        Vector3 verticalWallDims =  new Vector3(cellSize, cellSize, bWallHeight*cellSize);
        Vector3 horizontalWallDims =  new Vector3(width*cellSize, cellSize, cellSize);

        Vector3 leftWallPosition = new Vector3(-(float)cellSize, 0, (float)(bWallHeight*cellSize)/2.0f - cellSize - (cellSize/2.0f));
        Vector3 rightWallPosition = new Vector3((float)(width*cellSize), 0, (float)(bWallHeight*cellSize)/2.0f - cellSize - (cellSize/2.0f));
        Vector3 bottomWallPosition = new Vector3((float)(width*cellSize)/2.0f - (cellSize/2.0f), 0, (float)(height*cellSize));
        Vector3 topWallPosition = new Vector3((float)(width*cellSize)/2.0f - (cellSize/2.0f), 0, -cellSize);

        // Create model
        builder.begin();
        MeshPartBuilder mpb = builder.part("top", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(wall1Texture)));
        createWallSegment(mpb, leftWallPosition, verticalWallDims);
        createWallSegment(mpb, rightWallPosition, verticalWallDims);
        createWallSegment(mpb, topWallPosition, horizontalWallDims);
        createWallSegment(mpb, bottomWallPosition, horizontalWallDims);
        Model m = builder.end();
        models.add(m);

        // Create collision shape
        btCompoundShape compoundShape = new btCompoundShape();
        verticalWallDims.scl(0.5f);
        horizontalWallDims.scl(0.5f);
        compoundShape.addChildShape(new Matrix4().trn(leftWallPosition), new btBoxShape(verticalWallDims));
        compoundShape.addChildShape(new Matrix4().trn(rightWallPosition), new btBoxShape(verticalWallDims));
        compoundShape.addChildShape(new Matrix4().trn(bottomWallPosition), new btBoxShape(horizontalWallDims));
        compoundShape.addChildShape(new Matrix4().trn(topWallPosition), new btBoxShape(horizontalWallDims));

        modelInstances.add(new ModelObject(new ModelGraphicsComponent(new ModelInstance(m))));
    }

    // TODO Optimize this!
    private void createWallSegment(MeshPartBuilder mpb, Vector3 center, Vector3 dimensions) {
        // TOP
        mpb.setUVRange(0f, 0f, dimensions.z, dimensions.x);
        mpb.rect(new Vector3(center.x - (dimensions.x / 2f), cellSize / 2f, center.z - (dimensions.z / 2f)), // lower left
                new Vector3(center.x - (dimensions.x / 2f), cellSize / 2f, center.z + (dimensions.z / 2f)), // upper left
                new Vector3(center.x + (dimensions.x / 2f), cellSize / 2f, center.z + (dimensions.z / 2f)), // upper right
                new Vector3(center.x + (dimensions.x / 2f), cellSize / 2f, center.z - (dimensions.z / 2f)), // lower right
                new Vector3(0, 1, 0));

        // LEFT
        mpb.setUVRange(0f, 0f, dimensions.x, dimensions.z);
        mpb.rect(new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper right
                new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower right
                new Vector3(-1,0,0));

        // RIGHT
        mpb.setUVRange(0f, 0f, dimensions.x, dimensions.z);
        mpb.rect(new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper right
                new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower right
                new Vector3(1,0,0));

        // FRONT
        mpb.setUVRange(0f, 0f, dimensions.y, dimensions.x);
        mpb.rect(new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper right
                new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower right
                new Vector3(0,0,-1));

        // BACK
        mpb.setUVRange(0f, 0f, dimensions.y, dimensions.x);
        mpb.rect(new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper right
                new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower right
                new Vector3(0,0,-1));
    }

    private void createBox(MeshPartBuilder mpb, Vector3 center, Vector3 dimensions) {
        createDoorMain(mpb, center, dimensions);
        createDoorSides(mpb, center, dimensions);
    }

    // TODO Optimize this!
    private void createDoorMain(MeshPartBuilder mpb, Vector3 center, Vector3 dimensions) {
        // FRONT
        mpb.rect(new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper right
                new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower right
                new Vector3(0,0,-1));

        // BACK
        mpb.rect(new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper right
                new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower right
                new Vector3(0,0,-1));
    }

    // TODO Optimize this!
    private void createDoorSides(MeshPartBuilder mpb, Vector3 center, Vector3 dimensions) {
        // TOP
        mpb.setUVRange(0, 0, 1, 1);
        mpb.rect(new Vector3(center.x - (dimensions.x / 2f), cellSize / 2f, center.z - (dimensions.z / 2f)), // lower left
                new Vector3(center.x - (dimensions.x / 2f), cellSize / 2f, center.z + (dimensions.z / 2f)), // upper left
                new Vector3(center.x + (dimensions.x / 2f), cellSize / 2f, center.z + (dimensions.z / 2f)), // upper right
                new Vector3(center.x + (dimensions.x / 2f), cellSize / 2f, center.z - (dimensions.z / 2f)), // lower right
                new Vector3(0, 1, 0));

        // LEFT
        mpb.rect(new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper left
                new Vector3(center.x - (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper right
                new Vector3(center.x - (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower right
                new Vector3(-1,0,0));

        // RIGHT
        mpb.rect(new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z - (dimensions.z/2f)), // lower left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z - (dimensions.z/2f)), // upper left
                new Vector3(center.x + (dimensions.x/2f), cellSize/2f, center.z + (dimensions.z/2f)), // upper right
                new Vector3(center.x + (dimensions.x/2f), -cellSize/2f, center.z + (dimensions.z/2f)), // lower right
                new Vector3(1,0,0));

    }

    // TODO Optimize this!
    private void buildWalls() {
        Gdx.app.log("Level", "Building " + String.valueOf(chunkWidth) + "x" + String.valueOf(chunkHeight) + " chunk level.");

        // Dispose any existing wall models and chunks
        disposeWalls();

        // Create the chunk models and collision shapes
        for(int chunkX = 0; chunkX < chunkWidth; chunkX++) {
            for(int chunkY = 0; chunkY < chunkHeight; chunkY++) {

                // Get chunk layout
                TileState[][] chunkGrid = new TileState[CHUNK_SIZE][CHUNK_SIZE];
                for(int x = 0; x < CHUNK_SIZE; x++) {
                    for(int y = 0; y < CHUNK_SIZE; y++) {
                        chunkGrid[x][y] = grid[calculateGlobalPos(x, chunkX)][calculateGlobalPos(y, chunkY)];
                    }
                }

                // Build the chunk
                Chunk chunk = new Chunk(chunkX, chunkY, chunkGrid);
                buildChunk(chunk);

                Gdx.app.log("Chunk", "Chunk " + String.valueOf(chunkX) + "," + String.valueOf(chunkY) + " has " + String.valueOf(chunk.getSegments().size()) + " segments.");
            }
        }
    }

    // TODO Optimize this!
    private void buildChunk(Chunk chunk) {

        ArrayList<Segment> segments = chunk.getSegments();

        Gdx.app.log("height", String.valueOf(wall1Texture.getHeight()));

        // Construct the segments
        builder.begin();
       // btCompoundShape collisionShape = new btCompoundShape(); // Create a new compound shape for this chunk

        // Determine first texture
        TileState currentWallType = segments.get(0).getWallType();
        Texture currentTexture = getWallTexture(currentWallType);

        MeshPartBuilder mpb = builder.part("walls", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(currentTexture)));

        // Ignore walls for chunks with no wall segments
        if(segments.size() > 0) {
            for (Segment seg : segments) {

                // Calculate the correct position for this segment
                Vector3 center = new Vector3();
                Vector3 dimensions = new Vector3((float) seg.getWidth() * (float) cellSize, cellSize, (float) seg.getHeight() * (float) cellSize);
                center.x = (dimensions.x / 2.0f) + (seg.getX() * cellSize) - ((float) cellSize / 2.0f);
                center.y = 0;
                center.z = (dimensions.z / 2.0f) + (seg.getY() * cellSize) - ((float) cellSize / 2.0f);

                // Add wall boxes to a seperate array, so we can manage memory instead of leaving them to the garbage collector
                //wallBoxes.add(new btBoxShape(new Vector3(((float) cellSize / 2) * seg.getWidth(),
                        //(float) cellSize / 2, ((float) cellSize / 2) * seg.getHeight())));

                //collisionShape.addChildShape(new Matrix4().trn(center), wallBoxes.get(wallBoxes.size()-1));

                // If we have a different wall type, we need a new mesh part and a new texture bind
                if(seg.getWallType() != currentWallType) {
                    currentWallType = seg.getWallType();
                    currentTexture = getWallTexture(currentWallType);
                    mpb = builder.part("walls", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                            new Material(TextureAttribute.createDiffuse(currentTexture)));
                }

                // Create wall
                createWallSegment(mpb, center, dimensions);
            }
        }

        // Create the floor for this segment
        mpb = builder.part("floor", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(floorTexture)));
        // mpb.setUVRange(0f, 0f, (cellSize/3) * CHUNK_SIZE, (cellSize) * CHUNK_SIZE);
        mpb.setVertexTransform(new Matrix4().trn(-(float)cellSize/2.0f, 0, -(float)cellSize/2.0f));
        mpb.rect(new Vector3(0, -(float) cellSize / 2.0f, 0), // lower left // Backwards winding order here?
                new Vector3(0, -(float) cellSize / 2.0f, (cellSize * CHUNK_SIZE)), // upper left
                new Vector3(cellSize * CHUNK_SIZE, -(float) cellSize / 2.0f, (cellSize * CHUNK_SIZE)), // upper right
                new Vector3(cellSize * CHUNK_SIZE, -(float) cellSize / 2.0f, 0), // lower right
                new Vector3(0, 1, 0)); // normal

        // Add model for memory management, and add new entity to wall chunk collection
        models.add(builder.end());
        modelInstances.add(new ModelObject(new ModelGraphicsComponent(new ModelInstance(models.get(models.size() - 1)))));
        modelInstances.get(modelInstances.size()-1).setPosition(new Vector3((chunk.getX() * CHUNK_SIZE) * cellSize, 0, (chunk.getY() * CHUNK_SIZE) * cellSize)); // Transform entire chunk entity to correct location
    }

    /**
     * Get the wall texture for the given tile state.
     * @param state
     * @return
     */
    private Texture getWallTexture(TileState state) {
        if(state == TileState.WALL_ONE) {
            return wall1Texture;
        } else if(state == TileState.WALL_TWO) {
            return wall2Texture;
        } else if(state == TileState.WALL_THREE) {
            return wall3Texture;
        } else if(state == TileState.WALL_FOUR) {
            return wall4Texture;
        } else {
            return wall1Texture;
        }
    }

    private int calculateGlobalPos(int localPos, int chunkPos) {
        return localPos + (chunkPos*CHUNK_SIZE);
    }

    // TODO Optimize this!
    private Vector2 directionToPosition(Vector2 start, Vector2 dest) {
        Vector2 moveDir = new Vector2();
        int manDist = manhattanDistance(start, dest);

        if(manhattanDistance(new Vector2(start.x + 1, start.y), dest) < manDist) {
            moveDir.set(1, 0);
        }
        else if(manhattanDistance(new Vector2(start.x - 1, start.y), dest) < manDist) {
            moveDir.set(-1, 0);
        }
        else if(manhattanDistance(new Vector2(start.x, start.y + 1), dest) < manDist) {
            moveDir.set(0, 1);
        }
        else if(manhattanDistance(new Vector2(start.x, start.y - 1), dest) < manDist) {
            moveDir.set(0, -1);
        }

        return moveDir;
    }

    public void update(GameWorld world, ShapeRenderer renderer, Vector3 playerLocation, InputHandler input) {

        // Update navigation data
        navMesh.update(world.getMainCamera(), renderer, playerLocation);

        for (int i = 0; i < modelInstances.size(); i++) {
            modelInstances.get(i).update(world);
        }

        for(int i = 0; i < staticEntities.size(); i++) {
            staticEntities.get(i).update(world);
        }

        for(int i = 0; i < dynamicEntities.size(); i++) {
            dynamicEntities.get(i).update(world);
        }

        for(int i = 0; i < messageSequences.size(); i++) {
        	messageSequences.get(i).update(input, world);
        }
    
        checkForRemovals(world);

        exitEntity.update(world);
    }

    private void checkForRemovals(GameWorld world) {
        for(int i = 0; i < dynamicEntities.size(); i++) {
            if(dynamicEntities.get(i).isMarkedForRemoval()) {
                if(dynamicEntities.get(i) instanceof DestructableObject) {
                    destroy.play();
                }

                world.unregisterCollisionObject(dynamicEntities.get(i).getPhysicsComponent());
                dynamicEntities.get(i).dispose();
                dynamicEntities.remove(i);
            }
        }

        for(int i = 0; i < staticEntities.size(); i++) {
            if(staticEntities.get(i).isMarkedForRemoval()) {
                // Check sound effects here
                if(staticEntities.get(i) instanceof Door) {
                    doorOpen.play();
                }

                world.unregisterCollisionObject(staticEntities.get(i).getPhysicsComponent());
                staticEntities.get(i).dispose();
                staticEntities.remove(i);
            }
        }
    }

    public void render(GameWorld world) {

        if(!world.isCollisionDebugActive() && !navMesh.isDebug()) {
            for (int i = 0; i < modelInstances.size(); i++) {
                modelInstances.get(i).render(world);
            }
        }

        for(int i = 0; i < messageSequences.size(); i++) {
            messageSequences.get(i).render(world);
        }

        for(int i = 0; i < staticEntities.size(); i++) {
            staticEntities.get(i).render(world);
        }

        for(int i = 0; i < dynamicEntities.size(); i++) {
            dynamicEntities.get(i).render(world);
        }

        exitEntity.render(world);
    }

    private int manhattanDistance(Vector2 a, Vector2 b) {
        return Math.abs((int)a.x - (int)b.x) + Math.abs((int)a.y - (int)b.y);
    }

    /**
     * Checks if the given location is valid in this grid (that is, it is within grid bounds)
     * @param location The location to check for validity.
     */
    private boolean isValidLocation(Point location) {
        return location.x > 0 && location.y > 0 && location.x < width && location.y < height;
    }

    public void dispose() {
        disposeWalls();

        if(downDoorModel != null)
            downDoorModel.dispose();

        if(acrossDoorModel != null)
            acrossDoorModel.dispose();

        if(downDoorModelLock != null)
            downDoorModelLock.dispose();

        if(acrossDoorModelLock != null)
            acrossDoorModelLock.dispose();

        // Dispose all entities
        for(LevelEntity entity : staticEntities) {
            entity.dispose();
        }

        for(LevelEntity entity : dynamicEntities) {
            entity.dispose();
        }

        for(MessageSequence t : messageSequences) {
            t.dispose();
        }

        if(staticEntities != null)
            staticEntities.clear();

        if(dynamicEntities != null)
            dynamicEntities.clear();

        if(messageSequences != null)
            messageSequences.clear();

        // Dispose all push block models
        Iterator iter = pushBlockModels.entrySet().iterator();
        while(iter.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry) iter.next();
            Model model = (Model)entry.getValue();
            model.dispose();
        }

        if(pushBlockModels != null)
            pushBlockModels.clear();
    }

    public void disposeWalls() {
        for(ModelObject model : modelInstances) {
            model.dispose();
        }
        for(Model model : models) {
            model.dispose();
        }

        for(btBoxShape box : wallBoxes) {
            box.dispose();
        }

        if(wallBoxes != null)
            wallBoxes.clear();

        if(modelInstances != null)
            modelInstances.clear();

        if(models != null)
            models.clear();
    }

    public DecalEntity getExitEntity() { return exitEntity; }

    /**
     * Get the number of objects in this level.
     */
    public int getObjectCount() {
        return objectCount;
    }

    /**
     * Get the location, in cell coordinates, of the player start for this level.
     */
    public Point getPlayerStart() {
        return map.getPlayerStart();
    }

    /**
     * Get the width, in number of cells, of the level.
     */
    public int getWidth() {
        return map.getWidth();
    }

    /**
     * Get the height, in number of cells, of the level.
     */
    public int getHeight() {
        return map.getHeight();
    }

    /**
     * Get the size of an individual cell in the level.
     */
    public int getCellSize() { return cellSize; }

    /**
     * Get the state of the tile at location x,y in the level.
     */
    public TileState getTileState(int x, int y) {
        return grid[x][y];
    }

    /**
     * Get the navigation mesh for this level.
     */
    public NavigationMesh getNavMesh() {
        return navMesh;
    }

    /**
     * Get all model entities within this level.
     */
    public ArrayList<ModelObject> getModelEntities() {
        return modelInstances;
    }

    /**
     * Get all the static entities in this level.
     */
    public ArrayList<LevelEntity> getStaticEntities() {
        return staticEntities;
    }

    /**
     * Get all the dynamic entities in this level.
     */
    public ArrayList<LevelEntity> getDynamicEntities() {
        return dynamicEntities;
    }

    /**
     * Get all the triggered message sequences in this level.
     */
    public ArrayList<MessageSequence> getMessageSequences() { return messageSequences; }

    /**
     * Toggle navigation mesh debug render for this level.
     */
    public void toggleNavDebug() {
        navMesh.toggleDebug();
    }
}