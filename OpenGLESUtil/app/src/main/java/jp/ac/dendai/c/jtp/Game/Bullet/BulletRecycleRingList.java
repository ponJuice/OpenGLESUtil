package jp.ac.dendai.c.jtp.Game.Bullet;

import jp.ac.dendai.c.jtp.Util.UnorderedRingList.RecycleRingList;

/**
 * Created by wark on 2016/07/09.
 */
public class BulletRecycleRingList extends RecycleRingList<Bullet> {

    public BulletRecycleRingList(int maxData) {
        super(maxData);
    }

    public RecycleRingListContainer<Bullet> add(BulletTemplate bt) {
        if(isMax())
            return null;
        if(data[getEmpty()] == null)
            data[getEmpty()] = new RecycleRingListContainer<>(new Bullet(bt));
        else
            data[getEmpty()].getObject().setPropaty(bt);
        setContinerIndex(getEmpty(),data[getEmpty()]);
        RecycleRingListContainer<Bullet> temp = data[getEmpty()];

        if(first == null && end == null){
            first = temp;
            end = temp;
            setNext(temp,null);
            setPrev(temp,null);
        }
        else {
            setPrev(temp,end);
            setNext(temp,null);
            setNext(end,temp);
            end = temp;
        }
        nextInfo_add();
        size++;
        return temp;
    }
}
