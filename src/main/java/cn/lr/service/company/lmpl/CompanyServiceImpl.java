package cn.lr.service.company.lmpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.companyMapper;
import cn.lr.dao.dictMapper;
import cn.lr.exception.BusiException;
import cn.lr.po.company;
import cn.lr.service.company.CompanyService;
import cn.lr.util.DateUtil;
import cn.lr.util.TimeFormatUtil;
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	companyMapper companyMapper;
	@Autowired
	dictMapper dictMapper;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	
	@Override
	public Integer addCompany(JSONObject data) {
		company record = new company();
		record.setName(data.getString("name"));
		record.setAddress(data.getString("address"));
		record.setDateTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		record.setStartTime(data.getString("startTime"));
		record.setEndTime(data.getString("endTime"));
		// 完整填写信息
		int count = companyMapper.insertSelective(record);
		if(count == 0) {
			throw new BusiException("插入公司表失败");
		}
		record.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效",data.getInteger("companyId")));
		count = companyMapper.updateByPrimaryKey(record);
		if(count == 0) {
			throw new BusiException("更新公司表失败");
		}
		return record.getId();
	}

	@Override
	public Integer modifyCompany(JSONObject data) {
		company test1 = companyMapper.selectByPrimaryKey(data.getInteger("companyId"));
		test1.setName(data.getString("name"));
		test1.setAddress(data.getString("address"));
		test1.setStartTime(data.getString("startTime"));
		test1.setEndTime(data.getString("endTime"));
		// 完整填写信息
		int count = companyMapper.updateByPrimaryKeySelective(test1);
		if(count == 0) {
			throw new BusiException("更新公司表失败");
		}
		return test1.getId();
	}

	@Override
	public Integer deleteCompany(JSONObject data) {
		company test1 = companyMapper.selectByPrimaryKey(data.getInteger("companyId"));
		test1.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		// 完整填写信息
		int count = companyMapper.updateByPrimaryKeySelective(test1);
		if(count == 0) {
			throw new BusiException("更新公司表失败");
		}
		return test1.getId();
	}

	@Override
	public company getCompany(JSONObject data) {
		company test1 = companyMapper.selectByPrimaryKey(data.getInteger("companyId"));
		if(test1 == null || test1.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该公司不存在");
		}
		return test1;
	}

	public List<JSONObject> getTimeList(JSONObject data){
		company company = companyMapper.selectByPrimaryKey(data.getInteger("companyId"));
	    List<String> list = DateUtil.cutDate(company.getStartTime(), company.getEndTime());
	    List<JSONObject> timeJson = new ArrayList<JSONObject>();
	    int i = 0;
	    JSONObject time = new JSONObject();
        time.put("title", "所有时段");
        time.put("key", i++);
        timeJson.add(time);
	    for (String str :list){
	          time = new JSONObject();
	          time.put("title", str);
	          time.put("key", i++);
	          timeJson.add(time);
	      }
	    return timeJson;
	}
}
