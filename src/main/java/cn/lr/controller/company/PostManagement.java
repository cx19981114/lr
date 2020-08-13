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

import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.exception.DataException;
import cn.lr.po.post;
import cn.lr.service.company.CompanyService;
import cn.lr.service.company.PostService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;
@RequestMapping("/PostManagement")
@Controller
public class PostManagement {
	@Autowired
	private CompanyService CompanyService;
	@Autowired
	private PostService PostService;
	@PostMapping("/addPost")
	@ResponseBody
	public String addPost(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加公司岗位信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			int id = PostService.addPost(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "添加公司岗位信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加公司岗位信息--------------------");
		}
	}
	@PostMapping("/modifyPost")
	@ResponseBody
	public String modifyPost(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改公司岗位信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			PostService.getPost(dataJson);
			int id = PostService.modifyPost(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改公司岗位信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 修改公司岗位信息--------------------");
		}
	}
	@PostMapping("/deletePost")
	@ResponseBody
	public String deletePost(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除公司岗位信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			PostService.getPost(dataJson);
			int id = PostService.deletePost(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除公司岗位信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		}catch (DataException e) {
			return ResultJsonUtil.toJsonString(104, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 删除公司岗位信息--------------------");
		}
	}
	@PostMapping("/getPostList")
	@ResponseBody
	public String getPostList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据公司获得岗位信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			Page<post> posts = PostService.getPostByCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, posts, "根据公司获取岗位信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据公司获得岗位信息--------------------");
		}
	}
	@PostMapping("/getPost")
	@ResponseBody
	public String getPost(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得岗位信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			post post = PostService.getPost(dataJson);
			return ResultJsonUtil.toJsonString(200, post, "获取岗位信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获得岗位信息--------------------");
		}
	}
	@PostMapping("/getPostCount")
	@ResponseBody
	public String getPostCount(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得岗位数量信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			List<JSONObject> post = PostService.getPostTypeAndCount(dataJson);
			return ResultJsonUtil.toJsonString(200, post, "获取岗位数量信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获得岗位数量信息--------------------");
		}
	}
}
