package com.henrik.gdxFramework.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.henrik.advergame.Game;

import java.io.File;

public class DesktopLauncher {
	public static void main (String[] arg) {

        File[] uiInGame = new File("./ui/uiInGame/").listFiles();
        File[] uiInterlude = new File("./ui/uiInterlude/").listFiles();
        File[] uiLevelSelect = new File("./ui/uiLevelSelect/").listFiles();
        File[] uiTitle = new File("./ui/uiTitle/").listFiles();

        if(uiInGame!=null) {
            for(File f : uiInGame) {
                f.delete();
            }
        }

        if(uiInterlude!=null) {
            for(File f : uiInterlude) {
                f.delete();
            }
        }

        if(uiLevelSelect!=null) {
            for(File f : uiLevelSelect) {
                f.delete();
            }
        }

        if(uiTitle!=null) {
            for(File f : uiTitle) {
                f.delete();
            }
        }

        // Run the texture packer to create the UI sprite texture atlas
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.atlasExtension = ".pack";

        TexturePacker.process(settings, "../../images/uiInGame", "./ui/uiInGame", "uiInGame");
        TexturePacker.process(settings, "../../images/uiInterlude", "./ui/uiInterlude", "uiInterlude");
        TexturePacker.process(settings, "../../images/uiLevelSelect", "./ui/uiLevelSelect", "uiLevelSelect");
        TexturePacker.process(settings, "../../images/uiTitle", "./ui/uiTitle", "uiTitle");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Lincoln Imp: A Smashing Adventure";
		new LwjglApplication(new Game(), config);
	}
}
