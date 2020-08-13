package cn.lr.util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DownloadPicUtil {
	public static byte[] imageIn(String path){
		          //创建一个字节输出流
		          DataInputStream dataInputStream = null;
		          try {
		              dataInputStream = new DataInputStream(new FileInputStream(path));
		              //创建一个字节数组  byte的长度等于二进制图片的返回的实际字节数
		              byte[] b = new byte[dataInputStream.available()];
		              //读取图片信息放入这个b数组
		              dataInputStream.read(b);
		              return b;
		          } catch (FileNotFoundException e) {
		              e.printStackTrace();
		          } catch (IOException e) {
		              e.printStackTrace();
		          }finally {
		              try {
		                  //关闭流
		                  dataInputStream.close();
		              } catch (IOException e) {
		                  e.printStackTrace();
		              }
		          }
		          return null;
		      }
}
