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
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.company;
import cn.lr.service.company.CompanyService;
import cn.lr.service.employee.DynamicService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/CompanyManagement")
@Controller
public class CompanyManagement {
	@Autowired
	private CompanyService CompanyService;

	@PostMapping("/addCompany")
	@ResponseBody
	public String addCompany(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 添加公司信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			int id = CompanyService.addCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "添加公司信息成功",session.getId());
		} catch (BusiException e) {	
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 添加公司信息--------------------");
		}
	}
	@PostMapping("/modifyCompany")
	@ResponseBody
	public String modifyCompany(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 修改公司信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			int id = CompanyService.modifyCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "修改公司信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 修改公司信息--------------------");
		}
	}
	@PostMapping("/deleteCompany")
	@ResponseBody
	public String deleteCompany(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 删除公司信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(),session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			int id = CompanyService.deleteCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, null, "删除公司信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 删除公司信息--------------------");
		}
	}
	@PostMapping("/getCompany")
	@ResponseBody
	public String getCompany(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得公司信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			company company = CompanyService.getCompany(dataJson);
			return ResultJsonUtil.toJsonString(200, company, "获取公司信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获得公司信息--------------------");
		}
	}
	@PostMapping("/getTimeList")
	@ResponseBody
	public String getTimeList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获得公司时段信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			CompanyService.getCompany(dataJson);
			List<JSONObject> time = CompanyService.getTimeList(dataJson);
			return ResultJsonUtil.toJsonString(200, time, "获取公司时段信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获得公司时段信息--------------------");
		}
	}
	@PostMapping("/getCompanyList")
	@ResponseBody
	public String getCompanyList(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取所有公司--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			Page<company> companies = CompanyService.getCompanyList(dataJson);
			return ResultJsonUtil.toJsonString(200, companies, "获取所有公司成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, e.getMessage(), "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获取所有公司--------------------");
		}
	}
	@PostMapping("/getCompanyListCondition")
	@ResponseBody
	public String getCompanyListCondition(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据条件获取所有公司--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		if (session.getAttribute("companyId") != null) {
			dataJson.put("companyId", session.getAttribute("companyId"));
			}
		try {
			Page<company> companies = CompanyService.getCompanyListCondition(dataJson);
			return ResultJsonUtil.toJsonString(200, companies, "根据条件获取所有公司成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, e.getMessage(), "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据条件获取所有公司--------------------");
		}
	}
	
	

}
