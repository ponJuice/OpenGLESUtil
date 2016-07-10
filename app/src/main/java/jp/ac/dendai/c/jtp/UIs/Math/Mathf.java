package jp.ac.dendai.c.jtp.UIs.Math;

/**
 * Created by wark on 2016/07/09.
 */
public class Mathf {
    public static float degreeToRadian(float degree){
        return degree * (float)Math.PI / 180f;
    }

    public static float radianToDegree(float radian){
        return  radian * 180f/ (float)Math.PI;
    }
}
