package com.henrik.advergame.hud;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.henrik.advergame.Game;
import com.henrik.advergame.SessionManager;
import com.henrik.advergame.hud.tables.InGameDataTable;
import com.henrik.advergame.hud.tables.PauseTable;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.GameBase;
import com.henrik.gdxFramework.core.HUD;

/**
 * Manages the main game world hud elements.
 */
public class HUDManager {

    /**
     * Event listener for pause button.
     */
    private class PauseListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            selectSound.play();
            world.pause(true);
            return true;
        }
    }

    /**
     * The resume button listener for the pause menu.
     */
    private class ResumeListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            selectSound.play();
            world.unPause();
            return true;
        }
    }

    /**
     * The retry button listener for the pause menu.
     */
    private class RetryListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            selectSound.play();
            world.resetLevel();
            world.unPause();
            return true;
        }
    }

    /**
     * The exit button listener for the pause menu.
     */
    private class ExitListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            game.enableState(Game.State.TITLE_SCREEN);
            return true;
        }
    }

    /**
     * Represents a HUD state.
     */
    public enum State {
        NONE,
        PAUSE,
        MAIN
    }

    private State currentState; // The current HUD state

    private InGameDataTable dataTable;
    private PauseTable pauseTable;
    private Button pauseButton;
    private Drawable pauseOverlay;

    private HUD hud; // Reference to the main game HUD
    private GameWorld world;
    private GameBase game;

    private Sound selectSound;

    /**
     * Creates a new HUD  manager.
     * @param game The game the manager belongs to.
     * @param world The world the manager belongs to.
     * @param uiSkin The UI skin used by the HUD elements.
     */
    public HUDManager(GameBase game, GameWorld world, Sound selectSound) {
        this.hud = game.hud;
        this.world = world;
        this.game = game;

        dataTable = new InGameDataTable(Game.getFont("main", 3));
        pauseTable = new PauseTable(Game.getFont("main", 7), Game.getFont("main", 6), new ResumeListener(), new RetryListener(), new ExitListener());
        pauseButton = HUD.makeButton(0, 0, new PauseListener(), Game.getUISkin().getDrawable("pauseButton"), Game.getUISkin().getDrawable("pauseButton"), Game.getUISkin().getDrawable("pauseButton"));
        pauseOverlay = Game.getUISkin().getDrawable("pauseOverlay");

        currentState = State.NONE;

        this.selectSound = selectSound;
    }

    /**
     * Update any HUD elements that monitor the session manager.
     */
    public void update(SessionManager sessionManager) {
        dataTable.update(sessionManager);
    }

    /**
     * Enable the pause HUD.
     */
    public void enablePauseHUD() {
        if(currentState != State.PAUSE) {
            currentState = State.PAUSE;
            dataTable.remove();
            pauseButton.remove();
            hud.getMainTable().setBackground(pauseOverlay);
            hud.getMainTable().center();
            hud.getMainTable().add(pauseTable);
        }
    }

    /**
     * Enable the main game HUD.
     */
    public void enableMainHUD() {
        if(currentState != State.MAIN) {
            currentState = State.MAIN;
            pauseTable.remove();

            hud.getMainTable().setBackground((Drawable)null);
            hud.getMainTable().top().right();
            hud.getMainTable().add(pauseButton);

            hud.addActor(dataTable);
            dataTable.setPosition(0, HUD.HEIGHT-(dataTable.getPrefHeight()/2));
        }
    }

    /**
     * Get the current state of the HUD.
     */
    public State getCurrentState() {
        return currentState;
    }
}
