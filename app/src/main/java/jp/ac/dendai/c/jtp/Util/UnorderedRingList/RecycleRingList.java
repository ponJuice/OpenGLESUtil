package jp.ac.dendai.c.jtp.Util.UnorderedRingList;

import java.util.Iterator;

import jp.ac.dendai.c.jtp.Game.Bullet.Bullet;

/**
 * Created by テツヤ on 2016/07/08.
 */
public class RecycleRingList<T>{
    protected RecycleRingListIterator<T> ite;
    protected int max;
    protected int size;
    protected RecycleRingListContainer<T>[] data;
    protected int[] info;
    protected int info_add;
    protected int info_remove;
    public RecycleRingListContainer<T> first,end;
    public RecycleRingList(int maxData){
        max = maxData;
        data = new RecycleRingListContainer[max];
        info = new int[max];
        for(int n = 0;n < max;n++){
            info[n] = n;
        }
    }

    protected void nextInfo_add(){
        info_add++;
        if(info_add >= data.length)
            info_add = 0;
    }

    protected void nextInfo_remove(){
        info_remove++;
        if(info_remove >= data.length)
            info_remove = 0;
    }

    public T next(){
        return first.object;
    }

    protected  void setNext(RecycleRingListContainer<T> target,RecycleRingListContainer<T> next){
        target.next = next;
    }
    protected  void setPrev(RecycleRingListContainer<T> target,RecycleRingListContainer<T> prev){
        target.prev = prev;
    }

    protected  void setContinerIndex(int index,RecycleRingListContainer<T> r){
        r.recycleRingList_index = index;
    }

    public RecycleRingListContainer<T> add(T obj){
        if(isMax())
            return null;
        if(data[getEmpty()] == null)
            data[getEmpty()] = new RecycleRingListContainer<>(obj);
        else
            data[getEmpty()].object = obj;
        data[getEmpty()].recycleRingList_index=getEmpty();
        RecycleRingListContainer<T> temp = data[getEmpty()];

        if(first == null && end == null){
            first = temp;
            end = temp;
            temp.next = null;
            temp.prev = null;
        }
        else {
            temp.prev = end;
            temp.next = null;
            end.next = temp;
            end = temp;
        }
        nextInfo_add();
        size++;
        return temp;
    }

    public boolean remove(RecycleRingListContainer obj){
        if(obj.getRecycleRingList_index() < 0 || size <= 0)
            return false;
        info[info_remove] = obj.getRecycleRingList_index();

        if(obj.next != null)
            obj.next.prev = obj.prev;
        else
            end = obj.prev;

        if(obj.prev != null)
            obj.prev.next = obj.next;
        else
            first = obj.next;

        obj.next = null;
        obj.prev = null;

        nextInfo_remove();
        size--;
        return true;
    }

    public int getSize(){
        return size;
    }

    public int getEmpty(){
        if(isMax())
            return -1;
        return info[info_add];
    }
    public boolean isMax(){
        return max <= size;
    }

    public int ringConvert(int n){
        if(n >= data.length)
            n = 0;
        return n;
    }

    public RecycleRingListIterator<T> getIterator(){
        if(ite == null)
            ite = new RecycleRingListIterator(first);
        else
            resetIterator(ite);
        return ite;
    }

    public void resetIterator(RecycleRingListIterator<T> ite){
        ite.reset(first);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("RecycleRingList\ndata:");
        for(int n = 0;n < max ;n++){
            sb.append("["+n+" : "+ data[n] +"] , ");
        }
        sb.append("\ninfo:");
        for(int n = 0;n < max ;n++){
            sb.append(info[n]+" , ");
        }
        RecycleRingListContainer<T> temp = first;
        while(temp != null){
            temp.toString();
            temp = temp.next;
        }
        return sb.toString();
    }

    public class RecycleRingListContainer<T>{
        private RecycleRingListContainer<T> next,prev;
        private T object;
        private int recycleRingList_index;
        public RecycleRingListContainer(T obj){
            object = obj;
        }
        public int getRecycleRingList_index(){
            return recycleRingList_index;
        }
        public void recycleRingList_Remove(){
            if(recycleRingList_index == -1)
                return;
            remove(this);
            recycleRingList_index = -1;
        }
        public T getObject(){
            return object;
        }

        @Override
        public String toString() {
            if(object == null)
                return "_";
            else
                return "+";
            //return object.toString();
        }
    }

    public class RecycleRingListIterator<T>{
        private RecycleRingListContainer<T> ite;
        public RecycleRingListIterator(RecycleRingListContainer<T> ite){
            this.ite = ite;
        }
        public RecycleRingListContainer<T> next(){
            RecycleRingListContainer<T> temp = ite;
            ite = ite.next;
            return temp;
        }
        public boolean hasNext(){
            return ite != null;
        }
        public void reset(RecycleRingListContainer<T> con){
            ite = con;
        }
    }
}
