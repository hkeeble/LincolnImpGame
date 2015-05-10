package com.henrik;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.henrik.advergame.LevelSaveData;
import com.henrik.advergame.SaveGame;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Henri on 21/03/2015.
 */
public class SaveGameSerializer implements Json.Serializer<SaveGame> {

    @Override
    public void write(Json json, SaveGame object, Class knownType) {
        json.writeObjectStart();
        json.writeArrayStart("levelData");
        Iterator iter = object.getAllLevelSaveData().entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            LevelSaveData data = (LevelSaveData) entry.getValue();
            json.writeObjectStart();
            json.writeValue("level", data.getLevel());
            json.writeValue("score", data.getScore());
            json.writeValue("objectsDestroyed", data.getObjectsDestroyed());
            json.writeValue("potentialObjects", data.getPotentialObjects());
            json.writeObjectEnd();
        }
        json.writeArrayEnd();

        json.writeValue("currentLevel", object.getLevel());
        json.writeObjectEnd();
    }

    @Override
    public SaveGame read(Json json, JsonValue jsonData, Class type) {
        SaveGame saveGame = new SaveGame();
        for (JsonValue entry = jsonData.child; entry != null; entry = entry.next()) {
            if (entry.isArray()) {
                if (entry.name.equals("levelData")) {
                    for (JsonValue levelData = entry.child; levelData != null; levelData = levelData.next()) {
                        saveGame.saveLevelData(levelData.getInt("level"), new LevelSaveData(levelData.getInt("level"), levelData.getInt("score"), levelData.getInt("objectsDestroyed"),
                                levelData.getInt("potentialObjects")));
                    }
                }
            } else {
                if(entry.name.equals("currentLevel")) {
                    saveGame.setLevel(entry.asInt());
                }
            }
        }

        return saveGame;
    }
}
