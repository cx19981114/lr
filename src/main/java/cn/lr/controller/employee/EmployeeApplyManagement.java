package cn.lr.controller.employee;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.EmployeeApplyDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.service.employee.EmployeeApplyService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/EmployeeApplyManagement")
@Controller
public class EmployeeApplyManagement {
	@Autowired
	private EmployeeApplyService EmployeeApplyService;
	
	@PostMapping("/addEmployeeApply")
	@ResponseBody
	public String addEmployeeApply(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			int id = EmployeeApplyService.addEmployeeApply(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "添加申请成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 添加申请信息--------------------");
		}
	}
	@PostMapping("/modifyEmployeeApply")
	@ResponseBody
	public String modifyEmployeeApply(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeApplyService.getEmployeeApply(dataJson);
			int id = EmployeeApplyService.modifyEmployeeApply(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改申请成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 修改申请信息--------------------");
		}
	}
	@PostMapping("/deleteEmployeeApply")
	@ResponseBody
	public String deleteEmployeeApply(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeApplyService.getEmployeeApply(dataJson);
			int id = EmployeeApplyService.deleteEmployeeApply(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除申请成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 删除申请信息--------------------");
		}
	}
	@PostMapping("/annulEmployeeApply")
	@ResponseBody
	public String annulEmployeeApply(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 撤销申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeApplyService.getEmployeeApply(dataJson);
			int id = EmployeeApplyService.annulEmployeeApply(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "撤销申请成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 撤销申请信息--------------------");
		}
	}
	@PostMapping("/getEmployeeApply")
	@ResponseBody
	public String getEmployeeApply(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			JSONObject employeeApply = EmployeeApplyService.getEmployeeApply(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeApply, "获取申请信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 获取申请信息--------------------");
		}
	}
	@PostMapping("/getEmployeeApplyList")
	@ResponseBody
	public String getEmployeeApplyList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员获取申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<EmployeeApplyDTO> employeeApplys = EmployeeApplyService.getEmployeeApplyByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeApplys, "根据职员获取申请信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员获取申请信息--------------------");
		}
	}
	@PostMapping("/affirmEmployeeApply")
	@ResponseBody
	public String affirmEmployeeApply(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 确认提交申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Integer employeeApplys = EmployeeApplyService.affirmEmployeeApply(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeApplys, "确认提交申请信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 确认提交申请信息--------------------");
		}
	}
}
