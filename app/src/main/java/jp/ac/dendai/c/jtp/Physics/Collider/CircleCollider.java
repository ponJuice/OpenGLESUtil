package jp.ac.dendai.c.jtp.Physics.Collider;

import android.graphics.Canvas;
import android.graphics.Paint;

import jp.ac.dendai.c.jtp.Physics.Physics.IPhysics2D;
import jp.ac.dendai.c.jtp.Physics.Physics.Physics2D;
import jp.ac.dendai.c.jtp.UIs.Math.Vector2;

/**
 * Created by Goto on 2016/06/28.
 */
public class CircleCollider extends Collider{
    private static Vector2 buffer2D = new Vector2();
    private float radius;
    public CircleCollider(float radius){
        this.radius = radius;
    }

    @Override
    public float getDeepMagnitude(ICollider col){
        //二つのオブジェクトの距離ベクトルを計算
        buffer2D.zeroReset();
        buffer2D.copy(object.getPosition());
        buffer2D.sub(col.getPhysicsObject().getPosition());
        //それぞれのオブジェクトの半径の合計を計算
        float sumRadius = radius + col.getBoundaryCircle();

        return sumRadius - buffer2D.getMagnitude();
    }

    @Override
    public boolean isCollision(ICollider col) {
        //二つのオブジェクトの距離ベクトルを計算
        buffer2D.zeroReset();
        buffer2D.copy(object.getPosition());
        buffer2D.sub(col.getPhysicsObject().getPosition());
        //それぞれのオブジェクトの半径の合計を計算
        float sumRadius = radius + col.getBoundaryCircle();
        //もし距離ベクトルの長さが半径の合計よりも小さければ衝突、でなければ衝突しない
        return (buffer2D.getSqrMagnitude() <= (sumRadius * sumRadius));
    }

    @Override
    public float getBoundaryCircle() {
        return radius;
    }

    @Override
    public void debugOutlineDraw(Canvas canvas,Paint paint) {
        canvas.drawCircle(object.getPosition().getX(),object.getPosition().getY(),radius,paint);
    }

    @Override
    public void setPhysicsObject(IPhysics2D obj) {
        object = obj;
    }
}
