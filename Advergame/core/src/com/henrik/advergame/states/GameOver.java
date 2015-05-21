package com.henrik.advergame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.henrik.advergame.Game;
import com.henrik.advergame.LevelSaveData;
import com.henrik.advergame.hud.tables.GameOverTable;
import com.henrik.advergame.hud.tables.LevelInterludeTable;
import com.henrik.gdxFramework.core.GameBase;
import com.henrik.gdxFramework.core.GameState;

/**
 * Created by Henri on 01/04/2015.
 */
public class GameOver extends GameState {

    private Music loseMusic;
    private Sound music;

    private boolean musicPlaying;

    class ExitListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            music.stop();
            loseMusic.stop();

            assetManager.get("sounds/select.wav", Sound.class).play();

            game.enableState(TitleScreen.class);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    class ReplayListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            music.stop();
            loseMusic.stop();

            assetManager.get("sounds/select.wav", Sound.class).play();

            InGame inGameState = game.getState(InGame.class);
            inGameState.setReplayState();
            game.enableState(InGame.class);

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    // Concrete reference to the game
    private Game concreteGame;

    // Data from the level that was just finished
    private LevelSaveData levelData;

    private GameOverTable gameOverTable;

    public GameOver(GameBase game) {
        super(game);

        setLoadState(new LoadingScreen(assetManager, game.hud));

        addAsset("textures/sky.png", Texture.class);
        addAsset("ui/ui.pack", TextureAtlas.class);

        addAsset("sounds/select.wav", Sound.class);
        addAsset("sounds/lose.wav", Music.class);

    }

    @Override
    protected void initialize() {

        concreteGame = (Game)game;
        levelData = concreteGame.getSaveGame().getLastPlayedLevelData();

        gameOverTable = new GameOverTable(Game.getFont("main", 5), Game.getFont("main", 4), Game.getUISkin(), concreteGame.getSaveGame(), new ReplayListener(), new ExitListener());
        game.hud.getMainTable().setBackground(Game.getUISkin().getDrawable("loseScreen"));
        game.hud.getMainTable().add(gameOverTable).center();

        music = Gdx.audio.newSound(Gdx.files.internal("sounds/titleMusic.mp3"));
        loseMusic = assetManager.get("sounds/lose.wav", Music.class);
        loseMusic.play();

        musicPlaying = false;
    }

    @Override
    public void update() {
        super.update();

        if(!isLoading()) {
            if(!loseMusic.isPlaying() && musicPlaying == false) {
                music.play();
                musicPlaying = true;
            }
        }
    }

    @Override
    public void render() {
        if(!isLoading()) {

        }
    }

    @Override
    protected void dispose() {
        super.dispose();
        if(music != null)
            music.dispose();
        clear();
    }

    @Override
    protected void clear() {
        if(music != null)
            music.dispose();
        super.clear();
    }


}
