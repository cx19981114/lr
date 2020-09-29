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

import cn.lr.dto.CustomerPerformanceDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.service.customer.CustomerPerformanceService;
import cn.lr.service.customer.CustomerService;
import cn.lr.service.employee.EmployeeService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/CustomerPerformanceManagement")
@Controller
public class CustomerPerformanceManagement {
	
	@Autowired
	private CustomerPerformanceService CustomerPerformanceService;
	@Autowired
	private CustomerService CustomerService;
	@Autowired
	private EmployeeService EmployeeService;
	
	@PostMapping("/addCustomerPerformance")
	@ResponseBody
	public String addCustomerPerformance(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加顾客业绩信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeService.getEmployee(dataJson);
			CustomerService.getCustomer(dataJson);
			int id = CustomerPerformanceService.addCustomerPerformance(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "添加顾客业绩信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加顾客业绩信息--------------------");
		}
	}
	@PostMapping("/annulCustomerPerformance")
	@ResponseBody
	public String annulCustomerPerformance(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 撤销顾客业绩信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CustomerPerformanceService.getCustomerPerformance(dataJson);
			int id = CustomerPerformanceService.annulCustomerPerformance(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "撤销顾客业绩信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 撤销顾客业绩信息--------------------");
		}
	}
	@PostMapping("/affirmCustomerPerformance")
	@ResponseBody
	public String affirmCustomerPerformance(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 确认顾客业绩信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CustomerPerformanceService.getCustomerPerformance(dataJson);
			int id = CustomerPerformanceService.affirmCustomerPerformance(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "确认顾客业绩信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 确认顾客业绩信息--------------------");
		}
	}
	@PostMapping("/deleteCustomerPerformance")
	@ResponseBody
	public String deleteCustomerPerformance(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除顾客业绩信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CustomerPerformanceService.getCustomerPerformance(dataJson);
			int id = CustomerPerformanceService.deleteCustomerPerformance(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "删除顾客业绩信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 删除顾客业绩信息--------------------");
		}
	}
	@PostMapping("/getCustomerPerformanceByEmployee")
	@ResponseBody
	public String getCustomerPerformanceByEmployee(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据顾客和日期范围获取顾客业绩信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeService.getEmployee(dataJson);
			JSONObject cpJsonObject = CustomerPerformanceService.getCustomerPerformanceByEmployeeCondition(dataJson);
			return ResultJsonUtil.toJsonString(200, cpJsonObject, "根据顾客和日期范围获取顾客业绩信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, e.getMessage(), "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据顾客和日期范围获取顾客业绩信息--------------------");
		}
	}
	@PostMapping("/getCustomerPerformanceByEmployeeId")
	@ResponseBody
	public String getCustomerPerformanceByEmployeeId(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据顾客获取顾客业绩信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeService.getEmployee(dataJson);
			Page<CustomerPerformanceDTO> customerPerformanceDTOs = CustomerPerformanceService.getCustomerPerformanceByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, customerPerformanceDTOs, "根据顾客获取顾客业绩信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据顾客获取顾客业绩信息--------------------");
		}
	}
	@PostMapping("/getCustomerPerformance")
	@ResponseBody
	public String getCustomerPerformance(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据业绩获取顾客业绩信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			JSONObject jsonObject = CustomerPerformanceService.getCustomerPerformanceDetail(dataJson);
			return ResultJsonUtil.toJsonString(200, jsonObject, "根据业绩获取顾客业绩信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据业绩获取顾客业绩信息--------------------");
		}
	}
	@PostMapping("/getCustomerConsumeMoney")
	@ResponseBody
	public String getCustomerConsumeMoney(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据顾客按月获取消费信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			List<JSONObject> jsonObject = CustomerPerformanceService.getCustomerConsumeMoney(dataJson);
			return ResultJsonUtil.toJsonString(200, jsonObject, "根据顾客按月获取消费信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据顾客按月获取消费信息--------------------");
		}
	}
	@PostMapping("/getCustomerConsumeMoneyList")
	@ResponseBody
	public String getCustomerConsumeMoneyList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据顾客和月份获取消费列表信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<CustomerPerformanceDTO> cusPage = CustomerPerformanceService.getCustomerConsumeMoneyList(dataJson);
			return ResultJsonUtil.toJsonString(200, cusPage, "根据顾客和月份获取消费列表信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据顾客和月份获取消费列表信息--------------------");
		}
	}
}
