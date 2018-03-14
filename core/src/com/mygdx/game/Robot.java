package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by john on 2/25/18.
 */

public class Robot extends RobotsEntity {
    Robot(RobotsMainScreen gameReference, int posX, int posY) {
        super(gameReference, EntityType.Robot, posX, posY);
    }

    @Override
    public void act() {
        if(type == EntityType.Scrap) return;

        int x = (int) pos.x;
        int y = (int) pos.y;

        if(gameMain.player.pos.x < x) x--;
        if(gameMain.player.pos.x > x) x++;
        if(gameMain.player.pos.y < y) y--;
        if(gameMain.player.pos.y > y) y++;

        move(new Vector2(x, y));
    }

    @Override
    public void resolve(RobotsEntity other) {
        if(gameMain.player.pos.equals(pos)) {
            gameMain.restart();
            return;
        }

        if(other == this            ||
           type == EntityType.Scrap ||
           !pos.equals(other.pos)) return;

        if(other.type == EntityType.Robot) {
            type = EntityType.Scrap;
            gameMain.kill(this, false);
        }

        if(other.type == EntityType.Scrap) {
            gameMain.kill(this, true);
        }
    }
}
