package jp.ac.dendai.c.jtp.Game.Bullet;

import jp.ac.dendai.c.jtp.Game.Player;
import jp.ac.dendai.c.jtp.Physics.Collider.ICollider;
import jp.ac.dendai.c.jtp.UIs.Screen.GameScreen;

/**
 * Created by テツヤ on 2016/07/10.
 */
public class PlayerBullet extends Bullet{

    public PlayerBullet(ICollider c, int imageId, float x, float y, float ux, float uy, float sizeX, float sizeY) {
        super(c, imageId, x, y, ux, uy, sizeX, sizeY, GameScreen.COLLISION_MASK.ENEMY.getInt(),GameScreen.COLLISION_MASK.PLAYERBULLET.getInt());
    }

    public PlayerBullet(BulletTemplate bt){
        super(bt);
    }

    @Override
    public void collisionPlayerProc(Player player){

    }
}
