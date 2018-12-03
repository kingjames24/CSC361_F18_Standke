package com.mygdx.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Game;
/**
 * Class that manages audio sounds and music during the game
 * @author adam standke
 *
 */
public class AudioManager 
{
	//creates a new instance of AudioManger when class is loaded(ie Singleton pattern)
	public static final AudioManager instance = new AudioManager();
	
	public Game game; 
    private Music playingMusic;
    
    
    // singleton: prevent instantiation from other classes
    private AudioManager () { }
    
    public void init(Game game)
    {
    	this.game=game; 
    }
    
    /**
     * Method that plays a sound effect with a default volume of one
     * @param sound a Sound object that stores loads audio files into memory
     */
    public void play (Sound sound) 
    {
      play(sound, 1);
    }
    /**
     * Method that plays a sound effect with a default pitch value and 
     * variable volume
     * @param sound a Sound object that stores and loads audio files into memory
     * @param volume a float that represents the volume of the sound 0 being min 1 being max
     */
    public void play (Sound sound, float volume) 
    {
      play(sound, volume, 1);
    }
    /**
     * Method that plays a sound effect with a default pan value along with
     * variable volume and pitch
     * @param sound a Sound object that stores and loads audio files into memory
     * @param volume a float that represents the volume of the sound 0 being min 1 being max
     * @param pitch a float that represents the speed of a sound 1 being normal/>1 faster
     */
    public void play (Sound sound, float volume, float pitch) 
    {
      play(sound, volume, pitch, 0);
    }
    
    /**
     * Method that plays a sound effect so long as the game preferences allow for it
     * @param sound a Sound object that stores and loads audio files into memory
     * @param volume a float that represents the volume of the sound 0 being min 1 being max
     * @param pitch a float that represents the speed of a sound 1 being normal/>1 being faster
     * @param pan a float that represents in what amount the sound will be output for the left/right speaker
     */
    public void play (Sound sound, float volume, float pitch,float pan) 
    {
      if (!GamePreferences.instance.sound) return;
      sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
    } 
    
    /**
     * Method that is called to play the game's background music 
     * @param music a Music object that streams part of a decoded music snippet from memory 
     */
    public void play (Music music) 
    {
        stopMusic();
        playingMusic = music;
        if (GamePreferences.instance.music) 
        {
          music.setLooping(true);
          music.setVolume(GamePreferences.instance.volMusic);
          music.play();
        }
    }
    
    /**
     * Method that stops the playing of background music
     */
    public void stopMusic () 
    {
       if (playingMusic != null) playingMusic.stop();
    }
    
    /**
     * Method that is called when the user has changed the 
     * music preferences in the option window
     */
    public void onSettingsUpdated () 
    {
        if (playingMusic == null) return;
        playingMusic.setVolume(GamePreferences.instance.volMusic);
        if (GamePreferences.instance.music) 
        {
          if (!playingMusic.isPlaying()) playingMusic.play();
        } 
        else 
        {
          playingMusic.pause();
        }
    }

}
