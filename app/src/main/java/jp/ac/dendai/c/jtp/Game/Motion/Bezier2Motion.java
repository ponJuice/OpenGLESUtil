package jp.ac.dendai.c.jtp.Game.Motion;

import jp.ac.dendai.c.jtp.Game.Motion.Action;
import jp.ac.dendai.c.jtp.Game.Motion.Util.Bezier;
import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;

/**
 * Created by wark on 2016/07/09.
 */
public class Bezier2Motion extends Action {
    protected Vector2 control,start,end;
    public Bezier2Motion(float startx,float starty,float endx,float endy,float c1x,float c1y,int time,int startTime){
        start = new Vector2(startx,starty);
        end = new Vector2(endx,endy);
        control = new Vector2(c1x,c1y);
        this.startTime = startTime;
        this.endTime = startTime + time;
    }

    @Override
    public void init(Vector2 obj) {
        
    }

    @Override
    public void action(Vector2 obj) {
        if(nowTime > (endTime - startTime))
            nowTime = (endTime - startTime);
        Bezier.bezier2Trajectory(obj,start,end,control,1f/(float)(endTime - startTime) * (float)nowTime);
        nowTime++;
    }
}
