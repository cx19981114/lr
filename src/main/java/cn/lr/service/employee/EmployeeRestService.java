package cn.lr.service.employee;

import java.text.ParseException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.EmployeeRestDTO;
import cn.lr.dto.Page;

public interface EmployeeRestService {
	public Page<EmployeeRestDTO> getEmployeeRestByEmployee(JSONObject data) throws ParseException;

	public Integer addEmployeeRest(JSONObject data) throws ParseException;

	public Integer modifyEmployeeRest(JSONObject data);

	public Integer deleteEmployeeRest(JSONObject data);
	
	public Integer annulEmployeeRest(JSONObject data);
	
	public JSONObject getEmployeeRest(JSONObject data) throws ParseException; 
	
	public List<JSONObject> getEmployeeRestTypeList(JSONObject data);
	
	public Page<EmployeeRestDTO> getEmployeeRestByEmployeeTeam(JSONObject data) throws ParseException;
	
	public List<List<JSONObject>> getEmployeeRestByEmployeeTeamList(JSONObject data) throws ParseException;
	
	public Integer affirmEmployeeRest(JSONObject data);
	
	public EmployeeRestDTO getEmployeeRestByEmployeeNew(JSONObject data) throws ParseException;
	
}
