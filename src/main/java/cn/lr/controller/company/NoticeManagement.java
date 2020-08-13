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
import cn.lr.po.notice;
import cn.lr.service.company.CompanyService;
import cn.lr.service.company.NoticeService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/NoticeManagement")
@Controller
public class NoticeManagement {
	@Autowired
	private NoticeService NoticeService;
	@Autowired
	private CompanyService CompanyService;
	
	@PostMapping("/addNotice")
	@ResponseBody
	public String addNotice(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加公司公告栏信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			int id = NoticeService.addNotice(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "添加公司公告栏信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加公司公告栏信息--------------------");
		}
	}
	@PostMapping("/modifyNotice")
	@ResponseBody
	public String modifyNotice(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改公司公告栏信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			NoticeService.getNotice(dataJson);
			int id = NoticeService.modifyNotice(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改公司公告栏信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 修改公司公告栏信息--------------------");
		}
	}
	@PostMapping("/deleteNotice")
	@ResponseBody
	public String deleteNotice(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除公司公告栏信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			NoticeService.getNotice(dataJson);
			int id = NoticeService.deleteNotice(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除公司公告栏信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 删除公司公告栏信息--------------------");
		}
	}
	@PostMapping("/getNoticeList")
	@ResponseBody
	public String getNoticeList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据公司获取公告栏信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			Page<notice> notices = NoticeService.getNoticeByCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, notices, "根据公司获取公告栏信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据公司获取公告栏信息--------------------");
		}
	}
	@PostMapping("/getNotice")
	@ResponseBody
	public String getNotice(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取公告栏信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			notice notice = NoticeService.getNotice(dataJson);
			return ResultJsonUtil.toJsonString(200, notice, "获取公告栏信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获取公告栏信息--------------------");
		}
	}
	@PostMapping("/getNoticeArray")
	@ResponseBody
	public String getNoticeArray(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取公告栏信息数组--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			List<notice> notices = NoticeService.getNoticeByCompanyList(dataJson);
			return ResultJsonUtil.toJsonString(200, notices, "获取公告栏信息数组成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获取公告栏信息数组--------------------");
		}
	}
}
