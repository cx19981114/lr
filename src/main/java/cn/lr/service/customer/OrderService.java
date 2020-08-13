package cn.lr.service.customer;

import java.text.ParseException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.OrderDTO;
import cn.lr.dto.OrderDetailDTO;
import cn.lr.dto.Page;
import cn.lr.po.order;

public interface OrderService {

	public Page<OrderDTO> getOrderByEmployeeCondition(JSONObject data) throws ParseException;
	
	public Integer addOrder(JSONObject data);
	
	public Integer annulOrder(JSONObject data);
	
	public Integer affirmOrder(JSONObject data);
	
	public Integer deleteOrder(JSONObject data);
	
	public List<JSONObject> getOrderByEmployeeType(JSONObject data);
	
	public order getOrder(JSONObject data);
	
	public List<JSONObject> getOrderTime(JSONObject data);
	
	public OrderDTO sOrderDTO(order order) throws ParseException;
	
	public Integer startOrder(JSONObject data);
	
	public Integer endOrder(JSONObject data);
	
	public Integer cancelOrder(JSONObject data);
	
	public Integer affirmOrderFinish(JSONObject data);
	
	public OrderDetailDTO sOrderDetailDTO(order order) throws ParseException;
	
	public JSONObject getOrderDetail(JSONObject data) throws ParseException;
	
	public List<JSONObject> getOrderHistoryByEmployee(JSONObject data);
	
	public List<JSONObject> getOrderByProjectType(JSONObject data) throws ParseException;
	
	public Integer annulOrderFinish(JSONObject data);
	
	public Integer deleteOrderFinish(JSONObject data);
	
	public Page<OrderDTO> getOrderByEmployee(JSONObject data) throws ParseException;
	
}
