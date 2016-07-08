package jp.ac.dendai.c.jtp.Game;

import android.graphics.Bitmap;

import java.util.HashMap;

import jp.ac.dendai.c.jtp.openglesutil.R;
import jp.ac.dendai.c.jtp.openglesutil.core.GLES20Util;

/**
 * 表示するビットマップを管理するクラス
 * @author
 *
 */
public class BitmapList {
	/**
	 * 無かったと起用
	 */
	private static Bitmap noImage;
	/**
	 *
	 * ビットマップ配列
	 */
	private static HashMap<Integer,Bitmap> bitmap = new HashMap<>();

	/**
	 * 現在登録されている量を返します
	 * @return 現在登録されている量
	 */
	public static int getIndex(){
		return bitmap.size();
	}
	/**
	 * 指定したidのビットマップを取得します
	 * @param 取得したいビットマップのid
	 * @return 成功した場合は指定したビットマップ。失敗した場合はnullを返します
	 */
	public static Bitmap getBitmap(int id){
		if(bitmap.containsKey(id)){
			return bitmap.get(id);
		}
		if(noImage == null)
			noImage = GLES20Util.loadBitmap(R.drawable.noimage);
		return noImage;
	}
	/**
	 * ビットマップを登録します
	 * @param 登録するビットマップ
	 * @return　呼び出すときのID
	 */
	public static int setBitmap(int id){
		if(!bitmap.containsKey(id)){
			bitmap.put(id,GLES20Util.loadBitmap(id));
			return id;
		}
		else{
			return id;
		}
	}
	/**
	 * 登録したbitmapを解放します。
	 * @param 解放したいbitmapのID
	 * @return 0:正常終了 -1:IDの範囲外(1~128)
	 */
	public static int deleteBitmap(int id){
		if(bitmap.containsKey(id)){
			bitmap.remove(id);
		}
		return 0;
	}

	public static void clear(){
		bitmap.clear();
	}
}
