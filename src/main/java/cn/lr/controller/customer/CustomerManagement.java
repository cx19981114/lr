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

import cn.lr.dto.CustomerDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.service.customer.CustomerService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/CustomerManagement")
@Controller
public class CustomerManagement {
	@Autowired
	private CustomerService CustomerService;
	
	@PostMapping("/addCustomer")
	@ResponseBody
	public String addCustomer(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加客户信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			int id = CustomerService.addCustomer(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "添加客户信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加客户信息--------------------");
		}
	}
	@PostMapping("/modifyCustomer")
	@ResponseBody
	public String modifyCustomer(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改客户信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CustomerService.getCustomer(dataJson);
			int id = CustomerService.modifyCustomer(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "修改客户信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 修改客户信息--------------------");
		}
	}
	@PostMapping("/getCustomer")
	@ResponseBody
	public String getCustomer(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据id获取客户信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CustomerDTO customerDTO = CustomerService.getCustomer(dataJson);
			return ResultJsonUtil.toJsonString(200, customerDTO, "根据id获取客户信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据id获取客户信息--------------------");
		}
	}
	@PostMapping("/getCustomerByEmployee")
	@ResponseBody
	public String getCustomerByEmployee(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获取客户信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<JSONObject> customers = CustomerService.getCustomerByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, customers, "根据职员id获取客户信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取客户信息--------------------");
		}
	}
	@PostMapping("/getCustomerByEmployeeList")
	@ResponseBody
	public String getCustomerByEmployeeList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获取客户部分信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			List<JSONObject> customers = CustomerService.getCustomerByEmployeeList(dataJson);
			return ResultJsonUtil.toJsonString(200, customers, "根据职员id获取客户部分信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取客户部分信息--------------------");
		}
	}
	@PostMapping("/getNewCustomerType")
	@ResponseBody
	public String getNewCustomerType(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获取客户类型信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			List<JSONObject> customers = CustomerService.getNewCustomerTypeByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, customers, "根据职员id获取客户类型信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取客户类型信息--------------------");
		}
	}
	@PostMapping("/getNewCustomerList")
	@ResponseBody
	public String getNewCustomerList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id和时间段获取客户信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<JSONObject> customers = CustomerService.getNewCustomerByEmployeeTime(dataJson);
			return ResultJsonUtil.toJsonString(200, customers, "根据职员id和时间段获取客户信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id和时间段获取客户信息--------------------");
		}
	}
}