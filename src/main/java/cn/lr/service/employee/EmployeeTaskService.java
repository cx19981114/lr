package cn.lr.service.employee;

import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.EmployeeTaskDTO;
import cn.lr.dto.Page;
import cn.lr.po.task;

public interface EmployeeTaskService {
	public Page<task> getTaskByEmployee(JSONObject data);

	public Integer addEmployeeTask(JSONObject data);

	public Integer deleteEmployeeTask(JSONObject data);
	
	public Integer affirmEmployeeTask(JSONObject data);
	
	public Integer modifyEmployeeTask(JSONObject data);
	
	public Integer annulEmployeeTask(JSONObject data);
	
	public JSONObject getEmployeeTask(JSONObject data) throws ParseException;
	
	public EmployeeTaskDTO sEmployeeTaskDTO(task task,Integer employeeId,Integer companyId) throws ParseException;
	
}
