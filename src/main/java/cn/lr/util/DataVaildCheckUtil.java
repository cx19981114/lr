package cn.lr.util;

import java.util.regex.Pattern;

public class DataVaildCheckUtil {
	public static boolean isValidUserName(String userName) {
		if(userName.equals("")) {
			LoggerUtil.LOGGER.warn("空类型");
			return false;
		}
		return Pattern.matches("^[\\u4E00-\\u9FA5A-Za-z]{2,20}$", userName);
	}
	public static boolean isValidPhone(String phone) {
		if(phone.equals("")) {
			LoggerUtil.LOGGER.warn("空类型");
			return false;
		}
		return Pattern.matches("^1[3456789]\\d{9}$", phone) || Pattern.matches("^\\d{8}$", phone);
	}
	public static boolean isValidPassword(String password) {
		if(password == null) {
			LoggerUtil.LOGGER.warn("空类型");
			return false;
		}
		return Pattern.matches("^[a-zA-Z0-9]{6,20}$", password);
	}
	public static boolean isValidApparatusId(String apparatusId) {
		if(apparatusId.equals("")) {
			LoggerUtil.LOGGER.warn("空类型");
			return false;
		}
		return Pattern.matches("^v[0-9]{4}$", apparatusId);
	}
	public static boolean isValidEmail(String email) {
		if(email.equals("")) {
			LoggerUtil.LOGGER.warn("空类型");
			return false;
		}
		return Pattern.matches("^[a-zA-Z0-9]{1,10}@zucc.edu.cn$", email);
	}
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	} 
}
