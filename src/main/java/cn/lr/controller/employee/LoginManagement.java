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

import cn.lr.dto.employeeDTO;
import cn.lr.exception.BusiException;
import cn.lr.exception.EmployeeUnactiveException;
import cn.lr.service.employee.LoginService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/LoginManagement")
@Controller
public class LoginManagement {
	@Autowired
	private LoginService LoginService;
	
	@PostMapping("/checkLogin")
	@ResponseBody
	public String loginCheck(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 登录--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		try {
			employeeDTO user = LoginService.checkLogin(dataJson);
			session.setAttribute("employeeId", user.getEmployeeId());
			session.setAttribute("companyId", user.getCompanyId());
			return ResultJsonUtil.toJsonString(200, user, "登录成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (EmployeeUnactiveException e) {
			return ResultJsonUtil.toJsonString(102, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 登录--------------------");
		}
	}
	
	@PostMapping("/sendMsg")
	@ResponseBody
	public String sendMsg(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 发送验证码--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Integer id = LoginService.sendMsg(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "发送验证码成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 发送验证码--------------------");
		}
	}
	
	@PostMapping("/checkCode")
	@ResponseBody
	public String checkCode(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 验证验证码--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			Integer id = LoginService.checkCode(dataJson);
			session.setAttribute("employeeId", id);
			return ResultJsonUtil.toJsonString(200, id, "验证码校验成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 验证验证码--------------------");
		}
	}
	@PostMapping("/modifyPasswordFrist")
	@ResponseBody
	public String modifyPasswordFrist(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 第一次-修改用户密码--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("employeeId", session.getAttribute("employeeId"));
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			int id = LoginService.modifyPasswordFrist(dataJson);
			return ResultJsonUtil.toJsonString(200, id, "修改密码成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 第一次-修改用户密码--------------------");
		}
	}
}