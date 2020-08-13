package cn.lr.service.company;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.Page;
import cn.lr.po.system;

public interface SystemService {
	
	public Integer addSystem(JSONObject data);
	
	public Integer modifySystem(JSONObject data);
	
	public Integer deleteSystem(JSONObject data);
	
	public system getSystem(JSONObject data);
	
	public Page<system> getSystemByCompany(JSONObject data);
}
