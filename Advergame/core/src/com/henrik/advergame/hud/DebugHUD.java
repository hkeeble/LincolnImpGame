package com.henrik.advergame.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.henrik.advergame.Game;
import com.henrik.gdxFramework.core.HUD;
import com.henrik.gdxFramework.core.Profiler;

/**
 * Created by Henri on 22/11/2014.
 */
public class DebugHUD extends Table {

    // HUD Buttons
    private TextButton enableGLButton, enableCollisionDebugButton, enableNavmeshButton, regenerateButton, debugCamButton, pauseButton;

    // HUD Labels
    private Label fpsLabel, glCallLabel, glDrawCallLabel, glTextureBindLabel, glShaderSwitchLabel, glVertexCountLabel;

    // HUD Tables
    private Table glProfileTable;

    public DebugHUD(BitmapFont font, EventListener regenerateEventListener, EventListener glToggleEvent, EventListener collisionToggleEvent, EventListener navmeshToggleEvent,
                    EventListener debugCamEventListener, EventListener pauseListener) {

        Label.LabelStyle style = new Label.LabelStyle(font, Color.RED);

        // Create HUD labels
        fpsLabel             = HUD.makeLabel(0, 0,"FPS: ", style);
        glCallLabel          = HUD.makeLabel(0, 0, "GL Calls: ", style);
        glDrawCallLabel      = HUD.makeLabel(0, 0, "GL_Draw Calls: ", style);
        glShaderSwitchLabel  = HUD.makeLabel(0, 0, "GL Shader Switches: ", style);
        glTextureBindLabel   = HUD.makeLabel(0, 0, "GL_Texture Binds: ", style);
        glVertexCountLabel   = HUD.makeLabel(0, 0, "GL Vertex Count: ", style);

        // Create HUD buttons
        enableGLButton = HUD.makeTextButton(0, 0, "Toggle GL Profiling", glToggleEvent, Game.getUISkin().getDrawable("debugButton"), Game.getUISkin().getDrawable("debugButton"),
        		Game.getUISkin().getDrawable("debugButton"), font);
        enableCollisionDebugButton = HUD.makeTextButton(0, 0, "Toggle Collision Debug", collisionToggleEvent, Game.getUISkin().getDrawable("debugButton"), Game.getUISkin().getDrawable("debugButton"),
        		Game.getUISkin().getDrawable("debugButton"), font);
        enableNavmeshButton = HUD.makeTextButton(0, 0, "Toggle Navmesh", navmeshToggleEvent, Game.getUISkin().getDrawable("debugButton"), Game.getUISkin().getDrawable("debugButton"),
        		Game.getUISkin().getDrawable("debugButton"), font);
        regenerateButton = HUD.makeTextButton(0, 0, "Regenerate", regenerateEventListener, Game.getUISkin().getDrawable("debugButton"), Game.getUISkin().getDrawable("debugButton"),
        		Game.getUISkin().getDrawable("debugButton"), font);
        debugCamButton = HUD.makeTextButton(0, 0, "Toggle Debug Camera", debugCamEventListener, Game.getUISkin().getDrawable("debugButton"), Game.getUISkin().getDrawable("debugButton"),
        		Game.getUISkin().getDrawable("debugButton"), font);
        pauseButton = HUD.makeTextButton(0, 0, "Pause", pauseListener, Game.getUISkin().getDrawable("debugButton"), Game.getUISkin().getDrawable("debugButton"),
        		Game.getUISkin().getDrawable("debugButton"), font);

        glProfileTable = new Table();
        glProfileTable.add(fpsLabel).align(Align.left).row();
        glProfileTable.getCell(fpsLabel).align(Align.left);
        glProfileTable.add(glCallLabel).align(Align.left).row();
        glProfileTable.add(glDrawCallLabel).align(Align.left).row();
        glProfileTable.add(glShaderSwitchLabel).align(Align.left).row();
        glProfileTable.add(glTextureBindLabel).align(Align.left).row();
        glProfileTable.add(glVertexCountLabel).align(Align.left).row();
        glProfileTable.pack();

        add(glProfileTable).align(Align.center).padBottom(10f).row();
        add(enableGLButton).align(Align.center).padBottom(10f).row();
        add(enableCollisionDebugButton).align(Align.center).padBottom(10f).row();
        add(enableNavmeshButton).align(Align.center).padBottom(10f).row();
        add(regenerateButton).align(Align.center).padBottom(10f).row();
        add(debugCamButton).align(Align.center).padBottom(10f).row();
        add(pauseButton).align(Align.center).padBottom(10f).row();
        pack();
        setPosition(HUD.WIDTH - getWidth(), HUD.HEIGHT - getHeight());
    }

    public void update(Profiler profiler) {
        fpsLabel.setText("FPS: " + String.valueOf(profiler.getFps()));
        glCallLabel.setText("GL Calls: " + String.valueOf(profiler.getGlCalls()));
        glDrawCallLabel.setText("GL Draw Calls: " + String.valueOf(profiler.getGlDrawCalls()));
        glShaderSwitchLabel.setText("GL Shader Switches: " + String.valueOf(profiler.getGlShaderSwitches()));
        glTextureBindLabel.setText("GL Texture Bindings: " + String.valueOf(profiler.getGlTextureBindings()));
        glVertexCountLabel.setText("GL Vertex Count: " + String.valueOf(profiler.getGlVertexCount()));
    }

    public Table getGLProfileTable() {
        return glProfileTable;
    }
}
