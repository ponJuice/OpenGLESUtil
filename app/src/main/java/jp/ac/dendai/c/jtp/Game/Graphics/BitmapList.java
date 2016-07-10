package jp.ac.dendai.c.jtp.Game.Graphics;

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
	private static BitmapContainer noImage;
	/**
	 *
	 * ビットマップ配列
	 */
	private static HashMap<Integer,BitmapContainer> bitmap = new HashMap<>();

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
			return bitmap.get(id).get();
		}
		if(noImage == null) {
			noImage = new BitmapContainer(GLES20Util.loadBitmap(R.drawable.noimage));
		}
		return noImage.get();
	}
	/**
	 * ビットマップを登録します
	 * @param 登録するビットマップ
	 * @return　呼び出すときのID
	 */
	public static int setBitmap(int id){
		if(!bitmap.containsKey(id)){
			bitmap.put(id,new BitmapContainer(GLES20Util.loadBitmap(id)));
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

	public static BitmapContainer getAnimationBitmap(int id){
		if(!bitmap.containsKey(id)){
			if(noImage == null) {
				noImage = new BitmapContainer(GLES20Util.loadBitmap(R.drawable.noimage));
			}
			try {
				throw new Exception();
			}catch (Exception e){
				e.printStackTrace();
			}
			return noImage;
		}
		return bitmap.get(id);
	}

	public static int setAnimationBitmap(BitmapContainer c,int id){
		if(!bitmap.containsKey(id)){
			bitmap.put(id,c);
		}
		return id;
	}

	public static void clear(){
		bitmap.clear();
	}
}
