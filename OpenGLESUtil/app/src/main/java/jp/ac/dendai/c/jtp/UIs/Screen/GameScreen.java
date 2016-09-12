package jp.ac.dendai.c.jtp.UIs.Screen;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Game.Bullet.Barrage.Barrage;
import jp.ac.dendai.c.jtp.Game.Bullet.Barrage.LinearOneBarrage;
import jp.ac.dendai.c.jtp.Game.Bullet.Barrage.Radiationbarrage;
import jp.ac.dendai.c.jtp.Game.Enemy.Boss;
import jp.ac.dendai.c.jtp.Game.Enemy.EnemyList;
import jp.ac.dendai.c.jtp.Game.Graphics.BackGround;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapContainer;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapList;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Enemy.Enemy;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.Item.BulletUp;
import jp.ac.dendai.c.jtp.Game.Item.Item;
import jp.ac.dendai.c.jtp.Game.Item.LifeUp;
import jp.ac.dendai.c.jtp.Game.Motion.Action;
import jp.ac.dendai.c.jtp.Game.Player.Player;
import jp.ac.dendai.c.jtp.Game.Sound.IDPack;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics2D;
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
    private boolean victory_se_flag = false,gameover_se_flag = false;
    private Bitmap black;
    private Player player;
    private Enemy enemy;
    private NumberText nt; //リザルトスコア
    private NumberText listNum;
    private NumberText scoreNumber;
    private Text scoreText,zankiAdd,total;
    private Button backToMenuButton;
    private BitmapContainer bc;
    private int totalTime = 0;
    private BackGround background;
    private Text stageCrear,gameOverText;
    protected int clearEffectCounter=0;
    protected boolean useBomd = false;
    protected Physics2D bomd;
    protected int bomdCounter = 0;
    protected int bomd_nokori = 3;
    protected SoundPool effect,player_se,boss_se,item_sp;
    protected IDPack destroy = new IDPack(),lifeUp_se= new IDPack(),bulletUp_se= new IDPack(),bomd_se= new IDPack(),hit_se= new IDPack(),boss_destroy_se= new IDPack(),victory_se= new IDPack();
    protected MediaPlayer mediaPlayer;

    private int counter = 0; //でバグ

    public GameScreen(){
        gameState = GAMESTATE.PLAYING;
        isFreeze = true;
        score = 0;
        bulletList = new BulletList(128);
        this.player = new Player(new CircleCollider(0.03f),BitmapList.setBitmap(R.drawable.plane),0,0.5f,0.2f,0.2f);
        bomd = new Physics2D();
        bomd.setCollider(new CircleCollider(1f));
        bomd.getCollider().setMask(COLLISION_MASK.ENEMYBULLET.getInt());

        /****************************************************/
        //画像リソース読み込み
        BitmapList.setBitmap(R.drawable.damage_effect);
        BitmapList.setBitmap(R.drawable.back);
        black = GLES20Util.createBitmap(0,0,0,255);
        BitmapList.setBitmap(R.drawable.bomd_item);

        //効果音の読み込み
        resume();

        /*****************************************************/

        /*****************************************************/
        //ゲームオーバー用ボタン

        gameOverText = new Text("GAME OVER",255,29,0);
        gameOverText.setScaleX(1.5f);
        gameOverText.setScaleY(1.5f);
        gameOverText.setHorizontalTextAlign(Text.TextAlign.CENTOR);
        gameOverText.setVerticalTextAlign(Text.TextAlign.CENTOR);

        backToMenuButton = new Button(GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f-0.1f,1f,0.3f,1f,"BACK TO TITLE",255,70,130,180);
        backToMenuButton.setEnable(false);
        backToMenuButton.setListener(new ButtonListener() {
            @Override
            public void execute(Button button) {
                GameManager.sp.play(GameManager.button,1f,1f,0,0,1f);
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
        stageCrear = new Text("STAGE CLEAR",82,162,197);
        stageCrear.setScaleX(1.5f);
        stageCrear.setScaleY(1.5f);
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
        scoreText.setScaleY(0.5f);
        scoreText.setScaleX(0.5f);

        zankiAdd = new Text(" + 200×残機",255,255,255);
        zankiAdd.setHorizontalTextAlign(Text.TextAlign.LEFT);
        zankiAdd.setVerticalTextAlign(Text.TextAlign.BOTTOM);
        zankiAdd.setScaleX(0.7f);
        zankiAdd.setScaleY(0.7f);

        total = new Text("TOTAL : ",255,255,255);
        total.setHorizontalTextAlign(Text.TextAlign.LEFT);
        total.setVerticalTextAlign(Text.TextAlign.BOTTOM);
        total.setScaleX(1.3f);
        total.setScaleY(1.3f);
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
        //リザルトスコア
        nt = new NumberText();
        nt.setHorizontalTextAlign(Text.TextAlign.LEFT);
        nt.setVerticalTextAlign(Text.TextAlign.BOTTOM);
        nt.setWrittingAlign(Text.WrittingAlign.HOLIZONTAL);

        //弾リストの弾数表示用
        listNum = new NumberText();
        listNum.setHorizontalTextAlign(Text.TextAlign.LEFT);
        listNum.setVerticalTextAlign(Text.TextAlign.BOTTOM);
        listNum.setWrittingAlign(Text.WrittingAlign.HOLIZONTAL);

        /****************************************************/
        //敵1の作成
        this.enemy = new Enemy(BitmapList.setBitmap(R.drawable.enemy),0.5f,1f,0.2f,0.2f,0.05f,1,R.drawable.ship,10,40,100);
        //enemy.addAction(Action.movePointToPoint(GLES20Util.getWidth_gl(),GLES20Util.getHeight_gl(),GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f,180,180));
        enemy.addAction(Action.movePointToPoint(GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()+0.5f,GLES20Util.getWidth_gl()/2f,-0.5f,120,60));
        enemy.getBulletTemplate().uy = -0.05f;
        Item i = new LifeUp(BitmapList.getBitmap(BitmapList.setBitmap(R.drawable.life_up)));
        i.sp = item_sp;
        i.se = lifeUp_se;
        enemy.setItem(i);
        enemy.setSoundPool(effect);
        enemy.setDestroyNum(destroy);
        enemyList.addEnemys(enemy, 1);

        //敵1の作成
        this.enemy = new Enemy(BitmapList.setBitmap(R.drawable.enemy),0.5f,1f,0.2f,0.2f,0.05f,1,R.drawable.ship,10,40,100);
        //enemy.addAction(Action.movePointToPoint(GLES20Util.getWidth_gl(),GLES20Util.getHeight_gl(),GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f,180,180));
        enemy.addAction(Action.bezierMotion(GLES20Util.getWidth_gl(), GLES20Util.getHeight_gl(), 0, GLES20Util.getHeight_gl() / 2f, GLES20Util.getWidth_gl(), GLES20Util.getHeight_gl() / 2f, 180, 60));
        i = new BulletUp(BitmapList.getBitmap(BitmapList.setBitmap(R.drawable.power)));
        i.sp = item_sp;
        i.se = bulletUp_se;
        enemy.setItem(i);
        enemy.setSoundPool(effect);
        enemy.setDestroyNum(destroy);
        enemyList.addEnemys(enemy, 1);

        //敵1の作成
        this.enemy = new Enemy(BitmapList.setBitmap(R.drawable.enemy),0.5f,1f,0.2f,0.2f,0.05f,1,R.drawable.ship,10,40,100);
        //enemy.addAction(Action.movePointToPoint(GLES20Util.getWidth_gl(),GLES20Util.getHeight_gl(),GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f,180,180));
        enemy.addAction(Action.bezierMotion(0, GLES20Util.getHeight_gl(), GLES20Util.getWidth_gl(), GLES20Util.getHeight_gl() / 2f,0, GLES20Util.getHeight_gl() / 2f, 180, 60));
        i = new BulletUp(BitmapList.getBitmap(R.drawable.power));
        i.sp = item_sp;
        i.se = bulletUp_se;
        enemy.setItem(i);
        enemy.setSoundPool(effect);
        enemy.setDestroyNum(destroy);
        enemyList.addEnemys(enemy, 1);

        //ボスの作成
        Boss boss = new Boss(BitmapList.setBitmap(R.drawable.boss),GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()+0.5f/2f,0.5f*2f,0.219f*2f,0.05f,100,R.drawable.ship,10,40,1000);
        boss.sp = boss_se;
        boss.hit_se = hit_se;
        boss.destroy_se = boss_destroy_se;
        boss.getCollider().setRadius(0.219f);
        boss.setStartTime(180);
        boss.addAction(Action.moveRelative(GLES20Util.getWidth_gl() / 2f, GLES20Util.getHeight_gl() - 0.219f / 2f - 0.1f, 60, 0));
        boss.addAction(Action.moveRelative(GLES20Util.getWidth_gl()-0.5f/2f,GLES20Util.getHeight_gl()-0.219f / 2f - 0.1f, 60,60));
        boss.addAction(Action.moveRelative(0.5f / 2f, GLES20Util.getHeight_gl() - 0.219f / 2f - 0.1f, 120, 120));
        boss.addAction(Action.moveRelative(GLES20Util.getWidth_gl() / 2f, GLES20Util.getHeight_gl() - 0.219f / 2f - 0.1f, 60, 240));
        boss.addAction(Action.moveRelative(GLES20Util.getWidth_gl() / 2f, GLES20Util.getHeight_gl() - 0.219f / 2f - 0.1f, 300, 300));
        //攻撃パターン
        Barrage temp = new LinearOneBarrage(R.drawable.bomd2, GameScreen.COLLISION_MASK.PLAYER.getInt(), GameScreen.COLLISION_MASK.ENEMYBULLET.getInt(), 0, -0.05f, 5, 0, 300);
        boss.addBarrage(temp);
        temp = new Radiationbarrage(R.drawable.bomd2, GameScreen.COLLISION_MASK.PLAYER.getInt(),GameScreen.COLLISION_MASK.ENEMYBULLET.getInt(),280,300,5,10,0,10f,0.01f);
        boss.addBarrage(temp);

        enemyList.addEnemys(boss, 1);
        //プレイヤーコントロール用リスナ登録
        Input.getTouchArray()[0].addTouchListener(this.player);

        //音楽の設定
        player.setSoundPool(effect);

    }

    @Override
    public void freeze(){
        isFreeze = true;
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void unFreeze(){
        isFreeze = false;
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public static boolean isFreeze(){
        return isFreeze;
    }

    public void deadProc(){

    }

    @Override
    public void Proc() {
        if(isFreeze()) {
            return;
        }
        if(player.isDead() && player.getDamageFlag() == Player.DAMAGE_STATE.EFFECT){
            player.proc();
            return;
        }
        else if(player.isDead()) {
            //ゲームオーバー
            if(!gameover_se_flag){
                //effect.play(gameover_se,1f,1f,0,0,1f);
                mediaPlayer.setVolume(0.5f,0.5f);
                gameover_se_flag = true;
            }
            gameState = GAMESTATE.GAMEOVER;
            clearEffectCounter++;
            return;
        }else if(gameState == GAMESTATE.CLEAR){
            if(!victory_se_flag){
                effect.play(victory_se.id,1f,1f,0,0,1f);
                mediaPlayer.setVolume(0,0);
                victory_se_flag = true;
            }
            player.freeze();
            clearEffectCounter++;
        }else if(player.getDamageFlag() != Player.DAMAGE_STATE.EFFECT) {
            //ボム使用判定
            if(!useBomd && Input.getTouchCount() >= 2 && bomd_nokori > 0 && !useBomd){
                bomd_nokori--;
                useBomd = true;
                bomd.setPosition(player.getPosition());
                effect.play(bomd_se.id,1f,1f,0,0,1f);
            }
            if(useBomd) {
                if(bomdCounter <= 120) {
                    bomd.getCollider().setRadius((float)bomdCounter/100);
                    bulletList.playerBomdCollision(bomd);
                    bomdCounter++;
                }else{
                    bomdCounter = 0;
                    useBomd = false;
                }
            }
            //敵の移動
            enemyList.update();
            //敵と弾の衝突判定
            enemyList.playerBulletCollisionProc(player);
            //自機と弾の衝突判定
            bulletList.isPlayerCollisionProc(player);
            bulletList.update();
            //アイテムリスト
            Item.collisionSearch(player);
            Item.gravity();
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

        //ボムの表示
        if(useBomd) {
            if(bomdCounter < 60)
                GLES20Util.DrawGraph(bomd.getPosition().getX(), bomd.getPosition().getY(), bomd.getCollider().getRadius() * 2f, bomd.getCollider().getRadius() * 2f, BitmapList.getBitmap(R.drawable.damage_effect), 1f/60f*bomdCounter, GLES20COMPOSITIONMODE.ADD);
            else
                GLES20Util.DrawGraph(bomd.getPosition().getX(), bomd.getPosition().getY(), bomd.getCollider().getRadius() * 2f, bomd.getCollider().getRadius() * 2f, BitmapList.getBitmap(R.drawable.damage_effect), 1f - 1f/120f*bomdCounter, GLES20COMPOSITIONMODE.ADD);
        }

        enemyList.drawAll(offsetX, offsetY);
        player.Draw(offsetX, offsetY);
        bulletList.drawAll(offsetX, offsetY);
        //アイテムリスト
        Item.drawAll();

        //GLES20Util.DrawGraph(0.5f,0.5f,0.1f,0.1f,BitmapList.getAnimationBitmap(R.drawable.preset).getAt(debugFrameCounter),1f,GLES20COMPOSITIONMODE.ALPHA);
        //debugFrameCounter++;

        /*if(gameState == GAMESTATE.GAMEOVER){
            backToMenuButton.setEnable(true);
            gameOverText.draw(offsetX + GLES20Util.getWidth_gl() / 2f,offsetY + GLES20Util.getHeight_gl() / 2f + 0.3f, 1f, GLES20COMPOSITIONMODE.ALPHA);
            backToMenuButton.draw(offsetX,offsetY);*/

        float alpha = 1f;
        if(gameState == GAMESTATE.CLEAR || gameState == GAMESTATE.GAMEOVER)
            alpha = 0.5f;
        //残基表示
        for(int n = 0;n < player.getLife()-1;n++){
            GLES20Util.DrawGraph(offsetX + GLES20Util.getWidth_gl() - 0.05f - 0.1f*n,
                    offsetY + GLES20Util.getHeight_gl()-0.05f,
                    0.1f,
                    0.1f,
                    BitmapList.getBitmap(R.drawable.plane),
                    1.0f,
                    GLES20COMPOSITIONMODE.ALPHA);
        }
        //ボム残り表示
        for(int n = 0;n < bomd_nokori;n++){
            GLES20Util.DrawGraph(offsetX + GLES20Util.getWidth_gl() - 0.05f - 0.1f*n,
                    offsetY + GLES20Util.getHeight_gl()-0.2f,
                    0.1f,
                    0.1f,
                    BitmapList.getBitmap(R.drawable.bomd_item),
                    1.0f,
                    GLES20COMPOSITIONMODE.ALPHA);
        }

        if(gameState == GAMESTATE.CLEAR || gameState == GAMESTATE.GAMEOVER){
            if(clearEffectCounter <= 120){
                GLES20Util.DrawGraph(GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f,GLES20Util.getWidth(),GLES20Util.getHeight_gl(),black,clearEffectCounter/120f*0.8f,GLES20COMPOSITIONMODE.ALPHA);
            }else if(clearEffectCounter <= 180){
                GLES20Util.DrawGraph(GLES20Util.getWidth_gl() / 2f, GLES20Util.getHeight_gl() / 2f, GLES20Util.getWidth(), GLES20Util.getHeight_gl(), black, 0.8f, GLES20COMPOSITIONMODE.ALPHA);
                if(gameState == GAMESTATE.GAMEOVER){
                    gameOverText.draw(offsetX + GLES20Util.getWidth_gl() / 2f,offsetY + GLES20Util.getHeight_gl() / 2f + 0.8f, 1f, GLES20COMPOSITIONMODE.ALPHA);
                }else{
                    stageCrear.draw(offsetX + GLES20Util.getWidth_gl() / 2f, offsetY + GLES20Util.getHeight_gl() / 2f + 0.8f, 1f, GLES20COMPOSITIONMODE.ALPHA);
                }
                scoreText.setScaleX(1.2f);
                scoreText.setScaleY(1.2f);
                scoreText.setVerticalTextAlign(Text.TextAlign.BOTTOM);
                int a = clearEffectCounter - 120;
                scoreText.draw(offsetX + 0.05f
                        , offsetY + GLES20Util.getHeight_gl() / 2f + 0.5f
                        , 1f
                        , GLES20COMPOSITIONMODE.ALPHA);
                float buff = nt.draw(score / 60 * a,4
                        ,offsetX+0.05f + 0.05f + scoreText.getLengthX()
                        ,offsetY + GLES20Util.getHeight_gl() / 2f + 0.5f
                        ,1f,1,GLES20COMPOSITIONMODE.ALPHA);
            }else{
                backToMenuButton.setEnable(true);
                GLES20Util.DrawGraph(GLES20Util.getWidth_gl() / 2f, GLES20Util.getHeight_gl() / 2f, GLES20Util.getWidth(), GLES20Util.getHeight_gl(), black, 0.8f, GLES20COMPOSITIONMODE.ALPHA);
                if(gameState == GAMESTATE.GAMEOVER){
                    gameOverText.draw(offsetX + GLES20Util.getWidth_gl() / 2f,offsetY + GLES20Util.getHeight_gl() / 2f + 0.8f, 1f, GLES20COMPOSITIONMODE.ALPHA);
                }else {
                    stageCrear.draw(offsetX + GLES20Util.getWidth_gl() / 2f, offsetY + GLES20Util.getHeight_gl() / 2f + 0.8f, 1f, GLES20COMPOSITIONMODE.ALPHA);
                }
                scoreText.draw(offsetX + 0.05f
                        , offsetY + GLES20Util.getHeight_gl() / 2f + 0.5f
                        , 1f
                        , GLES20COMPOSITIONMODE.ALPHA);
                float buff = nt.draw(score,4
                        ,offsetX+0.05f + 0.05f + scoreText.getLengthX()
                        ,offsetY + GLES20Util.getHeight_gl() / 2f + 0.5f
                        ,1f,1,GLES20COMPOSITIONMODE.ALPHA);
                zankiAdd.draw(
                        offsetX+0.05f + 0.05f + scoreText.getLengthX()
                        ,offsetY + GLES20Util.getHeight_gl() / 2f + 0.4f
                        ,1f,GLES20COMPOSITIONMODE.ALPHA);
                total.draw(offsetX + 0.05f
                    ,offsetY + GLES20Util.getHeight_gl() / 2f + 0.2f
                    ,1f,GLES20COMPOSITIONMODE.ALPHA);
                if(gameState == GAMESTATE.CLEAR) {
                    nt.draw(score + (200 * (player.getLife() - 1)), 4
                            , total.getLengthX() + 0.05f
                            , offsetY + GLES20Util.getHeight_gl() / 2f + 0.21f
                            , 1.3f, 1, GLES20COMPOSITIONMODE.ALPHA);
                }else{
                    nt.draw(score, 4
                            , total.getLengthX() + 0.05f
                            , offsetY + GLES20Util.getHeight_gl() / 2f + 0.21f
                            , 1.3f, 1, GLES20COMPOSITIONMODE.ALPHA);
                }
                backToMenuButton.draw(offsetX, offsetY);
            }
        }

        if(gameState == GAMESTATE.PLAYING) {
            //スコア表示
            GLES20Util.DrawGraph(GLES20Util.getWidth_gl()/8f,GLES20Util.getHeight_gl()-GLES20Util.getHeight_gl()/14f+0.08f,GLES20Util.getWidth_gl()/4f+0.1f,GLES20Util.getHeight_gl()/7f-0.08f,black,0.3f,GLES20COMPOSITIONMODE.ALPHA);
            scoreText.draw(offsetX + 0.05f, offsetY + GLES20Util.getHeight_gl(), 1f, GLES20COMPOSITIONMODE.ALPHA);
            scoreNumber.draw(score, 4, offsetX + 0.05f, offsetY + GLES20Util.getHeight_gl() - scoreText.getLengthY(), 1f, 1f, GLES20COMPOSITIONMODE.ALPHA);
        }

        //でバグ
        //nt.draw(bulletList.size(),1,offsetX+0.5f, offsetY + 0.5f, 1f,1f,GLES20COMPOSITIONMODE.ALPHA);
        //listNum.draw(bulletList.size(),3,0,0.5f,1f,1f,GLES20COMPOSITIONMODE.ALPHA);

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
        Item.clear();
        effect.release();
        player_se.release();
        item_sp.release();
        boss_se.release();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    public void pause() {
        effect.release();
        player_se.release();
        boss_se.release();
        item_sp.release();
        mediaPlayer.release();
    }

    @Override
    public void resume() {
        // 予め音声データを読み込む
        effect = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        destroy.id = effect.load(GameManager.act.getApplicationContext(), R.raw.destruction1, 1);
        destroy.sp = effect;
        bomd_se.id = effect.load(GameManager.act.getApplicationContext(), R.raw.bomd, 1);
        bomd_se.sp = effect;
        victory_se.id = effect.load(GameManager.act.getApplicationContext(), R.raw.victory, 1);
        victory_se.sp = effect;

        player_se = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        player.destroy_se.id = player_se.load(GameManager.act.getApplicationContext(), R.raw.destroy, 1);
        player.destroy_se.sp = player_se;
        player.destroy_effect_se.id = player_se.load(GameManager.act.getApplicationContext(), R.raw.destroy_effect, 1);
        player.destroy_effect_se.sp  = player_se;
        player.attack_se.id = player_se.load(GameManager.act.getApplicationContext(), R.raw.laser1, 1);
        player.attack_se.sp = player_se;

        boss_se = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        hit_se.id = boss_se.load(GameManager.act.getApplicationContext(), R.raw.hit, 1);
        hit_se.sp = boss_se;
        boss_destroy_se.id = boss_se.load(GameManager.act.getApplicationContext(), R.raw.boss_destroy, 1);
        boss_destroy_se.sp = boss_se;

        item_sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        lifeUp_se.id = item_sp.load(GameManager.act.getApplicationContext(), R.raw.life_up, 1);
        lifeUp_se.sp = item_sp;
        bulletUp_se.id = item_sp.load(GameManager.act.getApplicationContext(), R.raw.bullet_up, 1);
        bulletUp_se.sp = item_sp;

        mediaPlayer = MediaPlayer.create(GameManager.act.getApplicationContext(), R.raw.bgm);
        mediaPlayer.setLooping(true);
    }
}
