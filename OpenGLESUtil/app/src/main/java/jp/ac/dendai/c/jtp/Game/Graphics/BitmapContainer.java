package jp.ac.dendai.c.jtp.Game.Graphics;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by wark on 2016/07/10.
 */
public class BitmapContainer {
    private Bitmap[] images;
    private int index = -1;
    public BitmapContainer(Bitmap[] list){
        images = list;
    }
    public BitmapContainer(Bitmap image){
        images = new Bitmap[1];
        images[0] = image;
    }
    public Bitmap getAt(int index){
        index = index % images.length;
        if(index < 0)
            index = images.length + index;
        return images[index];
    }
    public Bitmap get(){
        return images[0];
    }
    public Bitmap getNext(){
        index++;
        if(index >= images.length)
            index = 0;
        return images[index];
    }
    public Bitmap getPrev(){
        index--;
        if(index < 0)
            index = images.length-1;
        return images[index];
    }
    public int whatNext(){
        if(index + 1 >= images.length)
            return 0;
        return index + 1;
    }
    public int whatPrev(){
        if(index -1 < 0)
            return images.length-1;
        return index-1;
    }
    public int nowIndex(){
        return index;
    }
    public void setNext(int n){
        if(index >= images.length || index < 0)
            index = 0;
        index = n - 1;
    }
    public void setPrev(int n){
        if(index >= images.length || index < 0)
            index = 0;
        index = n + 1;
    }
    public int getBitmapCount(){
        return images.length;
    }

    public static BitmapContainer create(int startX,int startY,int deltaPixcelX,int deltaPixcelY,int frameNum,int resourceId){
        Bitmap[] images = new Bitmap[frameNum];
        int sx = startX,sy = startY;
        int ex = startX+deltaPixcelX,ey = startY+deltaPixcelY;
        for(int n = 0;n < frameNum ;n++) {
            images[n] = GLES20Util.loadBitmap(
                    sx,
                    sy,
                    ex,
                    ey,
                    resourceId);
            sx = ex+1;
            ex = sx + deltaPixcelX;
        }
        return new BitmapContainer(images);
    }
}
