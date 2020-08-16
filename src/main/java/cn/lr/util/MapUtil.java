package cn.lr.util;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
public class MapUtil {
	/**
     * 百度地图请求秘钥
     */
    private static final String KEY = "3knOCGnftK2XnlykbLApvbMjXpaWdIT1";
    /**
     * 返回值类型
     */
    private static final String OUTPUT = "json";
    /**
     * 根据地名获取经纬度
     */
    private static final String GET_LNG_LAT_URL = "http://api.map.baidu.com/geocoder/v2/";
    /**
     * 根据经纬度获取地名
     */
    private static final String GET_ADDRESS_URL = "http://api.map.baidu.com/reverse_geocoding/v3/";



    /**
     * 根据经纬度获得省市区信息
     * @param lon 纬度
     * @param lat 经度
     * @param coordtype 经纬度坐标系
     * @return
     */
    public static Map<String, String> getCityByLonLat(double lng, double lat, String coordtype) {
        String location = lat + "," + lng;
        Map<String, String> params = new HashMap<>();
        params.put("location", location);
        params.put("coordtype", coordtype);
        System.out.println(params.toString());
        try {
            //拼装url
            String url = joinUrl(params, OUTPUT, KEY, GET_ADDRESS_URL);
            JSONObject result = JSONObject.parseObject(JSONObject.parseObject(JSONObject.parseObject(HttpClientUtils.doGet(url)).
                    getString("result")).getString("addressComponent"));
            Map<String, String> area = new HashMap<>();
            area.put("province", result.getString("province"));
            area.put("city", result.getString("city"));
            area.put("district", result.getString("district"));
            area.put("street", result.getString("street"));
            return area;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拼接url字符串
     * @param params
     * @param output
     * @param key
     * @param url
     * @return
     * @throws IOException
     */
    private static String joinUrl(Map<String, String> params, String output, String key, String url) throws IOException {
        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(url);

        int index = 0;
        Set<Map.Entry<String, String>> entrys = params.entrySet();
        for (Map.Entry<String, String> param : entrys) {
            // 判断是否是第一个参数
            if (index == 0) {
                baseUrl.append("?");
            } else {
                baseUrl.append("&");
            }
            System.out.println(param.getValue());
            baseUrl.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue(), "UTF-8"));
            index++;
        }
        baseUrl.append("&output=").append(output).append("&ak=").append(key);

        return baseUrl.toString();
    }	
}
