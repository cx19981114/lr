package cn.lr.util;

import com.alibaba.fastjson.JSONObject;

 /**
  * 统一控制controller层的返回json格式, 字段可以缺失,但是不能多
  * <pre>
  *  {
  *     "code: int,
  *     "message": String,
  *     "data" : object
  *  }
  * </pre>
  * 
 */
public class ResultJsonUtil {
	
	/**
	 * 格式化返回json格式String, 
	 * <b>如果传入的参数为<code>null</code>,则该字段不会出现在返回json中</b>
	 * @param code 状态代码
	 * <pre>
	 *  -1: 系统错误
	 *   0: 业务异常
	 *   1: 成功
	 * </pre>
	 * @param message 错误信息
	 * @param data 返回数据
	 * @return 返回以上参数组合成的json格式String
	 */
	public static String toJsonString(int code, String message, Object data) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", code);
		jsonObject.put("message", message);
		jsonObject.put("data", data);
		return jsonObject.toJSONString();
	}
}
