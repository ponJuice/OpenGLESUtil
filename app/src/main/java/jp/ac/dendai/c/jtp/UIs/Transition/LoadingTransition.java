package jp.ac.dendai.c.jtp.UIs.Transition;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.UIs.Screen.Screenable;
import jp.ac.dendai.c.jtp.UIs.UI.Text.Text;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

public class LoadingTransition implements Transitionable {
	private static LoadingTransition instance;

	public static LoadingTransition getInstance(){
		if(instance == null)
			instance = new LoadingTransition();
		return instance;
	}
	private LoadingTransition(){
		nowLoading = new Text("NOW LOADING...",0,0,0);
		nowLoading.setHorizontalTextAlign(Text.TextAlign.LEFT);
		nowLoading.setVerticalTextAlign(Text.TextAlign.BOTTOM);
		bitmap = GLES20Util.createBitmap(r,g,b,255);
	}
	private Class<?> nextScreenClass;
	private Screenable nextScreen;
	public void setNextScreen(Class<?> nextScreen) {
		this.nextScreenClass = nextScreen;
		this.nextScreen = null;
	}
	public void setNextScreen(Screenable nextScreen){
		this.nextScreenClass = null;
		this.nextScreen = nextScreen;
	}
	public void setSleepTime(int n){
		sleepTime = n;
	}
	private int sleepTime = 0;
	private float alpha = 0;
	private float deltaAlpha =0;
	private int mode = 0;
	private Bitmap bitmap;
	private int transitionTime = 60;
	private float textx = 0.05f,texty = 0.05f;
	private int r = 255,g = 255, b = 255;
	private Text nowLoading;

	@Override
	public boolean Transition() {
		if(mode == 0){
			GameManager.nowScreen.freeze();
			deltaAlpha = 1f/(float)transitionTime;
			mode = 1;
		}
		if(mode == 1){
			GameManager.nowScreen.Draw(0,0);
			alpha += deltaAlpha;
			GLES20Util.DrawGraph(GLES20Util.getWidth_gl()/2f,GLES20Util.getHeight_gl()/2f,
					GLES20Util.getWidth_gl(),GLES20Util.getHeight_gl(), bitmap,alpha, GLES20COMPOSITIONMODE.ALPHA);
			//GLES20Util.DrawString("NowLoading...", 1, 255,255,255,alpha, 0.5f, 0, GLES20COMPOSITIONMODE.ALPHA);
			nowLoading.setAlpha(alpha);
			nowLoading.draw(textx,texty,1,GLES20COMPOSITIONMODE.ALPHA);
			transitionTime--;
			if(transitionTime <= 0)
				mode = 2;
			return true;
		}
		else if(mode == 2){
			GLES20Util.DrawGraph(GLES20Util.getWidth_gl() / 2f, GLES20Util.getHeight_gl() / 2f,
					GLES20Util.getWidth_gl(), GLES20Util.getHeight_gl(), bitmap, 1f, GLES20COMPOSITIONMODE.ALPHA);
			//GLES20Util.DrawString("NowLoading...", 1, 255,255,255,1f, 0.5f, 0, GLES20COMPOSITIONMODE.ALPHA);
			nowLoading.setAlpha(alpha);
			nowLoading.draw(textx,texty,1,GLES20COMPOSITIONMODE.ALPHA);
			try {
				if(nextScreenClass != null)
					GameManager.nowScreen  = (Screenable)(nextScreenClass.newInstance());
				else
					GameManager.nowScreen = nextScreen;
			} catch (InstantiationException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			mode = 3;
			alpha = 1f;
			transitionTime = 30;
			deltaAlpha = 1f/(float)transitionTime;
			return true;
		}
		else if(mode == 3){
			GameManager.nowScreen.Draw(0, 0);
			GLES20Util.DrawGraph(GLES20Util.getWidth_gl() / 2f, GLES20Util.getHeight_gl() / 2f,
					GLES20Util.getWidth_gl(), GLES20Util.getHeight_gl(), bitmap, alpha, GLES20COMPOSITIONMODE.ALPHA);
			//GLES20Util.DrawString("NowLoading...", 1, 255,255,255,alpha, 0.5f, 0, GLES20COMPOSITIONMODE.ALPHA);
			nowLoading.setAlpha(alpha);
			nowLoading.draw(textx,texty,1,GLES20COMPOSITIONMODE.ALPHA);
			alpha -= deltaAlpha;
			transitionTime--;
			if(transitionTime <= 0)
				mode = 4;
			return true;
		}
		else{
			GameManager.nowScreen.unFreeze();
			GameManager.nowScreen.Draw(0,0);
			mode = 0;
			alpha = 0;
			deltaAlpha = 0;
			nextScreen = null;
			return false;
		}
	}

	public void setTransitionTime(int transitionTime) {
		this.transitionTime = transitionTime;
	}

}
