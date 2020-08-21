package cn.lr.controller.company;

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

import cn.lr.exception.BusiException;
import cn.lr.service.company.CompanyService;
import cn.lr.service.company.PermissionService;
import cn.lr.service.company.PostService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;
@RequestMapping("/PermissionManagemant")
@Controller
public class PermissionManagemant {

	@Autowired
	private PermissionService PermissionService;
	@Autowired
	private PostService postService;
	@Autowired
	private CompanyService CompanyService;
	
	@PostMapping("/getPermissionList")
	@ResponseBody
	public String getPermissionList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据公司获取权限列表-------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			List<JSONObject> permissionList = PermissionService.getPermissionList(dataJson);
			return ResultJsonUtil.toJsonString(200, permissionList, "根据公司获取权限列表成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据公司获取权限列表--------------------");
		}
	}
	@PostMapping("/setPermission")
	@ResponseBody
	public String setPermission(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据职员添加权限-------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			postService.getPost(dataJson);
			Integer postInteger = PermissionService.setPermission(dataJson);
			return ResultJsonUtil.toJsonString(200, postInteger, "根据职员添加权限成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据职员添加权限--------------------");
		}
	}
}
