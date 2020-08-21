package cn.lr.service.employee;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.Page;
import cn.lr.dto.employeeDTO;
import cn.lr.po.employee;

public interface EmployeeService {
	
	public Integer addEmployee(JSONObject data);
	
	public Integer modifyEmployee(JSONObject data);
	
	public Integer deleteEmployee(JSONObject data) throws Exception ;
	
	public employeeDTO getEmployee(JSONObject data);
	
	public Page<employeeDTO> getEmployeeByCompany(JSONObject data);
	
	public Page<employeeDTO> getEmployeeByPost(JSONObject data);
	
	public List<JSONObject> getEmployeeLeader(JSONObject data);
	
	public List<JSONObject> getEmployeeUnder(JSONObject data);
	
	public List<JSONObject> getEmployeeByPostAll(JSONObject data);
	
	public JSONObject getBusiness(JSONObject data);
	
	public JSONObject getDayMsg(JSONObject data);
	
	public employeeDTO sEmployeeDTO(employee employee);
	
}
