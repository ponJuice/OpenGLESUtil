package jp.ac.dendai.c.jtp.Game.Enemy;

import jp.ac.dendai.c.jtp.Game.Graphics.BitmapList;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.UIs.Screen.GameScreen;
import jp.ac.dendai.c.jtp.openglesutil.R;
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
    protected BulletTemplate bt;

    public Enemy(int imageId,float x,float y,float sizeX,float sizeY){
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.imageId = imageId;

        bt = new BulletTemplate();
        bt.radius = 0.05f;
        bt.x = 0.05f;
        bt.y = 0.05f;
        bt.ux = 0.01f;
        bt.uy = 0.01f;
        bt.sizeX = 0.1f;
        bt.sizeY = 0.1f;
        bt.imageId = BitmapList.setBitmap(R.drawable.bomd2);
    }

    public void draw(float offsetX,float offsetY){
        GLES20Util.DrawGraph(offsetX+x,offsetY+y,sizeX,sizeY, BitmapList.getBitmap(imageId),1.0f, GLES20COMPOSITIONMODE.ALPHA);
    }

    public void proc(){
        if(counter > 10 ){
            GameScreen.bulletList.add(bt);
            counter = 0;
        }
        counter++;
    }

}
