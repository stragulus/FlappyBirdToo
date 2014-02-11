package org.avontuur.games.starbars.scene;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import org.avontuur.games.starbars.Player;
import org.avontuur.games.starbars.base.BaseScene;
import org.avontuur.games.starbars.manager.SceneManager;
import org.avontuur.games.starbars.manager.SceneManager.SceneType;

import com.badlogic.gdx.math.Vector2;

public class GameScene extends BaseScene implements IOnSceneTouchListener 
{
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";

	private PhysicsWorld physicsWorld;
    
	private Player player;

	@Override
	public void createScene()
	{
		setBackground(new Background(Color.BLUE));
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false); 
	    registerUpdateHandler(physicsWorld);
	    setOnSceneTouchListener(this);
	    
	    player = new Player(400, 240, vbom, camera, physicsWorld)
	    {
	        @Override
	        public void onDie()
	        {
	            // TODO Latter we will handle it.
	        }
	    };
	    player.setCullingEnabled(true);
	}
	
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{
	    if (pSceneTouchEvent.isActionDown())
	    {
	    	player.jump();
	    }
	    return false;
	}

	@Override
    public void onBackKeyPressed()
    {
        
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
    	camera.setChaseEntity(null);
    }

}
