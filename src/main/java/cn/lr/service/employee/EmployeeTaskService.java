package cn.lr.service.employee;

import java.text.ParseException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.EmployeeTaskDTO;
import cn.lr.dto.Page;
import cn.lr.po.employeeTask;
import cn.lr.po.task;

public interface EmployeeTaskService {
	public Page<task> getTaskByEmployee(JSONObject data);

	public Integer addEmployeeTask(JSONObject data);

	public Integer deleteEmployeeTask(JSONObject data);
	
	public Integer affirmEmployeeTask(JSONObject data);
	
	public Integer modifyEmployeeTask(JSONObject data);
	
	public Integer annulEmployeeTask(JSONObject data);
	
	public JSONObject getEmployeeTask(JSONObject data) throws ParseException;
	
	public EmployeeTaskDTO sEmployeeTaskDTO(employeeTask employeeTask) throws ParseException;
	
	public List<JSONObject> getTaskByEmployeeList(JSONObject data);
	
	public Page<EmployeeTaskDTO> getEmployeeTaskByEmployee(JSONObject data) throws ParseException;
	
}
