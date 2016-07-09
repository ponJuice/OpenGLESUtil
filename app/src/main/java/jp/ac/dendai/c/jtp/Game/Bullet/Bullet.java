package jp.ac.dendai.c.jtp.Game.Bullet;

import android.graphics.Canvas;
import android.graphics.Paint;

import jp.ac.dendai.c.jtp.Game.BitmapList;
import jp.ac.dendai.c.jtp.Physics.Collider.ICollider;
import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics2D;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;
import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/07/08.
 */
public class Bullet extends Physics2D {
    protected boolean allive;
    protected int imageId;
    protected float sizeX,sizeY;
    public Bullet(ICollider c,int imageId,float x,float y,float ux,float uy,float sizeX,float sizeY){
        super();
        this.imageId = imageId;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        velocity.setX(ux);
        velocity.setY(uy);
        position.setX(x);
        position.setY(y);
        allive = true;
        collider = c;
        collider.setPhysicsObject(this);
    }

    public void draw(float offsetX,float offsetY){
        GLES20Util.DrawGraph(offsetX+position.getX(),offsetY+position.getY(),sizeX,sizeY, BitmapList.getBitmap(imageId),1f, GLES20COMPOSITIONMODE.ADD);
        GLES20Util.DrawGraph(offsetX+position.getX(),offsetY+position.getY(),collider.getRadius()*2.0f,collider.getRadius()*2.0f, BitmapList.getBitmap(R.drawable.bomd2),1f, GLES20COMPOSITIONMODE.ADD);

    }

    public boolean isAllive(){
        return allive;
    }

    public void setAllive(boolean b){
        allive = b;
    }

    @Override
    public void updatePosition(float deltaTime) {
        position.setX(position.getX() + velocity.getX());
        position.setY(position.getY() + velocity.getY());
    }

    @Override
    public boolean isCollision(IPhysics2D obj) {
        return collider.isCollision(obj.getCollider());
    }

    public float getSizeX(){
        return sizeX;
    }
    public float getSizeY(){
        return  sizeY;
    }
}
