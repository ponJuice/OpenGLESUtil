package jp.ac.dendai.c.jtp.Game.Bullet.Barrage;

import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;

/**
 * Created by Goto on 2016/07/11.
 */
public class Radiationbarrage extends Barrage {
    protected int bulletNum;    //一度に放出する弾の数
    protected float speed;      //弾スピード
    protected float offsetRad;  //角度のオフセット
    protected float deltaRad;   //毎回の角度差分
    protected int drawCounter;  //リセット用カウンタ
    protected int attackCount;

    /**
     *
     * @param imageId   弾の画像ID
     * @param mask      コリジョンマスク
     * @param tag       コリジョンタグ
     * @param startTime 開始時間
     * @param lengthTime　弾幕時間
     * @param bulletNum 一度に放出する弾の数
     * @param attackCount       何回放出するか
     * @param offsetRad     //度数法
     * @param speed     弾の速度
     */
    public Radiationbarrage(int imageId, int mask, int tag, int startTime, int lengthTime,int bulletNum,int attackCount,float offsetRad,float deltaRad,float speed) {
        super(imageId, mask, tag, startTime, lengthTime);
        if(bulletNum <= 0)
            this.bulletNum = 1;
        else
            this.bulletNum = bulletNum;
        this.attackCount = attackCount;
        drawCounter = 0;
        deltaTime = (endTime - startTime)/attackCount;
        this.offsetRad = (float)Math.toRadians(offsetRad);
        this.deltaRad = deltaRad;
        this.speed = speed;
        bt.radius = 0.05f;
    }

    /**
     * 回転する角度を指定します
     * @param degree
     */
    public void setDeltaOffsetRad(float degree){
        this.deltaRad = degree;
    }

    @Override
    public void proc(int time, BulletList bulletList, float x, float y) {
        if (time >= startTime && time < endTime) {
            if ((time % deltaTime) == 0) {
                if(drawCounter >= attackCount)
                    drawCounter = 0;
                for (int n = 0; n < bulletNum; n++) {
                    bt.ux = speed*(float)Math.cos((2.0 * Math.PI)/(double)(bulletNum * n) + offsetRad + (deltaRad * drawCounter));
                    bt.uy = speed*(float)Math.sin((2.0 * Math.PI)/(double)(bulletNum * n) + offsetRad + (deltaRad * drawCounter));
                    bt.x = x;
                    bt.y = y;
                    bulletList.add(bt);
                }
                drawCounter++;
            }
        }
    }
}
