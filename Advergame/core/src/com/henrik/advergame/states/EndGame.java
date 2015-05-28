package com.henrik.advergame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.henrik.advergame.Game;
import com.henrik.advergame.hud.tables.EndGameFinalTable;
import com.henrik.advergame.hud.tables.EndGameMessageTable;
import com.henrik.gdxFramework.core.GameBase;
import com.henrik.gdxFramework.core.GameState;

/**
 * Created by Henri on 10/04/2015.
 */
public class EndGame extends GameState {

    private Sound music;
    private EndGameMessageTable msgTable;
    private EndGameFinalTable finalTable;

    private Drawable endScreen1, endScreen2;

    private class AdvanceListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            assetManager.get("sounds/select.mp3", Sound.class).play();

            msgTable.advancePage();

            if(msgTable.getCurrentPage() == 2) {

                game.hud.getMainTable().getCell(msgTable).padTop(0);
                game.hud.getMainTable().getCell(msgTable).padBottom(400f);

                game.hud.getMainTable().setBackground(endScreen2);
            }

            if(msgTable.isDone()) {
                msgTable.remove();
                game.hud.getMainTable().add(finalTable).center();
            }

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private class ExitListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            assetManager.get("sounds/select.mp3", Sound.class).play();

            music.stop();

            if(msgTable.isDone()) {
                game.enableState(TitleScreen.class);
            }

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    public EndGame(GameBase game) {
        super(game);

        setLoadState(new LoadingScreen(assetManager, game.hud));

        addAsset("ui/uiInterlude/uiInterlude.pack", TextureAtlas.class);
        addAsset("sounds/select.mp3", Sound.class);
        addAsset("sounds/titleMusic.mp3", Sound.class);

        addAsset("textures/endScreen1.png", Texture.class);
        addAsset("textures/endScreen2.png", Texture.class);

    }

    @Override
    protected void initialize() {
        Game.setUiSkin(assetManager.get("ui/uiInterlude/uiInterlude.pack", TextureAtlas.class));

        endScreen1 = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/endScreen1.png", Texture.class)));
        endScreen2 = new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/endScreen2.png", Texture.class)));

        msgTable = new EndGameMessageTable(((Game) game).getFont("main", Game.FontSize.LARGE), Game.getUISkin(), new AdvanceListener());
        finalTable = new EndGameFinalTable(((Game) game).getFont("main", Game.FontSize.LARGE), ((Game) game).getFont("main", Game.FontSize.LARGE), Game.getUISkin(), ((Game)game).getSaveGame(), new ExitListener());

        game.hud.getMainTable().setBackground(endScreen1);
        game.hud.getMainTable().add(msgTable).padTop(400f);

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
