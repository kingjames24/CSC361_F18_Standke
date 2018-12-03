package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;

public class Princess extends AbstractGameObject 
{
	private TextureRegion goal; 

	public Princess()
	{
		init(); 
	}
	
	private void init() 
	{
		dimension.set(1f, 1f); 
		goal= Assets.instance.leveldecoration.prince; 
		origin.set(0, 0);
	}

	@Override
	public void createBody(Vector2 position) 
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.fixedRotation=true;
		bodyDef.position.set(position); 
		body = WorldController.b2world.createBody(bodyDef);
		body.setUserData(this);
		
		PolygonShape polygonShape = new PolygonShape();
		origin.x = this.dimension.x/this.dimension.x; 
		origin.y = this.dimension.y/this.dimension.y; 
		polygonShape.setAsBox(this.dimension.x/2*4,this.dimension.y/2*4, origin, 0);
		FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.shape = polygonShape; 
	    Fixture f= body.createFixture(fixtureDef);
	    f.setSensor(true);
	    polygonShape.dispose();
		
		
	}

	@Override
	public void render(SpriteBatch batch) 
	{
		 TextureRegion reg = null;
	      reg = goal;  
	      batch.draw(reg.getTexture(), position.x, position.y,
	    		  origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
	    		  rotation, reg.getRegionX(), reg.getRegionY(),
	    		  reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
		
	}

	public void startContract() 
	{
		WorldController.goalReached=true; 
		
	}

}
