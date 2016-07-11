package jp.ac.dendai.c.jtp.Game.Bullet;

/**
 * Created by wark on 2016/07/09.
 */
public class BulletTemplate {
    public float radius;
    public int imageId;
    public float x,y;
    public float ux,uy;
    public float sizeX,sizeY;
    public int mask,tag;
    public BulletTemplate(){
        radius = 0.05f;
        sizeX = 0.1f;
        sizeY = 0.1f;
        mask = 0;
        tag = 0;
    }
}
