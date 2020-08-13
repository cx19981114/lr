package cn.lr.service.employee;

import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.EmployeeLogDayDTO;
import cn.lr.dto.Page;
import cn.lr.po.employeeLogDay;

public interface EmployeeLogDayService {

	public Page<EmployeeLogDayDTO> getEmployeeLogDayByEmployee(JSONObject data) throws ParseException;
	
	public JSONObject getEmployeeLogDay(JSONObject data) throws ParseException;
	
	public Integer addEmployeeLogDay(JSONObject data);
	
	public Integer modifyEmployeeLogDay(JSONObject data);
	
	public Integer deleteEmployeeLogDay(JSONObject data);
	
	public EmployeeLogDayDTO sEmployeeLogDayDTO(employeeLogDay employeeLogDay) throws ParseException;
	
	public Integer affirmEmployeeLogDay(JSONObject data);
	
	public EmployeeLogDayDTO getEmployeeLogDayByEmployeeNew(JSONObject data) throws ParseException;
	
	public Integer annulEmployeeLogDay(JSONObject data);
}
