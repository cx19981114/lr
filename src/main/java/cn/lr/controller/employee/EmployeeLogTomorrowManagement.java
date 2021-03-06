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

import cn.lr.dto.EmployeeLogTomorrowDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.service.employee.EmployeeLogTomorrowService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/EmployeeLogTomorrowManagement")
@Controller
public class EmployeeLogTomorrowManagement {
	@Autowired
	private EmployeeLogTomorrowService EmployeeLogTomorrowService;
	
	@PostMapping("/addEmployeeLogTomorrow")
	@ResponseBody
	public String addEmployeeLogTomorrowTomorrow(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加明日计划信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			int id = EmployeeLogTomorrowService.addEmployeeLogTomorrow(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "添加明日计划成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 添加明日计划信息--------------------");
		}
	}
	@PostMapping("/modifyEmployeeLogTomorrow")
	@ResponseBody
	public String modifyEmployeeLogTomorrow(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改明日计划信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			EmployeeLogTomorrowService.getEmployeeLogTomorrow(dataJson);
			int id = EmployeeLogTomorrowService.modifyEmployeeLogTomorrow(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改明日计划成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 修改明日计划信息--------------------");
		}
	}
	@PostMapping("/deleteEmployeeLogTomorrow")
	@ResponseBody
	public String deleteEmployeeLogTomorrow(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除明日计划信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			EmployeeLogTomorrowService.getEmployeeLogTomorrow(dataJson);
			int id = EmployeeLogTomorrowService.deleteEmployeeLogTomorrow(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除明日计划成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 删除明日计划信息--------------------");
		}
	}
	@PostMapping("/annulEmployeeLogTomorrow")
	@ResponseBody
	public String annulEmployeeLogTomorrow(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 撤销明日计划信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			EmployeeLogTomorrowService.getEmployeeLogTomorrow(dataJson);
			int id = EmployeeLogTomorrowService.annulEmployeeLogTomorrow(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "撤销明日计划成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 撤销明日计划信息--------------------");
		}
	}
	@PostMapping("/getEmployeeLogTomorrow")
	@ResponseBody
	public String getEmployeeLogTomorrow(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取明日计划信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			JSONObject EmployeeLogTomorrow = EmployeeLogTomorrowService.getEmployeeLogTomorrow(dataJson);
			return ResultJsonUtil.toJsonString(200, EmployeeLogTomorrow, "获取明日计划信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 获取明日计划信息--------------------");
		}
	}
	@PostMapping("/getEmployeeLogTomorrowList")
	@ResponseBody
	public String getEmployeeLogTomorrowList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员获明日计划信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			Page<EmployeeLogTomorrowDTO> employeeLogTomorrows = EmployeeLogTomorrowService.getEmployeeLogTomorrowByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeLogTomorrows, "根据职员获取明日计划信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员获取明日计划信息--------------------");
		}
	}
	@PostMapping("/affirmEmployeeLogTomorrow")
	@ResponseBody
	public String affirmEmployeeLogTomorrow(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 确认提交申请(明日计划)信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			Integer employeeLogTomorrows = EmployeeLogTomorrowService.affirmEmployeeLogTomorrow(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeLogTomorrows, "确认提交申请(明日计划)信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 确认提交申请(明日计划)信息--------------------");
		}
	}
	@PostMapping("/getEmployeeLogTomorrowByEmployeeNew")
	@ResponseBody
	public String getEmployeeLogTomorrowByEmployeeNew(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员获明日计划(最新)信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			EmployeeLogTomorrowDTO employeeLogTomorrow = EmployeeLogTomorrowService.getEmployeeLogTomorrowByEmployeeNew(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeLogTomorrow, "根据职员获取明日计划(最新)信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员获取明日计划(最新)信息--------------------");
		}
	}
}
