package com.henrik.advergame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
            assetManager.get("sounds/select.wav", Sound.class).play();

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
            assetManager.get("sounds/select.wav", Sound.class).play();

            music.stop();

            if(msgTable.isDone()) {
                game.enableState(Game.State.TITLE_SCREEN);
            }

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    public EndGame(GameBase game) {
        super(game);

        setLoadState(new LoadingScreen(assetManager, game.hud));

        addAsset("ui/ui.pack", TextureAtlas.class);
        addAsset("sounds/select.wav", Sound.class);
        music = Gdx.audio.newSound(Gdx.files.internal("sounds/titleMusic.mp3"));
    }

    @Override
    protected void initialize() {

        endScreen1 = Game.getUISkin().getDrawable("endScreen1");
        endScreen2 = Game.getUISkin().getDrawable("endScreen2");

        msgTable = new EndGameMessageTable(Game.getFont("main", 3), Game.getUISkin(), new AdvanceListener());
        finalTable = new EndGameFinalTable(Game.getFont("main", 5), Game.getFont("main", 4), Game.getUISkin(), ((Game)game).getSaveGame(), new ExitListener());

        game.hud.getMainTable().setBackground(endScreen1);
        game.hud.getMainTable().add(msgTable).padTop(400f);

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
