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
			return -length/2f;
		}
		else if(align == Align.RIGHT || align == Align.TOP){
			return length/2f;
		}
		else{
			return 0;
		}
	}
}
