package com.henrik.gdxFramework.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.henrik.advergame.Game;

import java.io.File;

public class DesktopLauncher {
	public static void main (String[] arg) {

/*        File[] filesUI = new File("./ui/").listFiles();
        if(filesUI!=null) {
            for(File f : filesUI) {
                f.delete();
            }
        }

        // Run the texture packer to create the UI sprite texture atlas
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.atlasExtension = ".pack";
        TexturePacker.process(settings, "../../images/ui", "./ui/", "ui");*/

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Lincoln Imp: A Smashing Adventure";
		new LwjglApplication(new Game(), config);
	}
}
