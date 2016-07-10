package jp.ac.dendai.c.jtp.Game.Motion;

import jp.ac.dendai.c.jtp.UIs.Math.Vector2;

/**
 * Created by wark on 2016/07/10.
 */
public class AbsoluteLinerMove extends Action {
    private Vector2 velocity;
    private Vector2 position;
    private int counter = 0;

    public AbsoluteLinerMove(float sx, float sy,float ex,float ey, int time, int startTime){
        velocity = new Vector2((ex - sx) / (float)time,(ey - sy) / (float)time);
        position = new Vector2(sx,sy);
        this.startTime = startTime;
        this.endTime = startTime + time;
    }

    @Override
    public void action(Vector2 obj) {
        obj.copy(velocity);
        obj.scalarMult(counter);
        obj.add(position);
        counter++;
    }
}
