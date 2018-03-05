package com.mygdx.game;

/**
 * Created by john on 2/25/18.
 */

public class Player extends RobotsEntity {

    Player(RobotsGame gameReference, int posX, int posY) {
        super(gameReference, EntityType.Player, posX, posY);
    }

    @Override
    public void act() {}

    @Override
    public void resolve(RobotsEntity other) {}
}
