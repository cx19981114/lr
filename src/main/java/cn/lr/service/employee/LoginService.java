package cn.lr.service.employee;

import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.employeeDTO;

public interface LoginService {

	public employeeDTO checkLogin(JSONObject data);
	
	public Integer sendMsg(JSONObject data) throws Exception;

	public Integer checkCode(JSONObject data) throws ParseException;

	public Integer modifyPasswordFrist(JSONObject data);
}
