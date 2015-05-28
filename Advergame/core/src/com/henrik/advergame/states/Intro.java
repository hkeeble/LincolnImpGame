package com.henrik.advergame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.henrik.advergame.Game;
import com.henrik.advergame.hud.TitleHUD;
import com.henrik.advergame.hud.tables.IntroTable;
import com.henrik.gdxFramework.core.GameBase;
import com.henrik.gdxFramework.core.GameState;
import com.henrik.gdxFramework.core.HUD;

/**
 * Created by Henri on 05/04/2015.
 */
public class Intro extends GameState {

    private Sound music;
    private IntroTable introTable;

    private class PlayListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            assetManager.get("sounds/select.mp3", Sound.class).play();

            introTable.advancePage();

            if(introTable.isDone()) {
                music.stop();
                game.getState(InGame.class).setContinueState();
                game.enableState(InGame.class);
            }

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    public Intro(GameBase game) {
        super(game);

        setLoadState(new LoadingScreen(assetManager, game.hud));

        addAsset("ui/uiInterlude/uiInterlude.pack", TextureAtlas.class);
        addAsset("sounds/select.mp3", Sound.class);
        addAsset("sounds/titleMusic.mp3", Sound.class);
        addAsset("textures/titleScreen.png", Texture.class);
    }

    @Override
    protected void initialize() {
        Game.setUiSkin(assetManager.get("ui/uiInterlude/uiInterlude.pack", TextureAtlas.class));


        introTable = new IntroTable(((Game) game).getFont("main", Game.FontSize.SMALL), Game.getUISkin(), new PlayListener());

        game.hud.getMainTable().setBackground(new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/titleScreen.png", Texture.class))));
        game.hud.getMainTable().add(introTable);

        music = assetManager.get("sounds/titleMusic.mp3", Sound.class);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        System.gc();
        music.loop();
    }

    @Override
    public void render() {
        super.render();

    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    protected void dispose() {
        super.dispose();
    }

    @Override
    protected void clear() {
        super.clear();
    }
}
