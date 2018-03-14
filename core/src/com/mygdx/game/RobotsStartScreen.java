package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by john on 3/13/18.
 */

public class RobotsStartScreen implements Screen {
    public enum STATE {START, GAME_OVER}

    private RobotsGame game;
    private Viewport viewport;

    private String text;
    private Vector2 textPos;

    private String subtext;
    private Vector2 subtextPos;

    RobotsStartScreen(RobotsGame gameReference, STATE state) {
        game = gameReference;

        viewport = new ScreenViewport();

        if(state == STATE.START) {
            text = "Robots!";
            subtext = "Tap anywhere to begin";
        } else if(state == STATE.GAME_OVER) {
            text = "YOU DED!";
            subtext = "Tap to restart";
        }

        GlyphLayout layout = new GlyphLayout();
        Vector2 screenCenter = new Vector2(viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2);

        layout.setText(game.font, text);
        textPos = new Vector2(screenCenter.x - (layout.width / 2), (screenCenter.y + 20) - (layout.height / 2));

        layout.setText(game.font, subtext);
        subtextPos = new Vector2(screenCenter.x - (layout.width / 2), (screenCenter.y - 20) - (layout.height / 2));
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.getCamera().update();
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        game.batch.begin();
        game.font.draw(game.batch, text, textPos.x, textPos.y);
        game.font.draw(game.batch, subtext, subtextPos.x, subtextPos.y);

        game.batch.end();

        if(Gdx.input.isTouched()) {
            game.setScreen(new RobotsMainScreen(game));
            dispose();
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}

}
