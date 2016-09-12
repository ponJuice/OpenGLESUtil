package jp.ac.dendai.c.jtp.UIs.Screen;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;

import java.util.NavigableMap;
import java.util.Objects;

import jp.ac.dendai.c.jtp.Game.Bullet.Bullet;
import jp.ac.dendai.c.jtp.Game.Bullet.BulletList;
import jp.ac.dendai.c.jtp.Game.GameManager;
import jp.ac.dendai.c.jtp.Game.Graphics.BitmapList;
import jp.ac.dendai.c.jtp.UIs.Transition.LoadingTransition;
import jp.ac.dendai.c.jtp.UIs.Transition.ScrollTransition;
import jp.ac.dendai.c.jtp.UIs.UI.Button;
import jp.ac.dendai.c.jtp.UIs.UI.Listener.ButtonListener;
import jp.ac.dendai.c.jtp.UIs.UI.Text.Text;
import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

public class MenuScreen implements Screenable {
	public enum TOSCREEN{
		NON,
		GAMESCREEN
	}
	private boolean isFreese = false;
	private TOSCREEN nextScreen = TOSCREEN.NON;
	private Object lock;
	private static MenuScreen instance;
	private static Button button,optionButton;
	private static Text title;
	private static OptionScreen os;
	private long time = 0;
	private static Bitmap background;

	private MenuScreen(){
		Log.d("MenuScreen",String.valueOf(GLES20Util.getAspect()));
		title = new Text("C班 STG",255,255,255);
		title.setVerticalTextAlign(Text.TextAlign.CENTOR);
		title.setHorizontalTextAlign(Text.TextAlign.CENTOR);
		title.setScaleY(2f);
		title.setScaleX(2f);
		button = new Button(GLES20Util.getAspect(), 0.5f, 0.7f, 0.2f, 1f, "START",255,0,0,0);
		button.setX(GLES20Util.getWidth_gl() / 2f);
		button.setListener(new StartButtonListener());
		optionButton = new Button(GLES20Util.getAspect(), 0.5f, 0.7f, 0.2f, 1f, "OPTION",255,0,0,0);
		optionButton.setX(GLES20Util.getWidth_gl() / 2f);
		optionButton.setListener(new OptionButtonListener());
		background = GLES20Util.loadBitmap(R.drawable.menu_background);
		lock = new Object();
	}

	public static MenuScreen getInstance(){
		if(instance == null)
			instance = new MenuScreen();
		return instance;
	}

	@Override
	public void Proc() {
		if(isFreese)
			return;
		if(nextScreen == TOSCREEN.GAMESCREEN)
			toGameScreen();
	}

	@Override
	public void Draw(float offsetX, float offsetY) {
		GLES20Util.DrawGraph(GLES20Util.getWidth_gl() / 2f, GLES20Util.getHeight_gl() / 2f, GLES20Util.getWidth_gl(), GLES20Util.getHeight_gl(), background, 1f, GLES20COMPOSITIONMODE.ALPHA);

		title.draw(offsetX + GLES20Util.getWidth_gl() / 2f, offsetY + GLES20Util.getHeight_gl() - 0.6f, 1f, GLES20COMPOSITIONMODE.ALPHA);

		button.setX(GLES20Util.getWidth_gl() / 2f);
		button.setY(GLES20Util.getHeight_gl() / 2f);
		button.draw(offsetX, offsetY);

		optionButton.setX(GLES20Util.getWidth_gl() / 2f);
		optionButton.setY(GLES20Util.getHeight_gl() / 2f - button.getLengthY() - 0.1f);
		optionButton.draw(offsetX, offsetY);
		time++;

		if(os != null){
			os.draw();
		}
	}
	@Override
	public void Touch(MotionEvent event) {
		float tempX = GLES20Util.screenToInnerPosition(event.getX(0), GLES20Util.GLES20UTIL_MODE.POSX);
	    float tempY = GLES20Util.screenToInnerPosition(event.getY(0), GLES20Util.GLES20UTIL_MODE.POSY);
		if(os != null){
			os.touch(event);
			return;
		}

		button.touch(event);
		optionButton.touch(event);
	}

	@Override
	public void death() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void freeze() {
		isFreese = true;
	}

	@Override
	public void unFreeze() {
		isFreese = false;
	}

	private void toGameScreen(){
		GameManager.isTransition = true;
		//ScrollTransition.getInstance().setScrollTime(10);
		//ScrollTransition.getInstance().setDirect(GLES20Util.getAspect() * 2f, 0);
		//GameManager.nextScreen = new StageSelectScreen();
		//GameManager.nextScreen = new GameScreen();
		//GameManager.transition = ScrollTransition.getInstance();
		LoadingTransition.getInstance().setNextScreen(new GameScreen());
		LoadingTransition.getInstance().setTransitionTime(10);
		GameManager.transition = LoadingTransition.getInstance();
		nextScreen = TOSCREEN.NON;
	}

