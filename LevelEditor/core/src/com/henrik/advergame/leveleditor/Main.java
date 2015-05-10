package com.henrik.advergame.leveleditor;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.henrik.advergame.leveleditor.StateManagers.EntityStates;
import com.henrik.advergame.leveleditor.StateManagers.TileStates;
import com.henrik.advergame.leveleditor.shared.*;
import com.henrik.advergame.leveleditor.shared.entities.*;
import sun.plugin2.message.Message;

import java.util.ArrayList;

public class Main extends ApplicationAdapter {

    public enum PlacementMode {
        TILE,
        ENTITY
    }

    private SpriteBatch batch;
    private Texture tileHighlighter;

    private OrthographicCamera camera;
    private Map currentMap;

    private AssetManager assetManager;

    private InputHandler input;

    private final float CAM_MOVE_SPEED = 7.0f;

    private final Vector3 UP = new Vector3(0, 1, 0);
    private final Vector3 LEFT = new Vector3(-1, 0, 0);
    private final Vector3 RIGHT = new Vector3(1, 0, 0);
    private final Vector3 DOWN = new Vector3(0, -1, 0);

    private final Vector3 ZOOMIN = new Vector3(0, 0, 1);
    private final Vector3 ZOOMOUT = new Vector3(0, 0, -1);

    private PlacementMode placementMode;

    private int currentEntityStateID;
    private MapEntityState currentMapEntityState;

    private int currentTileStateID;
    private TileState currentTileState;

    private HUD hud;
    private Skin uiSkin;
    private InputMultiplexer multiplexer;
    private Dialog dialog;

    private Label modeLabel;
    private Image selectedTileImage;

    private TileStates tileStateManager;
    private EntityStates mapEntityStateManager;


    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    private class CreateMapListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            currentMap = new Map(((NewMapDialog)dialog).getMapName(), ((NewMapDialog)dialog).getMapWidth(), ((NewMapDialog)dialog).getMapHeight());
            dialog.setVisible(false);
            dialog.hide();

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private class LoadMap extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            currentMap = Map.Load(((LoadMapDialog)dialog).getMapName(), tileStateManager);
            dialog.setVisible(false);
            dialog.hide();

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private class NewMapListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            dialog = new NewMapDialog("Create New Map", uiSkin, new CreateMapListener());
            dialog.show(hud);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private class LoadMapListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            dialog = new LoadMapDialog("Load A Map", uiSkin, new LoadMap());
            dialog.show(hud);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private class SaveMapListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if(currentMap != null)
                currentMap.save();
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private class EditWaypointListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            AngelWaypointDialog dia = (AngelWaypointDialog)dialog;
            dia.build();
            dia.getWaypoint().setInstructions(dia.getInstructions());

            dialog.setVisible(false);
            dialog.hide();
            input.clear();

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private class DeleteWaypointListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            AngelWaypointDialog dia = (AngelWaypointDialog)dialog;

            AngelEntity angel = (AngelEntity)currentMap.getSelectedEntity();
            angel.removeWaypoint(dia.getWaypoint());

            dialog.setVisible(false);
            dialog.hide();

            input.clear();

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private class CancelDialogListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            dialog.setVisible(false);
            dialog.hide();
            input.clear();

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private class EditMessageListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            EditMessageDialog dia = (EditMessageDialog) dialog;

            MessageEntity messageEntity = (MessageEntity)currentMap.getSelectedEntity();
            String[] messages = dia.getMessage().split(";");
            ArrayList<String> msgs = new ArrayList<String>();
            for(String msg : messages) {
                msgs.add(msg);
            }

            messageEntity.setMessages(msgs);

