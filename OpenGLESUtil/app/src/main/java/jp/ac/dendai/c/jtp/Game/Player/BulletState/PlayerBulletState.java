package jp.ac.dendai.c.jtp.Game.Player.BulletState;

import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletTemplate;
import jp.ac.dendai.c.jtp.Game.Player.Player;
import jp.ac.dendai.c.jtp.UIs.Screen.GameScreen;
import jp.ac.dendai.c.jtp.openglesutil.R;

/**
 * Created by テツヤ on 2016/09/10.
 */
public abstract class PlayerBulletState {
    public abstract void bulletAdd(BulletList list, BulletTemplate bt,Player player);
}
