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

import cn.lr.dto.DynamicDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.dynamic;
import cn.lr.service.employee.DynamicService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/DynamicManagement")
@Controller
public class DynamicManagement {

	@Autowired
	private DynamicService DynamicService;
	
	@PostMapping("/getDynamic")
	@ResponseBody
	public String getDynamic(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据Id获取动态信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			dynamic dynamic = DynamicService.getDynamic(dataJson);
			return ResultJsonUtil.toJsonString(200, dynamic, "根据Id获取动态成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据Id获取动态信息--------------------");
		}
	}
	@PostMapping("/getDynamicByCheck")
	@ResponseBody
	public String getDynamicByCheck(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据审核人获取申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<DynamicDTO> dynamics = DynamicService.getDynamicByCheck(dataJson);
			return ResultJsonUtil.toJsonString(200, dynamics, "根据审核人获取申请成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 申请积分信息--------------------");
		}
	}
	@PostMapping("/getDynamicByEmployee")
	@ResponseBody
	public String getDynamicByEmployee(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员获取申请信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<DynamicDTO> dynamics = DynamicService.getDynamicByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, dynamics, "根据职员获取申请成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员获取申请信息--------------------");
		}
	}
	@PostMapping("/getDynamicByCompany")
	@ResponseBody
	public String getDynamicByCompany(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据公司获取动态信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<DynamicDTO> dynamics = DynamicService.getDynamicByCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, dynamics, "根据公司获取动态成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据公司获取动态信息--------------------");
		}
	}
	@PostMapping("/getDynamicByCheckHistory")
	@ResponseBody
	public String getDynamicByCheckHistory(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据审核人获取申请历史信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<DynamicDTO> dynamics = DynamicService.getDynamicByCheckHistory(dataJson);
			return ResultJsonUtil.toJsonString(200, dynamics, "根据审核人获取申请历史成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据审核人获取申请历史信息--------------------");
		}
	}
}
