package cn.lr.controller.customer;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.OrderDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.service.customer.CustomerProjectService;
import cn.lr.service.customer.CustomerService;
import cn.lr.service.customer.OrderService;
import cn.lr.service.employee.EmployeeService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/OrderManagement")
@Controller
public class OrderManagement {
	@Autowired
	private OrderService OrderService;
	@Autowired
	private CustomerService CustomerService;
	@Autowired
	private CustomerProjectService CustomerProjectService;
	@Autowired
	private EmployeeService EmployeeService;
	
	@PostMapping("/addOrder")
	@ResponseBody
	public String addOrder(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加预约信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CustomerService.getCustomer(dataJson);
			CustomerProjectService.getCustomerProject(dataJson);
			Integer id = OrderService.addOrder(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "添加预约成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 添加预约信息--------------------");
		}
	}
	@PostMapping("/annulOrder")
	@ResponseBody
	public String annulOrder(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 撤销预约信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			OrderService.getOrder(dataJson);
			Integer id = OrderService.annulOrder(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "撤销预约成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 撤销预约信息--------------------");
		}
	}
	@PostMapping("/affirmOrder")
	@ResponseBody
	public String affirmOrder(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 确认预约信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			OrderService.getOrder(dataJson);
			Integer id = OrderService.affirmOrder(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "确认预约成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 确认预约信息--------------------");
		}
	}
	@PostMapping("/deleteOrder")
	@ResponseBody
	public String deleteOrder(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除预约信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			OrderService.getOrder(dataJson);
			Integer id = OrderService.deleteOrder(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "删除预约成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 删除预约信息--------------------");
		}
	}
	@PostMapping("/getOrderByEmployeeCondition")
	@ResponseBody
	public String getOrderByEmployeeAndState(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id和状态获取今日预约信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeService.getEmployee(dataJson);
			Page<OrderDTO> order = OrderService.getOrderByEmployeeCondition(dataJson);
			return ResultJsonUtil.toJsonString(200, order, "根据职员id和状态获取今日预约成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id和状态获取今日预约信息--------------------");
		}
	}
	@PostMapping("/getOrderByProjectType")
	@ResponseBody
	public String getOrderByProjectType(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id和日期范围获取预约项目类型信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeService.getEmployee(dataJson);
			List<JSONObject> order = OrderService.getOrderByProjectType(dataJson);
			return ResultJsonUtil.toJsonString(200, order, "根据职员id和日期范围获取预约项目类型成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id和日期范围获取预约项目类型信息--------------------");
		}
	}
	@PostMapping("/getOrderByEmployeeType")
	@ResponseBody
	public String getOrderByEmployeeType(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获取预约类型信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeService.getEmployee(dataJson);
			List<JSONObject> orderType = OrderService.getOrderByEmployeeType(dataJson);
			return ResultJsonUtil.toJsonString(200, orderType, "根据职员id获取预约类型成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取预约类型信息--------------------");
		}
	}
	@PostMapping("/getOrderTime")
	@ResponseBody
	public String getOrderTime(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获取当天可预约时间信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeService.getEmployee(dataJson);
			List<JSONObject> timeJson = OrderService.getOrderTime(dataJson);
			return ResultJsonUtil.toJsonString(200, timeJson, "根据职员id获取当天可预约时间成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取当天可预约时间信息--------------------");
		}
	}
	@PostMapping("/startOrder")
	@ResponseBody
	public String startOrder(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 预约开始--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			OrderService.getOrder(dataJson);
			Integer id = OrderService.startOrder(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "预约开始成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 预约开始--------------------");
		}
	}
	@PostMapping("/endOrder")
	@ResponseBody
	public String endOrder(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 预约结束--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			OrderService.getOrder(dataJson);
			Integer id = OrderService.endOrder(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "预约结束", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 预约结束--------------------");
		}
	}
	@PostMapping("/cancelOrder")
	@ResponseBody
	public String cancelOrder(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 取消预约--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			OrderService.getOrder(dataJson);
			Integer id = OrderService.cancelOrder(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "取消预约", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 取消预约--------------------");
		}
	}
	@PostMapping("/affirmOrderFinish")
	@ResponseBody
	public String affirmOrderFinish(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 确认预约结束--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			OrderService.getOrder(dataJson);
			Integer id = OrderService.affirmOrderFinish(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "确认预约结束成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 确认预约结束--------------------");
		}
	}
	@PostMapping("/getOrderDetail")
	@ResponseBody
	public String getOrderDetail(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取预约详细信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			OrderService.getOrder(dataJson);
			JSONObject orderDetailDTO = OrderService.getOrderDetail(dataJson);
			return ResultJsonUtil.toJsonString(200, orderDetailDTO, "获取预约详细成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 获取预约详细信息--------------------");
		}
	}
	@PostMapping("/getOrderHistoryByEmployee")
	@ResponseBody
	public String getOrderHistoryByEmployee(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获取预约历史信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeService.getEmployee(dataJson);
			Page<JSONObject> orderHistory = OrderService.getOrderHistoryByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, orderHistory, "根据职员id获取预约历史成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取预约历史信息--------------------");
		}
	}
	@PostMapping("/getOrderByEmployee")
	@ResponseBody
	public String getOrderByEmployee(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获取预约信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeService.getEmployee(dataJson);
			Page<OrderDTO> orderDTOs = OrderService.getOrderByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, orderDTOs, "根据职员id获取预约成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取预约信息--------------------");
		}
	}

}
