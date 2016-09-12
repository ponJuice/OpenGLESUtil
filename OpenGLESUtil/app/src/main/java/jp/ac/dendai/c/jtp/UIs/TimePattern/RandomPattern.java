package jp.ac.dendai.c.jtp.UIs.TimePattern;

import java.util.Random;

public class RandomPattern implements TimePattern {
	private static Random ram = new Random();
	private int maxCount,count;
	public RandomPattern(int maxCount) {
		this.maxCount = maxCount;
		count = 0;
	}

	@Override
	public boolean pulse(int totalFrame) {
		if(maxCount > count){
			if(count >= 0){
				count++;
			}
			return ram.nextBoolean();
		}
		return false;
	}

}
