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
import com.henrik.advergame.hud.TitleHUD;
import com.henrik.advergame.hud.tables.TitleDataTable;
import com.henrik.gdxFramework.core.GameBase;
import com.henrik.gdxFramework.core.GameState;
import com.henrik.gdxFramework.core.HUD;

/**
 * Created by Henri on 11/02/2015.
 */
public class TitleScreen extends GameState {

    private TitleHUD titleHUD;
    private TitleDataTable dataTable;

    private Sound music;

    class ContinueListener extends ClickListener
    {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
           music.stop();

            assetManager.get("sounds/select.mp3", Sound.class).play();

            if(((Game)game).getSaveGame().getLevel() == 1) {
                game.enableState(Intro.class);
                return super.touchDown(event, x, y, pointer, button);
            }

            game.getState(InGame.class).setContinueState();
            game.enableState(InGame.class);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    class LevelSelectListener extends ClickListener
    {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            music.stop();

            assetManager.get("sounds/select.mp3", Sound.class).play();

            game.enableState(LevelSelect.class);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    class CreditsListener extends ClickListener
    {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            music.stop();

            assetManager.get("sounds/select.mp3", Sound.class).play();

            game.enableState(Credits.class);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    public TitleScreen(GameBase game) {
        super(game);

        setLoadState(new LoadingScreen(assetManager, game.hud));

        addAsset("ui/uiTitle/uiTitle.pack", TextureAtlas.class);
        addAsset("textures/titleScreen.png", Texture.class);

        addAsset("sounds/select.mp3", Sound.class);
        addAsset("sounds/titleMusic.mp3", Sound.class);
    }

    @Override
    protected void initialize() {

        Game.setUiSkin(assetManager.get("ui/uiTitle/uiTitle.pack", TextureAtlas.class));

        boolean firstTimePlay = ((Game)game).getSaveGame().getLevel() == 1;

        dataTable = new TitleDataTable(((Game) game).getFont("main", Game.FontSize.SMALL), ((Game) game).getSaveGame(), Game.getUISkin());

        titleHUD = new TitleHUD(((Game) game).getFont("main", Game.FontSize.SMALL), new ContinueListener(), new LevelSelectListener(), new CreditsListener(), firstTimePlay);
        titleHUD.setWidth(HUD.WIDTH/3);

        game.hud.addActor(dataTable);

        game.hud.getMainTable().left().padLeft(HUD.WIDTH/9);
        game.hud.getMainTable().add(titleHUD).padBottom(30).row();
        game.hud.getMainTable().add(dataTable);
        game.hud.getMainTable().setBackground(new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/titleScreen.png", Texture.class))));

        music = assetManager.get("sounds/titleMusic.mp3", Sound.class);

        // Ugly, but no way to know if the sound is ready...
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        music.loop();
        System.gc();
    }

    @Override
    public void render() {
        super.render();

        if(!isLoading()) {

        }
    }

    @Override
    public void update() {
        if(!isLoading()) {

        }
        super.update();
    }

    @Override
    protected void dispose() {
        super.dispose();
        clear();
    }

    @Override
    protected void clear() {
        super.clear();
    }
}
