package jp.ac.dendai.c.jtp.UIs.UI.Slider;

/**
 * Created by Goto on 2016/07/11.
 */
public class Rect {
    protected float sx,sy,ex,ey;

    public Rect(float left,float top,float right,float bottom){
        setLeft(left);
        setTop(top);
        setRight(right);
        setBottom(bottom);
    }

    public void setPositionCentor(float x,float y){
        float lengthX = ex - sx;
        float lengthY = ey - sy;
        sx = x-lengthX/2f;
        sy = y+lengthY/2f;
        ex = sx + lengthX;
        ey = sy - lengthY;
    }

    public void setTop(float top){
        sx = top;
    }

    public void setLeft(float left){
        sy = left;
    }

    public void setBottom(float bottom){
        ey = bottom;
    }
    public void setRight(float right){
        ex = right;
    }

    public boolean isCollision(float x,float y){
        return sx <= x && ex >= x &&
                sy >= y && ey <= y;
    }
}
