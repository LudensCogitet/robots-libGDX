package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by john on 2/25/18.
 */

public abstract class RobotsEntity {
    public enum EntityType {
        None(-1),
        Player(0),
        Scrap(2),
        Robot(3);

        public int sprite;

        EntityType(int spriteIndex) {
            sprite = spriteIndex;
        }
    }

    protected final RobotsMainScreen gameMain;
    public EntityType type;
    public boolean dead = false;
    public Vector2 pos;
    public Vector2 lastPos;

    RobotsEntity(RobotsMainScreen gameReference, EntityType entityType, int posX, int posY) {
        gameMain = gameReference;
        type = entityType;
        pos = new Vector2(posX, posY);
        lastPos = new Vector2();
    }

    public void move(Vector2 newPos) {
        lastPos.set(pos);
        pos.set(newPos);
    }

    abstract void act();
    abstract void resolve(RobotsEntity other);
}
