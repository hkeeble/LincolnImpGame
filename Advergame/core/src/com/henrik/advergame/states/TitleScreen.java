package com.henrik.advergame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

            assetManager.get("sounds/select.wav", Sound.class).play();

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

            assetManager.get("sounds/select.wav", Sound.class).play();

            game.enableState(LevelSelect.class);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    class CreditsListener extends ClickListener
    {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            music.stop();

            assetManager.get("sounds/select.wav", Sound.class).play();

            game.enableState(Credits.class);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    public TitleScreen(GameBase game) {
        super(game);

        setLoadState(new LoadingScreen(assetManager, game.hud));

        addAsset("ui/ui.pack", TextureAtlas.class);

        addAsset("sounds/select.wav", Sound.class);

        music = Gdx.audio.newSound(Gdx.files.internal("sounds/titleMusic.mp3"));
    }

    @Override
    protected void initialize() {

        boolean firstTimePlay = ((Game)game).getSaveGame().getLevel() == 1;

        dataTable = new TitleDataTable(Game.getFont("main", 2), ((Game) game).getSaveGame(), Game.getUISkin());

        titleHUD = new TitleHUD(Game.getFont("main", 3), new ContinueListener(), new LevelSelectListener(), new CreditsListener(), firstTimePlay);
        titleHUD.setWidth(HUD.WIDTH/3);

        game.hud.addActor(dataTable);

        game.hud.getMainTable().left().padLeft(HUD.WIDTH/9);
        game.hud.getMainTable().add(titleHUD).padBottom(30).row();
        game.hud.getMainTable().add(dataTable);
        game.hud.getMainTable().setBackground(Game.getUISkin().getDrawable("titleScreen"));

        music.loop();
    }

    @Override
    public void render() {
        super.render();

        if(!isLoading()) {

        }
    }

    @Override
    public void update() {
        super.update();
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
