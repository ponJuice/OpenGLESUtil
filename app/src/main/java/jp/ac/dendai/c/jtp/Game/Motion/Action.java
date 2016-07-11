package jp.ac.dendai.c.jtp.Game.Motion;

import jp.ac.dendai.c.jtp.UIs.Math.Vector2;

/**
 * Created by wark on 2016/07/09.
 */
public abstract class Action {
    protected int startTime,endTime,nowTime;
    public static Action moveRelative(float x,float y,int effectTime,int startTime){
        return new RelativeLinerMove(x,y,effectTime,startTime);
    }
    public static Action movePointToPoint(float sx,float sy,float ex,float ey,int time,int startTime){
        return new AbsoluteLinerMove(sx,sy,ex,ey,time,startTime);
    }
    public static Action bezierMotion(float sx,float sy,float ex,float ey,float cx,float cy,int time,int startTime){
        return new Bezier2Motion(sx,sy,ex,ey,cx,cy,time,startTime);
    }
    public static Action bezierMotion(float sx,float sy,float ex,float ey,float c1x,float c1y,float c2x,float c2y,int time,int startTime){
        return null;
    }

    public int getStartTime(){
        return startTime;
    }
    public int getEndTime(){
        return endTime;
    }
    public int getEffctLength(){
        return endTime - startTime;
    }

    public abstract void init(Vector2 obj);
    public abstract void action(Vector2 obj);
}
