package com.mygdx.game.desktop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.mygdx.rainmaker.RainMaker;



public class DesktopLauncher {
	
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;
	
	public static void main (String[] arg) {
		
		//if needed rebuild the image atlas
				if(rebuildAtlas)
				{
					Settings settings = new Settings();
					settings.maxWidth = 1024;
					settings.maxHeight = 1024;
					settings.duplicatePadding = false;
					settings.debug = drawDebugOutline;
					TexturePacker.process(settings, "assets-raw/images", "../core/assets/images",
							"rainmaker.atlas");
					/*TexturePacker.process(settings,  "assets-raw/images-ui", "../core/assets/images-ui",
							"canyonbunny-ui.atlas");*/
				}
				
				LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
				cfg.title = "RainMaker";
				cfg.width = 800;
				cfg.height= 480;
				
				new LwjglApplication(new RainMaker(), cfg);
		
	}
}
