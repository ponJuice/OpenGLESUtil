package jp.ac.dendai.c.jtp.Game.Enemy;

import java.util.ArrayList;
import java.util.Iterator;

import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.UIs.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/07/10.
 */
public class EnemyList {
    private int index = 0;
    private ArrayList<EnemyListContainer> allEnemys;
    private EnemyListIterator ite;
    private EnemyListContainer first,end;
    private int totalTime = 0;
    private int size = 0;

    public void add(EnemyListContainer e){
        if(size == 0){
            first = e;
            end = e;
        }
        else {
            end.next = e;
            e.prev = end;
            end = e;
        }
        size++;
    }

    public EnemyListIterator getIterator(){
        if(ite == null)
            ite = new EnemyListIterator(first);
        else
            resetIterator(ite);
        return ite;
    }

    public void resetIterator(EnemyListIterator ite){
        ite.reset(first);
    }

    public void remove(EnemyListContainer e){
        if(size <= 0)
            return;
        size--;
        if(e == first){
            first = e.next;
        }
        if(e == end){
            end = e.prev;
        }
        e.remove();
    }

    //デバッグ　トータルタイム表示用
    private NumberText totalTimeDrawer = new NumberText();

    public void drawAll(float offsetX,float offsetY){
        EnemyListContainer temp = first;
        while(temp != null){
            temp.getObject().draw(offsetX,offsetY);
            temp = temp.next;
        }
        //totalTimeDrawer.draw(totalTime,3,0.8f,1.2f,1f,1f, GLES20COMPOSITIONMODE.ALPHA);
    }

    public void update(){
        while(index < allEnemys.size() && allEnemys.get(index).getObject().startTime <= totalTime){
            add(allEnemys.get(index));
            index++;
        }

        EnemyListContainer temp = first;
        while(temp != null){
            temp.getObject().proc(totalTime);
            temp = temp.next;
        }

        totalTime++;
    }

    public void setAllEnemys(Enemy[] e){
        allEnemys = new ArrayList<>(e.length);
        for(int n = 0;n < e.length ;n++){
            allEnemys.add(n, new EnemyListContainer(e[n]));
        }
    }

    public void addEnemys(Enemy e,int capacity){
        if(allEnemys == null)
            allEnemys = new ArrayList<>(capacity);
        e.setEnemyListContainer(new EnemyListContainer(e));
        allEnemys.add(e.getEnemyContainer());
    }

    public class EnemyListContainer{
        private Enemy object;
        private EnemyListContainer next,prev;
        public EnemyListContainer(Enemy e){
            object = e;
        }
        public Enemy getObject(){
            return object;
        }
        public void setObject(Enemy e){
            object = e;
        }
        private void remove(){
            if(next == null && prev == null){

            }
            else {
                if (next == null) {
                    end = prev;
                    prev.next = null;
                } else {
                    next.prev = prev;
                }
                if (prev == null) {
                    first = next;
                    next.prev = null;
                } else {
                    prev.next = next;
                }
            }
        }
    }

    public void playerBulletCollisionProc(Player p){
        EnemyListContainer temp = first;
        while(temp != null){
            p.getBulletList().playerBulletCollision(temp);
            temp = temp.next;
        }
    }

    public class EnemyListIterator{
        private EnemyListContainer ite;
        public EnemyListIterator(EnemyListContainer ite){
            this.ite = ite;
        }
        public EnemyListContainer next(){
            EnemyListContainer temp = ite;
            ite = ite.next;
            return temp;
        }
        public  void remove(){
            if(hasNext())
                ite.prev.remove();
            else
                end.remove();

        }
        public boolean hasNext(){
            return ite != null;
        }
        public void reset(EnemyListContainer con){
            ite = con;
        }
    }
}
