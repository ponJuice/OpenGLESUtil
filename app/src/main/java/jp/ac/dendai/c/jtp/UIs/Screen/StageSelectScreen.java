package jp.ac.dendai.c.jtp.UIs.Screen;

import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.UIs.Transition.LoadingTransition;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

public class StageSelectScreen implements Screenable {
	@Override
	public void Proc() {

	}

	@Override
	public void Draw(float offsetX, float offsetY) {
		GLES20Util.DrawString("StageSelectScreen", 1, 255, 255, 255, 1f, offsetX, offsetY, GLES20COMPOSITIONMODE.ALPHA);
	}

	@Override
	public void Touch(MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_UP:
			GameManager.isTransition = true;
			LoadingTransition.getInstance().setNextScreen(MenuScreen.getInstance());
			LoadingTransition.getInstance().setTransitionTime(10);
			GameManager.transition = LoadingTransition.getInstance();
			break;
		}
	}

	@Override
	public void death() {

	}

}