            dialog.setVisible(false);
            dialog.hide();
            input.clear();

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    @Override
    public void create() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 0, 0);
        camera.update();

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

        assetManager = new AssetManager();

        batch = new SpriteBatch();

        currentMap = null;

        tileStateManager = new TileStates(assetManager);
        mapEntityStateManager = new EntityStates(assetManager);

        placementMode = PlacementMode.TILE;
        currentTileState = TileState.FLOOR;
        currentTileStateID = TileState.FLOOR.getID();
        currentEntityStateID = MapEntityState.ANGEL_ROAM.getID();
        currentMapEntityState = MapEntityState.ANGEL_ROAM;

        assetManager.load("button.png", Texture.class);
        assetManager.load("tileHighlighter.png", Texture.class);
        assetManager.finishLoading();

        tileHighlighter = assetManager.get("tileHighlighter.png", Texture.class);

        Skin skin = new Skin();
        skin.add("button", assetManager.get("button.png", Texture.class));

        hud = new HUD();
        EditorMainHUD main = new EditorMainHUD(skin, new NewMapListener(), new LoadMapListener(), new SaveMapListener());
        main.setPosition(0, Gdx.graphics.getHeight());

        hud.addTable(main);

        selectedTileImage = new Image(tileStateManager.FloorTexture);
        selectedTileImage.setPosition(HUD.WIDTH-Map.CELL_WIDTH,0);
        hud.addImage(selectedTileImage);

        multiplexer = new InputMultiplexer();
        input = new InputHandler();
        multiplexer.addProcessor(input);
        multiplexer.addProcessor(hud);
        Gdx.input.setInputProcessor(multiplexer);

        dialog = new Dialog("Test", uiSkin);
        dialog.setVisible(false);

        modeLabel = new Label("MODE: Tile", uiSkin);
        modeLabel.setPosition((HUD.WIDTH-modeLabel.getWidth()-selectedTileImage.getWidth()-20),15);
        modeLabel.setColor(Color.RED);
        hud.addLabel(modeLabel);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.6f, 0.6f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update
        if(!dialog.isVisible()) {
            update();
        }

        // Render
        if(currentMap != null) {
            currentMap.render(batch, camera, uiSkin.getFont("default-font"), tileStateManager, mapEntityStateManager);
        }

        hud.render();

        Vector3 mousePos = input.unprojectedMousePos(camera, new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        if(currentMap != null) {
            Point tilePos = currentMap.mousePosToTile(new Vector2(mousePos.x, mousePos.y));
            if (currentMap.tileValid(tilePos)) {
                batch.setProjectionMatrix(camera.combined);
                batch.begin();
                batch.draw(tileHighlighter, tilePos.x * Map.CELL_WIDTH, tilePos.y * Map.CELL_HEIGHT);
                batch.end();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        hud.resize(width, height);
    }

    public void update() {

        updateInput();

        if(currentMap != null) {
            switch (placementMode) {
                case TILE:
                    if (input.isLeftClickDown()) {
                        Vector3 touchPos = input.unprojectedMousePos(camera, Input.Buttons.LEFT);
                        Point p = currentMap.mousePosToTile(new Vector2(touchPos.x, touchPos.y));
                        currentMap.setCell(p.x, p.y, currentTileState);
                    }
                    break;

                case ENTITY:
                    if (input.isLeftClickDown()) {
                        Vector3 touchPos = input.unprojectedMousePos(camera, Input.Buttons.LEFT);
                        Point p = currentMap.mousePosToTile(new Vector2(touchPos.x, touchPos.y));
                        currentMap.setSelectedEntity(p);

                        if (currentMap.getSelectedEntity() == null) {
                            switch (currentMapEntityState) {
                                case ANGEL_ROAM:
                                    currentMap.addEntity(new AngelRoamEntity(p));
                                    break;
                                case DOOR:
                                    currentMap.addEntity(new DoorEntity(p));
                                    break;
                                case KEY:
                                    currentMap.addEntity(new KeyEntity(p));
                                    break;
                                case TELEPORTER_ENTRANCE:
                                    currentMap.addEntity(new TeleporterEntity(p));
                                    break;
                                case SWITCH:
                                    currentMap.addEntity(new SwitchEntity(p));
                                    break;
                                case PUSH_BLOCK:
                                    currentMap.addEntity(new PushBlockEntity(p));
                                    break;
                                case CANNON:
                                    currentMap.addEntity(new CannonEntity(p));
                                    break;
                                case DESTRUCTABLE_BLOCK:
                                    currentMap.addEntity(new DestructableBlockEntity(p));
                                    break;
                                case ANGEL:
                                    currentMap.addEntity(new AngelEntity(p));
                                    break;
                                case OBJECT:
                                    currentMap.addEntity(new ObjectEntity(p));
                                    break;
                                case MESSAGE:
                                    currentMap.addEntity(new MessageEntity(p));
                                    break;
                            }

                            currentMap.setCell(p.x, p.y, TileState.FLOOR);
                        }

                    } else if(input.isRightClickDown()) {
                        if(currentMap.getSelectedEntity() != null) {

                            Vector3 touchPos = input.unprojectedMousePos(camera, Input.Buttons.RIGHT);
                            Point p = currentMap.mousePosToTile(new Vector2(touchPos.x, touchPos.y));
                            MapEntity entity = currentMap.getSelectedEntity();

                            if(entity instanceof AngelRoamEntity) {
                                AngelRoamEntity angel = (AngelRoamEntity) entity;
                                if (angel.getWayPoints().contains(p)) {
                                    angel.removeWayPoint(p);
                                } else {
                                    angel.addWayPoint(p);
                                    currentMap.setCell(p.x, p.y, TileState.FLOOR);
                                }
                            } else if(entity instanceof CannonEntity) {
                                CannonEntity cannon = (CannonEntity) entity;
                                Point dir = new Point(MathUtils.clamp(p.x - cannon.getLocation().x, -1, 1), MathUtils.clamp(p.y - cannon.getLocation().y, -1, 1));
                                if(dir.x != 0 && dir.y != 0)
                                    dir.y = 0;
                                cannon.setDirection(dir);
                            } else if(entity instanceof SwitchEntity) {
                                SwitchEntity switchEntity = (SwitchEntity) entity;
                                if (switchEntity.getDoorLocations().contains(p)) {
                                    switchEntity.removeDoorLocation(p);
                                } else {
                                    switchEntity.addDoorLocation(p);
                                    currentMap.setCell(p.x, p.y, TileState.FLOOR);
                                }
                            } else if (entity instanceof TeleporterEntity) {
                                TeleporterEntity teleporter = (TeleporterEntity) entity;
                                teleporter.setExitLocation(p);
                                currentMap.setCell(p.x, p.y, TileState.FLOOR);
                            } else if (entity instanceof AngelEntity) {
                                AngelEntity angel = (AngelEntity) entity;
                                AngelEntity.Waypoint waypoint = angel.getWaypoint(p);
                                if(waypoint == null) {
                                    angel.addWaypoint(p);
                                    waypoint = angel.getWaypoint(p);
                                }
                                currentMap.setCell(p.x, p.y, TileState.FLOOR);
                                dialog = new AngelWaypointDialog("Waypoint Editor", uiSkin, new EditWaypointListener(), new DeleteWaypointListener(), new CancelDialogListener(), waypoint);
                                dialog.setVisible(true);
                                dialog.show(hud);
                            } else if (entity instanceof PushBlockEntity) {
                                PushBlockEntity pushBlock = (PushBlockEntity) entity;
                                pushBlock.setNewCorner(p);
                            } else if (entity instanceof MessageEntity) {
                                MessageEntity messageEntity = (MessageEntity) entity;
                                if(p.equals(messageEntity.getLocation())) {
                                    dialog = new EditMessageDialog("Message Editor", uiSkin, new EditMessageListener(), messageEntity.getMessages());
                                    dialog.setVisible(true);
                                    dialog.show(hud);
                                } else {
                                    messageEntity.setFocusPoint(p);
                                }
                            }

                            input.clear();
                        }
                    }

                    if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                        MapEntity entity = currentMap.getSelectedEntity();
                        if(entity instanceof AngelRoamEntity) {
                            ((AngelRoamEntity)entity).reverseDirection();
                        }
                    }

                    break;
            }
        }

        if(input.isScrolled()) {
            switch (placementMode) {
                case TILE:
                    if (input.getScrollAmount() == 1)
                        currentTileStateID += 1;
                    else
                        currentTileStateID -= 1;

                    if (currentTileStateID < 0)
                        currentTileStateID = TileState.COUNT.getID() - 1;
                    if (currentTileStateID >= TileState.COUNT.getID())
                        currentTileStateID = 0;
                    currentTileState = (TileState)tileStateManager.getState(currentTileStateID);
                    break;

                case ENTITY:
                    if(input.getScrollAmount() == 1)
                        currentEntityStateID +=1;
                    else
                        currentEntityStateID -= 1;

                    if(currentEntityStateID < 0)
                        currentEntityStateID = MapEntityState.COUNT.getID() - 1;
                    if (currentEntityStateID >= MapEntityState.COUNT.getID())
                        currentEntityStateID = 0;
                    currentMapEntityState = (MapEntityState)mapEntityStateManager.getState(currentEntityStateID);
                    break;
            }

            updateSelectedTileImage();
            input.scrollHandled();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            currentMap.save();
        }

        camera.update();
    }

    private void updateSelectedTileImage() {
        switch (placementMode){
            case TILE:
                selectedTileImage.setDrawable(new SpriteDrawable(new Sprite(tileStateManager.getStateIDTexture(currentTileStateID))));
                break;
            case ENTITY:
                selectedTileImage.setDrawable(new SpriteDrawable(new Sprite(mapEntityStateManager.getStateIDTexture(currentEntityStateID))));
                break;
        }
    }

    private void updateInput() {

        if(currentMap != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                camera.position.add(new Vector3(LEFT).scl(CAM_MOVE_SPEED));
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                camera.position.add(new Vector3(RIGHT).scl(CAM_MOVE_SPEED));
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                camera.position.add(new Vector3(DOWN).scl(CAM_MOVE_SPEED));
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                camera.position.add(new Vector3(UP).scl(CAM_MOVE_SPEED));
            } else if (Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)) {
                if (camera.zoom > 1) {
                    camera.zoom -= 0.1f;
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)) {
                if (camera.zoom <= 6f) {
                    camera.zoom += 0.1f;
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                currentMap.clearSelectedEntity();
                if (placementMode == PlacementMode.TILE) {
                    placementMode = placementMode.ENTITY;
                    modeLabel.setText("MODE: ENTITY");
                    modeLabel.pack();

                } else {
                    placementMode = placementMode.TILE;
                    modeLabel.setText("MODE: TILE");
                    modeLabel.pack();
                    modeLabel.setPosition((HUD.WIDTH-modeLabel.getWidth()-selectedTileImage.getWidth()-20),15);
                }
                updateSelectedTileImage();
            } else if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                if(currentMap.getSelectedEntity() != null) {
                    currentMap.deleteSelectedEntity();
                }
            } else if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                if(currentMap.getSelectedEntity() != null && currentMap.getSelectedEntity() instanceof MessageEntity) {
                    ((MessageEntity)currentMap.getSelectedEntity()).setFocusPoint(currentMap.getSelectedEntity().getLocation());
                }
            }
        }
    }

}

