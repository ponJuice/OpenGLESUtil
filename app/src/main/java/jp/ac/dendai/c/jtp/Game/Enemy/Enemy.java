package jp.ac.dendai.c.jtp.Game.Enemy;

import java.util.LinkedList;

import jp.ac.dendai.c.jtp.Game.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapList;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.Game.Motion.Action;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;
import jp.ac.dendai.c.jtp.UIs.Screen.GameScreen;
import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/07/09.
 */
public class Enemy extends Bullet {
    protected float collision_radius = 0.05f;
    protected float HP;
    protected BulletTemplate bt;
    protected LinkedList<Action> actions;
    protected int timeCounter = 0;
    protected int startTime;

    public Enemy(int imageId, float x, float y, float sizeX, float sizeY, float radius,int startTime){
        super(new CircleCollider(radius),imageId,x,y,0,0,0.1f,0.1f,GameScreen.COLLISION_MASK.PLAYERBULLET.getInt(),GameScreen.COLLISION_MASK.ENEMY.getInt());
        position = new Vector2(x,y);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.imageId = imageId;
        this.startTime = startTime;

        bt = new BulletTemplate();
        bt.radius = 0.05f;
        bt.x = 0.05f;
        bt.y = 0.05f;
        bt.ux = 0;
        bt.uy = -0.01f;
        bt.sizeX = 0.1f;
        bt.sizeY = 0.1f;
        bt.tag = GameScreen.COLLISION_MASK.ENEMYBULLET.getInt();
        bt.mask = GameScreen.COLLISION_MASK.PLAYER.getInt();
        bt.imageId = BitmapList.setBitmap(R.drawable.bomd2);

        actions = new LinkedList<>();
    }

    public void addAction(Action action){
        actions.add(action);
    }

    public void removeAction(Action action){
        actions.remove(action);
    }

    public void draw(float offsetX,float offsetY){
        GLES20Util.DrawGraph(offsetX + position.getX(), offsetY + position.getY(), sizeX, sizeY, BitmapList.getBitmap(imageId), 1.0f, GLES20COMPOSITIONMODE.ALPHA);
    }

    public void proc(){
        for (Action a: actions) {
            if(a.getStartTime() <= timeCounter && a.getEndTime() >= timeCounter)
                a.action(position);
        }
        if(timeCounter % 10 == 0){
            bt.x = position.getX();
            bt.y = position.getY();
            GameScreen.bulletList.add(bt);
        }
        timeCounter++;
    }
}
