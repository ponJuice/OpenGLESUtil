package jp.ac.dendai.c.jtp.Game.Motion;

import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;

/**
 * Created by wark on 2016/07/09.
 */
public class RelativeLinerMove extends Action{
    private Vector2 target;
    private Vector2 velocity;

    public RelativeLinerMove(float x, float y, int effectTime, int startTime){
        target = new Vector2(x,y);
        this.startTime = startTime;
        this.endTime = startTime + effectTime;
        velocity = new Vector2();
    }

    @Override
    public void init(Vector2 obj) {
        velocity.setX((target.getX() - obj.getX())/(endTime - startTime));
        velocity.setY((target.getY() - obj.getY())/(endTime - startTime));
    }

    @Override
    public void action(Vector2 obj){
        obj.add(velocity);
    }
}
