package org.avontuur.games.starbars;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.avontuur.games.starbars.manager.ResourcesManager;
import org.avontuur.games.starbars.manager.SceneManager;

import android.view.KeyEvent;

public class GameActivity extends BaseGameActivity {
	private BoundCamera camera;
	private static final int CAMERA_WIDTH = Constants.CAMERA_WIDTH;
	private static final int CAMERA_HEIGHT = Constants.CAMERA_HEIGHT;

	private ResourcesManager resourcesManager;

	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO: Set camera to actual display size (given same ratio) and scale everything accordingly
		camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), this.camera);
	    engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
	    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	    return engineOptions;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new LimitedFPSEngine(pEngineOptions, 60);
	}
	
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
		ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
	    resourcesManager = ResourcesManager.getInstance();
	    pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}
	
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
    {
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback()
		{
			@Override
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                //SceneManager.getInstance().createMenuScene();
                SceneManager.getInstance().createGameScene();
            }
	    }));
	    pOnPopulateSceneCallback.onPopulateSceneFinished();

    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (this.isGameLoaded()) {
			System.exit(0);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}

}
