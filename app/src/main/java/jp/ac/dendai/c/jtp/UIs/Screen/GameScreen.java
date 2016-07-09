package jp.ac.dendai.c.jtp.UIs.Screen;

import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Game.BitmapList;
import jp.ac.dendai.c.jtp.Game.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Enemy.Enemy;
import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.MainActivity;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.TouchUtil.TouchListener;
import jp.ac.dendai.c.jtp.UIs.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.UIs.UI.Text.Text;
import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/07/08.
 */
public class GameScreen implements Screenable {
    private static boolean isFreeze;
    public static BulletList bulletList;
    private Player player;
    private Enemy enemy;
    private NumberText nt;

    private int counter = 0; //でバグ

    public GameScreen(){
        isFreeze = false;
        bulletList = new BulletList(128);
        this.player = new Player(new CircleCollider(0.03f),BitmapList.setBitmap(R.drawable.plane),0,0.5f,0.2f,0.2f);
        this.enemy = new Enemy(BitmapList.setBitmap(R.drawable.plane),0.5f,1f,0.2f,0.2f);
        Input.getTouchArray()[0].addTouchListener(this.player);
        nt = new NumberText();
        nt.setHorizontalTextAlign(Text.TextAlign.LEFT);
        nt.setVerticalTextAlign(Text.TextAlign.CENTOR);
        nt.setWrittingAlign(Text.WrittingAlign.HOLIZONTAL);
    }

    public static void freeze(){
        isFreeze = true;
    }

    public static void unfreeze(){
        isFreeze = false;
    }

    public static boolean isFreeze(){
        return isFreeze;
    }

    public void deadProc(){

    }

    @Override
    public void Proc() {
        if(isFreeze())
            return;
        bulletList.isPlayerCollisionProc(player);
        if(player.isDead()){
            return;
        }
        enemy.proc();
        bulletList.update();
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        enemy.draw(offsetX, offsetY);
        player.Draw(offsetX, offsetY);
        bulletList.drawAll(offsetX,offsetY);
        nt.draw(player.getLife()-1,1,offsetX+0.5f,offsetY+0.5f,1f,1f, GLES20COMPOSITIONMODE.ALPHA);

        //Log.d("bulletList : ",bulletList.toString());
    }

    @Override
    public void Touch(MotionEvent event) {

    }

    @Override
    public void death() {
        Input.getTouchArray()[0].removeTouchListener(this.player);
    }
}
