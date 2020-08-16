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

import cn.lr.dto.EmployeeRestDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.service.employee.EmployeeRestService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/EmployeeRestManagement")
@Controller
public class EmployeeRestManagement {
	@Autowired
	private EmployeeRestService EmployeeRestService;
	
	@PostMapping("/addEmployeeRest")
	@ResponseBody
	public String addEmployeeRest(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加行程信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			int id = EmployeeRestService.addEmployeeRest(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "添加行程成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 添加行程信息--------------------");
		}
	}
	@PostMapping("/modifyEmployeeRest")
	@ResponseBody
	public String modifyEmployeeRest(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改行程信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeRestService.getEmployeeRest(dataJson);
			int id = EmployeeRestService.modifyEmployeeRest(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "修改行程成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 修改行程信息--------------------");
		}
	}
	@PostMapping("/deleteEmployeeRest")
	@ResponseBody
	public String deleteEmployeeRest(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除行程信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeRestService.getEmployeeRest(dataJson);
			int id = EmployeeRestService.deleteEmployeeRest(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "删除行程成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 删除行程信息--------------------");
		}
	}
	@PostMapping("/annulEmployeeRest")
	@ResponseBody
	public String annulEmployeeRest(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 撤销行程信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeRestService.getEmployeeRest(dataJson);
			int id = EmployeeRestService.annulEmployeeRest(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "撤销行程成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 撤销行程信息--------------------");
		}
	}
	@PostMapping("/getEmployeeRest")
	@ResponseBody
	public String getEmployeeRest(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据id获取行程信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			JSONObject employeeRest = EmployeeRestService.getEmployeeRest(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeRest, "根据id获取行程信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据id获取行程信息--------------------");
		}
	}
	@PostMapping("/getEmployeeRestList")
	@ResponseBody
	public String getEmployeeRestList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获取行程信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<EmployeeRestDTO> employeeRests = EmployeeRestService.getEmployeeRestByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeRests, "根据职员id获取行程信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取行程信息--------------------");
		}
	}
	@PostMapping("/getEmployeeRestTypeList")
	@ResponseBody
	public String getEmployeeRestTypeList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获每日行程总信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			List<JSONObject> employeeRests = EmployeeRestService.getEmployeeRestTypeList(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeRests, "根据职员id获取每日行程总信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取每日行程总信息--------------------");
		}
	}
	@PostMapping("/getEmployeeRestListTeam")
	@ResponseBody
	public String getEmployeeRestListTeam(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获团队每日行程信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<EmployeeRestDTO> employeeRests = EmployeeRestService.getEmployeeRestByEmployeeTeam(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeRests, "根据职员id获取团队每日行程信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取团队每日行程信息--------------------");
		}
	}
	@PostMapping("/getEmployeeRestListTeamList")
	@ResponseBody
	public String getEmployeeRestListTeamList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获团队每日行程列表信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			List<List<JSONObject>> employeeRests = EmployeeRestService.getEmployeeRestByEmployeeTeamList(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeRests, "根据职员id获取团队每日行程列表信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取团队每日行程列表信息--------------------");
		}
	}
	@PostMapping("/affirmEmployeeRest")
	@ResponseBody
	public String affirmEmployeeRest(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 确认提交申请(每日行程)信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Integer employeeRests = EmployeeRestService.affirmEmployeeRest(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeRests, "确认提交申请(每日行程)信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 确认提交申请(每日行程)信息--------------------");
		}
	}
	@PostMapping("/getEmployeeRestByEmployeeNew")
	@ResponseBody
	public String getEmployeeRestByEmployeeNew(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获每日行程(最新)信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeRestDTO employeeRest = EmployeeRestService.getEmployeeRestByEmployeeNew(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeRest, "根据职员id获取每日行程(最新)信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取每日行程(最新)信息--------------------");
		}
	}
}
