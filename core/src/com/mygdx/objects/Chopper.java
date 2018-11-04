package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;


public class Chopper extends AbstractGameObject
{

	private float length; 
	private TextureRegion chopper; 
	private Array<Helicopter> choppers;
	private Array<RevoluteJoint> joints; 
	
	
	private class Helicopter extends AbstractGameObject
	{
		private TextureRegion helicopter; 
			
		
		
		@Override
		public void render(SpriteBatch batch) 
		{
			TextureRegion reg = helicopter;
    		batch.draw(reg.getTexture(), position.x + origin.x,
    				position.y + origin.y, origin.x, origin.y, dimension.x,
    				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
    				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
    				false, false);
			
		}



		@Override
		public void createBody(Vector2 position) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	

	
	public Chopper(float width) 
	{
		this.length=width; 
		 
		
	}

	public void intit() 
	{
		dimension.set(3.0f, 2.0f);
		chopper= Assets.instance.leveldecoration.chopper;
		int distFac = 10;
    	int numChoppers = (int)(length / distFac);
		choppers = new Array<Helicopter>(numChoppers);
		joints = new Array<RevoluteJoint>(numChoppers); 
		for(int i=0; i<numChoppers; i++)
		{
			
			Helicopter cop = spawnCoppter(); 
			cop.position.x = (i+2)*distFac;
			cop.body.setUserData(cop);
			choppers.add(cop);
			
		}
	}
	
	

	private Helicopter spawnCoppter()//trying to create rope swing with helicopter 
	{
		Helicopter cop = new Helicopter(); 
		cop.dimension.set(dimension); 
		cop.helicopter=chopper;
		Vector2 pos = new Vector2(); 
		pos.x= length+10; 
		pos.y+=2.5f;
		pos.y+= MathUtils.random(0.0f, 0.2f)*(MathUtils.randomBoolean()?1:-1); 
		cop.position.set(pos); 
		
		BodyDef bodyDef1 = new BodyDef();
		bodyDef1.type = BodyType.KinematicBody;
		bodyDef1.position.set(cop.position);
		FixtureDef fixtureDef1 = new FixtureDef();
		PolygonShape polygonShape1 = new PolygonShape();
		polygonShape1.setAsBox(cop.dimension.x/2f,cop.dimension.y/2f);
		fixtureDef1.shape=polygonShape1; 
		Body chainBase= WorldController.b2world.createBody(bodyDef1); 
		chainBase.createFixture(fixtureDef1);
		chainBase.setUserData(cop);
		cop.body=chainBase;
		
	
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(bodyDef1.position); 
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density=1;
		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(new Vector2(0f, -1f), new Vector2(0f, 1f));
		fixtureDef.shape=edgeShape;
		Body link = WorldController.b2world.createBody(bodyDef); 
		link.createFixture(fixtureDef);
		link.setAngularVelocity(1.5f);
		RevoluteJointDef revoluteJointDef = new RevoluteJointDef(); 
		revoluteJointDef.collideConnected=false; 
		revoluteJointDef.bodyA=link; 
		revoluteJointDef.bodyB=chainBase; 
		revoluteJointDef.localAnchorA.set(0, 1); 
		revoluteJointDef.localAnchorB.set(0f, -.5f); 
	    WorldController.b2world.createJoint(revoluteJointDef); 
		
		return cop;
	}
	

	@Override
	public void render(SpriteBatch batch)
	{
		for (Helicopter cop: choppers)
		{
			cop.render(batch);
		}
		
	}

	@Override
	public void createBody(Vector2 position) {
		// TODO Auto-generated method stub
		
	}

}
