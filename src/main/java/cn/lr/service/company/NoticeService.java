package cn.lr.service.company;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.Page;
import cn.lr.po.notice;

public interface NoticeService {

	public Page<notice> getNoticeByCompany(JSONObject data);

	public Integer addNotice(JSONObject data);

	public Integer modifyNotice(JSONObject data);

	public Integer deleteNotice(JSONObject data);
	
	public notice getNotice(JSONObject data);
	
	public List<notice> getNoticeByCompanyList(JSONObject data);
}
