package cn.lr.service.company;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.Page;
import cn.lr.po.project;

public interface ProjectService {

	public Integer addProject(JSONObject data);
	
	public Integer modifyProject(JSONObject data);
	
	public Integer deleteProject(JSONObject data);
	
	public project getProject(JSONObject data);
	
	public Page<project> getProjectByCompany(JSONObject data);
	
	public List<JSONObject> getProjectByCompanyJson(JSONObject data);
}
