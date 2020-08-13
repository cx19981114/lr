package cn.lr.util;

import com.alibaba.fastjson.JSONObject;

public class SortUtil {
	public static JSONObject sortFristNumTogether(String[] args,String[] state) {
		String liString = "";
		String stateString = "";
		for (int j = 0; j < args.length - 1; j++) {
			if (Integer.valueOf(args[j]) > Integer.valueOf(args[j + 1])) {
				String temp = args[j + 1];
				args[j + 1] = args[j];
				args[j] = temp;
				temp = state[j+1];
				state[j+1] = state[j];
				state[j] = temp;
			}
			liString += args[j] +"-";
			stateString += state[j]+"-";
		}
		liString += args[args.length - 1] +"-";
		stateString += state[state.length - 1] +"-";
		JSONObject data = new JSONObject();
		data.put("idList", liString);
		data.put("stateList", stateString);
		return data;
	}
	public static String sortFristNum(String[] args) {
		String liString = "";
		for (int j = 0; j < args.length - 1; j++) {
			if (Integer.valueOf(args[j]) > Integer.valueOf(args[j + 1])) {
				String temp = args[j + 1];
				args[j + 1] = args[j];
				args[j] = temp;
			}
			liString += args[j] +"-";
		}
		liString += args[args.length - 1] +"-";
		return liString;
	}
}
