package jp.ac.dendai.c.jtp.Game.Enemy;

import java.util.LinkedList;
import java.util.Random;

import jp.ac.dendai.c.jtp.Game.Bullet.Barrage.Barrage;
import jp.ac.dendai.c.jtp.Game.Bullet.Barrage.BarrageList;
import jp.ac.dendai.c.jtp.Game.Bullet.Barrage.LinearOneBarrage;
import jp.ac.dendai.c.jtp.Game.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapList;
import jp.ac.dendai.c.jtp.Game.Motion.Action;
import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;
import jp.ac.dendai.c.jtp.UIs.Screen.GameScreen;
import jp.ac.dendai.c.jtp.UIs.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.UIs.UI.Text.Text;
import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/07/11.
 */
public class Boss extends Enemy{
    private class AnimationPoint{
        public int frame,startFrame,updateFrame;
        public Vector2 position;
        public float sizeX,sizeY;
        public AnimationPoint(){
            frame = -1;
            position = new Vector2();
            sizeX = 0.01f;
            sizeY = 0.01f;
        }
        public void update(int f){
            if((f - this.startFrame) % updateFrame == 0){
                frame++;
            }
        }
    }

    protected NumberText hpText;
    protected AnimationPoint[] frames;
    protected Random ram;
    protected BarrageList barrageList;

    public Boss(int imageId, float x, float y, float sizeX, float sizeY, float radius, int hp, int deadAnimationId, int effectDelta, int endTime, int score) {
        super(imageId, x, y, sizeX, sizeY, radius, hp, deadAnimationId, effectDelta, endTime, score);
        this.hpText = new NumberText();
        this.hpText.setHorizontalTextAlign(Text.TextAlign.LEFT);
        this.hpText.setVerticalTextAlign(Text.TextAlign.TOP);
        this.hpText.setWrittingAlign(Text.WrittingAlign.HOLIZONTAL);

        //攻撃のパターン作成
        barrageList = new BarrageList();
        barrageList.add(new LinearOneBarrage(R.drawable.bomd2, GameScreen.COLLISION_MASK.PLAYER.getInt(), GameScreen.COLLISION_MASK.ENEMYBULLET.getInt(), 0, -0.05f, 5, 0, 60));

        ram = new Random();

        frames = new AnimationPoint[5];
        for(int n = 0;n < frames.length;n++){
            frames[n] = new AnimationPoint();
        }

        bt.uy = -0.05f;
    }

    @Override
    public void damage(int value,IPhysics2D owner){
        HP -= value;
        for(AnimationPoint n : frames){
            if(n.frame < 0 || n.frame >= 4) {
                n.position.copy(owner.getPosition());
                n.sizeX = ram.nextFloat()*0.1f;
                n.sizeY = n.sizeX;
                n.updateFrame = ram.nextInt(20)+10;
                n.startFrame = timeCounter;
                n.frame++;
                break;
            }
        }
        if(HP <= 0) {
            state = DAMAGE_STATE.EFFECT;
        }
    }
    public void setStartTime(int startTime){
        this.startTime = startTime;
    }

    public void addAction(Action action){
        endTime += action.getEffctLength();
        actions.add(action);
    }

    @Override
    public void draw(float offsetX,float offsetY) {
        if (state != DAMAGE_STATE.DEAD)
            GLES20Util.DrawGraph(offsetX + position.getX(), offsetY + position.getY(), sizeX, sizeY, BitmapList.getBitmap(imageId), alpha, GLES20COMPOSITIONMODE.ALPHA);
        if (state == DAMAGE_STATE.EFFECT) {
            //盛大な死亡エフェクト
            GLES20Util.DrawGraph(offsetX + position.getX(), offsetY + position.getY(), sizeY*2f, sizeY*2f, BitmapList.getAnimationBitmap(deadAnimationId).getAt(frame), 1f, GLES20COMPOSITIONMODE.ALPHA);
            GLES20Util.DrawGraph(offsetX + position.getX(), offsetY + position.getY(), sizeY, sizeY, BitmapList.getAnimationBitmap(deadAnimationId).getAt(frame), 1f, GLES20COMPOSITIONMODE.ADD);
        }

        for(AnimationPoint n : frames){
            if(n.frame >= 0){
                GLES20Util.DrawGraph(offsetX+n.position.getX(),offsetY+n.position.getY(),n.sizeX,n.sizeY,BitmapList.getAnimationBitmap(deadAnimationId).getAt(n.frame-1),1f,GLES20COMPOSITIONMODE.ADD);
            }
        }

        debugDraw(offsetX,offsetY);
        hpText.draw(HP, 3, offsetX + position.getX(), offsetY + position.getY(), 1f, 1f, GLES20COMPOSITIONMODE.ALPHA);
    }

    @Override
    public void proc(int time){
        //死んでいたとき
        if(isDead()){
            if(state == DAMAGE_STATE.EFFECT){
                if(effectTimeCounter >= effectEndTime){
                    //削除
                    state = DAMAGE_STATE.DEAD;
                    GameScreen.addScore(score);
                    GameScreen.enemyList.remove(elc);
                    GameScreen.setGameState(GameScreen.GAMESTATE.CLEAR);
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
            //ボスの自機弾ヒットエフェクト表示
            for(AnimationPoint n : frames){
                if(n.frame >= BitmapList.getAnimationBitmap(deadAnimationId).getBitmapCount()){
                    n.frame = -1;
                }
                else if(n.frame >= 0){
                    n.update(timeCounter);
                }
            }
            if(time >= startTime) {
                int temp = timeCounter % endTime;
                for (Action a : actions) {
                    if (a.getStartTime() <= temp && a.getEndTime() > temp) {
                        if (temp == a.getStartTime()) {
                            a.init(position);
                        }
                        a.action(position);
                    }
                }
                //弾の発射
                /*if (timeCounter % 20 == 0) {
                    bt.x = position.getX();
                    bt.y = position.getY();
                    GameScreen.bulletList.add(bt);
                }*/
                barrageList.proc(timeCounter%barrageList.getTotalTime(),GameScreen.bulletList,position.getX(),position.getY());
                timeCounter++;
            }
        }
    }
}
