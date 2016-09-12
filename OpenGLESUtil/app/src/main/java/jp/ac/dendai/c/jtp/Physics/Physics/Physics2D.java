package jp.ac.dendai.c.jtp.Physics.Physics;

import android.graphics.Canvas;
import android.graphics.Paint;

import jp.ac.dendai.c.jtp.Game.Constant;
import jp.ac.dendai.c.jtp.Physics.Collider.ICollider;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;

/**
 * Created by Goto on 2016/06/28.
 */
public class Physics2D implements IPhysics2D{
    protected static Vector2 buffer2D = new Vector2();
    protected float mass;            //質量
    protected Vector2 velocity;      //速度
    protected Vector2 velocityUpdate;
    protected Vector2 impulseVelocity;    //撃力
    protected Vector2 position;       //位置
    protected float e;                //跳ね返り係数
    protected ICollider collider;     //衝突判定用

    public Physics2D(Physics2DTemplate p2t,ICollider collider){
        mass = p2t.mass;
        velocity = new Vector2(p2t.velocity);
        velocityUpdate = new Vector2();
        impulseVelocity = new Vector2();
        position = new Vector2(p2t.position);
        e = p2t.e;
        this.collider = collider;
    }

    public Physics2D(){
        mass = 1;
        e = 1;
        velocityUpdate = new Vector2();
        impulseVelocity = new Vector2();
        velocity = new Vector2();
        position = new Vector2();
    }

    @Override
    public void addForceImpulse(Vector2 newton) {
        buffer2D.copy(newton);
        buffer2D.scalarDiv(mass);
        velocity.add(buffer2D);
        buffer2D.zeroReset();
    }

    @Override
    public void addVelocityImpulse(Vector2 vector){
        impulseVelocity.add(vector);
    }

    @Override
    public void setPosition(Vector2 position) {
        this.position.copy(position);
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void addVelocity(Vector2 velocity) {
        this.velocityUpdate.setX(this.velocityUpdate.getX() + this.velocity.getX() + velocity.getX());
        this.velocityUpdate.setY(this.velocityUpdate.getY() + this.velocity.getY() + velocity.getY());
    }

    @Override
    public void setVelocity(Vector2 velocity) {
        velocityUpdate.add(velocity);
    }

    @Override
    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public void setMass(float mass) {
        this.mass = mass;
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public void setE(float e) {
        this.e = e;
    }

    @Override
    public float getE() {
        return e;
    }

    @Override
    public void updatePosition(float deltaTimeSec){
        position.setX(position.getX() + velocityUpdate.getX() * deltaTimeSec);
        position.setY(position.getY() + velocityUpdate.getY() * deltaTimeSec);
        velocity.copy(velocityUpdate);
        velocityUpdate.zeroReset();

        //撃力
        position.setX(position.getX() + impulseVelocity.getX());
        position.setY(position.getY() + impulseVelocity.getY());
        impulseVelocity.zeroReset();
    }

    @Override
    public ICollider getCollider() {
        return collider;
    }

    @Override
    public void setCollider(ICollider collider) {
        this.collider = collider;
        this.collider.setPhysicsObject(this);
    }

    @Override
    public boolean isCollision(IPhysics2D obj) {
        return collider.isCollision(obj.getCollider());
    }

    @Override
    public void debugDrawColliderOutline(Canvas canvas, Paint paint) {
        collider.debugOutlineDraw(canvas,paint);
    }
}
