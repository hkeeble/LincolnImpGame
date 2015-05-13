package com.henrik.advergame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
            assetManager.get("sounds/select.wav", Sound.class).play();

            introTable.advancePage();

            if(introTable.isDone()) {
                music.stop();
                game.getState(Game.State.IN_GAME, InGame.class).setContinueState();
                game.enableState(Game.State.IN_GAME);
            }

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    public Intro(GameBase game) {
        super(game);

        setLoadState(new LoadingScreen(assetManager, game.hud));

        addAsset("ui/ui.pack", TextureAtlas.class);
        addAsset("sounds/select.wav", Sound.class);
        music = Gdx.audio.newSound(Gdx.files.internal("sounds/titleMusic.mp3"));
    }

    @Override
    protected void initialize() {

        introTable = new IntroTable(Game.getFont("main", 3), Game.getUISkin(), new PlayListener());

        game.hud.getMainTable().setBackground(Game.getUISkin().getDrawable("titleScreen"));
        game.hud.getMainTable().add(introTable);

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
        music.dispose();
        super.dispose();
    }

    @Override
    protected void clear() {
    }
}
