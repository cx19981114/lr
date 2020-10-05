package cn.lr.controller.company;

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
import cn.lr.po.pic;
import cn.lr.service.company.CompanyService;
import cn.lr.service.company.PicService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;
@RequestMapping("/PicManagement")
@Controller
public class PicManagement {
	@Autowired
	private PicService PicService;
	@Autowired
	private CompanyService CompanyService;
	
	@PostMapping("/addPic")
	@ResponseBody
	public String addPic(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加公司轮播图信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			int id = PicService.addPic(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "添加公司轮播图成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加公司轮播图信息--------------------");
		}
	}
	@PostMapping("/modifyPic")
	@ResponseBody
	public String modifyPic(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改公司轮播图信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			PicService.getPic(dataJson);
			int id = PicService.modifyPic(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改公司轮播图成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 修改公司轮播图信息--------------------");
		}
	}
	@PostMapping("/deletePic")
	@ResponseBody
	public String deletePic(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除公司轮播图信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			int id = PicService.deletePic(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除公司轮播图成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 删除公司轮播图信息--------------------");
		}
	}
	@PostMapping("/getPicList")
	@ResponseBody
	public String getPicList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据公司获取轮播图信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			JSONObject pics = PicService.getPicByCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, pics, "根据公司获取轮播图成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据公司获取轮播图信息--------------------");
		}
	}
	@PostMapping("/getPic")
	@ResponseBody
	public String getPic(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取轮播图信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			pic pic = PicService.getPic(dataJson);
			return ResultJsonUtil.toJsonString(200, pic, "获取轮播图成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获取轮播图信息--------------------");
		}
	}
}
