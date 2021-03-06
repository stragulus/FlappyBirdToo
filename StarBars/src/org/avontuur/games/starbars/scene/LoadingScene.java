package org.avontuur.games.starbars.scene;

import org.avontuur.games.starbars.Constants;
import org.avontuur.games.starbars.base.BaseScene;
import org.avontuur.games.starbars.manager.SceneManager.SceneType;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

public class LoadingScene extends BaseScene
{
    @Override
    public void createScene()
    {
        setBackground(new Background(Color.WHITE));
        attachChild(new Text(Constants.CAMERA_WIDTH/2, Constants.CAMERA_HEIGHT/2, resourcesManager.font, "Loading...", vbom));
    }

    @Override
    public void onBackKeyPressed()
    {
        return;
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene()
    {

    }
}