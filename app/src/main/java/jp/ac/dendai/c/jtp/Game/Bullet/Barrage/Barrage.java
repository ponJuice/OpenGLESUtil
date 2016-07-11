package jp.ac.dendai.c.jtp.Game.Bullet.Barrage;

import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletTemplate;

/**
 * Created by Goto on 2016/07/11.
 */
public abstract class Barrage {
    protected int startTime;
    protected int endTime;
    protected BulletTemplate bt;
    protected int deltaTime;

    public void setVelocityX(float x){
        bt.ux = x;
    }

    public int getStartTime(){
        return startTime;
    }

    public int getEndTime(){
        return endTime;
    }

    public int getLengthTime(){
        return endTime - startTime;
    }

    public void setVelocityY(float y){
        bt.y = y;
    }

    public Barrage(int imageId,int mask,int tag,int startTime,int lengthTime){
        bt = new BulletTemplate();
        bt.imageId = imageId;
        this.startTime = startTime;
        this.endTime = startTime + lengthTime;
    }
    public abstract void proc(int time,BulletList bulletList,float x,float y);

}
