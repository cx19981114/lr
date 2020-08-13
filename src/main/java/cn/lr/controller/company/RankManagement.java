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
import cn.lr.service.company.CompanyService;
import cn.lr.service.company.RankService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/RankManagement")
@Controller
public class RankManagement {
	@Autowired
	private RankService RankService;
	@Autowired
	private CompanyService CompanyService;

	@PostMapping("/getRankByType")
	@ResponseBody
	public String getRankByType(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 根据类型获取积分信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}, companyId : {}", session.getId(), session.getAttribute("employeeId"),session.getAttribute("comapnyId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			CompanyService.getCompany(dataJson);
			int score = RankService.getRankByType(dataJson);
			return ResultJsonUtil.toJsonString(200, score, "根据类型获取积分信息成功",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 根据类型获取积分信息--------------------");
		}
	}
}
