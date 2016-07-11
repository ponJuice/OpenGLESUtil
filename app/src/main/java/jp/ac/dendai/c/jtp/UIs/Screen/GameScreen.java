package jp.ac.dendai.c.jtp.UIs.Screen;

import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Game.Enemy.Boss;
import jp.ac.dendai.c.jtp.Game.Enemy.EnemyList;
import jp.ac.dendai.c.jtp.Game.Graphics.BackGround;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapContainer;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapList;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Enemy.Enemy;
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
import jp.ac.dendai.c.jtp.UIs.UI.UIAlign;
import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by Goto on 2016/07/08.
 */
public class GameScreen implements Screenable {
    public enum COLLISION_MASK{
        PLAYER(1),
        ENEMY(2),
        PLAYERBULLET(4),
        ENEMYBULLET(8);

        private final int id;

        private COLLISION_MASK(final int id) {
            this.id = id;
        }

        public int getInt() {
            return this.id;
        }
    }
    public enum GAMESTATE{
        PLAYING,
        GAMEOVER,
        PAUSE,
        CLEAR
    }
    private int gameEndTime = 300;
    public static int score;

    public static void setGameState(GAMESTATE state) {
        gameState = state;
    }

    private static GAMESTATE gameState = GAMESTATE.PLAYING;
    private static boolean isFreeze;
    public static EnemyList enemyList;
    public static BulletList bulletList;
    private Player player;
    private Enemy enemy;
    private NumberText nt;          //デバッグ用
    private NumberText scoreNumber;
    private Text scoreText;
    private Button backToMenuButton;
    private BitmapContainer bc;
    private int totalTime = 0;
    private BackGround background;
    private Text stageCrear,gameOverText;

    private int counter = 0; //でバグ

    public GameScreen(){
        gameState = GAMESTATE.PLAYING;
        isFreeze = true;
        score = 0;
        bulletList = new BulletList(128);
        this.player = new Player(new CircleCollider(0.03f),BitmapList.setBitmap(R.drawable.plane),0,0.5f,0.2f,0.2f);

        /****************************************************/
        //画像リソース読み込み
        BitmapList.setBitmap(R.drawable.damage_effect);
        BitmapList.setBitmap(R.drawable.back);

        /*****************************************************/

        /*****************************************************/
        //ゲームオーバー用ボタン

        gameOverText = new Text("GAME OVER",255,255,255);
        gameOverText.setScaleX(0.5f);
        gameOverText.setScaleY(0.5f);
        gameOverText.setHorizontalTextAlign(Text.TextAlign.CENTOR);
        gameOverText.setVerticalTextAlign(Text.TextAlign.CENTOR);

        backToMenuButton = new Button(GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f-0.1f,1f,0.3f,1f,"BACK TO TITLE",255,70,130,180);
        backToMenuButton.setEnable(false);
        backToMenuButton.setListener(new ButtonListener() {
            @Override
            public void execute(Button button) {
                GameManager.isTransition = true;
                ScrollTransition.getInstance().setScrollTime(10);
                ScrollTransition.getInstance().setDirect(GLES20Util.getAspect() * 2f, 0);
                //GameManager.nextScreen = new StageSelectScreen();
                GameManager.nextScreen = MenuScreen.getInstance();
                GameManager.transition = ScrollTransition.getInstance();
            }
        });

        /***************************************************/

        /***************************************************/
        //ゲームクリアボタン
        stageCrear = new Text("STAGE CLEAR",255,255,255);
        stageCrear.setScaleX(0.5f);
        stageCrear.setScaleY(0.5f);
        stageCrear.setHorizontalTextAlign(Text.TextAlign.CENTOR);
        stageCrear.setVerticalTextAlign(Text.TextAlign.CENTOR);

        /***************************************************/

        /***************************************************/
        //背景の設定
        //background = new BackGround(0,0.01f,1.506f,2f,BitmapList.getBitmap(R.drawable.back));
        background = new BackGround(0,-0.01f,1.506f,2f,BitmapList.getBitmap(R.drawable.back));

        /***************************************************/

        /***************************************************/
        //スコア表示の初期化
        scoreNumber = new NumberText();
        scoreNumber.setHorizontalTextAlign(Text.TextAlign.LEFT);
        scoreNumber.setVerticalTextAlign(Text.TextAlign.TOP);
        scoreNumber.setWrittingAlign(Text.WrittingAlign.HOLIZONTAL);

        scoreText = new Text("SCORE",255,255,255);
        scoreText.setHorizontalTextAlign(Text.TextAlign.LEFT);
        scoreText.setVerticalTextAlign(Text.TextAlign.TOP);

        /***************************************************/

        /****************************************************/
        //敵リスト作成
        enemyList = new EnemyList();

        /****************************************************/

        /****************************************************/
        //アニメーション読み込み
        bc = BitmapContainer.create(1,120,59,59,4,R.drawable.ship);     //爆発
        BitmapList.setAnimationBitmap(bc, R.drawable.ship);

        /*****************************************************/

        /*****************************************************/
        //デバッグ用
        nt = new NumberText();
        nt.setHorizontalTextAlign(Text.TextAlign.LEFT);
        nt.setVerticalTextAlign(Text.TextAlign.CENTOR);
        nt.setWrittingAlign(Text.WrittingAlign.HOLIZONTAL);

        /****************************************************/
        //敵1の作成
        this.enemy = new Enemy(BitmapList.setBitmap(R.drawable.enemy),0.5f,1f,0.2f,0.2f,0.05f,1,R.drawable.ship,10,40,100);
        //enemy.addAction(Action.movePointToPoint(GLES20Util.getWidth_gl(),GLES20Util.getHeight_gl(),GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f,180,180));
        enemy.addAction(Action.bezierMotion(GLES20Util.getWidth_gl(), GLES20Util.getHeight_gl(), 0, GLES20Util.getHeight_gl() / 2f, GLES20Util.getWidth_gl(), GLES20Util.getHeight_gl() / 2f, 180, 60));
        enemyList.addEnemys(enemy, 1);

        //敵1の作成
        this.enemy = new Enemy(BitmapList.setBitmap(R.drawable.enemy),0.5f,1f,0.2f,0.2f,0.05f,1,R.drawable.ship,10,40,100);
        //enemy.addAction(Action.movePointToPoint(GLES20Util.getWidth_gl(),GLES20Util.getHeight_gl(),GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f,180,180));
        enemy.addAction(Action.bezierMotion(0, GLES20Util.getHeight_gl(), GLES20Util.getWidth_gl(), GLES20Util.getHeight_gl() / 2f,0, GLES20Util.getHeight_gl() / 2f, 180, 60));
        enemyList.addEnemys(enemy, 1);

        //ボスの作成
        Boss boss = new Boss(BitmapList.setBitmap(R.drawable.boss),GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()+0.5f/2f,0.5f*2f,0.219f*2f,0.05f,100,R.drawable.ship,10,40,1000);
        boss.getCollider().setRadius(0.219f);
        boss.setStartTime(180);
        boss.addAction(Action.moveRelative(GLES20Util.getWidth_gl()/ 2f, GLES20Util.getHeight_gl() - 0.219f / 2f - 0.1f,
                60, 0));
        boss.addAction(Action.moveRelative(GLES20Util.getWidth_gl()-0.5f/2f,GLES20Util.getHeight_gl()-0.219f/2f-0.1f,
                        60,60));
        boss.addAction(Action.moveRelative(0.5f / 2f, GLES20Util.getHeight_gl() - 0.219f / 2f - 0.1f,
                120, 120));
        enemyList.addEnemys(boss, 1);
        //プレイヤーコントロール用リスナ登録
        Input.getTouchArray()[0].addTouchListener(this.player);


    }

