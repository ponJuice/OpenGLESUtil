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
    private int maxFrame;
    private int frame = 0;

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
        this.roop = roop;
        this.maxFrame = BitmapList.getAnimationBitmap(animationId).getBitmapCount();
    }

    public void resetAnimation(){
        frameCounter = 0;
        frame = 0;
    }

    @Override
    public void draw(float offsetX, float offsetY, Vector2 position, float sizeX, float sizeY, float alpha, float degree) {
        if(roop != 0){
            if(animateTime <= frameCounter){
                frame++;
                frameCounter = 0;
                roop--;
            }
        }
        GLES20Util.DrawGraph(position.getX()+offsetX
                ,position.getY()+offsetY
                ,sizeX
                ,sizeY
                ,degree
                ,BitmapList.getAnimationBitmap(animationId).getAt(frame)
                ,alpha,mode);
        frameCounter++;
    }

    @Override
    public void proc() {
    }
}
