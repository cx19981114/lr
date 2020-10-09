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
import cn.lr.dao.postMapper;
import cn.lr.dao.postTaskMapper;
import cn.lr.dao.taskMapper;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.company;
import cn.lr.po.dict;
import cn.lr.po.post;
import cn.lr.po.postTask;
import cn.lr.po.task;
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
	@Autowired
	postMapper postMapper;
	@Autowired
	postTaskMapper postTaskMapper;
	@Autowired
	taskMapper taskMapper;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${noImg}")
	private String NOIMG;
	@Value("${permissionList}")
	private String PERMISSIONLIST;
	@Value("${post}")
	private String POST;
	@Value("${prevTask.type}")
	private String PRETASK_TYPE;
	@Value("${task.type}")
	private String TASK_TYPE;
	
	@Override
	public Integer addCompany(JSONObject data) {
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<>();
		stateList.add(stateWSX);
		company record = new company();
		record.setName(data.getString("name"));
		company company = companyMapper.selectByName(record);
		if(company != null) {
			throw new BusiException("该公司名称已存在");
		}
		record.setAddress(data.getString("address"));
		record.setDateTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		record.setStartTime(data.getString("startTime"));
		record.setEndTime(data.getString("endTime"));
		record.setState(stateWSX);
		// 完整填写信息
		int count = companyMapper.insertSelective(record);
		if(count == 0) {
			throw new BusiException("插入公司表失败");
		}
		// 添加post
		String[] postList = POST.split("-");
		List<Integer> postIdList = new ArrayList<>();
		postIdList.add(0);
		for(int i = 0;i<postList.length;i++) {
			post post = new post();
			post.setCompanyId(record.getId());
			post.setName(postList[i]);
			post.setPermissionList(PERMISSIONLIST);
			post.setPic(NOIMG);
			post.setLeaderPostId(postIdList.get(i));
			post.setNum(0);
			post.setState(stateWSX);
			count = postMapper.insertSelective(post);
			if (count == 0) {
				throw new BusiException("添加post表失败");
			}
			postIdList.add(post.getId());
			post post2 = new post();
			post2.setCompanyId(2);
			post2.setName(postList[i]);
			post2 = postMapper.selectByCompanyIdAndPostName(post2);
			postTask postTask = new postTask();
			postTask.setPostId(post.getId());
			//task-赋能思维
			String taskIdList = "";
			List<task> taskList = taskMapper.selectByPost(post2.getId(),
					dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "赋能思维", record.getId()),null, stateList);
			for(task t:taskList) {
				task tCopy = new task();
				tCopy.setCompanyId(record.getId());
				tCopy.setContent(t.getContent());
				tCopy.setName(t.getName());
				tCopy.setPostIdList(String.valueOf(post.getId()));
				tCopy.setPrevType(t.getPrevType());
				tCopy.setRank(t.getRank());
				tCopy.setState(stateWSX);
				tCopy.setStep(t.getStep());
				tCopy.setType(t.getType());
				count = taskMapper.insertSelective(tCopy);
				taskIdList += tCopy.getId()+"-";
				if(count == 0) {
					throw new BusiException("插入任务失败");
				}
			}
			postTask.setTaskIdListFN(taskIdList);
			//task-任务清单-日流程
			taskIdList = "";
			taskList = taskMapper.selectByPost(post2.getId(),
					dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单", record.getId()),
					dictMapper.selectByCodeAndStateName(TASK_TYPE, "日流程", record.getId()), stateList);
			for(task t:taskList) {
				task tCopy = new task();
				tCopy.setCompanyId(record.getId());
				tCopy.setContent(t.getContent());
				tCopy.setName(t.getName());
				tCopy.setPostIdList(String.valueOf(post.getId()));
				tCopy.setPrevType(t.getPrevType());
				tCopy.setRank(t.getRank());
				tCopy.setState(stateWSX);
				tCopy.setStep(t.getStep());
				tCopy.setType(t.getType());
				count = taskMapper.insertSelective(tCopy);
				taskIdList += tCopy.getId()+"-";
				if(count == 0) {
					throw new BusiException("插入任务失败");
				}
			}
			postTask.setTaskIdListRWDay(taskIdList);
			//task-任务清单-周安排
			taskIdList = "";
			taskList = taskMapper.selectByPost(post2.getId(),
					dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单", record.getId()),
					dictMapper.selectByCodeAndStateName(TASK_TYPE, "周安排", record.getId()), stateList);
			for(task t:taskList) {
				task tCopy = new task();
				tCopy.setCompanyId(record.getId());
				tCopy.setContent(t.getContent());
				tCopy.setName(t.getName());
				tCopy.setPostIdList(String.valueOf(post.getId()));
				tCopy.setPrevType(t.getPrevType());
				tCopy.setRank(t.getRank());
				tCopy.setState(stateWSX);
				tCopy.setStep(t.getStep());
				tCopy.setType(t.getType());
				count = taskMapper.insertSelective(tCopy);
				taskIdList += tCopy.getId()+"-";
				if(count == 0) {
					throw new BusiException("插入任务失败");
				}
			}
			postTask.setTaskIdListRWWeek(taskIdList);
			//task-任务清单-月计划
			taskIdList = "";
			taskList = taskMapper.selectByPost(post2.getId(),
					dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单", record.getId()),
					dictMapper.selectByCodeAndStateName(TASK_TYPE, "月计划", record.getId()), stateList);
			for(task t:taskList) {
				task tCopy = new task();
				tCopy.setCompanyId(record.getId());
				tCopy.setContent(t.getContent());
				tCopy.setName(t.getName());
				tCopy.setPostIdList(String.valueOf(post.getId()));
				tCopy.setPrevType(t.getPrevType());
				tCopy.setRank(t.getRank());
				tCopy.setState(stateWSX);
				tCopy.setStep(t.getStep());
				tCopy.setType(t.getType());
				count = taskMapper.insertSelective(tCopy);
				taskIdList += tCopy.getId()+"-";
				if(count == 0) {
					throw new BusiException("插入任务失败");
				}
			}
			postTask.setTaskIdListRWMon(taskIdList);
			count = postTaskMapper.insertSelective(postTask);
			if (count == 0) {
				throw new BusiException("添加postTask表失败");
			}
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
	
	public Page<company> getCompanyList(JSONObject data){
		String search = data.getString("search");
		Integer pageNum = data.getInteger("pageNum");
		List<company> company = companyMapper.selectCompanyCondition(search,(pageNum-1)*PAGESIZE, PAGESIZE);
		int total = companyMapper.selectCompanyConditionCount(search);
		Page<company> page = new Page<company>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(company);
		return page;
	}
	public Page<company> getCompanyListCondition(JSONObject data){
		String search = data.getString("search");
		Integer id = data.getInteger("id");
		String state = data.getString("state");
		Integer pageNum = data.getInteger("pageNum");
		List<company> company = companyMapper.selectCompanyConditionMore(search,id,state,(pageNum-1)*PAGESIZE, PAGESIZE);
		int total = companyMapper.selectCompanyConditionCountMore(search,id,state);
		Page<company> page = new Page<company>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(company);
		return page;
	}
}
