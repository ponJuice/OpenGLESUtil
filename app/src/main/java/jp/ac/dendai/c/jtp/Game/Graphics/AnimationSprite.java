package jp.ac.dendai.c.jtp.Game.Graphics;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.UIs.Math.Vector2;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by wark on 2016/07/10.
 */
public class AnimationSprite extends Sprite{
    private GLES20COMPOSITIONMODE mode;
    private int animationId;
    private int roop;                       //0で停止　-1で無限
    private int animateTime,frameCounter;   //animationTime 次の画像にするまでの間の時間
    private Bitmap nowSprite;

    public void setMode(GLES20COMPOSITIONMODE mode){
        this.mode = mode;
    }

    public GLES20COMPOSITIONMODE getMode(){
        return mode;
    }
    public AnimationSprite(int animationId,int animateTime,int roop,GLES20COMPOSITIONMODE mode){
        this.animateTime = animateTime;
        this.animationId = animationId;
        this.mode = mode;
        nowSprite = BitmapList.getAnimationBitmap(animationId).get();
        this.roop = roop;
    }

    @Override
    public void draw(float offsetX, float offsetY, Vector2 position, float sizeX, float sizeY, float alpha, float degree) {
        if(roop != 0){
            if(animateTime <= frameCounter){
                nowSprite = BitmapList.getAnimationBitmap(animationId).getNext();
                frameCounter = 0;
            }
            frameCounter++;
            roop--;
        }
        GLES20Util.DrawGraph(position.getX()+offsetX,position.getY()+offsetY,sizeX,sizeY, degree,nowSprite,alpha,mode);
    }

    @Override
    public void proc() {
        frameCounter++;
    }
}
