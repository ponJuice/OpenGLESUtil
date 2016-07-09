package jp.ac.dendai.c.jtp.UIs.UI.Text;

import android.text.TextWatcher;

import java.util.HashMap;

/**
 * Created by テツヤ on 2016/07/09.
 */
public class NumbersBitmapList {
    private static HashMap<String,NumbersBitmap> list;
    static{
        list = new HashMap<>();
    }

    public static NumbersBitmap getNumbersBitmap(String fontName){
        if(!list.containsKey(fontName))
            list.put(fontName,new NumbersBitmap(fontName));
        return list.get(fontName);
    }

    public static float getWide(String fontName, int number, Text.WrittingAlign wa){
        if(!list.containsKey(fontName))
            setNumbersBitmap(fontName);
        float length = 0;
        int num_l = (int)Math.log10(number) + 1;
        for(int l = 0;l < num_l;l++) {
            if (wa == Text.WrittingAlign.HOLIZONTAL) {
                //横書き
                length += list.get(fontName).getNumber(NumberText.getDigit(num_l, l)).getWidth()/1000f;
            } else {
                //縦書き
                length = list.get(fontName).getNumber(NumberText.getDigit(num_l,l)).getHeight()/1000f;
            }
        }
        return length;
    }

    public static float getHeight(String fontName, int number, Text.WrittingAlign wa){
        if(!list.containsKey(fontName))
            setNumbersBitmap(fontName);
        float length = 0;
        int num_l = (int)Math.log10(number) + 1;
        for(int l = 0;l < num_l;l++) {
            if (wa == Text.WrittingAlign.VERTICCAL) {
                //横書き
                length += list.get(fontName).getNumber(NumberText.getDigit(num_l, l)).getHeight()/1000f;
            } else {
                //縦書き
                length = list.get(fontName).getNumber(NumberText.getDigit(num_l,l)).getWidth()/1000f;
            }
        }
        return length;
    }

    public static float getWide(String fontName,int n,int digit){
        if(!list.containsKey(fontName))
            setNumbersBitmap(fontName);
        return list.get(fontName).getNumber(NumberText.getDigit(n,digit)).getWidth()/1000f;
    }

    public static float getHeight(String fontName,int n,int digit){
        if(!list.containsKey(fontName))
            setNumbersBitmap(fontName);
        return list.get(fontName).getNumber(NumberText.getDigit(n,digit)).getHeight()/1000f;
    }

    public static void setNumbersBitmap(String fontName){
        if(!list.containsKey(fontName))
            list.put(fontName,new NumbersBitmap(fontName));
    }
}
