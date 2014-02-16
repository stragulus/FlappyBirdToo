package org.avontuur.games.starbars.entity;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.avontuur.games.starbars.Constants;
import org.avontuur.games.starbars.manager.ResourcesManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class Player extends Sprite
{
	
	private Body body;
	
	public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
    {
        super(pX, pY, ResourcesManager.getInstance().player_region, vbo);
        createPhysics(camera, physicsWorld);
        // for this game, do NOT have the camera chase the player! It should remain at the center of the screen.
        //camera.setChaseEntity(this);
    }
	
	public abstract void onDie();

	public void jump()
	{
	    body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 48)); 
	}
	
	public void startPlayer()
	{
		body.setType(BodyType.DynamicBody);
	}
	
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
	{        
		// start as a static body until the player initiates game play
	    body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));

	    body.setUserData("player");
	    body.setFixedRotation(true);
	    
	    physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
	    {
	        @Override
	        public void onUpdate(float pSecondsElapsed)
	        {
	            super.onUpdate(pSecondsElapsed);
	            camera.onUpdate(0.1f);
	            
	            if (getY() >= Constants.CAMERA_HEIGHT) {
	            	// you can't leave the screen!
	            	if (body.getLinearVelocity().y > 0) {
	            		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 0));
	            	}
	            }
	            if (getY() <= 0)
	            {                    
	                onDie();
	            }
	            
	        }
	    });
	}

}
