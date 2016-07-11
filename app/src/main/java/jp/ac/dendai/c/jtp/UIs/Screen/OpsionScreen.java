package jp.ac.dendai.c.jtp.UIs.Screen;

import android.view.MotionEvent;

/**
 * Created by Goto on 2016/07/11.
 */
public class OpsionScreen implements Screenable{
    protected boolean isFreeze;

    @Override
    public void Proc() {

    }

    @Override
    public void Draw(float offsetX, float offsetY) {

    }

    @Override
    public void Touch(MotionEvent event) {

    }

    @Override
    public void death() {

    }

    @Override
    public void freeze() {
        isFreeze = true;
    }

    @Override
    public void unFreeze() {
        isFreeze = false;
    }
}
