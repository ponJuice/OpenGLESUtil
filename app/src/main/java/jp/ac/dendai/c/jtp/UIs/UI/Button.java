package jp.ac.dendai.c.jtp.UIs.UI;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;

import jp.ac.dendai.c.jtp.UIs.UI.Listener.ButtonListener;
import jp.ac.dendai.c.jtp.UIs.UI.Text.Text;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

public class Button {
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getLengthX() {
		return lengthX;
	}

	public void setLengthX(float lengthX) {
		this.lengthX = lengthX;
	}

	public float getLengthY() {
		return lengthY;
	}

	public void setLengthY(float lengthY) {
		this.lengthY = lengthY;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public  void setEnable(boolean e){
		enable = e;
	}

	public  boolean getEnable(){
		return enable;
	}

	private static Bitmap mask;
	private boolean enable = true;
	private float x,y,lengthX,lengthY;
	private float alpha;
	private Bitmap image;
	private int backgroundId;
	private Text text;
	private UIAlign.Align xAlign;
	private UIAlign.Align yAlign;
	private ButtonListener listener;

	public Button(float x,float y,float lengthX,float lengthY,float alpha,int background,String text){
		this.text = new Text(text);
		backgroundId = background;
		init(x,y,lengthX,lengthY,alpha);
	}

	public Button(float x,float y,float lengthX,float lengthY,float alpha,String text,int a,int r,int g,int b){
		this.text = new Text(text);
		image = GLES20Util.createBitmap(r, g, b,a);
		init(x,y,lengthX,lengthY,alpha);
	}

	private void init(float x,float y,float lengthX,float lengthY,float alpha){
		this.x = x;
		this.y = y;
		this.lengthX = lengthX;
		this.lengthY = lengthY;
		this.alpha = alpha;
		this.text.setHorizontalTextAlign(Text.TextAlign.CENTOR);
		this.text.setVerticalTextAlign(Text.TextAlign.CENTOR);
		xAlign = UIAlign.Align.CENTOR;
		yAlign = UIAlign.Align.CENTOR;

		if(mask == null)
			mask = GLES20Util.createBitmap(169,169,169,255);
	}

	public void setListener(ButtonListener l){
		listener = l;
	}

	public boolean contains(float _x,float _y){
		return _x >= (x - lengthX/2f) && _x <= (x + lengthX/2f) &&
				_y >= (y - lengthY/2f) && _y <= (y + lengthY/2f);
	}

	private float xbuffer;
	private float ybuffer;
	public void touch(MotionEvent event){
		if(!enable)
			return;
		xbuffer = GLES20Util.screenToInnerPosition(event.getX(0), GLES20Util.GLES20UTIL_MODE.POSX);
		ybuffer = GLES20Util.screenToInnerPosition(event.getY(0), GLES20Util.GLES20UTIL_MODE.POSY);
		if(contains(xbuffer,ybuffer))
		{
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				alpha = 0.5f;
				break;
			case MotionEvent.ACTION_MOVE:
				alpha = 0.5f;
				break;
			case MotionEvent.ACTION_UP:
				alpha = 1f;
				execute();
				break;
			}
		}
		else
			alpha = 1f;
	}

	public void execute(){
		Log.d("Button","execute");
		if(listener != null)
			listener.execute(this);
	}

	public void draw(){
		if(!enable) {
			GLES20Util.DrawGraph(x - UIAlign.convertAlign(lengthX, xAlign),
					y - UIAlign.convertAlign(lengthY, yAlign),
					lengthX,
					lengthY,
					mask,
					alpha,
					GLES20COMPOSITIONMODE.SUB);
		}
		GLES20Util.DrawGraph(x-UIAlign.convertAlign(lengthX, xAlign),
				y-UIAlign.convertAlign(lengthY, yAlign),
				lengthX,
				lengthY,
				image,
				alpha,
				GLES20COMPOSITIONMODE.ALPHA);
		text.draw(UIAlign.convertAlign(lengthX,xAlign), UIAlign.convertAlign(lengthY,yAlign), alpha, GLES20COMPOSITIONMODE.ALPHA);
	}
	public void draw(float offsetX,float offsetY){
		GLES20Util.DrawGraph(x-UIAlign.convertAlign(lengthX, xAlign)+offsetX,
				y-UIAlign.convertAlign(lengthY, yAlign)+offsetY,
				lengthX,
				lengthY,
				image,
				alpha,
				GLES20COMPOSITIONMODE.ALPHA);
		text.draw(x+offsetX, y+offsetY, alpha, GLES20COMPOSITIONMODE.ALPHA);
	}
}
