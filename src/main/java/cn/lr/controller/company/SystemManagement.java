package cn.lr.controller.company;

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
import cn.lr.exception.BusiException;
import cn.lr.po.system;
import cn.lr.service.company.CompanyService;
import cn.lr.service.company.SystemService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/SystemManagement")
@Controller
public class SystemManagement {
	@Autowired
	private SystemService SystemService;
	@Autowired
	private CompanyService CompanyService;

	@PostMapping("/addSystem")
	@ResponseBody
	public String addSystem(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加公司制度信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			int id = SystemService.addSystem(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "添加公司制度信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加公司制度信息--------------------");
		}
	}
	@PostMapping("/modifySystem")
	@ResponseBody
	public String modifySystem(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改公司制度信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			SystemService.getSystem(dataJson);
			int id = SystemService.modifySystem(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改公司制度信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 修改公司制度信息--------------------");
		}
	}
	@PostMapping("/deleteSystem")
	@ResponseBody
	public String deleteSystem(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除公司制度信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			SystemService.getSystem(dataJson);
			int id = SystemService.deleteSystem(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除公司制度信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 删除公司制度信息--------------------");
		}
	}
	@PostMapping("/getSystemList")
	@ResponseBody
	public String getSystemList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得公司制度信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			Page<system> systems = SystemService.getSystemByCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, systems, "获取公司制度信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获得公司制度信息--------------------");
		}
	}
	@PostMapping("/getSystem")
	@ResponseBody
	public String getSystem(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得制度信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			system system = SystemService.getSystem(dataJson);
			return ResultJsonUtil.toJsonString(200, system, "获取制度信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获得制度信息--------------------");
		}
	}
	
	

}
