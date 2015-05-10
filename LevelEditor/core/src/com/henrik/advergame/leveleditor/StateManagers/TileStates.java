package com.henrik.advergame.leveleditor.StateManagers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.henrik.advergame.leveleditor.shared.TileState;

import java.util.Iterator;

/**
 * Created by Henri on 24/02/2015.
 */
public class TileStates extends StateManager {

    public Texture WallTexture1;
    public Texture WallTexture2;
    public Texture WallTexture3;
    public Texture WallTexture4;

    public Texture FloorTexture;
    public Texture ExitTexture;
    public Texture StartTexture;

    public TileStates(AssetManager manager) {
        super(manager);

        manager.load("tiles/blockedTile.png", Texture.class);
        manager.load("tiles/blockedTile2.png", Texture.class);
        manager.load("tiles/blockedTile3.png", Texture.class);
        manager.load("tiles/blockedTile4.png", Texture.class);
        manager.load("tiles/floorTile.png", Texture.class);
        manager.load("tiles/exitTile.png", Texture.class);
        manager.load("tiles/startTile.png", Texture.class);
        manager.finishLoading();

        WallTexture1 = manager.get("tiles/blockedTile.png", Texture.class);
        WallTexture2 = manager.get("tiles/blockedTile2.png", Texture.class);
        WallTexture3 = manager.get("tiles/blockedTile3.png", Texture.class);
        WallTexture4 = manager.get("tiles/blockedTile4.png", Texture.class);
        FloorTexture = manager.get("tiles/floorTile.png", Texture.class);
        ExitTexture = manager.get("tiles/exitTile.png", Texture.class);
        StartTexture = manager.get("tiles/startTile.png", Texture.class);

        stateMap.put(TileState.FLOOR, FloorTexture);
        stateMap.put(TileState.WALL_ONE, WallTexture1);
        stateMap.put(TileState.WALL_TWO, WallTexture2);
        stateMap.put(TileState.WALL_THREE, WallTexture3);
        stateMap.put(TileState.WALL_FOUR, WallTexture4);
        stateMap.put(TileState.EXIT, ExitTexture);
        stateMap.put(TileState.START, StartTexture);
    }

}
