package cn.lr.service.company;

import com.alibaba.fastjson.JSONObject;

import cn.lr.po.pic;

public interface PicService {

	public JSONObject getPicByCompany(JSONObject data);
	
	public Integer addPic(JSONObject data);
	
	public Integer modifyPic(JSONObject data);
	
	public Integer deletePic(JSONObject data);
	
	public pic getPic(JSONObject data);
}
