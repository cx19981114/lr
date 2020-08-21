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

import cn.lr.dto.EmployeeAttendanceDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.service.employee.EmployeeAttendanceService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/EmployeeAttendanceManagement")
@Controller
public class EmployeeAttendanceManagement {
	@Autowired
	private EmployeeAttendanceService EmployeeAttendanceService;
	
	@PostMapping("/getAddress")
	@ResponseBody
	public String getAddress(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据经纬度获取位置信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		try {
			String addressString = EmployeeAttendanceService.getAddress(dataJson);
			return ResultJsonUtil.toJsonString(200, addressString, "根据经纬度获取位置信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据经纬度获取位置信息--------------------");
		}
	}
	@PostMapping("/addEmployeeAttendance")
	@ResponseBody
	public String addEmployeeAttendance(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加打卡信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			int id = EmployeeAttendanceService.addEmployeeAttendance(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "添加打卡成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 添加打卡信息--------------------");
		}
	}
	@PostMapping("/modifyEmployeeAttendance")
	@ResponseBody
	public String modifyEmployeeAttendance(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改打卡信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeAttendanceService.getEmployeeAttendance(dataJson);
			int id = EmployeeAttendanceService.modifyEmployeeAttendance(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改打卡成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 修改打卡信息--------------------");
		}
	}
	@PostMapping("/deleteEmployeeAttendance")
	@ResponseBody
	public String deleteEmployeeAttendance(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除打卡信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeAttendanceService.getEmployeeAttendance(dataJson);
			int id = EmployeeAttendanceService.deleteEmployeeAttendance(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除打卡成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 删除打卡信息--------------------");
		}
	}
	@PostMapping("/annulEmployeeAttendance")
	@ResponseBody
	public String annulEmployeeAttendance(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 撤销打卡信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeAttendanceService.getEmployeeAttendance(dataJson);
			int id = EmployeeAttendanceService.annulEmployeeAttendance(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "撤销打卡成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 撤销打卡信息--------------------");
		}
	}
	@PostMapping("/getEmployeeAttendance")
	@ResponseBody
	public String getEmployeeAttendance(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取打卡信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			JSONObject employeeAttendance = EmployeeAttendanceService.getEmployeeAttendance(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeAttendance, "获取打卡信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 获取打卡信息--------------------");
		}
	}
	@PostMapping("/getEmployeeAttendanceList")
	@ResponseBody
	public String getEmployeeAttendanceList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员获每日打卡信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<EmployeeAttendanceDTO> employeeAttendances = EmployeeAttendanceService.getEmployeeAttendanceByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeAttendances, "根据职员获取每日打卡信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员获取每日打卡信息--------------------");
		}
	}
	@PostMapping("/affirmEmployeeAttendance")
	@ResponseBody
	public String affirmEmployeeAttendance(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 确认提交申请(每日打卡)信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Integer employeeAttendances = EmployeeAttendanceService.affirmEmployeeAttendance(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeAttendances, "确认提交申请(每日打卡)信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 确认提交申请(每日打卡)信息--------------------");
		}
	}
	@PostMapping("/getEmployeeAttendanceByEmployeeNew")
	@ResponseBody
	public String getEmployeeAttendanceByEmployeeNew(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员获每日打卡(最新)信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeAttendanceDTO employeeAttendance = EmployeeAttendanceService.getEmployeeAttendanceByEmployeeNew(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeAttendance, "根据职员获取每日打卡(最新)信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员获取每日打卡(最新)信息--------------------");
		}
	}
}
