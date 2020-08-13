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

import cn.lr.dto.CustomerProjectDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.service.company.ProjectService;
import cn.lr.service.customer.CustomerProjectService;
import cn.lr.service.customer.CustomerService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/CustomerProjectManagement")
@Controller
public class CustomerProjectManagement {
	@Autowired
	private CustomerService CustomerService;
	@Autowired
	private CustomerProjectService CustomerProjectService;
	@Autowired
	private ProjectService ProjectService;
	
	@PostMapping("/addCustomerProject")
	@ResponseBody
	public String addCustomerProject(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加顾客的项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CustomerService.getCustomer(dataJson);
			ProjectService.getProject(dataJson);
			int id = CustomerProjectService.addCustomerProject(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "添加顾客的项目信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加顾客的项目信息--------------------");
		}
	}
	@PostMapping("/getCustomerProjectByCustomer")
	@ResponseBody
	public String getCustomerProjectByCustomer(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据顾客id获取所拥有的项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CustomerService.getCustomer(dataJson);
			List<JSONObject> projects= CustomerProjectService.getCustomerProjectByCustomer(dataJson);
			return ResultJsonUtil.toJsonString(200, projects, "根据顾客id获取所拥有的项目信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据顾客id获取所拥有的项目信息--------------------");
		}
	}
	@PostMapping("/deleteCustomerProject")
	@ResponseBody
	public String deleteCustomerProject(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除服务顾客项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CustomerProjectService.getCustomerProject(dataJson);
			int id = CustomerProjectService.deleteCustomerProject(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "删除服务顾客项目成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 删除服务顾客项目信息--------------------");
		}
	}
	@PostMapping("/annulCustomerProject")
	@ResponseBody
	public String annulCustomerProject(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 撤销服务顾客项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CustomerProjectService.getCustomerProject(dataJson);
			int id = CustomerProjectService.annulCustomerProject(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "撤销服务顾客项目成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 撤销服务顾客项目信息--------------------");
		}
	}
	@PostMapping("/affirmCustomerProject")
	@ResponseBody
	public String affirmCustomerProject(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 确认提交服务顾客项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Integer CustomerProjects = CustomerProjectService.affirmCustomerProject(dataJson);
			return ResultJsonUtil.toJsonString(200, CustomerProjects, "确认提交服务顾客项目信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 确认提交服务顾客项目信息--------------------");
		}
	}
	@PostMapping("/getCustomerProjectByEmployee")
	@ResponseBody
	public String getCustomerProjectByEmployee(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获取项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<CustomerProjectDTO> customerProjectDTOs = CustomerProjectService.getCustomerProjectByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, customerProjectDTOs, " 根据职员id获取项目信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end  根据职员id获取项目信息--------------------");
		}
	}
	@PostMapping("/getCustomerProject")
	@ResponseBody
	public String getCustomerProject(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据项目id获取项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			JSONObject customerProjectDTOs = CustomerProjectService.getCustomerProjectDetail(dataJson);
			return ResultJsonUtil.toJsonString(200, customerProjectDTOs, " 根据项目id获取项目信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end  根据项目id获取项目信息--------------------");
		}
	}
}
