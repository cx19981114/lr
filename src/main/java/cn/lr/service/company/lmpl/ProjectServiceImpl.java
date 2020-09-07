package cn.lr.service.company.lmpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.customerProjectMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.projectMapper;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.customerProject;
import cn.lr.po.project;
import cn.lr.service.company.ProjectService;
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	dictMapper dictMapper;
	@Autowired
	projectMapper projectMapper;
	@Autowired
	customerProjectMapper customerProjectMapper;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	
	@Override
	public Integer addProject(JSONObject data) {
		project project = new project();
		project.setCompanyId(data.getInteger("companyId"));
		project.setContent(data.getString("content"));
		project.setName(data.getString("name"));
		project.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效",data.getInteger("companyId")));
		if(data.getInteger("money") == null) {
			throw new BusiException("请填写项目价格");
		}
		if(data.getInteger("num") == null) {
			throw new BusiException("请填写项目次数");
		}
		project.setMoney(data.getInteger("money"));
		project.setNum(data.getInteger("num"));
		int count = projectMapper.insertSelective(project);
		if(count == 0) {
			throw new BusiException("添加project表失败");
		}
		return project.getId();
	}

	@Override
	public Integer modifyProject(JSONObject data) {
		project project = projectMapper.selectByPrimaryKey(data.getInteger("projectId"));
		project.setCompanyId(data.getInteger("companyId"));
		project.setContent(data.getString("content"));
		project.setName(data.getString("name"));
//		project.setMoney(data.getInteger("money"));
//		project.setNum(data.getInteger("num"));
		int count = projectMapper.updateByPrimaryKeySelective(project);
		if(count == 0) {
			throw new BusiException("更新project表失败");
		}
		return project.getId();
	}

	@Override
	public Integer deleteProject(JSONObject data) {
		project project = projectMapper.selectByPrimaryKey(data.getInteger("projectId"));
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(state);
		List<customerProject> customerProjects = customerProjectMapper.selectByProject(project.getId(), stateList);
		if(!customerProjects.isEmpty()) {
			throw new BusiException("该项目下有客人，无法删除");
		}
		project.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = projectMapper.updateByPrimaryKeySelective(project);
		if(count == 0) {
			throw new BusiException("更新project表失败");
		}
		return project.getId();
	}

	@Override
	public project getProject(JSONObject data) {
		project project = projectMapper.selectByPrimaryKey(data.getInteger("projectId"));
		if(project == null || project.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该项目不存在");
		}
		return project;
	}
	
	@Override
	public Page<project> getProjectByCompany(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer pageNum = data.getInteger("pageNum");
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateWSX);
		List<project> projects = projectMapper.selectByCompanyId(companyId,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
		int total = projectMapper.selectByCompanyCount(companyId,stateList);
		Page<project> page = new Page<project>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(projects);
		return page;
	}
	@Override
	public List<JSONObject> getProjectByCompanyJson(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateWSX);
		List<project> projects = projectMapper.selectByCompanyId(companyId,stateList,0,Integer.MAX_VALUE);
		List<JSONObject> projectJsonList = new ArrayList<JSONObject>();
		for(project p:projects) {
			JSONObject projectJsonObject = new JSONObject();
			projectJsonObject.put("id", p.getId());
			projectJsonObject.put("name", p.getName());
			projectJsonObject.put("money", p.getMoney());
			projectJsonObject.put("count", p.getNum());
			projectJsonList.add(projectJsonObject);
		}
		return projectJsonList;
	}
}
