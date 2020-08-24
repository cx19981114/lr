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

import cn.lr.dao.taskMapper;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.task;
import cn.lr.service.company.CompanyService;
import cn.lr.service.company.PostService;
import cn.lr.service.company.TaskService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/TaskManagement")
@Controller
public class TaskManagement {
	@Autowired
	private TaskService TaskService;
	@Autowired
	private CompanyService CompanyService;
	@Autowired
	private PostService PostService;

	@PostMapping("/addTask")
	@ResponseBody
	public String addTask(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加公司任务信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			int id = TaskService.addTask(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "添加公司任务信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加公司任务信息--------------------");
		}
	}
	@PostMapping("/modifyTask")
	@ResponseBody
	public String modifyTask(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改公司任务信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			TaskService.getTask(dataJson);
			int id = TaskService.modifyTask(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改公司任务信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 修改公司任务信息--------------------");
		}
	}
	@PostMapping("/deleteTask")
	@ResponseBody
	public String deleteTask(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除公司任务信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			TaskService.getTask(dataJson);
			int id = TaskService.deleteTask(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除公司任务信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 删除公司任务信息--------------------");
		}
	}
	@PostMapping("/getTaskList")
	@ResponseBody
	public String getTaskList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得公司任务信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			Page<task> Tasks = TaskService.getTaskByCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, Tasks, "获取公司任务信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获得公司任务信息--------------------");
		}
	}
	@PostMapping("/getTaskByPost")
	@ResponseBody
	public String getTaskByPost(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据岗位获得公司任务信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			PostService.getPost(dataJson);
			Page<task> Tasks = TaskService.getTaskByPost(dataJson);
			return ResultJsonUtil.toJsonString(200, Tasks, "获取公司任务信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据岗位获得公司任务信息--------------------");
		}
	}
	@PostMapping("/getTask")
	@ResponseBody
	public String getTask(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得任务信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			task Task = TaskService.getTask(dataJson);
			return ResultJsonUtil.toJsonString(200, Task, "获取任务信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获得任务信息--------------------");
		}
	}
	@PostMapping("/getMaxStep")
	@ResponseBody
	public String getMaxStep(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据岗位和任务类型获取最大任务序号--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			PostService.getPost(dataJson);
			Integer stepInteger = TaskService.maxStep(dataJson);
			return ResultJsonUtil.toJsonString(200, stepInteger, "根据岗位和任务类型获取最大任务序号成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据岗位和任务类型获取最大任务序号--------------------");
		}
	}
	
	

}
