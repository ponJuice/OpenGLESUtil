package jp.ac.dendai.c.jtp.Physics.Collider;

import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;

/**
 * Created by Goto on 2016/06/28.
 */
public abstract class Collider implements ICollider{
    protected int mask,tag;
    protected IPhysics2D object;
    @Override
    public IPhysics2D getPhysicsObject(){
        return object;
    }
    @Override
    public void setMask(int mask){
        this.mask = mask;
    }
    @Override
    public int getMask(){
        return mask;
    }
    @Override
    public int getTag(){
        return tag;
    }
    @Override
    public void setTag(int tag){
        this.tag = tag;
    }
    @Override
    public boolean mask(ICollider col){
        if((col.getTag() & mask) != 0){
            //衝突
            return true;
        }
        return false;
    }
}
