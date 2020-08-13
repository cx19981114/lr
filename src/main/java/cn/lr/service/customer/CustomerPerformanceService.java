package cn.lr.service.customer;

import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.CustomerPerformanceDTO;
import cn.lr.dto.Page;
import cn.lr.po.customerPerformance;

public interface CustomerPerformanceService {
	public Integer addCustomerPerformance(JSONObject data);
	
	public Integer annulCustomerPerformance(JSONObject data);
	
	public Integer affirmCustomerPerformance(JSONObject data);
	
	public Integer deleteCustomerPerformance(JSONObject data);
	
	public customerPerformance getCustomerPerformance(JSONObject data);
	
	public JSONObject getCustomerPerformanceByEmployeeCondition(JSONObject data) throws ParseException;
	
	public CustomerPerformanceDTO sCustomerPerformanceDTO(customerPerformance customerPerformance) throws ParseException;
	
	public Page<CustomerPerformanceDTO> getCustomerPerformanceByEmployee(JSONObject data) throws ParseException;
	
	public JSONObject getCustomerPerformanceDetail(JSONObject data) throws ParseException;
}
