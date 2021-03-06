package jp.ac.dendai.c.jtp.Game.Enemy;

import android.media.SoundPool;

import java.util.Random;

import jp.ac.dendai.c.jtp.Game.Bullet.Barrage.Barrage;
import jp.ac.dendai.c.jtp.Game.Bullet.Barrage.BarrageList;
import jp.ac.dendai.c.jtp.Game.Bullet.Barrage.LinearOneBarrage;
import jp.ac.dendai.c.jtp.Game.Bullet.Barrage.Radiationbarrage;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapList;
import jp.ac.dendai.c.jtp.Game.Motion.Action;
import jp.ac.dendai.c.jtp.Game.Sound.IDPack;
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
    private enum HPSTATE{
        MAX,
        HALF,
        LOW
    }
    protected boolean destroy_se_flag = false;
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
            if(updateFrame <= 0)
                return;
            if((f - this.startFrame) % updateFrame == 0){
                frame++;
            }
        }
    }
    protected HPSTATE hpState;
    protected NumberText hpText;
    protected int maxHP;
    protected AnimationPoint[] frames;
    protected AnimationPoint[] damageAnime;
    protected Random ram;
    protected int hpStateCounter = 0;
    protected BarrageList barrageList;
    public SoundPool sp;
    public IDPack hit_se,destroy_se;

    public Boss(int imageId, float x, float y, float sizeX, float sizeY, float radius, int hp, int deadAnimationId, int effectDelta, int endTime, int score) {
        super(imageId, x, y, sizeX, sizeY, radius, hp, deadAnimationId, effectDelta, endTime, score);
        this.hpText = new NumberText();
        this.hpText.setHorizontalTextAlign(Text.TextAlign.LEFT);
        this.hpText.setVerticalTextAlign(Text.TextAlign.TOP);
        this.hpText.setWrittingAlign(Text.WrittingAlign.HOLIZONTAL);

        hpState = HPSTATE.MAX;
        maxHP = hp;


        //攻撃のパターン作成
        barrageList = new BarrageList();

        ram = new Random();

        frames = new AnimationPoint[5];
        for(int n = 0;n < frames.length;n++){
            frames[n] = new AnimationPoint();
        }

        damageAnime = new AnimationPoint[5];
        for(int n = 0;n < damageAnime.length;n++){
            damageAnime[n] = new AnimationPoint();
            damageAnime[n].frame = -1;
        }
    }

    public void addBarrage(Barrage b){
        barrageList.add(b);
    }

    @Override
    public void damage(int value,IPhysics2D owner){
        if(state == DAMAGE_STATE.NON) {
            HP -= value;
            hit_se.sp.play(hit_se.id, 0.5f, 0.5f, 0, 0, 1f);
            for (AnimationPoint n : frames) {
                if (n.frame < 0 || n.frame >= 4) {
                    n.position.copy(owner.getPosition());
                    n.sizeX = ram.nextFloat() * 0.1f;
                    n.sizeY = n.sizeX;
                    n.updateFrame = ram.nextInt(20) + 10;
                    n.startFrame = timeCounter;
                    n.frame++;
                    break;
                }
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


        if(hpState == HPSTATE.LOW || hpState == HPSTATE.HALF){
            for(AnimationPoint n : damageAnime) {
                if(n.frame >= 0)
                    GLES20Util.DrawGraph(offsetX + n.position.getX(), offsetY + n.position.getY(), n.sizeX, n.sizeY, BitmapList.getAnimationBitmap(deadAnimationId).getAt(n.frame - 1), 1f, GLES20COMPOSITIONMODE.ADD);
            }
            if(hpStateCounter % 50 == 0){
                hit_se.sp.play(hit_se.id,1f,1f,0,0,1f);
            }
            hpStateCounter++;
        }

        for(AnimationPoint n : frames){
            if(n.frame >= 0){
                GLES20Util.DrawGraph(offsetX+n.position.getX(),offsetY+n.position.getY(),n.sizeX,n.sizeY,BitmapList.getAnimationBitmap(deadAnimationId).getAt(n.frame-1),1f,GLES20COMPOSITIONMODE.ADD);
            }
        }

        //debugDraw(offsetX,offsetY);
        //hpText.draw(HP, 3, offsetX + position.getX(), offsetY + position.getY(), 1f, 1f, GLES20COMPOSITIONMODE.ALPHA);
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
                    if(!destroy_se_flag){
                        destroy_se.sp.play(destroy_se.id,1f,1f,0,0,1f);
                        destroy_se_flag = true;
                    }
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
            for(AnimationPoint n : damageAnime){
                if(n.frame >= BitmapList.getAnimationBitmap(deadAnimationId).getBitmapCount()){
                    n.frame = -1;
                }
                else if(n.frame >= 0){
                    n.update(hpStateCounter);
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
            if(((float)HP / (float)maxHP) < 0.2f){
                for(AnimationPoint n : damageAnime) {
                    if (n.frame < 0 || n.frame >= 4) {
                        n.position.setX(this.position.getX()+ram.nextFloat()*0.3f-0.15f);
                        n.position.setY(this.position.getY()+ram.nextFloat()*0.3f-0.15f);
                        n.sizeX = ram.nextFloat() * 0.25f;
                        n.sizeY = n.sizeX;
                        n.updateFrame = ram.nextInt(20) + 10;
                        n.startFrame = hpStateCounter;
                        n.frame++;
                        break;
                    }
                }
                hpState = HPSTATE.LOW;
            }
            else if(((float)HP / (float)maxHP) < 0.5f){
                for(AnimationPoint n : damageAnime) {
                    if (n.frame < 0 || n.frame >= 4) {
                        n.position.setX(this.position.getX()+ram.nextFloat()*0.3f-0.15f);
                        n.position.setY(this.position.getY()+ram.nextFloat()*0.3f-0.15f);
                        n.sizeX = ram.nextFloat() * 0.25f;
                        n.sizeY = n.sizeX;
                        n.updateFrame = ram.nextInt(20) + 10;
                        n.startFrame = hpStateCounter;
                        n.frame++;
                        break;
                    }
                }
                hpState = HPSTATE.HALF;
            }
        }
    }
}
