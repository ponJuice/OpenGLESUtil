package jp.ac.dendai.c.jtp.Game.Motion.Util;

import jp.ac.dendai.c.jtp.UIs.Math.Vector2;

/**
 * Created by wark on 2016/07/09.
 */
public class Bezier {
    public static void bezier2Trajectory(Vector2 targetPosition,Vector2 start,Vector2 end,Vector2 control,float time){
        targetPosition.setX((1f-time)*(1-time)*start.getX() + 2f*(1f-time)*time*control.getX() + time*time*end.getX());
        targetPosition.setY((1f-time)*(1-time)*start.getY() + 2f*(1f-time)*time*control.getY() + time*time*end.getY());
    }
    public static void bezier3Trajectory(Vector2 targetPosition,Vector2 start,Vector2 end,Vector2 control1,Vector2 control2,float time){
        targetPosition.setX(
                (1f-time)*(1f-time)*(1f-time)*start.getX() + 3f*(1f-time)*(1f-time)*time*control1.getX() + 3f*(1-time)*time*time*control2.getX() + time*time*time*end.getX()
        );
        targetPosition.setY(
                (1f-time)*(1f-time)*(1f-time)*start.getY() + 3f*(1f-time)*(1f-time)*time*control1.getY() + 3f*(1-time)*time*time*control2.getY() + time*time*time*end.getY()
        );
    }
}
