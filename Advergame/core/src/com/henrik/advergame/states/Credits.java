package com.henrik.advergame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
            assetManager.get("sounds/select.mp3", Sound.class).play();

            music.stop();

            game.enableState(TitleScreen.class);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private CreditsTable creditsTable;

    public Credits(GameBase game) {
        super(game);

        setLoadState(new LoadingScreen(assetManager, game.hud));

        addAsset("ui/uiTitle/uiTitle.pack", TextureAtlas.class);
        addAsset("sounds/select.mp3", Sound.class);
        addAsset("textures/titleScreen.png", Texture.class);
    }

    @Override
    protected void initialize() {

        Game.setUiSkin(assetManager.get("ui/uiTitle/uiTitle.pack", TextureAtlas.class));

        creditsTable = new CreditsTable(((Game) game).getFont("main", Game.FontSize.SMALL), ((Game) game).getFont("main", Game.FontSize.SMALL), Game.getUISkin(), new ExitListener());
        game.hud.getMainTable().setBackground(new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/titleScreen.png", Texture.class))));
        game.hud.getMainTable().add(creditsTable).center();


        music = Gdx.audio.newSound(Gdx.files.internal("sounds/titleMusic.mp3"));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
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
