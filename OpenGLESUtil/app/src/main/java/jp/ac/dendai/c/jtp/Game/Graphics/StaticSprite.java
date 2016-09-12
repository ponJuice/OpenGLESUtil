package jp.ac.dendai.c.jtp.Game.Graphics;

import jp.ac.dendai.c.jtp.UIs.Math.Vector2;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by wark on 2016/07/10.
 */
public class StaticSprite extends Sprite {
    protected int imageId;
    protected GLES20COMPOSITIONMODE mode;

    public void setMode(GLES20COMPOSITIONMODE mode){
        this.mode = mode;
    }

    public GLES20COMPOSITIONMODE getMode(){
        return mode;
    }

    @Override
    public void draw(float offsetX, float offsetY, Vector2 position, float sizeX, float sizeY,float alpha, float degree) {
        GLES20Util.DrawGraph(position.getX()+offsetX,position.getY()+offsetY,sizeX,sizeY,0,0,1,1, degree,BitmapList.getBitmap(imageId),alpha,mode);
    }

    @Override
    public void proc() {

    }
}
