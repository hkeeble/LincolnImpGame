package com.henrik.advergame;

/**
 * Created by Henri on 21/03/2015.
 */
public class LevelSaveData  {

    private int level;
    private int score;
    private int objectsDestroyed;
    private int potentialObjects;

    public LevelSaveData() {
        level = 0;
        score = 0;
        objectsDestroyed = 0;
        potentialObjects = 0;
    }

    public LevelSaveData(int level, int score, int objectsDestroyed, int potentialObjects) {
        this.level = level;
        this.score = score;
        this.objectsDestroyed = objectsDestroyed;
        this.potentialObjects = potentialObjects;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getObjectsDestroyed() {
        return objectsDestroyed;
    }

    public int getPotentialObjects() { return potentialObjects; }

    public void setObjectsDestroyed(int objectsDestroyed) {
        this.objectsDestroyed = objectsDestroyed;
    }

    public void setPotentialObjects(int potentialObjects) {
        this.potentialObjects = potentialObjects;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
