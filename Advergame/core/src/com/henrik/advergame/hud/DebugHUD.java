package com.henrik.advergame.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
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

        // Create HUD labels
        fpsLabel             = HUD.makeLabel(0, 0, Color.RED, "FPS: ", font);
        glCallLabel          = HUD.makeLabel(0, 0, Color.RED, "GL Calls: ", font);
        glDrawCallLabel      = HUD.makeLabel(0, 0, Color.RED, "GL_Draw Calls: ", font);
        glShaderSwitchLabel  = HUD.makeLabel(0, 0, Color.RED, "GL Shader Switches: ", font);
        glTextureBindLabel   = HUD.makeLabel(0, 0, Color.RED, "GL_Texture Binds: ", font);
        glVertexCountLabel   = HUD.makeLabel(0, 0, Color.RED, "GL Vertex Count: ", font);

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
        glProfileTable.add(fpsLabel).row().align(Align.left);
        glProfileTable.getCell(fpsLabel).align(Align.left);
        glProfileTable.add(glCallLabel).row().align(Align.left);
        glProfileTable.add(glDrawCallLabel).row().align(Align.left);
        glProfileTable.add(glShaderSwitchLabel).row().align(Align.left);
        glProfileTable.add(glTextureBindLabel).row().align(Align.left);
        glProfileTable.add(glVertexCountLabel).row().align(Align.left);
        glProfileTable.pack();

        add(glProfileTable).row().align(Align.center).padBottom(10f);
        add(enableGLButton).row().align(Align.center).padBottom(10f);
        add(enableCollisionDebugButton).row().align(Align.center).padBottom(10f);
        add(enableNavmeshButton).row().align(Align.center).padBottom(10f);
        add(regenerateButton).row().align(Align.center).padBottom(10f);
        add(debugCamButton).row().align(Align.center).padBottom(10f);
        add(pauseButton).row().align(Align.center).padBottom(10f);
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
