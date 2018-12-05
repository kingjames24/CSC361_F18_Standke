package com.mygdx.util;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Class that saves and load the high-score file of 
 * the player's names and scores so far
 * @author adam
 *
 */
public class HighScoreList 
{
	public static final HighScoreList instance = new HighScoreList();
	private FileHandle score; 
	public String login; 
	public static ArrayList<String> topTenPlayers; 
	public static int scoreGame;
	
	/**
	 * Private constructor that creates an arraylist that contains lines of
	 * the deconstructed file
	 */
	private HighScoreList()
	{
		topTenPlayers = new ArrayList<String>(); 
	
	}
	
	/**
	 * Method that that creates a filehander called score to 
	 * read in the file and output it as a text file. After doing so, 
	 * each line is added to the topTenPlayer array 
	 */
	public void load()
	{
		
			score= Gdx.files.local("score/hi.txt"); 
			String file = score.readString();
			String[] s = file.split("\n");
			for(int i=0; i<s.length; i++)
			{
				String p = s[i]; 
				topTenPlayers.add(p); 
			}
			
		
	}
	
	/**
	 * Method that save's players to the current high-score file
	 * @param name a String representing the player's login name
	 * @param sc a int represneting the player's score
	 */
	public void save(String name, int sc)
	{
		
		score.writeString(name+":"+sc+"\n", true);
		
	}
	
	/**
	 * Helper method to store the player's score from his/her previous play
	 * @param score an int representing the player's previous score of playing the game
	 * @return an int representing the player's score
	 */
	public int getScore(int score)
	{
		return this.scoreGame= score;  
	}
}
