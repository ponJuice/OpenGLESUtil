package jp.ac.dendai.c.jtp.UIs.Screen;

import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Game.Graphics.AnimationSprite;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapContainer;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapList;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Enemy.TestEnemy;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.Motion.Action;
import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.UIs.Transition.ScrollTransition;
import jp.ac.dendai.c.jtp.UIs.UI.Button;
import jp.ac.dendai.c.jtp.UIs.UI.Listener.ButtonListener;
import jp.ac.dendai.c.jtp.UIs.UI.Text.NumberText;
import jp.ac.dendai.c.jtp.UIs.UI.Text.Text;
import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/07/08.
 */
public class GameScreen implements Screenable {
    public enum GAMESTATE{
        PLAYING,
        GAMEOVER,
        PAUSE
    }
    private GAMESTATE gameState = GAMESTATE.PLAYING;
    private static boolean isFreeze;
    public static BulletList bulletList;
    private Player player;
    private TestEnemy enemy;
    private NumberText nt;
    private Button gameOverButton;

    private int counter = 0; //でバグ

    public GameScreen(){
        isFreeze = false;
        bulletList = new BulletList(128);
        this.player = new Player(new CircleCollider(0.03f),BitmapList.setBitmap(R.drawable.plane),0,0.5f,0.2f,0.2f);

        /****************************************************/
        //敵の作成
        this.enemy = new TestEnemy(BitmapList.setBitmap(R.drawable.plane),0.5f,1f,0.2f,0.2f);
        //enemy.addAction(Action.movePointToPoint(GLES20Util.getWidth_gl(),GLES20Util.getHeight_gl(),GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f,180,180));
        enemy.addAction(Action.bezierMotion(GLES20Util.getWidth_gl(),GLES20Util.getHeight_gl(),0,GLES20Util.getHeight_gl()/2f,GLES20Util.getWidth_gl(),GLES20Util.getHeight_gl()/2f,180,180));

        /****************************************************/
        //アニメーション読み込み
        BitmapList.setAnimationBitmap(BitmapContainer.create(1,120,59,59,4,R.drawable.ship),R.drawable.ship);     //爆発

        Input.getTouchArray()[0].addTouchListener(this.player);
        nt = new NumberText();
        nt.setHorizontalTextAlign(Text.TextAlign.LEFT);
        nt.setVerticalTextAlign(Text.TextAlign.CENTOR);
        nt.setWrittingAlign(Text.WrittingAlign.HOLIZONTAL);

        /****************************************************/
        //画像リソース読み込み
        BitmapList.setBitmap(R.drawable.damage_effect);

        gameOverButton = new Button(GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f,1f,0.3f,1f,"GAMEOVER",255,70,130,180);
        gameOverButton.setEnable(false);
        gameOverButton.setListener(new ButtonListener() {
            @Override
            public void execute(Button button) {
                GameManager.isTransition = true;
                ScrollTransition.getInstance().setScrollTime(10);
                ScrollTransition.getInstance().setDirect(GLES20Util.getAspect()*2f, 0);
                //GameManager.nextScreen = new StageSelectScreen();
                GameManager.nextScreen = MenuScreen.getInstance();
                GameManager.transition = ScrollTransition.getInstance();
            }
        });
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
        if(player.isDead() && player.getDamageFlag() == Player.DAMAGE_STATE.EFFECT){
            player.proc();
            return;
        }
        else if(player.isDead()){
            gameState = GAMESTATE.GAMEOVER;
            return;
        }
        else if(player.getDamageFlag() != Player.DAMAGE_STATE.EFFECT){
            //ダメージをウケテナイ
            bulletList.isPlayerCollisionProc(player);
            bulletList.update();
            enemy.proc();
        }
        player.proc();
    }

    @Override
    public void Draw(float offsetX, float offsetY) {
        enemy.draw(offsetX, offsetY);
        player.Draw(offsetX, offsetY);
        bulletList.drawAll(offsetX, offsetY);
        nt.draw(player.getLife()-1,1,offsetX+0.5f,offsetY+0.5f,1f,1f,GLES20COMPOSITIONMODE.ALPHA);

        if(gameState == GAMESTATE.GAMEOVER){
            gameOverButton.setEnable(true);
            gameOverButton.draw(offsetX,offsetY);
        }

        //Log.d("bulletList : ",bulletList.toString());
    }

    @Override
    public void Touch(MotionEvent event) {
        gameOverButton.touch(event);
    }

    @Override
    public void death() {
        Input.getTouchArray()[0].removeTouchListener(this.player);
    }
}
