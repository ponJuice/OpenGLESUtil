package jp.ac.dendai.c.jtp.Game.Graphics;

import jp.ac.dendai.c.jtp.UIs.Math.Vector2;

/**
 * Created by wark on 2016/07/10.
 */
public abstract class Sprite {
    public abstract void draw(float offsetX,float offsetY,Vector2 position,float sizeX,float sizeY,float alpha,float degree);
    public abstract void proc();
}
