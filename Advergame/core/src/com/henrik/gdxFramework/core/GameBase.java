package com.henrik.gdxFramework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;

/**
 * The base game class from which the main game inherits.
 * @author Henri
 */
public class GameBase {

    public static final boolean DEBUG_ACTIVE = false;

    protected GameStateCollection states;
    protected InputHandler input;
    protected InputMultiplexer inputProcessor;

    // For debugging only
    protected Profiler profiler;
    protected ShapeRenderer debugRenderer;

    public HUD hud;

    protected AssetManager assetManager;

    protected boolean resized;

    private int fpsCap;

    public void create () {
        // Initialize list of game states
        states = new GameStateCollection();

        // Set the input handler for the game
        inputProcessor = new InputMultiplexer();
        input = new InputHandler();
        addInputProcessor(input);
        Gdx.input.setInputProcessor(inputProcessor);

        // Initialize toggleGLProfiling
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Initialize debugging objects
        profiler = new Profiler();
        debugRenderer = new ShapeRenderer();

        assetManager = new AssetManager();

        // Create the game-wide HUD
        hud = new HUD();

        resized = false;

        addInputProcessor(hud);
    }

    public void setFPSCap(int fps) {
        this.fpsCap = fpsCap;
    }

    public void render () {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Update all current states
        states.update();

        // Update the profiler
        profiler.update();

        // Render the current HUD
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        hud.render();
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);

        if(resized)
            resized = false;
    }

    public void enableProfiling() {
        profiler.enable();
    }

    public void disableProfiling() {
        profiler.disable();
    }

    public void toggleGLProfiling() {
        if(profiler.isEnabled())
            profiler.disable();
        else
            profiler.enable();
    }

    public boolean isGLProfilerEnabled() {
        return profiler.isEnabled();
    }

    public Profiler getProfile() {
        return profiler;
    }

    public void resize (int width, int height) {
        resized = true;
        hud.resize(width, height);
    }

    public InputHandler getInput() {
        return  input;
    }

    public void addInputProcessor(InputProcessor processor) { inputProcessor.addProcessor(processor); }

    public void enableState(Class<?> type) {
        hud.clear(); // Clear the HUD for this state
        states.enable(type); // Enable the new state, disabling all other states
    }

    public ShapeRenderer getDebugRenderer() {
        return debugRenderer;
    }

    public void pause () {

    }

    public boolean wasResized() {
        return resized;
    }

    public void resume () {

    }
    public void dispose () {
        states.dispose();
        assetManager.dispose();
    }


    public <T> T getState(Class<T> type) {
        return (T)states.get(type);
    }

    public AssetManager getAssetManager() { return assetManager; }
}