package com.mygdx.util;

import java.nio.charset.Charset;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences 
{
	 public static final GamePreferences instance = new GamePreferences();
	 private Preferences prefs;
	 public boolean sound;
     public boolean music;
     public String login; 
     public float volSound;
     public float volMusic;
     
     private GamePreferences () 
     {
         prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
         
     }
     
     public void load () 
     { 
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
