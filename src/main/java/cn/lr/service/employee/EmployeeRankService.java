package cn.lr.service.employee;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.EmployeeRankDTO;
import cn.lr.dto.Page;
import cn.lr.po.employeeRank;

public interface EmployeeRankService {
	public Page<EmployeeRankDTO> getEmployeeRankByCompany(JSONObject data);
	
	public EmployeeRankDTO sEmployeeRankDTO(List<employeeRank> employeeRanks,Integer employeeId);
	
	public EmployeeRankDTO getEmployeeRankByEmployee(JSONObject data);
	
	public String getEmployeeRankFrist(JSONObject data);
	
	public Integer addEmployeeRank(JSONObject data);
	
	public Integer deleteEmployeeRank(JSONObject data);
}
