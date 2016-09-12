package jp.ac.dendai.c.jtp.Game.Bullet.Barrage;

import java.util.LinkedList;

import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.MainActivity;

/**
 * Created by Goto on 2016/07/11.
 */
public class BarrageList {
    protected LinkedList<Barrage> barrages;

    public int getTotalTime() {
        return totalTime+startTime;
    }

    protected int totalTime;

    public int getStartTime() {
        return startTime;
    }

    public int getOffsetStartTime() {
        return offsetStartTime;
    }

    protected int startTime = 999999999;
    protected int offsetStartTime;

    public BarrageList(){
        barrages = new LinkedList<>();
        totalTime = 0;
        startTime = 0;
        offsetStartTime = 0;
    }

    public void add(Barrage b){
        startTime = Math.min(startTime,b.getStartTime());
        totalTime += b.getLengthTime();
        barrages.add(b);
    }

    public void remove(Barrage b){
        if(barrages.remove(b)){
            totalTime -= b.getLengthTime();
            startTime = barrages.getFirst().getStartTime();
            for(Barrage ba : barrages){
                startTime = Math.min(startTime,ba.getStartTime());
            }
        }
    }

    public void proc(int time,BulletList bulletList,float x,float y){
        for(Barrage b : barrages){
            b.proc(time+offsetStartTime,bulletList,x,y);
        }
    }
}
