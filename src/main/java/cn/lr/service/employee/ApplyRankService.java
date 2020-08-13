package cn.lr.service.employee;

import java.text.ParseException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.ApplyRankDTO;
import cn.lr.dto.EmployeeTaskDTO;
import cn.lr.dto.Page;
import cn.lr.dto.employeeDTO;

public interface ApplyRankService {

	public Integer addApplyRank(JSONObject data);
	
	public Integer modifyApplyRank(JSONObject data);
	
	public Integer deleteApplyRank(JSONObject data);
	
	public List<ApplyRankDTO> getApplyRank(JSONObject data) throws ParseException;
	
	public List<ApplyRankDTO> getApplyRankByDynamic(JSONObject data) throws ParseException;
	
	public List<employeeDTO> getCheckList(JSONObject data);
	
	public void setState(Integer dynamicId,Integer state,Integer checkId,Integer companyId);
	
	public Page<EmployeeTaskDTO> getEmployeeTask(JSONObject data) throws ParseException;
	
	public void setEmployeeTask(Integer dynamicId, Integer state,Integer companyId);
	
	public Integer annulApplyRank(JSONObject data);
	
}
