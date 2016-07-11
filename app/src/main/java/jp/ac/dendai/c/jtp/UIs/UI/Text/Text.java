package jp.ac.dendai.c.jtp.UIs.UI.Text;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

public class Text {
	public enum TextAlign{
		CENTOR ,
		LEFT,
		RIGHT,
		TOP,
		BOTTOM
	}

	public  enum WrittingAlign{
		VERTICCAL,
		HOLIZONTAL
	}

	private String text;
	private Bitmap bitmap;
	private TextAlign horizontalTextAlign = TextAlign.LEFT;
	private TextAlign verticalTextAlign = TextAlign.TOP;
	private float scaleX = 1f;
	private float scaleY = 1f;
	private float x = 0,y = 0;
	private int r,g,b;
	private float alpha;

	public Text(String text,int r,int g,int b){
		this.text = text;
		this.r = r;
		this.g = g;
		this.b = b;
		bitmap = GLES20Util.stringToBitmap(text, 1, r, g, b);
		alpha = 1f;
	}
	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public float getLengthX(){
		return bitmap.getWidth() / 1000f * scaleX;
	}

	public float getLengthY(){
		return bitmap.getHeight() /  1000f * scaleY;
	}


	public void draw(float x,float y,float alpha,GLES20COMPOSITIONMODE mode){
		if(horizontalTextAlign == TextAlign.CENTOR){
			//this.x = bitmap.getWidth()/(scaleX*2000f);
			this.x = 0;
		}
		else if(horizontalTextAlign == TextAlign.RIGHT){
			this.x = bitmap.getWidth()/ 2000f * scaleX;
		}
		else{
			this.x = -bitmap.getWidth()/ 2000f*scaleX;
		}
		if(verticalTextAlign == TextAlign.CENTOR){
			//this.y = bitmap.getHeight()/(scaleY*2000f);
			this.y = 0;
		}
		else if(verticalTextAlign == TextAlign.TOP){
			this.y = bitmap.getHeight()/ 2000f * scaleY;
		}
		else{
			this.y = -bitmap.getHeight()/ 2000f * scaleY;
		}
		GLES20Util.DrawGraph(x-this.x, y-this.y, bitmap.getWidth()/ 1000f * scaleX, bitmap.getHeight()/ 1000f * scaleY, bitmap,alpha,mode);
	}

	public void setAlpha(float a){
		alpha = a;
	}

	public float getAlpha(){
		return alpha;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setHorizontalTextAlign(TextAlign horizontalTextAlign) {
		this.horizontalTextAlign = horizontalTextAlign;
	}

	public void setVerticalTextAlign(TextAlign verticalTextAlign) {
		this.verticalTextAlign = verticalTextAlign;
	}
}
