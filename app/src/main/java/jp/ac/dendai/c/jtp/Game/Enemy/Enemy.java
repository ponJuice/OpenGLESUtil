package jp.ac.dendai.c.jtp.Game.Enemy;

import android.graphics.Bitmap;

import java.util.LinkedList;

import jp.ac.dendai.c.jtp.Game.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapList;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.Game.Motion.Action;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;
import jp.ac.dendai.c.jtp.UIs.Screen.GameScreen;
import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/07/09.
 */
public class Enemy extends Bullet {
    public enum DAMAGE_STATE{
        NON,
        EFFECT,
        DEAD
    }
    protected boolean debug = false;
    protected EnemyList.EnemyListContainer elc;
    protected DAMAGE_STATE state = DAMAGE_STATE.NON;
    protected float collision_radius = 0.05f;
    protected int HP = 1;
    protected BulletTemplate bt;
    protected LinkedList<Action> actions;
    protected int timeCounter = 0,effectTimeCounter = 0;
    protected int startTime = 1000000000,effectDelta,effectEndTime;
    protected int deadAnimationId;
    protected int frame = -1;
    protected int endTime = 0;
    protected boolean flag = false;
    protected float alpha = 1.0f;
    protected int score = 100;

    public Enemy(int imageId, float x, float y, float sizeX, float sizeY, float radius,int hp,int deadAnimationId,int effectDelta,int endTime,int score){
        super(new CircleCollider(radius),imageId,x,y,0,0,0.1f,0.1f,GameScreen.COLLISION_MASK.PLAYERBULLET.getInt(),GameScreen.COLLISION_MASK.ENEMY.getInt());
        position = new Vector2(x,y);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.imageId = imageId;

        HP = hp;
        this.deadAnimationId = deadAnimationId;
        this.effectEndTime = endTime;
        this.effectDelta = effectDelta;

        this.score = score;

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

    public void damage(int value,IPhysics2D owner){
        HP -= value;
        if(HP <= 0) {
            GameScreen.addScore(score);
            score = 0;
            state = DAMAGE_STATE.EFFECT;
        }
    }

    public void addAction(Action action){
        endTime = Math.max(endTime,action.getEndTime());
        startTime = Math.min(startTime, action.getStartTime());
        actions.add(action);
    }

    public DAMAGE_STATE getDamageState(){
        return state;
    }

    public void removeAction(Action action){
        actions.remove(action);
    }

    public void setEnemyListContainer(EnemyList.EnemyListContainer elc){
        this.elc = elc;
    }

    public EnemyList.EnemyListContainer getEnemyContainer(){
        return elc;
    }

    public void draw(float offsetX,float offsetY){
        if(state != DAMAGE_STATE.DEAD)
            GLES20Util.DrawGraph(offsetX + position.getX(), offsetY + position.getY(), sizeX, sizeY, BitmapList.getBitmap(imageId), alpha, GLES20COMPOSITIONMODE.ALPHA);
        if(state == DAMAGE_STATE.EFFECT){
            GLES20Util.DrawGraph(offsetX + position.getX(), offsetY + position.getY(), sizeX*2f, sizeY*2f, BitmapList.getAnimationBitmap(deadAnimationId).getAt(frame), 1f, GLES20COMPOSITIONMODE.ALPHA);
        }
        debugDraw(offsetX,offsetY);
    }

    public void debugDraw(float offsetX,float offsetY){
        if(!debug)
            return;
        GLES20Util.DrawGraph(offsetX + position.getX(), offsetY + position.getY(), collider.getRadius()*2f, collider.getRadius()*2f, BitmapList.getBitmap(R.drawable.bomd2), 1f, GLES20COMPOSITIONMODE.ALPHA);
    }
    public boolean isDead(){
        return HP <= 0;
    }

    public void proc(int time){
        //死んでいたとき
        if(isDead()){
            if(state == DAMAGE_STATE.EFFECT){
                if(effectTimeCounter >= effectEndTime){
                    //削除
                    state = DAMAGE_STATE.DEAD;
                    GameScreen.enemyList.remove(elc);
                }
                else {
                    //エフェクトを更新
                    if(effectTimeCounter % effectDelta == 0)
                        frame++;
                    alpha = 1f - 1f / effectEndTime * effectTimeCounter;
                    effectTimeCounter++;
                }
            }
        }else {//死んでない
            if(time >= endTime){
                GameScreen.enemyList.remove(elc);
            }
            else if(time >= startTime) {
                for (Action a : actions) {
                    if (a.getStartTime() <= time && a.getEndTime() >= time)
                        a.action(position);
                }
                if (timeCounter % 10 == 0) {
                    bt.x = position.getX();
                    bt.y = position.getY();
                    GameScreen.bulletList.add(bt);
                }
            }
            timeCounter++;
        }
    }
}
