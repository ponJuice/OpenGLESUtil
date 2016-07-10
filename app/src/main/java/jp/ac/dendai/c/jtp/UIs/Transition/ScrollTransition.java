package jp.ac.dendai.c.jtp.UIs.Transition;

import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.UIs.Screen.Screenable;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

public class ScrollTransition implements Transitionable {
	private static ScrollTransition instance;
	public static ScrollTransition getInstance(){
		if(instance == null){
			instance = new ScrollTransition();
		}
		return instance;
	}

	private int scrollTime = 30;
	private int frameBuffer = 0;
	private float offsetX = 0,offsetY = 0;
	private float x = 0,y = 0;
	public void setDirect(float x,float y) {
		this.x = x;
		this.y = y;
	}
	public void setScrollTime(int scrollTime) {
		this.scrollTime = scrollTime;
	}
	@Override
	public boolean Transition() {
		if(frameBuffer < scrollTime){
			GameManager.nowScreen.freeze();
			GameManager.nextScreen.freeze();
			GameManager.nowScreen.Draw(-offsetX, -offsetY);
			GameManager.nextScreen.Draw(GLES20Util.getAspect()*2f-offsetX, -offsetY);

			offsetX += x/(float)scrollTime;
			offsetY += y/(float)scrollTime;

			frameBuffer++;
			return true;
		}
		else{
			GameManager.nowScreen.unFreeze();
			GameManager.nextScreen.unFreeze();
			GameManager.nowScreen.death();
			GameManager.nowScreen = GameManager.nextScreen;
			offsetX = 0;
			offsetY = 0;
			frameBuffer = 0;

			return false;
		}
	}

}
