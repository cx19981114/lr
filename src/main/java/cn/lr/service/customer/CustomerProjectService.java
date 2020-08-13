package cn.lr.service.customer;

import java.text.ParseException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.CustomerProjectDTO;
import cn.lr.dto.CustomerProjectDetailDTO;
import cn.lr.dto.Page;
import cn.lr.po.customerProject;

public interface CustomerProjectService {
	public Integer addCustomerProject(JSONObject data);
	
	public List<JSONObject> getCustomerProjectByCustomer(JSONObject data);
	
	public customerProject getCustomerProject(JSONObject data);
	
	public Integer deleteCustomerProject(JSONObject data);
	
	public Integer annulCustomerProject(JSONObject data);
	
	public Integer affirmCustomerProject(JSONObject data);
	
	public Page<CustomerProjectDTO> getCustomerProjectByEmployee(JSONObject data) throws ParseException;
	
	public CustomerProjectDTO sCustomerProjectDTO(customerProject customerProject) throws ParseException;
	
	public CustomerProjectDetailDTO sCustomerProjectDetailDTO(customerProject customerProject) throws ParseException;
	
	public JSONObject getCustomerProjectDetail(JSONObject data) throws ParseException;
}
