package cn.lr.controller.employee;

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

import cn.lr.dto.Page;
import cn.lr.dto.employeeDTO;
import cn.lr.exception.BusiException;
import cn.lr.service.company.CompanyService;
import cn.lr.service.company.PostService;
import cn.lr.service.employee.EmployeeService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/EmployeeManagement")
@Controller
public class EmployeeManagement {
	@Autowired
	private EmployeeService EmployeeService;
	@Autowired
	private CompanyService CompanyService;
	@Autowired
	private PostService PostService;
	
	@PostMapping("/addEmployee")
	@ResponseBody
	public String addEmployee(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加职员信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
		}
		try {
			CompanyService.getCompany(dataJson);
			PostService.getPost(dataJson);
			int id = EmployeeService.addEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "添加职员信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加职员信息--------------------");
		}
	}
	@PostMapping("/addEmployeeJYZ")
	@ResponseBody
	public String addEmployeeJYZ(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加经营者信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
		}
		try {
			CompanyService.getCompany(dataJson);
			int id = EmployeeService.addEmployeeJYZ(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "添加经营者信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加经营者信息--------------------");
		}
	}
	@PostMapping("/modifyEmployee")
	@ResponseBody
	public String modifyEmployee(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改职员信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
		}
		try {
			EmployeeService.getEmployee(dataJson);
			int id = EmployeeService.modifyEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改职员信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 修改职员信息--------------------");
		}
	}
	@PostMapping("/deleteEmployee")
	@ResponseBody
	public String deleteEmployee(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除职员信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			PostService.getPost(dataJson);
			EmployeeService.getEmployee(dataJson);
			int id = EmployeeService.deleteEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除职员信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 删除职员信息--------------------");
		}
	}
	@PostMapping("/getEmployee")
	@ResponseBody
	public String getEmployee(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得职员信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {dataJson.put("companyId", session.getAttribute("companyId"));}
		try {
			CompanyService.getCompany(dataJson);
			PostService.getPost(dataJson);
			employeeDTO employee = EmployeeService.getEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, employee, "获取职员信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获取职员信息--------------------");
		}
	}
	@PostMapping("/getEmployeeList")
	@ResponseBody
	public String getEmployeeList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据公司获得职员信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {dataJson.put("companyId", session.getAttribute("companyId"));}
		try {
			CompanyService.getCompany(dataJson);
			Page<employeeDTO> employees = EmployeeService.getEmployeeByCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, employees, "根据公司获取职员信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据公司获取职员信息--------------------");
		}
	}
	@PostMapping("/getEmployeeByPost")
	@ResponseBody
	public String getEmployeeByPost(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职位获得职员信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
		}
		try {
			CompanyService.getCompany(dataJson);
			PostService.getPost(dataJson);
			Page<employeeDTO> employees = EmployeeService.getEmployeeByPost(dataJson);
			return ResultJsonUtil.toJsonString(200, employees, "根据职位获取职员信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据职位获取职员信息--------------------");
		}
	}
	@PostMapping("/getEmployeeByPostAll")
	@ResponseBody
	public String getEmployeeByPostAll(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职位获得所有职员信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {dataJson.put("companyId", session.getAttribute("companyId"));}
		try {
			CompanyService.getCompany(dataJson);
			List<JSONObject> employees = EmployeeService.getEmployeeByPostAll(dataJson);
			return ResultJsonUtil.toJsonString(200, employees, "根据职位获取所有职员信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据职位获取所有职员信息--------------------");
		}
	}
	@PostMapping("/getEmployeeLeader")
	@ResponseBody
	public String getEmployeeLeader(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职位获得所有领导信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
		}
		try {
			CompanyService.getCompany(dataJson);
			PostService.getPost(dataJson);
			List<JSONObject> employees = EmployeeService.getEmployeeLeader(dataJson);
			return ResultJsonUtil.toJsonString(200, employees, "根据职位获得所有领导信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据职位获得所有领导信息--------------------");
		}
	}
	@PostMapping("/getEmployeeUnder")
	@ResponseBody
	public String getEmployeeUnder(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据用户获得所有下属信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
		}
		try {
			List<JSONObject> employees = EmployeeService.getEmployeeUnder(dataJson);
			return ResultJsonUtil.toJsonString(200, employees, "根据用户Id获得所有下属信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据用户Id获得所有下属信息--------------------");
		}
	}
	@PostMapping("/getBusiness")
	@ResponseBody
	public String getBusiness(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得客户预约，目标业绩，服务项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
		}
		try {
			JSONObject businessJson = EmployeeService.getBusiness(dataJson);
			return ResultJsonUtil.toJsonString(200, businessJson, "获取客户预约，目标业绩，服务项目信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获取客户预约，目标业绩，服务项目信息--------------------");
		}
	}
	@PostMapping("/getDayMsg")
	@ResponseBody
	public String getDayMsg(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得今日客户数量，今日积分，今日作息信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
		}
		try {
			JSONObject dayJson = EmployeeService.getDayMsg(dataJson);
			return ResultJsonUtil.toJsonString(200, dayJson, "获取今日客户数量，今日积分，今日作息信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获取今日客户数量，今日积分，今日作息信息--------------------");
		}
	}
}
