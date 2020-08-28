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

	public void changePostList(Integer idList, Integer taskId, Integer step,boolean isAdd, Integer prevType, Integer type,Integer companyId);
	
	public Page<task> getTaskByPost(JSONObject data);
	
	public Integer maxStep(JSONObject data);
	
	public void AddTaskByExcel(JSONObject data) throws Exception;
}
