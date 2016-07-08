package jp.ac.dendai.c.jtp.UIs.UI;

public class UIAlign {
	public enum Align{
		CENTOR ,
		LEFT,
		RIGHT,
		TOP,
		BOTTOM
	}
	public static float convertAlign(float length,Align align){
		if(align == Align.LEFT || align == Align.BOTTOM){
			return 0;
		}
		else if(align == Align.RIGHT || align == Align.TOP){
			return length;
		}
		else{
			return Math.abs(length/2f);
		}
	}
}
