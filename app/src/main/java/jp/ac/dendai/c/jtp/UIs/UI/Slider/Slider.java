package jp.ac.dendai.c.jtp.UIs.UI.Slider;

import jp.ac.dendai.c.jtp.UIs.Math.Vector2;

/**
 * Created by Goto on 2016/07/11.
 */
public class Slider {
    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getNowValue() {
        return (max - min)*normalizeValue + min;
    }

    public void setNowValue(float nowValue) {
        if(max <= nowValue)
            nowValue = max;
        if(min >= nowValue)
            nowValue = min;
        normalizeValue = max / nowValue - min;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    protected float max;
    protected float min;
    protected float normalizeValue;//1.0～0.0
    protected Rect boud;
    public Slider(){
        max = 1;
        min = 0;
        normalizeValue = 0.5f;
        boud = new Rect(0,0.1f,0.5f,0);
    }



    public void draw(float offsetX,float offsetY){

    }

    public void proc(){

    }

    public void touch(float x,float y){

    }
}
