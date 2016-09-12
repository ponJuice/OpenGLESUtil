package jp.ac.dendai.c.jtp.Game.Item;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import jp.ac.dendai.c.jtp.Game.Player.Player;
import jp.ac.dendai.c.jtp.Physics.Collider.CircleCollider;
import jp.ac.dendai.c.jtp.Physics.Collider.ICollider;
import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;

/**
 * Created by テツヤ on 2016/09/11.
 */
public class LifeUp extends Item {
    public LifeUp(Bitmap bitmap){
        collider = new CircleCollider(0.05f);
        collider.setPhysicsObject(this);
        this.bitmap = bitmap;
        pos = new Vector2();
        vel=  new Vector2();
    }

    @Override
    public void collisionProc(Player player) {
        se.sp.play(se.id,1f,1f,0,0,1f);
        player.setLife(player.getLife()+1);
    }

    @Override
    public void addForceImpulse(Vector2 newton) {

    }

    @Override
    public void addVelocityImpulse(Vector2 velocity) {

    }

    @Override
    public void setPosition(Vector2 position) {
        pos.copy(position);
    }

    @Override
    public Vector2 getPosition() {
        return pos;
    }

    @Override
    public void addVelocity(Vector2 velocity) {

    }

    @Override
    public void setVelocity(Vector2 velocity) {

    }

    @Override
    public Vector2 getVelocity() {
        return null;
    }

    @Override
    public void setMass(float mass) {

    }

    @Override
    public float getMass() {
        return 0;
    }

    @Override
    public void setE(float e) {

    }

    @Override
    public float getE() {
        return 0;
    }

    @Override
    public void updatePosition(float deltaTime) {

    }

    @Override
    public ICollider getCollider() {
        return null;
    }

    @Override
    public void setCollider(ICollider collider) {

    }

    @Override
    public boolean isCollision(IPhysics2D obj) {
        return false;
    }

    @Override
    public void debugDrawColliderOutline(Canvas canvas, Paint paint) {

    }
}
