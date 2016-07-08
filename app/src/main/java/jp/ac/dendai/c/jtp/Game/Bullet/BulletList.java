package jp.ac.dendai.c.jtp.Game.Bullet;

import java.util.LinkedList;

import jp.ac.dendai.c.jtp.Physics.Physics.Physics2D;

/**
 * Created by Goto on 2016/07/08.
 */
public class BulletList {
    static LinkedList<Bullet> bullets;
    static{
        bullets = new LinkedList<>();
    }

    public static void addBullet(Bullet b){
        bullets.add(b);
    }

    public static void drawAll(float offsetX,float offsetY){
        for (Bullet b : bullets){
            b.draw(offsetX,offsetY);
        }
    }

    public static void update(){
        for (Bullet b : bullets){
            b.updatePosition(1);
        }
    }

    public static boolean isCollision(Physics2D p){
        for(Bullet b : bullets){
            if(b.isAllive() && p.isCollision(b))
                return true;
        }
        return  false;
    }
}
