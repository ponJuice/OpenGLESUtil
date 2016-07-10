package jp.ac.dendai.c.jtp.UIs.UI.Text;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.MainActivity;
import jp.ac.dendai.c.jtp.UIs.UI.UIAlign;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;
import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

/**
 * Created by テツヤ on 2016/07/09.
 */
public class NumberText{
    private String fontName;
    private Text.TextAlign horizontalTextAlign,verticalTextAlign;
    private Text.WrittingAlign writtingAlign = Text.WrittingAlign.HOLIZONTAL;
    private boolean align = false; //false 横書き true 縦書き
    public NumberText(String fontName) {
        this.fontName = fontName;
    }

    public NumberText(){
        this.fontName = "serif-monospace";
    }

    public void setHorizontalTextAlign(Text.TextAlign horizontalTextAlign) {
        this.horizontalTextAlign = horizontalTextAlign;
    }

    public void setVerticalTextAlign(Text.TextAlign verticalTextAlign) {
        this.verticalTextAlign = verticalTextAlign;
    }

    public void setWrittingAlign(Text.WrittingAlign wa){
        writtingAlign = wa;
    }

    public void draw(int number,int digitLength,float x,float y,float size,float alpha,GLES20COMPOSITIONMODE mode) {
        if (number < 0)//未満ならば0で表示
            number = 0;
        int l = Math.max(digitLength,(int)Math.log10(number)+1);
        float length = NumbersBitmapList.getWide(fontName,number,writtingAlign);

        if(verticalTextAlign == Text.TextAlign.BOTTOM){
            y += NumbersBitmapList.getHeight(fontName,number,l)/2f;
        }else if(verticalTextAlign == Text.TextAlign.TOP){
            if(writtingAlign == Text.WrittingAlign.HOLIZONTAL){
                //横書き
                y -= NumbersBitmapList.getHeight(fontName,number,1)/4f;
            }else{
                //縦書き
                y += length - NumbersBitmapList.getHeight(fontName,number,l)/2f;
            }
        }else{
            //中央
            if(writtingAlign == Text.WrittingAlign.HOLIZONTAL){
                //横書き 何もしない
            }else{
                //縦書き
                y += length/2f - NumbersBitmapList.getHeight(fontName,number,l/2)/2f;
            }
        }

        if(horizontalTextAlign == Text.TextAlign.LEFT){
            x += NumbersBitmapList.getWide(fontName,number,1)/2f;
        }else if(horizontalTextAlign == Text.TextAlign.RIGHT){
            if(writtingAlign == Text.WrittingAlign.HOLIZONTAL){
                //横書き
                x -= length + NumbersBitmapList.getWide(fontName,number,l)/2f;
            }else{
                //縦書き
                x -= NumbersBitmapList.getWide(fontName,number,1)/2f;
            }
        }else{
            //中央
            if(writtingAlign == Text.WrittingAlign.HOLIZONTAL){
                //横書き
                x += length/2 - NumbersBitmapList.getWide(fontName,number,l/2)/2f;
            }else{
                //縦書き
            }
        }

        for (int n = 0; n < l; n++) {
            Bitmap bitmap = NumbersBitmapList.getNumbersBitmap(fontName).getNumber(getDigit(number,l-n));
            GLES20Util.DrawGraph(x, y, bitmap.getWidth() / (size * 1000f), bitmap.getHeight() / (size * 1000f), bitmap, 1f, mode);
            if(writtingAlign == Text.WrittingAlign.HOLIZONTAL) {
                x += bitmap.getWidth() / (size * 1000f);
            }else {
                y += bitmap.getHeight() / (size * 1000f);
            }

            number -= getNumTruncation(number,l-n);
        }
    }

    public static int getDigit(int num,int digit){
        int m = pow(10, digit - 1);
        return num / pow(10, digit - 1) % 10;
    }

    public static int getNumTruncation(int num,int digit){
        return getDigit(num,digit) * pow(10,digit-1);
    }

    public static int pow(int n,int d){
        int m = n;
        if(d <= 0)
            return 1;
        for(int l = 1;l < d;l++){
            m *= n;
        }
        return m;
    }
}
