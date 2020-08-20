package cn.lr.service.employee;

import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.DynamicDTO;
import cn.lr.dto.Page;
import cn.lr.po.dynamic;

public interface DynamicService {
	public Integer writeDynamic(JSONObject data);
	
	public dynamic getDynamic(JSONObject data);
	
	public Page<DynamicDTO> getDynamicByEmployee(JSONObject data) throws ParseException;
	
	public Page<DynamicDTO> getDynamicByCheck(JSONObject data) throws ParseException;

	public Page<DynamicDTO> getDynamicByCompany(JSONObject data) throws ParseException;
	
	public dynamic getDynamicByTypeAndId(JSONObject data);
	
	public Integer deleteDynamic(JSONObject data);
	
	public dynamic selectByTypeAndIdAndEmployeeId(JSONObject data);
	
	public Integer modifyDynamic(JSONObject data);
	
	public DynamicDTO sDynamicDTO(dynamic dynamic) throws ParseException;
	
	public Integer annulDynamic(JSONObject data);
	
	public Page<DynamicDTO> getDynamicByCheckHistory(JSONObject data) throws ParseException;
	
	public Integer setStateAbnormal(JSONObject data);
	
	public Page<DynamicDTO> getAbnormalDynamic(JSONObject data) throws ParseException;
	
	public Integer setStateAgain(JSONObject data);
}
