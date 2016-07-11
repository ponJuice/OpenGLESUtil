package jp.ac.dendai.c.jtp.Game.Bullet.Barrage;

import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;

/**
 * Created by Goto on 2016/07/11.
 */

//単発　単方向
public class LinearOneBarrage extends Barrage{
    public LinearOneBarrage(int imageId, int mask, int tag,float ux,float uy,int number,int startTime,int lengthTime) {
        super(imageId, mask, tag,startTime,lengthTime);
        bt.ux = ux;
        bt.uy = uy;
        deltaTime = (endTime - startTime)/number;
        bt.radius = 0.05f;
    }

    @Override
    public void proc(int time, BulletList bulletList, float x, float y) {
        if (time >= startTime && time < endTime){
            if((time % deltaTime) == 0) {
                bt.x = x;
                bt.y = y;
                bulletList.add(bt);
            }
        }
    }
}
