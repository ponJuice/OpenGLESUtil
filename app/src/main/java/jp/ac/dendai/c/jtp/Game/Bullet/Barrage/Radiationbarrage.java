package jp.ac.dendai.c.jtp.Game.Bullet.Barrage;

import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;

/**
 * Created by Goto on 2016/07/11.
 */
public class Radiationbarrage extends Barrage {
    protected int bulletNum;    //一度に放出する弾の数
    protected float speed;      //弾スピード

    public Radiationbarrage(int imageId, int mask, int tag, int startTime, int lengthTime,int bulletNum,int num,float speed) {
        super(imageId, mask, tag, startTime, lengthTime);
    }

    @Override
    public void proc(int time, BulletList bulletList, float x, float y) {

    }
}
