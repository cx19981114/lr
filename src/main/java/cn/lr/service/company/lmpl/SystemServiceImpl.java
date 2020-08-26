package cn.lr.service.company.lmpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.dictMapper;
import cn.lr.dao.systemMapper;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.system;
import cn.lr.service.company.SystemService;
@Service
@Transactional
public class SystemServiceImpl implements SystemService {

	@Autowired
	dictMapper dictMapper;
	@Autowired
	systemMapper systemMapper;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	
	@Override
	public Integer addSystem(JSONObject data) {
		system system = new system();
		system.setCompanyId(data.getInteger("companyId"));
		system.setContent(data.getString("content"));
		system.setName(data.getString("name"));
		system.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效",data.getInteger("companyId")));
		int count = systemMapper.insertSelective(system);
		if(count == 0) {
			throw new BusiException("添加system表失败");
		}
		return system.getId();
	}

	@Override
	public Integer modifySystem(JSONObject data) {
		system system = systemMapper.selectByPrimaryKey(data.getInteger("systemId"));
		system.setCompanyId(data.getInteger("companyId"));
		system.setContent(data.getString("content"));
		system.setName(data.getString("name"));
		int count = systemMapper.updateByPrimaryKeySelective(system);
		if(count == 0) {
			throw new BusiException("更新system表失败");
		}
		return system.getId();
	}

	@Override
	public Integer deleteSystem(JSONObject data) {
		system system = systemMapper.selectByPrimaryKey(data.getInteger("systemId"));
		system.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = systemMapper.updateByPrimaryKeySelective(system);
		if(count == 0) {
			throw new BusiException("更新system表失败");
		}
		return system.getId();
	}

	@Override
	public system getSystem(JSONObject data) {
		system system = systemMapper.selectByPrimaryKey(data.getInteger("systemId"));
		if(system == null || system.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该公司制度不存在");
		}
		return system;
	}
	
	@Override
	public Page<system> getSystemByCompany(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer pageNum = data.getInteger("pageNum");
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateWSX);
		List<system> systems = systemMapper.selectByCompanyId(companyId,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
		int total = systemMapper.selectByCompanyCount(companyId,stateList);
		Page<system> page = new Page<system>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(systems);
		return page;
	}

}
