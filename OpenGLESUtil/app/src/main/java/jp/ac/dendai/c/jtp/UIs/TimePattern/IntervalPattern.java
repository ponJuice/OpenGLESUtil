package jp.ac.dendai.c.jtp.UIs.TimePattern;

public class IntervalPattern implements TimePattern {
	private int maxCount,count,index,deltaTime;
	private int[] intervals;

	public IntervalPattern(int maxCount,int[] intervals) {
		this.maxCount = maxCount;
		count = 0;
		index = 0;
		deltaTime = 0;
		this.intervals = intervals;
	}

	@Override
	public boolean pulse(int totalFrame) {
		if(maxCount > count && intervals[index] >= (totalFrame-deltaTime)){
			if(index == intervals.length-1){
				index = 0;
			}
			else{
				index++;
			}
			deltaTime = totalFrame;
			if(count >= 0){
				count++;
			}
			return true;
		}
		return false;
	}

}
