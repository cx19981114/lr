package cn.lr.util;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

public class SendMessageUtil {
	 public static String [] sendMsgByTxPlatform(String phone) throws Exception {

	       // 短信应用SDK AppID
	       int appId = 1400391360;

	       // 短信应用SDK AppKey
	       String appKey = "7313b620354ed171f158be51b16c359c";

	       // 短信模板ID，需要在短信应用中申请
	       int templateId = 649199;

	       // 签名
	       // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
	       String smsSign = "ppp啦小程序";

	       SmsSingleSender sSender = new SmsSingleSender(appId, appKey);
	       
	       String [] params ={RandomCodeUtil.getSixValidationCode()};
	       SmsSingleSenderResult result = sSender.sendWithParam("86",phone,templateId,params, smsSign, "","");

	       if (result.result != 0) {
	           throw new Exception("send phone validateCode is error" + result.errMsg);
	       }	
	       return params;
	   }
}
