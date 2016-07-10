package jp.ac.dendai.c.jtp.Game.Motion;

import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;

/**
 * Created by wark on 2016/07/09.
 */
public class RelativeLinerMove extends Action{
    private Vector2 velocity;

    public RelativeLinerMove(float x, float y, int time, int startTime){
        velocity = new Vector2(x / (float)time,y / (float)time);
        this.startTime = startTime;
        this.endTime = startTime + time;
    }

    @Override
    public void action(Vector2 obj){
        obj.add(velocity);
    }
}