	public class StartButtonListener implements ButtonListener {
		@Override
		public void execute(Button button) {
			synchronized (lock) {
				nextScreen = TOSCREEN.GAMESCREEN;
			}
			GameManager.sp.play(GameManager.button,1f,1f,0,0,1f);
		}
	}

	public class OptionButtonListener implements ButtonListener{
		@Override
		public void execute(Button button) {
			os = new OptionScreen();
			GameManager.sp.play(GameManager.button,1f,1f,0,0,1f);
		}
	}

	public class OptionScreen{
		private int moveTime = 15;
		private float offsetY = 0;
		private int counter = 0;
		private Bitmap black;
		private boolean debug;
		private Button isDebug;
		private Text debugModeON,debugModeOFF;
		private Button back;
		private boolean end;

		public OptionScreen(){
			debug = GameManager.debug;

			offsetY = GLES20Util.getHeight_gl() + GLES20Util.getHeight_gl()/2f;

			black = GLES20Util.createBitmap(0,0,0,255);
			end = false;

			debugModeON = new Text("ON",82,162,197);
			debugModeON.setHorizontalTextAlign(Text.TextAlign.LEFT);
			debugModeON.setVerticalTextAlign(Text.TextAlign.CENTOR);

			debugModeOFF = new Text("OFF",255,0,0);
			debugModeOFF.setHorizontalTextAlign(Text.TextAlign.LEFT);
			debugModeOFF.setVerticalTextAlign(Text.TextAlign.CENTOR);

			isDebug = new Button(GLES20Util.getWidth_gl()/2f-0.3f,GLES20Util.getHeight_gl()/2f+0.5f,0.7f, 0.2f,1f,"無敵モード",255,51,51,51);
			isDebug.setEnable(false);
			isDebug.setListener(new ButtonListener() {
				@Override
				public void execute(Button button) {
					GameManager.sp.play(GameManager.button,1f,1f,0,0,1f);
					debug = !debug;
				}
			});

			back = new Button(GLES20Util.getWidth_gl()-0.4f,0.15f,0.7f, 0.2f,1f,"BACK",255,51,51,51);
			back.setEnable(false);
			back.setListener(new ButtonListener() {
				@Override
				public void execute(Button button) {
					GameManager.sp.play(GameManager.button,1f,1f,0,0,1f);
					GameManager.debug = debug;
					counter = 0;
					isDebug.setEnable(false);
					back.setEnable(false);
					end = true;
				}
			});
		}

		public void draw(){
			if(end){
				if(counter <= moveTime) {
					GLES20Util.DrawGraph(GLES20Util.getWidth_gl() / 2f, GLES20Util.getHeight_gl() / 2f + (offsetY / moveTime * counter), GLES20Util.getWidth(), GLES20Util.getHeight_gl(), black, 0.5f, GLES20COMPOSITIONMODE.ALPHA);
					isDebug.draw(0, offsetY / moveTime * counter);
					onOffDraw(0, offsetY / moveTime * counter, isDebug);
					back.draw(0, offsetY / moveTime * counter);
					counter++;
					return;
				}else{
					os = null;
					return;
				}
			}
			if(counter <= moveTime) {
				GLES20Util.DrawGraph(GLES20Util.getWidth_gl() / 2f, GLES20Util.getHeight_gl() / 2f + (offsetY - (offsetY / moveTime * counter)), GLES20Util.getWidth(), GLES20Util.getHeight_gl(), black, 0.5f, GLES20COMPOSITIONMODE.ALPHA);
				isDebug.draw(0, (offsetY - (offsetY / moveTime * counter)));
				onOffDraw(0,(offsetY - (offsetY/moveTime*counter)),isDebug);
				back.draw(0,(offsetY - (offsetY/moveTime*counter)));
				counter++;
			}else {
				isDebug.setEnable(true);
				back.setEnable(true);
				GLES20Util.DrawGraph(GLES20Util.getWidth_gl() / 2f, GLES20Util.getHeight_gl() / 2f, GLES20Util.getWidth(), GLES20Util.getHeight_gl(), black, 0.5f, GLES20COMPOSITIONMODE.ALPHA);
				isDebug.draw(0,0);
				onOffDraw(0,0,isDebug);
				back.draw(0,0);
			}
		}

		public void onOffDraw(float offsetX,float offsetY,Button bt){
			if(debug){
				//ON
				debugModeON.draw(offsetX + bt.getLengthX()+0.05f,offsetY+bt.getY(),1f,GLES20COMPOSITIONMODE.ALPHA);
			}else{
				debugModeOFF.draw(offsetX + bt.getLengthX()+0.05f,offsetY+bt.getY(),1f,GLES20COMPOSITIONMODE.ALPHA);
			}

		}

		public void touch(MotionEvent event){
			isDebug.touch(event);
			back.touch(event);
		}
	}

}
