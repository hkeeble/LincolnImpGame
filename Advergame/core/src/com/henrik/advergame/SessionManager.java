package com.henrik.advergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;
import com.henrik.advergame.worlds.GameWorld;

/**
 * A session manager is used to manage a game session.
 */
public class SessionManager {

    GameWorld gameWorld; // Reference to the world being managed

    private final int SCORE_PER_OBJECT = 50;

    private final int MAX_MULTIPLIER = 5;

    private int keys;
    private int score;
    private int objectsDestroyed;

    private boolean gameOver; // Whether or not the game is over
    private boolean playerExitAttempt; // Whether or not the player has attempted to exit this level this frame

    private boolean playerSighted;

    private Sound keyGet;

    public SessionManager(GameWorld gameWorld, Sound keyGet) {
        super();

        this.gameWorld = gameWorld;
        this.keyGet = keyGet;

        reset();
    }

    public int getScore() { return score; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPlayerExitAttempt() { return  playerExitAttempt; }

    public void setPlayerExitAttempt(boolean exit) {
        playerExitAttempt = exit;
    }

    public void setGameOver(boolean over) {
        gameOver = over;
    }

    public void setPlayerSighted(boolean playerSighted) {
        this.playerSighted = playerSighted;
    }

    public boolean isPlayerSighted() {
        return playerSighted;
    }

    public int getKeyCount() { return keys; }
    public boolean hasKey() { return keys > 0; }
    public void addKey() { keys++; keyGet.play(); }
    public void removeKey() { keys--; }

    public void addScore(int amount) {
        score += amount;
    }

    public void addObjectDestroyed() {
        objectsDestroyed++;
        addScore(SCORE_PER_OBJECT);
    }

    public int getObjectsDestroyed() {
        return objectsDestroyed;
    }

    public void removeScore(int score) {
        this.score -= score;
        if(this.score < 0)
            this.score = 0;
    }

    public void reset() {
        score = 0;
        keys = 0;
        objectsDestroyed = 0;
        gameOver = false;
    }
 }
