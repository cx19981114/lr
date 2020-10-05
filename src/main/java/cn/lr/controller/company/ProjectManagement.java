package cn.lr.controller.company;

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
import cn.lr.exception.BusiException;
import cn.lr.po.project;
import cn.lr.service.company.CompanyService;
import cn.lr.service.company.ProjectService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/ProjectManagement")
@Controller
public class ProjectManagement {
	@Autowired
	private ProjectService ProjectService;
	@Autowired
	private CompanyService CompanyService;

	@PostMapping("/addProject")
	@ResponseBody
	public String addProject(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加公司项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			int id = ProjectService.addProject(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "添加公司项目信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加公司项目信息--------------------");
		}
	}
	@PostMapping("/modifyProject")
	@ResponseBody
	public String modifyProject(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改公司项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			ProjectService.getProject(dataJson);
			int id = ProjectService.modifyProject(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改公司项目信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 修改公司项目信息--------------------");
		}
	}
	@PostMapping("/deleteProject")
	@ResponseBody
	public String deleteProject(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除公司项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			ProjectService.getProject(dataJson);
			int id = ProjectService.deleteProject(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除公司项目信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 删除公司项目信息--------------------");
		}
	}
	@PostMapping("/getProjectList")
	@ResponseBody
	public String getProjectList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得公司项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			Page<project> projects = ProjectService.getProjectByCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, projects, "获取公司项目信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获得公司项目信息--------------------");
		}
	}
	@PostMapping("/getProjectJson")
	@ResponseBody
	public String getProjectJson(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得公司项目部分信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			List<JSONObject> projects = ProjectService.getProjectByCompanyJson(dataJson);
			return ResultJsonUtil.toJsonString(200, projects, "获取公司项目部分信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获得公司项目部分信息--------------------");
		}
	}
	@PostMapping("/getProject")
	@ResponseBody
	public String getProject(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得该项目信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			project project = ProjectService.getProject(dataJson);
			return ResultJsonUtil.toJsonString(200, project, "获取该项目信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获得该项目信息--------------------");
		}
	}
	
	

}
