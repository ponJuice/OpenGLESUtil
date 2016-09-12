package jp.ac.dendai.c.jtp.Game.Item;

import android.graphics.Bitmap;
import android.media.SoundPool;

import jp.ac.dendai.c.jtp.Game.Player.Player;
import jp.ac.dendai.c.jtp.Game.Sound.IDPack;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/09/10.
 */
public abstract class Item implements IPhysics2D{
    public Vector2 pos;
    public Vector2 vel;
    public boolean flag = false;
    public SoundPool sp;
    public IDPack se;
    protected Bitmap bitmap;
    protected float alpha = 1f;
    private static Item[] items = new Item[8];
    protected CircleCollider collider;
    public static void collisionSearch(Player player){
        for(int n = 0;n < items.length;n++){
            if(items[n] != null && items[n].collider.isCollision(player.getCollider())){
                items[n].collisionProc(player);
                items[n] = null;
            }
        }
    }
    public static void drawAll(){
        for(int n = 0;n < items.length;n++){
            if(items[n] != null && items[n].flag){
                GLES20Util.DrawGraph(items[n].collider.getPhysicsObject().getPosition().getX(),items[n].collider.getPhysicsObject().getPosition().getY()
                ,items[n].collider.getRadius(),items[n].collider.getRadius()
                ,items[n].bitmap
                ,items[n].alpha
                , GLES20COMPOSITIONMODE.ALPHA);
            }
        }
    }
    public static void add(Item item){
        for(int n = 0;n < items.length;n++){
            if(items[n] == null){
                items[n] = item;
                return;
            }
        }
    }

    public static void remove(Item item){
        for(int n = 0;n < items.length;n++){
            if(items[n] == item){
                items[n] = null;
                return;
            }
        }
    }

    public static void gravity(){
        for(int n = 0;n < items.length;n++){
            if(items[n] != null){
                items[n].vel.setY(items[n].vel.getY()-0.00098f);
                items[n].pos.add(items[n].vel);
            }
        }
    }

    public static void clear(){
        for(int n = 0;n < items.length;n++){
            items[n] = null;
        }
    }

    public abstract  void collisionProc(Player player);
}
