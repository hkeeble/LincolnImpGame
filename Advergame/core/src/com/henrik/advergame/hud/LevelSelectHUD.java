package com.henrik.advergame.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.henrik.advergame.Game;
import com.henrik.advergame.LevelSaveData;
import com.henrik.advergame.SaveGame;
import com.henrik.advergame.level.Level;
import com.henrik.gdxFramework.core.HUD;

import java.util.ArrayList;

/**
 * Created by Henri on 04/03/2015.
 */
public class LevelSelectHUD extends Table {

    private Button goButton, backButton;
    private ScrollPane scrollPane;
    private Table levelTable;
    private ArrayList<Table> levelTables;

    private int currentlySelected;

    public int getCurrentlySelected() {
        return currentlySelected;
    }

    public LevelSelectHUD(Game game, EventListener levelHitListener, EventListener backListener, EventListener goListener) {

        // Create the current level table
        levelTable = new Table();

        SaveGame saveGame = game.getSaveGame();
        int levelCount = Game.LEVEL_COUNT;

        levelTables = new ArrayList<Table>();

        Label levelLabel;
        Label.LabelStyle redStyle = new Label.LabelStyle(((Game) game).getFont("main", Game.FontSize.SMALL), Color.RED);
        Label.LabelStyle grayStyle = new Label.LabelStyle(((Game) game).getFont("main", Game.FontSize.SMALL), Color.GRAY);

        for(int i = 0; i < levelCount; i++) {

            Table table = new Table();
            table.setBackground(Game.getUISkin().getDrawable("cathedralFloorButton"));

            if(!(i <= saveGame.getLevel()-1)) {
                levelLabel = HUD.makeLabel(0, 0, "Level " + String.valueOf(i+1), grayStyle);
            } else {
                levelLabel = HUD.makeLabel(0, 0, "Level " + String.valueOf(i+1), redStyle);
            }
            levelLabel.setAlignment(Align.center);

            table.add(levelLabel).align(Align.center).width(HUD.WIDTH/2).row();

            if(i <= saveGame.getLevel()-1) {
                LevelSaveData data = saveGame.getLevelSaveData(i+1);

                Label objLabel = HUD.makeLabel(0, 0, "Objects Found: " + String.valueOf(data.getObjectsDestroyed()) + "/" +
                        String.valueOf(Level.GetObjectCount(String.valueOf(i+1))), redStyle);

                objLabel.setAlignment(Align.center);
                if (i == saveGame.getLevel()) {
                    table.setBackground(Game.getUISkin().getDrawable("cathedralFloorButtonSelected"));
                }
                table.add(objLabel).align(Align.center).width(HUD.WIDTH/2);
            }

            table.addListener(levelHitListener);
            levelTables.add(table);
        }

        for(int i = levelTables.size()-1; i > -1; i--) {
            levelTable.add(levelTables.get(i)).width(HUD.WIDTH/2).padBottom(20);
            levelTable.row();
        }

        levelTable.setWidth(HUD.WIDTH/2);
        scrollPane = new ScrollPane(levelTable);

        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        style.background = new TextureRegionDrawable(new TextureRegion( game.getAssetManager().get("textures/cathedralTower.png", Texture.class)));

        scrollPane.setStyle(style);
        scrollPane.setScrollingDisabled(true, false);

        // Make Buttons
        goButton = HUD.makeButton(0, 0, goListener, Game.getUISkin().getDrawable("playButton"), Game.getUISkin().getDrawable("playButton"), Game.getUISkin().getDrawable("playButton"));
        backButton = HUD.makeButton(0, 0, backListener, Game.getUISkin().getDrawable("exitButton"), Game.getUISkin().getDrawable("exitButton"), Game.getUISkin().getDrawable("exitButton"));

        add(backButton).size(96).pad(10);
        add(scrollPane);
        add(goButton).size(96).pad(10);

        scrollPane.setScrollPercentY(0);

        currentlySelected = saveGame.getLevel()-1;
    }

    public void setSelected(int level) {
        levelTables.get(currentlySelected).setBackground(Game.getUISkin().getDrawable("cathedralFloorButton"));
        currentlySelected = level;
        levelTables.get(currentlySelected).setBackground(Game.getUISkin().getDrawable("cathedralFloorButtonSelected"));
    }

}
