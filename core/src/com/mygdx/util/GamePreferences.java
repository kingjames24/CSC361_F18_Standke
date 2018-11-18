package com.mygdx.util;

import java.nio.charset.Charset;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

/**
 * Stores all the values for the settings like music, and
 * sound, volume etc...
 * 
 * @author adam
 *
 */
public class GamePreferences 
{
	 public static final GamePreferences instance = new GamePreferences();
	 private Preferences prefs;
	 public boolean sound;
     public boolean music;
     public String login; 
     public float volSound;
     public float volMusic;
     
     // Singleton: prevent instantiation from other classes
     private GamePreferences () 
     {
         prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
         
     }
     /**
 	 * Method that loads in default values if none are 
 	 * chosen by the player. Sound values are also clamped
 	 * making sure the value is between 0-1.
 	 */
     public void load () 
     { 
    	//random string generator to be used if player did not enter a name
    	byte[] array = new byte[7];
    	new Random().nextBytes(array);
    	String generatedString = new String(array, Charset.forName("UTF-8"));
    	
    	sound = prefs.getBoolean("sound", true);
 		music = prefs.getBoolean("music", true);
 		login = prefs.getString("login", generatedString); 
 		
 		volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f),
 			0.0f, 1.0f);
 		
 		volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f),
 			0.0f, 1.0f);
    	 
     }
     /**
 	 * Method takes the default values set above and saves them
 	 * into LibGDX's preferences file.
 	 */
     public void save () 
     { 
    	prefs.putBoolean("sound", sound);
 		prefs.putBoolean("music", music);
 		prefs.putFloat("volSound", volSound);
 		prefs.putFloat("volMusic", volMusic);
 		prefs.putString("login", login); 
 		prefs.flush();
     }
	 
}
