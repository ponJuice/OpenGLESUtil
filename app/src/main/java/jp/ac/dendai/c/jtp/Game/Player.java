package jp.ac.dendai.c.jtp.Game;

import java.util.Objects;

import jp.ac.dendai.c.jtp.Game.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Enemy.Enemy;
import jp.ac.dendai.c.jtp.Physics.Collider.ICollider;
import jp.ac.dendai.c.jtp.TouchUtil.Input;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;
import jp.ac.dendai.c.jtp.TouchUtil.TouchListener;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;
import jp.ac.dendai.c.jtp.UIs.Screen.GameScreen;
import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * 自機制御用クラス
 * @author
 *
 */
public class Player extends Bullet implements TouchListener{
	protected  int life = 3;
	protected  boolean damage = false;
	protected int damageCounter = 0,damageTime = 180;
	protected boolean flag = false;
	protected float alpha = 1;
	/**
	 * 排他制御用のオブジェクト
	 */
	protected Object lock;
	/**
	 * コンストラクタ
	 */
	public Player(ICollider col,int id,float x,float y,float sizeX,float sizeY){
		super(col,id,x,y,0,0,sizeX,sizeY);
		lock = new Object();
	}
	/**
	 * ゲッタ
	 * @return　サイズX
	 */
	public float getSizeX(){
		return sizeX;
	}
	/**
	 * ゲッタ
	 * @return　サイズY
	 */
	public float getSizeY(){
		return sizeY;
	}
	/**
	 * セッタ
	 */
	public void setGraphId(int id){
		imageId = id;
	}
	/**
	 * ゲッタ
	 * @return　画像ID
	 */
	public int getGraphId(){
		return imageId;
	}
	/**
	 * 画面外に出ないようにする関数
	 */
	public void regulation(){
		if(position.getX() < 0.0f){
			position.setX(0.0f);
		}
		if(position.getX() > (GLES20Util.getAspect()*2.0f)-(1.0f*sizeX)){
			position.setX((GLES20Util.getAspect()*2.0f)-(1.0f*sizeX));
		}
		if(position.getY() < 0.0f){
			position.setY(0.0f);
		}
		if(position.getY() > 2.0f-(1.0f*sizeY)){
			position.setY(2.0f-(1.0f*sizeY));
		}
		//Log.d("debug[Player][regulation]",String.valueOf(GLES20Util.getAspect()*2.0f));
	}
	/**
	 * 表示
	 * GLES20Util依存なので注意
	 */
	public void Draw(float offsetX,float offsetY){//true 点有
		synchronized (lock) {
			if(damage){
				if(damageCounter % 30 == 0){
					if(flag) {
						alpha = 0.5f;
						flag = !flag;
					}
					else {
						alpha = 1.0f;
						flag = !flag;
					}
				}
				damageCounter++;
				if(damageTime <= damageCounter) {
					alpha = 1.0f;
					damage = false;
					damageCounter = 0;
				}
			}
			GLES20Util.DrawGraph(position.getX() + offsetX, position.getY() + offsetY, sizeX, sizeY, BitmapList.getBitmap(imageId), 1.0f, GLES20COMPOSITIONMODE.ALPHA);
			GLES20Util.DrawGraph(position.getX() + offsetX, position.getY() + offsetY, collider.getRadius()*2.0f, collider.getRadius()*2.0f, BitmapList.getBitmap(R.drawable.bomd2), alpha, GLES20COMPOSITIONMODE.ALPHA);
		}
	}

	public int getLife(){
		return life;
	}

	public boolean isDamage(){
		return damage;
	}

	public float getPosition(Touch.Pos_Flag pos) {
		synchronized (lock) {
			regulation();
			if (pos == Touch.Pos_Flag.X)
				return position.getX();
			return position.getY();
		}
	}

	public void collisionBulletProc(){
		life--;
		damage = true;
	}

	public boolean isDead(){
		return life <= 0;
	}

	@Override
	public Vector2 getPosition(){
		synchronized (lock){
			buffer2D.copy(position);
		}
		return buffer2D;
	}

	@Override
	public void execute(Touch t) {
		if(GameScreen.isFreeze() || isDead())
			return;
		synchronized (lock) {
			position.setX(position.getX() - GLES20Util.getWidth_gl() / GLES20Util.getWidth() * (t.getDelta(Touch.Pos_Flag.X) * Constant.getSens()));
			position.setY(position.getY() + GLES20Util.getHeight_gl() / GLES20Util.getHight() * (t.getDelta(Touch.Pos_Flag.Y) * Constant.getSens()));
			regulation();
		}
	}
}
