package jp.ac.dendai.c.jtp.Game.Enemy;

import jp.ac.dendai.c.jtp.Game.BitmapList;
import jp.ac.dendai.c.jtp.Game.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Collider.ICollider;
import jp.ac.dendai.c.jtp.UIs.Screen.GameScreen;
import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.Util.FpsController;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/07/08.
 */
public class Enemy {
    protected int imageId;
    protected float x,y;
    protected float sizeX,sizeY;
    protected float HP;
    protected int counter;    //でバグ

    public Enemy(int imageId,float x,float y,float sizeX,float sizeY){
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.imageId = imageId;
    }

    public void draw(float offsetX,float offsetY){
        GLES20Util.DrawGraph(offsetX+x,offsetY+y,sizeX,sizeY, BitmapList.getBitmap(imageId),1.0f, GLES20COMPOSITIONMODE.ALPHA);
    }

    public void proc(){
        if(counter > 10 ){
            GameScreen.bulletList.add(new Bullet(new CircleCollider(0.1f),BitmapList.setBitmap(R.drawable.bomd2),0.05f,0.05f,0.01f,0.01f,0.1f,0.1f));
            counter = 0;
        }
        counter++;
    }

}
