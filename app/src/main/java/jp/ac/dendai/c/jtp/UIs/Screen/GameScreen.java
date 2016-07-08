package jp.ac.dendai.c.jtp.UIs.Screen;

import android.opengl.GLES20;
import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Game.BitmapList;
import jp.ac.dendai.c.jtp.Game.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Enemy.Enemy;
import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.TouchUtil.TouchListener;
import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by Goto on 2016/07/08.
 */
public class GameScreen implements Screenable {
    private Player player;
    private Enemy enemy;

    public GameScreen(){

        this.player = new Player(new CircleCollider(0.1f),BitmapList.setBitmap(R.drawable.plane),0,0,0.2f,0.2f);
        this.enemy = new Enemy(BitmapList.setBitmap(R.drawable.plane),0.5f,1f,0.2f,0.2f);
        Input.getTouchArray()[0].addTouchListener(this.player);
    }

    @Override
    public void Proc() {
        enemy.proc();
        BulletList.isCollision(player);
        BulletList.update();
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        enemy.draw(offsetX,offsetY);
        player.Draw(offsetX, offsetY);
        BulletList.drawAll(offsetX,offsetY);
    }

    @Override
    public void Touch(MotionEvent event) {

    }

    @Override
    public void death() {
        Input.getTouchArray()[0].removeTouchListener(this.player);
    }
}
