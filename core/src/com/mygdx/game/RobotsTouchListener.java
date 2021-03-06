package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by john on 2/24/18.
 */

class RobotsTouchListener extends InputAdapter {
    private RobotsMainScreen game;

    private long        lastTouchTime = 0;

    private final long  TapDelay = 250;
    private final long  bufferTime = 50;
    private final float DeadZone;

    private Vector2 touchPoint;
    private Vector2 dragPoint;

    private boolean dragged = false;

    RobotsTouchListener(RobotsMainScreen gameReference) {
        game = gameReference;

        touchPoint = new Vector2();
        dragPoint = new Vector2();
        DeadZone = game.squareSize * 2;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer > 1 || TimeUtils.timeSinceMillis(lastTouchTime) <= bufferTime) return false;

        if(TimeUtils.timeSinceMillis(lastTouchTime) <= TapDelay) {
            game.input(RobotsMainScreen.InputTypes.Teleport);
        }

        lastTouchTime = TimeUtils.millis();
        touchPoint.set(screenX, screenY);

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pointer > 1) return false;

        dragged = true;
        dragPoint.set(screenX, screenY);

        if(touchPoint.dst(dragPoint) > DeadZone) {
            Vector2 diff = dragPoint.sub(touchPoint);

            float angle = diff.angle();

            if(angle > 337.5f || angle < 22.5f)
                game.wouldInput(RobotsMainScreen.InputTypes.Right);
            else if(angle > 22.5f && angle < 67.5f)
                    game.wouldInput((RobotsMainScreen.InputTypes.DownRight));
            else if(angle > 67.5f && angle < 112.5f)
                game.wouldInput((RobotsMainScreen.InputTypes.Down));
            else if(angle > 112.5f && angle < 157.5)
                game.wouldInput((RobotsMainScreen.InputTypes.DownLeft));
            else if(angle > 157.5f && angle < 202.5)
                game.wouldInput((RobotsMainScreen.InputTypes.Left));
            else if(angle > 202.5f && angle < 247.5)
                game.wouldInput((RobotsMainScreen.InputTypes.UpLeft));
            else if(angle > 247.5f && angle < 292.5)
                game.wouldInput((RobotsMainScreen.InputTypes.Up));
            else if(angle > 292.5f && angle < 337.5)
                game.wouldInput((RobotsMainScreen.InputTypes.UpRight));
        } else {
            game.wouldInput(RobotsMainScreen.InputTypes.None);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer > 1 || TimeUtils.timeSinceMillis(lastTouchTime) <= bufferTime) return false;

        if(dragged && touchPoint.dst(dragPoint) > DeadZone) {
            game.input(RobotsMainScreen.InputTypes.Move);
        }

        game.wouldInput(RobotsMainScreen.InputTypes.None);

        dragged = false;
        return false;
    }
}
