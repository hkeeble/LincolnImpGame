package com.henrik.advergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A saved game state.
 */
public class SaveGame {
    public static String SAVEGAME_FILE = "saveGame.json";

    private int lastPlayedLevel;
    private int currentLevel;
    private HashMap<Integer,LevelSaveData> levelData;

    public SaveGame() {
        currentLevel = 1;

        levelData = new HashMap();
        levelData.put(1, new LevelSaveData(1, 0, 0, 0));
    }

    public void saveLevelData(int level, LevelSaveData data) {

        // If the level is already saved, just update the neccesary scores
        if(levelData.containsKey(level)) {
            LevelSaveData oldData = levelData.get(level);
            if(oldData.getScore() < data.getScore()) {
                oldData.setScore(data.getScore());
            }
            if(oldData.getObjectsDestroyed() < data.getObjectsDestroyed()) {
                oldData.setObjectsDestroyed(data.getObjectsDestroyed());
            }
            oldData.setPotentialObjects(data.getPotentialObjects());
            return;
        }

        levelData.put(level, data);
    }

    /**
     * Advance the current level.
     */
    public void advanceLevel() {
        currentLevel++;

        if(currentLevel > Game.LEVEL_COUNT)
            currentLevel = Game.LEVEL_COUNT;

        saveLevelData(currentLevel, new LevelSaveData(currentLevel, 0, 0, 0));
    }

    /**
     * Sets the current, highest level, that the player can play.
     * @param level
     */
    public void setLevel(int level) { currentLevel = level;  }

    /**
     * Gets the current, highest level, that the player can play.
     * @return
     */
    public int getLevel() {
        return currentLevel;
    }

    /**
     * Gets data from the last played level.
     */
    public LevelSaveData getLastPlayedLevelData() {
        return getLevelSaveData(lastPlayedLevel);
    }

    /**
     * Gets the number of the last played level.
     */
    public int getLastPlayedLevel() { return lastPlayedLevel; }

    public void setLastPlayedLevel(int level) {
        lastPlayedLevel = level;
    }

    public LevelSaveData getLevelSaveData(int level) { return levelData.get(level); }

    public HashMap<Integer, LevelSaveData> getAllLevelSaveData() { return levelData; }

    public int getTotalScore() {
        int total = 0;
        Iterator iter = levelData.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            LevelSaveData data = (LevelSaveData) entry.getValue();
            total += data.getScore();
        }

        return total;
    }

    public int getTotalObjectsDestroyed() {
        int total = 0;
        Iterator iter = levelData.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            LevelSaveData data = (LevelSaveData) entry.getValue();
            total += data.getObjectsDestroyed();
        }

        return total;
    }
}
