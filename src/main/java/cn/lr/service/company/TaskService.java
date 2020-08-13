package cn.lr.service.company;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.Page;
import cn.lr.po.task;

public interface TaskService {
	
	public Integer addTask(JSONObject data);
	
	public Integer modifyTask(JSONObject data);
	
	public Integer deleteTask(JSONObject data);
	
	public task getTask(JSONObject data);
	
	public Page<task> getTaskByCompany(JSONObject data);
	
	public JSONObject testType(JSONObject data);

	public void changePostList(String idList, Integer taskId, boolean isAdd,Integer prevType,Integer type,Integer companyId);
	
	public void changeEmployeeList(String idList, Integer taskId, boolean isAdd,Integer prevType,Integer type,Integer companyId);
	
	public Page<task> getTaskByPost(JSONObject data);
}
