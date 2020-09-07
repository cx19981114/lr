package cn.lr.service.company;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.Page;
import cn.lr.po.company;

public interface CompanyService {

	public Integer addCompany(JSONObject data);
	
	public Integer modifyCompany(JSONObject data);
	
	public Integer deleteCompany(JSONObject data);
	
	public company getCompany(JSONObject data);
	
	public List<JSONObject> getTimeList(JSONObject data);
	
	public Page<company> getCompanyList(JSONObject data);
}
