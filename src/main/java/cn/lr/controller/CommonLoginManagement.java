package cn.lr.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.lr.util.ResultJsonUtil;

@RequestMapping("/LoginManagement")
@Controller
public class CommonLoginManagement {
	
	@PostMapping("/checkLogin")
	@ResponseBody
	public String loginCheck(@RequestBody String data, HttpSession session) {
		JSONObject dataJson = JSON.parseObject(data);
		try {
			System.out.println(dataJson.get("name"));
			return ResultJsonUtil.toJsonString(200, "success", null);
		}catch(Exception e) {
			return ResultJsonUtil.toJsonString(-1, "系统未知错误", null);
		}
	}
	
}