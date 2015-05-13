package com.henrik.advergame.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.henrik.advergame.Game;
import com.henrik.gdxFramework.core.HUD;


/**
 * Created by Henri on 04/03/2015.
 */
public class TitleHUD extends Table {

    private Button continueButton, levelSelectButton, creditsButton;

    public TitleHUD(BitmapFont font, EventListener contiueListener, EventListener levelSelectListener, EventListener creditsListener, boolean firstLevel)
    {
        String continueText = "";
        if(firstLevel)
            continueText = "Start";
        else
            continueText = "Continue";

        continueButton = HUD.makeTextButton(0, 0, continueText, contiueListener, Game.getUISkin().getDrawable("debugButton"), Game.getUISkin().getDrawable("debugButton"), Game.getUISkin().getDrawable("debugButton"), font);

        // If this is the first level, use grey out and a blank event listener
        if(firstLevel)
            levelSelectButton = HUD.makeTextButton(0, 0, "Level Select", new ClickListener(), Game.getUISkin().getDrawable("debugButtonGreyOut"), Game.getUISkin().getDrawable("debugButtonGreyOut"), Game.getUISkin().getDrawable("debugButtonGreyOut"), font);
        else
            levelSelectButton = HUD.makeTextButton(0, 0, "Level Select", levelSelectListener, Game.getUISkin().getDrawable("debugButton"), Game.getUISkin().getDrawable("debugButton"), Game.getUISkin().getDrawable("debugButton"), font);

        creditsButton = HUD.makeTextButton(0, 0, "Credits", creditsListener,  Game.getUISkin().getDrawable("debugButton"), Game.getUISkin().getDrawable("debugButtonPressed"), Game.getUISkin().getDrawable("debugButton"), font);

        align(Align.center);
        add(continueButton).width((float)HUD.WIDTH/2.5f).height(HUD.HEIGHT/9).padBottom(30).padTop(150);
        row();
        add(levelSelectButton).width((float)HUD.WIDTH/2.5f).height(HUD.HEIGHT/9).padBottom(30).align(Align.center).row();
        row();
        add(creditsButton).width((float)HUD.WIDTH/2.5f).height(HUD.HEIGHT/9).align(Align.center).row();
    }
}
