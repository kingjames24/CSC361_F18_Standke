package com.mygdx.util;




import java.io.OutputStream;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


public class HighScoreList 
{
	public static final HighScoreList instance = new HighScoreList();
	private FileHandle score; 
	public String login; 
	public static ArrayList<String> topTenPlayers; 
	public static int scoreGame;
	
	private HighScoreList()
	{
		topTenPlayers = new ArrayList<String>(); 
		
	}
	
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
	
	public void save(String name, int sc)
	{
		
		score.writeString(name+":"+sc+"\n", true);
		
	}
	
	public int getScore(int score)
	{
		return this.scoreGame= score;  
	}
}
