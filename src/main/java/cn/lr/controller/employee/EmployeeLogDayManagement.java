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

import cn.lr.dto.EmployeeLogDayDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.service.employee.EmployeeLogDayService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/EmployeeLogDayManagement")
@Controller
public class EmployeeLogDayManagement {
	@Autowired
	private EmployeeLogDayService EmployeeLogDayService;
	
	@PostMapping("/addEmployeeLogDay")
	@ResponseBody
	public String addEmployeeLogDayDay(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加今日总结信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			int id = EmployeeLogDayService.addEmployeeLogDay(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "添加今日总结成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 添加今日总结信息--------------------");
		}
	}
	@PostMapping("/modifyEmployeeLogDay")
	@ResponseBody
	public String modifyEmployeeLogDay(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改今日总结信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			EmployeeLogDayService.getEmployeeLogDay(dataJson);
			int id = EmployeeLogDayService.modifyEmployeeLogDay(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改今日总结成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 修改今日总结信息--------------------");
		}
	}
	@PostMapping("/deleteEmployeeLogDay")
	@ResponseBody
	public String deleteEmployeeLogDay(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除今日总结信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			EmployeeLogDayService.getEmployeeLogDay(dataJson);
			int id = EmployeeLogDayService.deleteEmployeeLogDay(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除今日总结成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 删除今日总结信息--------------------");
		}
	}
	@PostMapping("/annulEmployeeLogDay")
	@ResponseBody
	public String annulEmployeeLogDay(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 撤销今日总结信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			EmployeeLogDayService.getEmployeeLogDay(dataJson);
			int id = EmployeeLogDayService.annulEmployeeLogDay(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "撤销今日总结成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 撤销今日总结信息--------------------");
		}
	}
	@PostMapping("/getEmployeeLogDay")
	@ResponseBody
	public String getEmployeeLogDay(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取今日总结信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			JSONObject EmployeeLogDay = EmployeeLogDayService.getEmployeeLogDay(dataJson);
			return ResultJsonUtil.toJsonString(200, EmployeeLogDay, "获取今日总结信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 获取今日总结信息--------------------");
		}
	}
	@PostMapping("/getEmployeeLogDayList")
	@ResponseBody
	public String getEmployeeLogDayList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员获今日总结信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			Page<EmployeeLogDayDTO> employeeLogDays = EmployeeLogDayService.getEmployeeLogDayByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeLogDays, "根据职员获取今日总结信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员获取今日总结信息--------------------");
		}
	}
	@PostMapping("/affirmEmployeeLogDay")
	@ResponseBody
	public String affirmEmployeeLogDay(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 确认提交申请(今日总结)信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			Integer employeeLogDays = EmployeeLogDayService.affirmEmployeeLogDay(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeLogDays, "确认提交申请(今日总结)信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 确认提交申请(今日总结)信息--------------------");
		}
	}
	@PostMapping("/getEmployeeLogDayByEmployeeNew")
	@ResponseBody
	public String getEmployeeLogDayByEmployeeNew(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员获今日总结(最新)信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			EmployeeLogDayDTO employeeLogDay = EmployeeLogDayService.getEmployeeLogDayByEmployeeNew(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeLogDay, "根据职员获取今日总结(最新)信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员获取今日总结(最新)信息--------------------");
		}
	}
}
