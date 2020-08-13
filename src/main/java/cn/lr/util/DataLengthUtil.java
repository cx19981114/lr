package cn.lr.util;

import java.math.BigDecimal;

public class DataLengthUtil {
	public static int LengthNum(int num) {
		int count = 0; // 计数
		if(num == 0) {
			count++;
		}
		while (num >= 1) {
			num /= 10;
			count++;
		}
		return count;
	}
	public static String CountNum(int num) {
		int numCount = LengthNum(num);
		if(numCount > 3) {
			String wString = "";
			double a;
			BigDecimal b; 
			double f1; 
			if(numCount == 4) {
				a = num/(Math.pow(10,3));
				b = new BigDecimal(a); 
				f1 = b.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue(); 
				wString = "k";
			}else {
				a = num/(Math.pow(10,4));
				b = new BigDecimal(a); 
				f1 = b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue(); 
				wString = "w";
			}
			return f1+wString;
		}
		return null;
	}
}
