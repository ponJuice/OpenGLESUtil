package jp.ac.dendai.c.jtp.Game;

import jp.ac.dendai.c.jtp.Game.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.Game.Graphics.AnimationSprite;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapList;
import jp.ac.dendai.c.jtp.Physics.Collider.ICollider;
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
public class Player extends Bullet implements TouchListener {
	public enum DAMAGE_STATE {
		NON,
		EFFECT,
		INVISIBLE,
	}

	protected DAMAGE_STATE d_state = DAMAGE_STATE.NON;
	protected int life = 3;
	protected int damageEffectTime = 60;
	protected int damageCounter = 0, damageTime = 180;
	protected boolean flag = false;
	protected float alpha = 1;
	protected AnimationSprite explosion;
	protected int attackRate = 10;
	protected int totalTime = 0;
	protected BulletTemplate bt;
	/**
	 * 排他制御用のオブジェクト
	 */
	protected Object lock;

	/**
	 * コンストラクタ
	 */
	public Player(ICollider col, int id, float x, float y, float sizeX, float sizeY) {
		super(col, id, x, y, 0, 0, sizeX, sizeY
				,GameScreen.COLLISION_MASK.ENEMYBULLET.getInt() & GameScreen.COLLISION_MASK.ENEMY.getInt()
				,GameScreen.COLLISION_MASK.PLAYER.getInt());
		degree = 45f;
		lock = new Object();
		explosion = new AnimationSprite(R.drawable.ship,10,-1,GLES20COMPOSITIONMODE.ADD);
		bt = new BulletTemplate();
	}

	/**
	 * ゲッタ
	 *
	 * @return　サイズX
	 */
	public float getSizeX() {
		return sizeX;
	}

	/**
	 * ゲッタ
	 *
	 * @return　サイズY
	 */
	public float getSizeY() {
		return sizeY;
	}

	/**
	 * セッタ
	 */
	public void setGraphId(int id) {
		imageId = id;
	}

	/**
	 * ゲッタ
	 *
	 * @return　画像ID
	 */
	public int getGraphId() {
		return imageId;
	}

	/**
	 * 画面外に出ないようにする関数
	 */
	public void regulation() {
		if (position.getX() < 0.0f) {
			position.setX(0.0f);
		}
		if (position.getX() > (GLES20Util.getAspect() * 2.0f) - (1.0f * sizeX)) {
			position.setX((GLES20Util.getAspect() * 2.0f) - (1.0f * sizeX));
		}
		if (position.getY() < 0.0f) {
			position.setY(0.0f);
		}
		if (position.getY() > 2.0f - (1.0f * sizeY)) {
			position.setY(2.0f - (1.0f * sizeY));
		}
		//Log.d("debug[Player][regulation]",String.valueOf(GLES20Util.getAspect()*2.0f));
	}

	public void proc() {
		if (d_state == DAMAGE_STATE.EFFECT) {
			if (damageCounter < damageEffectTime) {
				damageCounter++;
			}
			else if(damageCounter <= damageEffectTime + 40){
				damageCounter++;
				explosion.proc();
			}
			else {
				explosion.resetAnimation();
				d_state = DAMAGE_STATE.INVISIBLE;
				damageCounter = 0;
			}
		} else if (d_state == DAMAGE_STATE.INVISIBLE) {
			if (damageCounter % 5 == 0) {
				if (flag) {
					alpha = 0.5f;
					flag = !flag;
				} else {
					alpha = 1.0f;
					flag = !flag;
				}
			}
			damageCounter++;
			if (damageTime <= damageCounter) {
				alpha = 1.0f;
				d_state = DAMAGE_STATE.NON;
				damageCounter = 0;
			}
		}
		if(totalTime % attackRate == 0){
			bt.imageId = R.drawable.bomd2;
			bt.sizeX = 0.05f;
			bt.sizeY = 0.05f;
			bt.ux = 0;
			bt.uy = 0.05f;
			bt.x = position.getX();
			bt.radius = 0.05f/2f;
			bt.y = position.getY();
			bt.tag = GameScreen.COLLISION_MASK.PLAYERBULLET.getInt();
			bt.mask = GameScreen.COLLISION_MASK.ENEMY.getInt();
			GameScreen.bulletList.add(bt);
		}
		totalTime++;
	}

	/**
	 * 表示
	 * GLES20Util依存なので注意
	 */
	public void Draw(float offsetX,float offsetY){//true 点有
		synchronized (lock) {
			GLES20Util.DrawGraph(position.getX() + offsetX, position.getY() + offsetY, sizeX, sizeY,degree, BitmapList.getBitmap(imageId), alpha, GLES20COMPOSITIONMODE.ALPHA);
			GLES20Util.DrawGraph(position.getX() + offsetX, position.getY() + offsetY, collider.getRadius()*2.0f, collider.getRadius()*2.0f, BitmapList.getBitmap(R.drawable.bomd2), 1.0f, GLES20COMPOSITIONMODE.ALPHA);
			if(d_state == DAMAGE_STATE.EFFECT) {
				if(damageCounter <= damageEffectTime) {
					float t = (float) damageEffectTime;
					float c = (float) damageCounter;
					GLES20Util.DrawGraph(position.getX() + offsetX, position.getY() + offsetY
							, 2f - (2f / t) * c, 2f - (2f / t) * c
							, BitmapList.getBitmap(R.drawable.damage_effect)
							, (1f / t) * c
							, GLES20COMPOSITIONMODE.ADD);
				}
				else if(damageCounter <= damageEffectTime + 40) {
					explosion.draw(offsetX, offsetY, position, sizeX*2f, sizeY*2f, 1f, 0);
				}
			}
		}
	}

	public int getLife(){
		return life;
	}

	public DAMAGE_STATE getDamageFlag(){
		return d_state;
	}

	public boolean isDamage(){
		return d_state != DAMAGE_STATE.NON;
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
		d_state = DAMAGE_STATE.EFFECT;
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
		if(GameScreen.isFreeze() || isDead() || d_state == DAMAGE_STATE.EFFECT)
			return;
		synchronized (lock) {
			position.setX(position.getX() - GLES20Util.getWidth_gl() / GLES20Util.getWidth() * (t.getDelta(Touch.Pos_Flag.X) * Constant.getSens()));
			position.setY(position.getY() + GLES20Util.getHeight_gl() / GLES20Util.getHight() * (t.getDelta(Touch.Pos_Flag.Y) * Constant.getSens()));
			regulation();
		}
	}
}
