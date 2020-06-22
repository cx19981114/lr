package cn.lr.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 加密工具类,主要用户密码的加密
 * <p>加密算法: SHA</p>
 * 
 * @author zyl
 *
 */
public class EncryptionUtil {
	
    public static final String KEY_SHA = "SHA";   

    /**
           * 加密数据,并返回加密后的密文
     * @param plaintext 明文
     * @return 加密后的密文
     */
    public static String encryping(String plaintext)
    {
        BigInteger sha =null;
        byte[] inputData = plaintext.getBytes();   
        try {
             MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);  
             messageDigest.update(inputData);
             sha = new BigInteger(messageDigest.digest());   
        } catch (Exception e) {e.printStackTrace();}
        return sha.toString(32);
    }
}