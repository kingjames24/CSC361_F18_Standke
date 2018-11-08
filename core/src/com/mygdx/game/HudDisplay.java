package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.util.Constants;

public class HudDisplay implements Disposable 
{
	public Stage stage; 
	private Viewport viewport;
	private Skin hudSkin; 
	
	private ProgressBar healthBar;
	private Label healthScore; 
	
	public HudDisplay(SpriteBatch batch)
	{
		viewport = new FitViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, new OrthographicCamera()); 
		stage = new Stage(viewport, batch);
		buildHudSkin();
		Table progressBar = buildPrograssBar();
		stage.addActor(progressBar);
	}
	
	private Table buildPrograssBar() 
	{
		Table bar = new Table();
		bar.top().left(); 
		bar.setFillParent(true);
		bar.add(new Label("Health:", hudSkin, "font", Color.GRAY));
		healthBar= new ProgressBar(0f, 100f, 10f, false, hudSkin); 
		bar.add(healthBar);
		return bar;
	}

	private void buildHudSkin() 
	{
		hudSkin = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
			     new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
		
	}

	public void update(float deltaTime)
	{
		
	}
	

	@Override
	public void dispose() 
	{
		stage.dispose();
		
	}

}
