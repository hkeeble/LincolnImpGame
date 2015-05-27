package com.henrik.advergame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.henrik.SaveGameSerializer;
import com.henrik.advergame.states.*;
import com.henrik.gdxFramework.core.GameBase;
import com.henrik.gdxFramework.core.GameState;

import java.util.*;

public class Game extends GameBase implements ApplicationListener {

    public enum FontSize {
        LARGE,
        SMALL
    }

    private GameState inGame, gameOver, intro, levelSelect, levelInterlude, titleScreen, credits, endGame;

    public static final int LEVEL_COUNT = 14;

    private SaveGame saveGame;

    // UI Atlas and Skin
    private static Skin uiSkin;
    private static TextureAtlas uiAtlas;

    private static HashMap<String,BitmapFont> fonts;

    @Override
    public void create () {
        super.create();

        // Initialize saveGame game
        loadGame();

        Bullet.init(); // Initialize bullet for collision detection
        
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

    public void loadFonts() {
        fonts = new HashMap<String, BitmapFont>();

        // Add fonts
        addFont("fonts/Minecraftia_Regular.ttf", "debug");
        addFont("fonts/AlbertText-Bold.ttf", "main");
    }

    private void addFont(String filePath, String internalName) {
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 52;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(filePath));

        BitmapFont base = generator.generateFont(params);
        BitmapFont small = new BitmapFont(generator.generateData(params), base.getRegions(), true);

        small.getData().setScale(0.5f);

        fonts.put(internalName + String.valueOf(FontSize.LARGE), base);
        fonts.put(internalName + String.valueOf(FontSize.SMALL), small);
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

        Iterator iter = fonts.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            ((BitmapFont)entry.getValue()).dispose();
        }

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

        for(int i = 1; i < Game.LEVEL_COUNT+1; i++) {
        	saveGame.saveLevelData(i, new LevelSaveData(i, 0, 0, 0));
        	saveGame();
        }
    	saveGame.setLevel(Game.LEVEL_COUNT);

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

    public static void setUiSkin(TextureAtlas atlas) {
        if(uiSkin != null)
            uiSkin.dispose();

        uiSkin = new Skin(atlas);
    }

    public static BitmapFont getFont(String name, FontSize size) {
        return fonts.get(name + String.valueOf(size));
    }
}
