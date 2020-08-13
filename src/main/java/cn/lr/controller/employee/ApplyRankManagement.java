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

import cn.lr.dto.ApplyRankDTO;
import cn.lr.dto.EmployeeTaskDTO;
import cn.lr.dto.Page;
import cn.lr.dto.employeeDTO;
import cn.lr.exception.BusiException;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.service.employee.EmployeeService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/ApplyRankManagement")
@Controller
public class ApplyRankManagement {
	@Autowired
	private ApplyRankService ApplyRankService;
	@Autowired
	private DynamicService DynamicService;
	@Autowired
	private EmployeeService EmployeeService;

	@PostMapping("/modifyApplyRank")
	@ResponseBody
	public String modifyApplyRank(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			ApplyRankService.getApplyRankByDynamic(dataJson);
			int id = ApplyRankService.modifyApplyRank(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改申请信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 修改申请信息--------------------");
		}
	}
	@PostMapping("/getApplyRank")
	@ResponseBody
	public String getApplyRank(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据Id获取申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			List<ApplyRankDTO> applyRankDTOs = ApplyRankService.getApplyRank(dataJson);
			return ResultJsonUtil.toJsonString(200, applyRankDTOs, "根据Id获取申请信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据Id获取申请信息--------------------");
		}
	}
	@PostMapping("/getApplyRankByDynamic")
	@ResponseBody
	public String getApplyRankByDynamic(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据动态Id获取申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			DynamicService.getDynamic(dataJson);
			List<ApplyRankDTO> applyRankDTOs = ApplyRankService.getApplyRankByDynamic(dataJson);
			return ResultJsonUtil.toJsonString(200, applyRankDTOs, "根据动态Id获取申请信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据动态Id获取申请信息--------------------");
		}
	}
	@PostMapping("/getCheckList")
	@ResponseBody
	public String getCheckList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取审核人列表信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeService.getEmployee(dataJson);
			List<employeeDTO> employeeDTOs = ApplyRankService.getCheckList(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeDTOs, "获取审核人列表信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 获取审核人列表请信息--------------------");
		}
	}
	@PostMapping("/getApplyTask")
	@ResponseBody
	public String getApplyTask(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取任务申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeService.getEmployee(dataJson);
			Page<EmployeeTaskDTO> task = ApplyRankService.getEmployeeTask(dataJson);
			return ResultJsonUtil.toJsonString(200, task, "获取任务申请信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 获取任务申请信息--------------------");
		}
	}
}
