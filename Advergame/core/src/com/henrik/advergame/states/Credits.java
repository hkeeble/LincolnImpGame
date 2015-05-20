package com.henrik.advergame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.henrik.advergame.Game;
import com.henrik.advergame.hud.tables.CreditsTable;
import com.henrik.gdxFramework.core.GameBase;
import com.henrik.gdxFramework.core.GameState;

/**
 * Created by Henri on 09/04/2015.
 */
public class Credits extends GameState {

    private Sound music;

    class ExitListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            assetManager.get("sounds/select.wav", Sound.class).play();

            music.stop();

            game.enableState(TitleScreen.class);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private CreditsTable creditsTable;

    public Credits(GameBase game) {
        super(game);

        setLoadState(new LoadingScreen(assetManager, game.hud));

        addAsset("ui/ui.pack", TextureAtlas.class);
        addAsset("sounds/select.wav", Sound.class);
        music = Gdx.audio.newSound(Gdx.files.internal("sounds/titleMusic.mp3"));
    }

    @Override
    protected void initialize() {

        creditsTable = new CreditsTable(game.getFont("main", 3), game.getFont("main", 2), Game.getUISkin(), new ExitListener());
        game.hud.getMainTable().setBackground(Game.getUISkin().getDrawable("titleScreen"));
        game.hud.getMainTable().add(creditsTable).center();

        music.loop();
    }

    @Override
    public void update() {
        super.update();

        if(!isLoading()) {

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
        music.dispose();
        clear();
    }

    @Override
    protected void clear() {

    }

}