    @Override
    public void freeze(){
        isFreeze = true;
    }

    @Override
    public void unFreeze(){
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
        else if(player.isDead()) {
            //ゲームオーバー
            gameState = GAMESTATE.GAMEOVER;
            return;
        }else if(gameState == GAMESTATE.CLEAR){
            player.freeze();
        }else if(player.getDamageFlag() != Player.DAMAGE_STATE.EFFECT) {
            //敵の移動
            enemyList.update();
            //敵と弾の衝突判定
            enemyList.playerBulletCollisionProc(player);
            //自機と弾の衝突判定
            bulletList.isPlayerCollisionProc(player);
            bulletList.update();
        }
        player.proc();
        background.proc();
        if(player.getDamageFlag() != Player.DAMAGE_STATE.EFFECT) {
            totalTime++;
        }
    }

    public static void addScore(int n){
        score += n;
    }

    private int debugFrameCounter = 0;

    @Override
    public void Draw(float offsetX, float offsetY) {
        //背景表示
        background.draw(offsetX, offsetY);

        enemyList.drawAll(offsetX, offsetY);
        player.Draw(offsetX, offsetY);
        bulletList.drawAll(offsetX, offsetY);

        //GLES20Util.DrawGraph(0.5f,0.5f,0.1f,0.1f,BitmapList.getAnimationBitmap(R.drawable.preset).getAt(debugFrameCounter),1f,GLES20COMPOSITIONMODE.ALPHA);
        //debugFrameCounter++;

        if(gameState == GAMESTATE.GAMEOVER){
            backToMenuButton.setEnable(true);
            gameOverText.draw(offsetX + GLES20Util.getWidth_gl() / 2f,offsetY + GLES20Util.getHeight_gl() / 2f + 0.3f, 1f, GLES20COMPOSITIONMODE.ALPHA);;
            backToMenuButton.draw(offsetX,offsetY);
        }else if(gameState == GAMESTATE.CLEAR){
            backToMenuButton.setEnable(true);
            stageCrear.draw(offsetX + GLES20Util.getWidth_gl()/2f,offsetY +GLES20Util.getHeight_gl()/2f+0.3f,1f,GLES20COMPOSITIONMODE.ALPHA);
            backToMenuButton.draw(offsetX,offsetY);
        }

        //残基表示
        for(int n = 0;n < player.getLife();n++){
            GLES20Util.DrawGraph(offsetX + GLES20Util.getWidth_gl() - 0.05f - 0.05f*n,
                    offsetY + GLES20Util.getHeight_gl()-0.05f,
                    0.05f,
                    0.05f,
                    BitmapList.getBitmap(R.drawable.plane),
                    1.0f,
                    GLES20COMPOSITIONMODE.ALPHA);
        }

        //スコア表示
        scoreText.draw(offsetX+0.05f,offsetY+GLES20Util.getHeight_gl()-0.1f,1f,GLES20COMPOSITIONMODE.ALPHA);
        scoreNumber.draw(score,4,offsetX+0.05f,offsetY+GLES20Util.getHeight_gl()-scoreText.getLengthY()-0.15f,1f,1f,GLES20COMPOSITIONMODE.ALPHA);

        //でバグ
        nt.draw(bulletList.size(),1,offsetX+0.5f, offsetY + 0.5f, 1f,1f,GLES20COMPOSITIONMODE.ALPHA);

        //Log.d("bulletList : ",bulletList.toString());
    }

    @Override
    public void Touch(MotionEvent event) {
        backToMenuButton.touch(event);
    }

    @Override
    public void death() {
        Input.getTouchArray()[0].removeTouchListener(this.player);
        enemyList = null;
        BitmapList.clear();
        bulletList = null;
    }
}
