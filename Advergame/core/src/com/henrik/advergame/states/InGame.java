package com.henrik.advergame.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.henrik.advergame.Game;
import com.henrik.advergame.hud.tables.DebugTable;
import com.henrik.gdxFramework.core.GameBase;
import com.henrik.gdxFramework.core.GameState;
import com.henrik.advergame.worlds.GameWorld;

public class InGame extends GameState {

    private GameWorld world;
    private DebugTable dbgHud;
    private EntryState entryState;
    private int levelToLoad;

    public enum EntryState {
        REPLAY,
        CONTINUE,
        SET_LEVEL
    }

    public void setReplayState() {
        entryState = EntryState.REPLAY;
    }

    public void setContinueState() {
        entryState = EntryState.CONTINUE;
    }

    public void setLevelState(int levelNumber) {
        entryState = EntryState.SET_LEVEL;
        levelToLoad = levelNumber;
    }

    // HUD Button Listeners
    private class EnableGLProfileEvent extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            game.toggleGLProfiling();

            if(game.isGLProfilerEnabled())
                dbgHud.getGLProfileTable().setVisible(true);
            else
                dbgHud.getGLProfileTable().setVisible(false);

            return true;
        }
    }

    private class EnableCollisionDebug extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            world.toggleCollisionDebug();
            return true;
        }
    }

    private class EnableNavDebug extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            world.toggleNavDebug();
            return true;
        }
    }

    private class RegenerateLevel extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            world.resetLevel();
            return true;
        }
    }

    private class ToggleDebugCamera extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            world.toggleDebugCamera();
            return true;
        }
    }

    public InGame(GameBase game) {
        super(game);

        setLoadState(new LoadingScreen(assetManager, game.hud));

        // Add assets to load for this state here, will begin loading as soon as state is enabled
        addAsset("textures/wall1Texture.png", Texture.class);
        addAsset("textures/wall2Texture.png", Texture.class);
        addAsset("textures/wall3Texture.png", Texture.class);
        addAsset("textures/wall4Texture.png", Texture.class);
        addAsset("textures/crackedWallTexture.png", Texture.class);
        addAsset("textures/floorBase.png", Texture.class);
        addAsset("textures/doorTexture.png", Texture.class);
        addAsset("textures/doorLockTexture.png", Texture.class);
        addAsset("textures/pushBlockTexture.png", Texture.class);
        addAsset("textures/black.png", Texture.class);

        addAsset("sprites/table.png", Texture.class);
        addAsset("sprites/chest.png", Texture.class);
        addAsset("sprites/steps.png", Texture.class);
        addAsset("sprites/switch_on.png", Texture.class);
        addAsset("sprites/switch_off.png", Texture.class);
        addAsset("sprites/cannonAnimation.png", Texture.class);
        addAsset("sprites/key.png", Texture.class);
        addAsset("sprites/explosion.png", Texture.class);
        addAsset("sprites/player.png", Texture.class);
        addAsset("sprites/angel.png", Texture.class);
        addAsset("sprites/teleporter.png", Texture.class);
        addAsset("sprites/cannonBall.png", Texture.class);
        addAsset("sprites/fire_ball_left.png", Texture.class);
        addAsset("sprites/fire_ball_right.png", Texture.class);
        addAsset("sprites/fire_ball_down.png", Texture.class);
        addAsset("sprites/fire_ball_up.png", Texture.class);
        addAsset("sprites/dog.png", Texture.class);

        addAsset("ui/ui.pack", TextureAtlas.class);

        addAsset("sounds/select.wav", Sound.class);
        addAsset("sounds/talk.wav", Sound.class);
        addAsset("sounds/keyGet.wav", Sound.class);
        addAsset("sounds/hit1.wav", Sound.class);
        addAsset("sounds/hit2.wav", Sound.class);
        addAsset("sounds/hit3.wav", Sound.class);
        addAsset("sounds/lose.wav", Sound.class);
        addAsset("sounds/win.wav", Sound.class);
        addAsset("sounds/destroy.wav", Sound.class);
        addAsset("sounds/switch.wav", Sound.class);
        addAsset("sounds/teleport.wav", Sound.class);
        addAsset("sounds/hurt.wav", Sound.class);
        addAsset("sounds/teleport.wav", Sound.class);
    }

    @Override
    public void initialize() {

        // Initialize the Debug HUD
        initDebugHUD();

        // Initialize the game world
        world = new GameWorld(game, assetManager);

        if(entryState == EntryState.CONTINUE) {
            world.loadLevel(((Game) game).getPlayerLevel());
        } else if(entryState == EntryState.SET_LEVEL) {
            world.loadLevel(levelToLoad);
        } else if(entryState == EntryState.REPLAY) {
            world.loadLevel(((Game)game).getSaveGame().getLastPlayedLevel());
        }
    }

    private void initDebugHUD() {
        if(Game.DEBUG_ACTIVE) {

            BitmapFont font = Game.getFont("debug", 1);

            dbgHud = new DebugTable(font, Game.getUISkin(), new RegenerateLevel(), new EnableGLProfileEvent(), new EnableCollisionDebug(), new EnableNavDebug(), new ToggleDebugCamera());
            game.hud.addActor(dbgHud);
        }
    }

    @Override
    public void render() {
        if(!isLoading()) {
            world.render();
        }
    }

    @Override
    public void update() {
        super.update();

        if(!isLoading()) {
            world.update();
            updateHUD();
        }
    }

    private void updateHUD() {
        if(game.isGLProfilerEnabled()) {
            if(Game.DEBUG_ACTIVE) {
                dbgHud.update(game.getProfile());
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        clear();
    }

    @Override
    protected void clear() {
        if(world != null) {
            world.dispose();
            world = null;
        }
    }
}
