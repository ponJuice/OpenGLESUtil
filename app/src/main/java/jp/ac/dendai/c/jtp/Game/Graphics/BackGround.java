package jp.ac.dendai.c.jtp.Game.Graphics;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.UIs.Math.Vector2;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by wark on 2016/07/11.
 */
public class BackGround {
    private Bitmap back;
    private float ux,uy;
    private float x,y;
    private float sizeX,sizeY;
    private float timeCounter;
    public BackGround(float ux,float uy,float sizeX,float sizeY,Bitmap background){
        this.back = background;
        this.ux = ux;
        this.uy = uy;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        timeCounter = 0;
        x = GLES20Util.getWidth_gl()/2f;
        y = GLES20Util.getHeight_gl()/2f;
    }

    public void proc(){
        x += ux;
        y += uy;
        if(x < (-sizeX/2f) || x > (GLES20Util.getWidth_gl()+sizeX/2f)){
            x = GLES20Util.getWidth_gl()/2f;
        }
        if(y < (-sizeY/2f) || y > (GLES20Util.getHeight_gl()+sizeY/2f)){
            y = GLES20Util.getHeight_gl()/2f;
        }
        timeCounter++;
    }

    public void  draw(float offsetX,float offsetY){
        //左上
        GLES20Util.DrawGraph(x-sizeX,y+sizeY,sizeX,sizeY,back,1f, GLES20COMPOSITIONMODE.ALPHA);
        //上
        GLES20Util.DrawGraph(x,y+sizeY,sizeX,sizeY,back,1f, GLES20COMPOSITIONMODE.ALPHA);
        //右上
        GLES20Util.DrawGraph(x+sizeX,y+sizeY,sizeX,sizeY,back,1f, GLES20COMPOSITIONMODE.ALPHA);
        //左
        GLES20Util.DrawGraph(x-sizeX,y,sizeX,sizeY,back,1f, GLES20COMPOSITIONMODE.ALPHA);
        //真ん中
        GLES20Util.DrawGraph(x,y,sizeX,sizeY,back,1f, GLES20COMPOSITIONMODE.ALPHA);
        //右
        GLES20Util.DrawGraph(x+sizeX,y,sizeX,sizeY,back,1f, GLES20COMPOSITIONMODE.ALPHA);
        //左下
        GLES20Util.DrawGraph(x-sizeX,y-sizeY,sizeX,sizeY,back,1f, GLES20COMPOSITIONMODE.ALPHA);
        //下
        GLES20Util.DrawGraph(x,y-sizeY,sizeX,sizeY,back,1f, GLES20COMPOSITIONMODE.ALPHA);
        //右下
        GLES20Util.DrawGraph(x+sizeX,y-sizeY,sizeX,sizeY,back,1f, GLES20COMPOSITIONMODE.ALPHA);
    }
}
