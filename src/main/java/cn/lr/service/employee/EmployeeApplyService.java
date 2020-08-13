package cn.lr.service.employee;

import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.EmployeeApplyDTO;
import cn.lr.dto.Page;

public interface EmployeeApplyService {
	public Page<EmployeeApplyDTO> getEmployeeApplyByEmployee(JSONObject data) throws ParseException;

	public Integer addEmployeeApply(JSONObject data);

	public Integer modifyEmployeeApply(JSONObject data);

	public Integer deleteEmployeeApply(JSONObject data);
	
	public JSONObject getEmployeeApply(JSONObject data) throws ParseException;
	
	public Integer affirmEmployeeApply(JSONObject data);
	
	public Integer annulEmployeeApply(JSONObject data);
	
}
