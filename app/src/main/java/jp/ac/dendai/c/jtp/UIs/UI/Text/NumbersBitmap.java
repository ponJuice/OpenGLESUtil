package jp.ac.dendai.c.jtp.UIs.UI.Text;

import android.graphics.Bitmap;
import android.opengl.GLException;

import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * Created by テツヤ on 2016/07/09.
 */
public class NumbersBitmap {
    private Bitmap[] numbers;
    public NumbersBitmap(String fontName){
        numbers = new Bitmap[10];
        for(int n = 0;n < 10; n++){
            numbers[n] = GLES20Util.stringToBitmap(String.valueOf(n),1,255,255,255);
        }
    }
    public Bitmap getNumber(int n){
        if(numbers.length <= n || n < 0)
            return numbers[0];
        return numbers[n];
    }
}
