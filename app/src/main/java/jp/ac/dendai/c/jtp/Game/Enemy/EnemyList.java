package jp.ac.dendai.c.jtp.Game.Enemy;

import java.util.Iterator;

/**
 * Created by テツヤ on 2016/07/10.
 */
public class EnemyList {
    private EnemyListIterator ite;
    private EnemyListContainer first,end;
    private int size = 0;

    public void add(EnemyListContainer e){
        if(size == 0){
            first = e;
            end = e;
        }
        end.next = e;
        e.prev  = end;
        end = e;
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
            next.prev = prev;
            prev.next = next;
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
        public boolean hasNext(){
            return ite != null;
        }
        public void reset(EnemyListContainer con){
            ite = con;
        }
    }
}
