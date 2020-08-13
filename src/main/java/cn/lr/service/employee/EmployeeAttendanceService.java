package cn.lr.service.employee;

import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.EmployeeAttendanceDTO;
import cn.lr.dto.Page;
import cn.lr.po.employeeAttendance;

public interface EmployeeAttendanceService {
	public Page<EmployeeAttendanceDTO> getEmployeeAttendanceByEmployee(JSONObject data) throws ParseException;

	public Integer addEmployeeAttendance(JSONObject data) throws ParseException;

	public Integer modifyEmployeeAttendance(JSONObject data);

	public Integer deleteEmployeeAttendance(JSONObject data);
	
	public Integer annulEmployeeAttendance(JSONObject data);
	
	public JSONObject getEmployeeAttendance(JSONObject data) throws ParseException;
	
	public EmployeeAttendanceDTO sEmployeeAttendanceDTO(employeeAttendance employeeAttendance) throws ParseException;
	
	public Integer affirmEmployeeAttendance(JSONObject data);
	
	public EmployeeAttendanceDTO getEmployeeAttendanceByEmployeeNew(JSONObject data) throws ParseException;
}
