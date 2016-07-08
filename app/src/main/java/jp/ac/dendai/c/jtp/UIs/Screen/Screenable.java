package jp.ac.dendai.c.jtp.UIs.Screen;

import android.view.MotionEvent;

public interface Screenable {
	public void Proc();
	public void Draw(float offsetX,float offsetY);
	public void Touch(MotionEvent event);
	public void death();
}
