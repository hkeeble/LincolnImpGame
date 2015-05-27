package com.henrik.advergame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.henrik.advergame.Game;
import com.henrik.advergame.cameras.GameCameraController;
import com.henrik.advergame.entities.DecalObject;
import com.henrik.advergame.hud.LevelSelectHUD;
import com.henrik.gdxFramework.core.GameBase;
import com.henrik.gdxFramework.core.GameState;
import com.henrik.gdxFramework.core.HUD;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;

/**
 * Created by Henri on 04/03/2015.
 */
public class LevelSelect extends GameState {

    private LevelSelectHUD levelSelectHUD;;

    private Sound music;

    class GoListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            music.stop();

            assetManager.get("sounds/select.mp3", Sound.class).play();

            game.getState(InGame.class).setLevelState(levelSelectHUD.getCurrentlySelected()+1);
            game.enableState(InGame.class);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    class BackListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            music.stop();

            assetManager.get("sounds/select.mp3", Sound.class).play();

            game.enableState(TitleScreen.class);
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    class SelectListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            // Determine the level that has been selected from the string in the actor label
            Table table = (Table)event.getListenerActor();
            Array<Cell> cells = table.getCells();
            Label label = (Label)cells.get(0).getActor();

            char[] levelArray = new char[label.getText().length-6];
            label.getText().getChars(6, label.getText().length, levelArray, 0);

            String str = "";
            for(int i = 0; i < levelArray.length; i++) {
                str += levelArray[i];
            }

            if( ((Game)game).getSaveGame().getLevel() >= Integer.parseInt(str)) {
                levelSelectHUD.setSelected(Integer.parseInt(str)-1);
                assetManager.get("sounds/select.mp3", Sound.class).play();
            }

            return super.touchDown(event, x, y, pointer, button);
        }
    }

    public LevelSelect(GameBase game) {
        super(game);

        setLoadState(new LoadingScreen(assetManager, game.hud));

        addAsset("textures/sky.png", Texture.class);
        addAsset("textures/cathedralTower.png", Texture.class);
        addAsset("ui/uiLevelSelect/uiLevelSelect.pack", TextureAtlas.class);

        addAsset("sounds/select.mp3", Sound.class);
        addAsset("sounds/titleMusic.mp3", Sound.class);
    }

    @Override
    protected void initialize() {

        Game.setUiSkin(assetManager.get("ui/uiLevelSelect/uiLevelSelect.pack", TextureAtlas.class));

        // Create the level select scroll pane
        levelSelectHUD = new LevelSelectHUD(((Game)game), new SelectListener(), new BackListener(), new GoListener());
        levelSelectHUD.setFillParent(true);
        game.hud.getMainTable().add(levelSelectHUD);

        game.hud.getMainTable().setBackground(new TextureRegionDrawable(new TextureRegion(assetManager.get("textures/sky.png", Texture.class))));

        music = assetManager.get("sounds/titleMusic.mp3", Sound.class);
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
        clear();
    }

    @Override
    protected void clear() {
        super.clear();
    }
}
