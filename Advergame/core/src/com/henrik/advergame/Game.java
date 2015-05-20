package com.henrik.advergame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.henrik.SaveGameSerializer;
import com.henrik.advergame.states.*;
import com.henrik.gdxFramework.core.GameBase;
import com.henrik.gdxFramework.core.GameState;

import java.util.*;

public class Game extends GameBase implements ApplicationListener {

    private GameState inGame, gameOver, intro, levelSelect, levelInterlude, titleScreen, credits, endGame;

    public final int FONT_COUNT = 10;
    public final int[] FONT_SIZES = new int[] { 3, 2, 7, 6, 5, 4, 1};
    public final int FONT_SIZE_INCREMENT = 12;

    public static final int LEVEL_COUNT = 14;

    private ArrayList<FreeTypeFontGenerator.FreeTypeFontParameter> fontParamList;

    private SaveGame saveGame;

    // UI Atlas and Skin
    private static Skin uiSkin;
    private static TextureAtlas uiAtlas;
    
    @Override
    public void create () {
        super.create();

        // Initialize saveGame game
        loadGame();

        Bullet.init(); // Initialize bullet for collision detection

        // Load the UI Atlas so it can be accessed from anywhere, and disposed of uniformly
        uiAtlas = new TextureAtlas(Gdx.files.internal("ui/ui.pack"));
        uiSkin = new Skin(uiAtlas);
        
        intro = new Intro(this);
        titleScreen = new TitleScreen(this);
        inGame = new InGame(this);
        gameOver = new GameOver(this);
        levelSelect = new LevelSelect(this);
        levelInterlude = new LevelInterlude(this);
        credits = new Credits(this);
        endGame = new EndGame(this);

        states.add(intro);
        states.add(titleScreen);
        states.add(inGame);
        states.add(gameOver);
        states.add(levelSelect);
        states.add(levelInterlude);
        states.add(credits);
        states.add(endGame);

        states.enable(TitleScreen.class);

        // Enable culling
        Gdx.gl.glEnable(GL30.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL30.GL_BACK);

        loadFonts();
    }

    @Override
    public void loadFonts() {
        // Sizes
        fontParamList = new ArrayList<FreeTypeFontGenerator.FreeTypeFontParameter>();
        for(int i = 0; i < FONT_SIZES.length; i++) {
            FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
            params.size = 12 + (i* FONT_SIZE_INCREMENT);
            fontParamList.add(params);
        }

        // Add fonts
        addFont("fonts/Minecraftia_Regular.ttf", "debug");
        addFont("fonts/AlbertText-Bold.ttf", "main");
    }

    private void addFont(String filePath, String internalName) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(filePath));
        for(int i = 0; i < FONT_SIZES.length; i++) {
            fonts.put(internalName + String.valueOf(i+1), generator.generateFont(fontParamList.get(i)));
        }
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void resize (int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause () {
        super.pause();
    }

    @Override
    public void resume () {
        super.resume();
    }

    @Override
    public void dispose () {
    	uiSkin.dispose();
        fontManager.dispose();
        super.dispose();
    }

    /**
     * Save the current level progress.
     */
    public void saveGame() {
        Json json = new Json();

        json.setSerializer(SaveGame.class, new SaveGameSerializer());

        FileHandle handle = Gdx.files.local(SaveGame.SAVEGAME_FILE);
        String data = json.prettyPrint(saveGame);
        handle.writeString(data, false);
    }

    /**
     * Load saved game if it exists, if not will create a new blank save game.
     */
    public void loadGame() {
        Json json = new Json();

        json.setSerializer(SaveGame.class, new SaveGameSerializer());

        FileHandle handle = Gdx.files.local(SaveGame.SAVEGAME_FILE);
        if(handle.exists()) {
            saveGame = json.fromJson(SaveGame.class, handle);
        } else {
            saveGame = new SaveGame();
            saveGame();
        }

        // Unlocks all levels
        /*
        for(int i = 1; i < Game.LEVEL_COUNT+1; i++) {
        	saveGame.saveLevelData(i, new LevelSaveData(i, 0, 0, 0));
        	saveGame();
        }
    	saveGame.setLevel(Game.LEVEL_COUNT);
    	*/
    }

    /**
     * Get the currently loaded saveGame game.
     */
    public SaveGame getSaveGame() {
        return saveGame;
    }

    /**
     * Get the currently saved player level.
     */
    public int getPlayerLevel() {
        return saveGame.getLevel();
    }
    
    public static Skin getUISkin() {
    	return uiSkin;
    }
}
