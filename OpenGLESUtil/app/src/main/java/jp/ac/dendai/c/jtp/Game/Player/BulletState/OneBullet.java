package jp.ac.dendai.c.jtp.Game.Player.BulletState;

import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.Game.Player.BulletState.PlayerBulletState;
import jp.ac.dendai.c.jtp.Game.Player.Player;
import jp.ac.dendai.c.jtp.UIs.Screen.GameScreen;
import jp.ac.dendai.c.jtp.openglesutil.R;

/**
 * Created by テツヤ on 2016/09/10.
 */
public class OneBullet extends PlayerBulletState {
    @Override
    public void bulletAdd(BulletList list,BulletTemplate bt,Player player) {
        bt.imageId = R.drawable.bomd2;
        bt.sizeX = 0.05f;
        bt.sizeY = 0.05f;
        bt.ux = 0;
        bt.uy = 0.05f;
        bt.x = player.getPosition().getX();
        bt.radius = 0.05f/2f;
        bt.y = player.getPosition().getY();
        bt.tag = GameScreen.COLLISION_MASK.PLAYERBULLET.getInt();
        bt.mask = GameScreen.COLLISION_MASK.ENEMY.getInt();
        list.add(bt);
    }
}
