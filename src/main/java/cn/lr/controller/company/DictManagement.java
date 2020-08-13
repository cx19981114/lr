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
import cn.lr.service.company.DictService;
import cn.lr.util.LoggerUtil;
import cn.lr.util.ResultJsonUtil;

@RequestMapping("/DictManagement")
@Controller
public class DictManagement {
	@Autowired
	private DictService DictService;
	
	@PostMapping("/getDictType")
	@ResponseBody
	public String getDictType(@RequestBody String data, HttpSession session) {
		LoggerUtil.LOGGER.info("-------------enter 获取字典表类型信息--------------------");
		LoggerUtil.LOGGER.info("sessionId : {}, employeeId : {}", session.getId(), session.getAttribute("employeeId"));
		LoggerUtil.LOGGER.debug("data : {}", data);
		JSONObject dataJson = JSON.parseObject(data);
		dataJson.put("companyId", session.getAttribute("companyId"));
		try {
			List<JSONObject> dict = DictService.getDictType(dataJson);
			return ResultJsonUtil.toJsonString(200, dict, "成功获取字典表类型信息",session.getId());
		} catch (BusiException e) {
			return ResultJsonUtil.toJsonString(101, null, e.getMessage(),session.getId());
		} catch (Exception e) {
			return ResultJsonUtil.toJsonString(404, null, "系统未知错误",session.getId());
		}finally {
			LoggerUtil.LOGGER.info("-------------end 获取字典表类型信息--------------------");
		}
	}
}
