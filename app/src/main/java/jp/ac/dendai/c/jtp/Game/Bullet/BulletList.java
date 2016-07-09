package jp.ac.dendai.c.jtp.Game.Bullet;

import android.opengl.GLES20;

import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.UIs.Screen.GameScreen;
import jp.ac.dendai.c.jtp.Util.UnorderedRingList.RecycleRingList;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/08.
 */
public class BulletList{
    private RecycleRingList<Bullet> rrl;
    public BulletList(int maxData) {
        rrl = new RecycleRingList<Bullet>(maxData);
    }
    public void add(Bullet b){
        rrl.add(b);
    }
    public void remove(RecycleRingList<Bullet>.RecycleRingListContainer<Bullet> r){
        rrl.remove(r);
    }

    public int size(){
        return rrl.getSize();
    }

    public void drawAll(float offsetX,float offsetY){
        RecycleRingList<Bullet>.RecycleRingListIterator<Bullet> ite = rrl.getIterator();
        while(ite.hasNext()){
            Bullet b = ite.next().getObject();
            b.draw(offsetX,offsetY);
        }
    }

    public void update(){
        RecycleRingList<Bullet>.RecycleRingListIterator<Bullet> ite = rrl.getIterator();
        while(ite.hasNext()){
            RecycleRingList<Bullet>.RecycleRingListContainer<Bullet> temp = ite.next();
            temp.getObject().updatePosition(1);
            if(temp.getObject().getPosition().getX() < -temp.getObject().getSizeX()/2.0f || temp.getObject().getPosition().getX() > GLES20Util.getWidth_gl()+temp.getObject().getSizeX()/2.0f
                    || temp.getObject().getPosition().getY() < temp.getObject().getSizeY()/2.0f || temp.getObject().getPosition().getY() > GLES20Util.getHeight_gl()+temp.getObject().getSizeY()/2.0){
                temp.recycleRingList_Remove();
            }
        }
    }
    public boolean isPlayerCollisionProc(Player obj){
        RecycleRingList<Bullet>.RecycleRingListIterator<Bullet> ite = rrl.getIterator();
        while(ite.hasNext()){
            Bullet b = ite.next().getObject();
            if(!obj.isDamage() && b.isCollision(obj)){
                obj.collisionBulletProc();
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return rrl.toString();
    }
}
