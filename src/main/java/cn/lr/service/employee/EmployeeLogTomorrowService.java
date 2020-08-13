package cn.lr.service.employee;

import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.EmployeeLogTomorrowDTO;
import cn.lr.dto.Page;
import cn.lr.po.employeeLogTomorrow;

public interface EmployeeLogTomorrowService {

	public Page<EmployeeLogTomorrowDTO> getEmployeeLogTomorrowByEmployee(JSONObject data) throws ParseException;
	
	public JSONObject getEmployeeLogTomorrow(JSONObject data) throws ParseException;
	
	public Integer addEmployeeLogTomorrow(JSONObject data);
	
	public Integer modifyEmployeeLogTomorrow(JSONObject data);
	
	public Integer deleteEmployeeLogTomorrow(JSONObject data);
	
	public EmployeeLogTomorrowDTO sEmployeeLogTomorrowDTO(employeeLogTomorrow employeeLogTomorrow) throws ParseException;
	
	public Integer affirmEmployeeLogTomorrow(JSONObject data);
	
	public EmployeeLogTomorrowDTO getEmployeeLogTomorrowByEmployeeNew(JSONObject data) throws ParseException;
	
	public Integer annulEmployeeLogTomorrow(JSONObject data);
}
