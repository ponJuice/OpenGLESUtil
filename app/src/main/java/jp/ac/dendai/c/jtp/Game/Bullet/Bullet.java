package jp.ac.dendai.c.jtp.Game.Bullet;

import jp.ac.dendai.c.jtp.Game.Graphics.BitmapList;
import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Collider.ICollider;
import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics2D;
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
    protected float degree = 0;
    protected int attackValue = 1;
    public Bullet(ICollider c,int imageId,float x,float y,float ux,float uy,float sizeX,float sizeY,int mask,int tag){
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
        collider.setMask(mask);
        collider.setTag(tag);
    }

    public Bullet(BulletTemplate bt){
        super();
        collider = new CircleCollider(bt.radius);
        collider.setPhysicsObject(this);
        setPropaty(bt);
    }

    public void setPropaty(BulletTemplate bt){
        imageId = bt.imageId;
        sizeX = bt.sizeX;
        sizeY = bt.sizeY;
        velocity.setX(bt.ux);
        velocity.setY(bt.uy);
        position.setX(bt.x);
        position.setY(bt.y);
        collider.setRadius(bt.radius);
        collider.setMask(bt.mask);
        collider.setTag(bt.tag);

        allive = true;
    }

    public int getAttackValue(){
        return attackValue;
    }

    public void collisionPlayerProc(Player player){
        player.collisionBulletProc();
    }

    public void draw(float offsetX,float offsetY){
        //GLES20Util.DrawGraph(offsetX+position.getX(),offsetY+position.getY(),sizeX,sizeY, BitmapList.getBitmap(imageId),1f, GLES20COMPOSITIONMODE.ADD);
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
