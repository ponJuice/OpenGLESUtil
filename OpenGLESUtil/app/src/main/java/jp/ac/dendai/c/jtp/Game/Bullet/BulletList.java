package jp.ac.dendai.c.jtp.Game.Bullet;

import jp.ac.dendai.c.jtp.Game.Enemy.EnemyList;
import jp.ac.dendai.c.jtp.Game.Player.Player;
import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.Util.UnorderedRingList.RecycleRingList;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/08.
 */
public class BulletList{
    private BulletRecycleRingList rrl;
    public BulletList(int maxData) {
        rrl = new BulletRecycleRingList(maxData);
    }
    public void add(BulletTemplate bt){
        rrl.add(bt);
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

            //画面外にでたら非活性化
            /*if(temp.getObject().getPosition().getX() < -temp.getObject().getSizeX()/2.0f || temp.getObject().getPosition().getX() > GLES20Util.getWidth_gl()+temp.getObject().getSizeX()/2.0f
                    || temp.getObject().getPosition().getY() < -temp.getObject().getSizeY()/2.0f || temp.getObject().getPosition().getY() > GLES20Util.getHeight_gl()+temp.getObject().getSizeY()/2.0){
                temp.recycleRingList_Remove();
            }*/
            if(temp.getObject().getPosition().getX() < 0 || temp.getObject().getPosition().getX() > GLES20Util.getWidth_gl()-temp.getObject().getSizeX()/2.0f
                    || temp.getObject().getPosition().getY() <0  || temp.getObject().getPosition().getY() > GLES20Util.getHeight_gl()-temp.getObject().getSizeY()/2.0){
                temp.recycleRingList_Remove();
            }
        }
    }

    public void playerBomdCollision(IPhysics2D p){
        RecycleRingList<Bullet>.RecycleRingListIterator<Bullet> ite = rrl.getIterator();
        while(ite.hasNext()){
            RecycleRingList<Bullet>.RecycleRingListContainer<Bullet> cont = ite.next();
            if(p.isCollision(cont.getObject()) && p.getCollider().mask(cont.getObject().getCollider())){
                remove(cont);
            }
        }
    }

    public boolean isPlayerCollisionProc(Player obj){
        RecycleRingList<Bullet>.RecycleRingListIterator<Bullet> ite = rrl.getIterator();
        while(ite.hasNext()){
            Bullet b = ite.next().getObject();
            if(!obj.isDamage() && b.isCollision(obj) && b.getCollider().mask(obj.getCollider())){
                b.collisionPlayerProc(obj);
                return true;
            }
        }
        return false;
    }
    public void playerBulletCollision(EnemyList.EnemyListContainer e){
        RecycleRingList<Bullet>.RecycleRingListIterator<Bullet> ite = rrl.getIterator();
        while(ite.hasNext()){
            RecycleRingList<Bullet>.RecycleRingListContainer<Bullet> cont = ite.next();
            if(cont.getObject().isCollision(e.getObject()) && cont.getObject().getCollider().mask(e.getObject().getCollider())){
                //衝突
                e.getObject().damage(cont.getObject().getAttackValue(),cont.getObject());
                remove(cont);
            }
        }
    }

    @Override
    public String toString() {
        return rrl.toString();
    }
}
