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

import cn.lr.dto.EmployeeRankDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.service.employee.EmployeeRankService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/EmployeeRankManagement")
@Controller
public class EmployeeRankManagement {
	@Autowired
	private EmployeeRankService EmployeeRankService;
	
	@PostMapping("/getEmployeeRankByCompany")
	@ResponseBody
	public String getEmployeeRankByCompany(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据公司id获取所有职员积分信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Page<EmployeeRankDTO> employeeRanks = EmployeeRankService.getEmployeeRankByCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeRanks, "根据公司id获取所有职员积分信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据公司id获取所有职员积分信息--------------------");
		}
	}
	@PostMapping("/getEmployeeRankByEmployee")
	@ResponseBody
	public String getEmployeeRankByEmployee(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员id获取积分信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			EmployeeRankDTO employeeRank = EmployeeRankService.getEmployeeRankByEmployee(dataJson);
			return ResultJsonUtil.toJsonString(200, employeeRank, "根据职员id获取积分信息成功", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员id获取积分信息--------------------");
		}
	}
	@PostMapping("/getEmployeeRankFrist")
	@ResponseBody
	public String getEmployeeRankFrist(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据公司id获取排名第一职员姓名--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			String employee = EmployeeRankService.getEmployeeRankFrist(dataJson);
			return ResultJsonUtil.toJsonString(200, employee, "根据公司id获取排名第一职员姓名", session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(), session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误", session.getId());
		} finally {
			LoggerUtil.LOGGER.info("-------------end 根据公司id获取排名第一职员姓名--------------------");
		}
	}
}
