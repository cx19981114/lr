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
import cn.lr.exception.BusiException;
import cn.lr.po.task;
import cn.lr.service.employee.EmployeeTaskService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/EmployeeTaskManagement")
@Controller
public class EmployeeTaskMangement {
	@Autowired
	private EmployeeTaskService EmployeeTaskService;
	
	@PostMapping("/addEmployeeTask")
	@ResponseBody
	public String addEmployeeTask(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加任务积分信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			//返回的是申请列表的Id
			int id = EmployeeTaskService.addEmployeeTask(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "添加任务积分成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 添加任务积分信息--------------------");
		}
	}
	
	@PostMapping("/deleteEmployeeTask")
	@ResponseBody
	public String deleteEmployeeTask(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除任务积分信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			int id = EmployeeTaskService.deleteEmployeeTask(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除任务积分成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 删除任务积分信息--------------------");
		}
	}
	@PostMapping("/annulEmployeeTask")
	@ResponseBody
	public String annulEmployeeLogTomorrow(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 撤销任务积分信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			int id = EmployeeTaskService.annulEmployeeTask(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "撤销任务积分成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 撤销任务积分信息--------------------");
		}
	}
	@PostMapping("/getEmployeeTaskList")
	@ResponseBody
	public String getEmployeeTaskList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员获取任务积分信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<task> employeeTasks = EmployeeTaskService.getTaskByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeTasks, "根据职员获取任务积分信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员获取任务积分信息--------------------");
		}
	}
	@PostMapping("/getEmployeeTaskJson")
	@ResponseBody
	public String getEmployeeTaskJson(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员获取任务积分信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			List<JSONObject> employeeTasks = EmployeeTaskService.getTaskByEmployeeList(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeTasks, "根据职员获取任务积分信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员获取任务积分信息--------------------");
		}
	}
	@PostMapping("/getEmployeeTask")
	@ResponseBody
	public String getEmployeeTask(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员获取任务积分详细信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			JSONObject employeeTaskDTO = EmployeeTaskService.getEmployeeTask(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeTaskDTO, "根据职员获取任务积分信息详细成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员获取任务积分详细信息--------------------");
		}
	}
	@PostMapping("/affirmEmployeeTask")
	@ResponseBody
	public String affirmEmployeeTask(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 确认提交申请(任务)信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Integer employeeTasks = EmployeeTaskService.affirmEmployeeTask(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeTasks, "确认提交申请(任务)信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 确认提交申请(任务)信息--------------------");
		}
	}
	@PostMapping("/modifyEmployeeTask")
	@ResponseBody
	public String modifyEmployeeTask(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改任务信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Integer employeeTasks = EmployeeTaskService.modifyEmployeeTask(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改任务信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 修改任务信息--------------------");
		}
	}
}
