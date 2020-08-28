package cn.lr.service.customer;

import java.text.ParseException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.CustomerDTO;
import cn.lr.dto.Page;
import cn.lr.po.customer;

public interface CustomerService {

	public Integer addCustomer(JSONObject data);
	
	public Integer modifyCustomer(JSONObject data);
	
	public CustomerDTO getCustomer(JSONObject data) throws ParseException;
	
	public Page<JSONObject> getCustomerByEmployee(JSONObject data) ;
	
	public List<JSONObject> getNewCustomerTypeByEmployee(JSONObject data);
	
	public Page<JSONObject> getNewCustomerByEmployeeTime(JSONObject data);
	
	public CustomerDTO sCustomerDTO(customer customer) throws ParseException;
	
	public List<JSONObject> getCustomerByEmployeeList(JSONObject data);
	
	public Page<JSONObject> getCustomerByService(JSONObject data)  throws ParseException;
	
	public Page<JSONObject> getCustomerByConsume(JSONObject data)  throws ParseException;
	
	public JSONObject getCustomerChart(JSONObject data);
	
	public Page<JSONObject> getCustomerByUnService(JSONObject data) throws ParseException;
}
