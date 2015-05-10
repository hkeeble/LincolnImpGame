package com.henrik.advergame.leveleditor.StateManagers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.henrik.advergame.leveleditor.shared.MapEntityState;

import java.util.Iterator;

/**
 * Created by Henri on 27/02/2015.
 */
public class EntityStates extends StateManager {

    public static Texture AngelRoamTexture;
    public static Texture AngelTexture;
    public static Texture DoorTexture;
    public static Texture KeyTexture;
    public static Texture DestructableBlockTexture;
    public static Texture PushBlockTexture;
    public static Texture SwitchTexture;
    public static Texture SwitchDoorTexture;
    public static Texture CannonTexture;
    public static Texture TeleporterEntranceTexture;
    public static Texture TeleporterExitTexture;
    public static Texture CannonDirectionTexture;
    public static Texture ObjectTexture;
    public static Texture MessageTexture;
    public static Texture MessageFocusPointTexture;

    public EntityStates(AssetManager manager) {
        super(manager);

        manager.load("entities/keyTile.png", Texture.class);
        manager.load("entities/doorTile.png", Texture.class);
        manager.load("entities/angelRoamTile.png", Texture.class);
        manager.load("entities/angelTile.png", Texture.class);
        manager.load("entities/destructableBlock.png", Texture.class);
        manager.load("entities/pushBlockTile.png", Texture.class);
        manager.load("entities/switchTile.png", Texture.class);
        manager.load("entities/switchDoorTile.png", Texture.class);
        manager.load("entities/cannonTile.png", Texture.class);
        manager.load("entities/teleporterEntranceTile.png", Texture.class);
        manager.load("entities/teleporterExitTile.png", Texture.class);
        manager.load("entities/cannonDirectionTile.png", Texture.class);
        manager.load("entities/objectTile.png", Texture.class);
        manager.load("entities/messageTile.png", Texture.class);
        manager.load("entities/messageFocusPointTile.png", Texture.class);
        manager.finishLoading();

        AngelRoamTexture = manager.get("entities/angelRoamTile.png", Texture.class);
        AngelTexture = manager.get("entities/angelTile.png", Texture.class);
        DoorTexture = manager.get("entities/doorTile.png", Texture.class);
        KeyTexture = manager.get("entities/keyTile.png", Texture.class);
        DestructableBlockTexture= manager.get("entities/destructableBlock.png", Texture.class);
        PushBlockTexture = manager.get("entities/pushBlockTile.png", Texture.class);
        SwitchTexture = manager.get("entities/switchTile.png", Texture.class);
        SwitchDoorTexture = manager.get("entities/switchDoorTile.png", Texture.class);
        CannonTexture = manager.get("entities/cannonTile.png", Texture.class);
        CannonDirectionTexture = manager.get("entities/cannonDirectionTile.png", Texture.class);
        TeleporterEntranceTexture = manager.get("entities/teleporterEntranceTile.png", Texture.class);
        TeleporterExitTexture = manager.get("entities/teleporterExitTile.png", Texture.class);
        ObjectTexture = manager.get("entities/objectTile.png", Texture.class);
        MessageTexture = manager.get("entities/messageTile.png", Texture.class);
        MessageFocusPointTexture = manager.get("entities/messageFocusPointTile.png", Texture.class);

        stateMap.put(MapEntityState.ANGEL_ROAM, AngelRoamTexture);
        stateMap.put(MapEntityState.ANGEL, AngelTexture);
        stateMap.put(MapEntityState.DOOR, DoorTexture);
        stateMap.put(MapEntityState.KEY, KeyTexture);
        stateMap.put(MapEntityState.DESTRUCTABLE_BLOCK, DestructableBlockTexture);
        stateMap.put(MapEntityState.PUSH_BLOCK, PushBlockTexture);
        stateMap.put(MapEntityState.SWITCH, SwitchTexture);
        stateMap.put(MapEntityState.CANNON, CannonTexture);
        stateMap.put(MapEntityState.TELEPORTER_ENTRANCE, TeleporterEntranceTexture);
        stateMap.put(MapEntityState.OBJECT, ObjectTexture);
        stateMap.put(MapEntityState.MESSAGE, MessageTexture);
    }

    public MapEntityState GetState(int id) {
        Iterator iter = stateMap.entrySet().iterator();
        while(iter.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry)iter.next();
            MapEntityState state = (MapEntityState)entry.getKey();

            if(id == state.getID())
                return state;
        }

        return null;
    }
}
