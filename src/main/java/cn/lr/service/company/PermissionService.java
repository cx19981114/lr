package cn.lr.service.company;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface PermissionService {
	public List<JSONObject> getPermissionList(JSONObject data);
	
	public Integer setPermission(JSONObject data);
}
