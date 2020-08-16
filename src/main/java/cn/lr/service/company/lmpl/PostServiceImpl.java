package cn.lr.service.company.lmpl;

import java.util.ArrayList;
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
import cn.lr.dto.PostDTO;
import cn.lr.exception.BusiException;
import cn.lr.exception.DataException;
import cn.lr.po.post;
import cn.lr.po.postTask;
import cn.lr.po.task;
import cn.lr.service.company.PostService;
@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	postMapper postMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	companyMapper companyMapper;
	@Autowired
	postTaskMapper postTaskMapper; 
	@Autowired
	taskMapper taskMapper;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${prevTask.type}")
	private String PRETASK_TYPE;
	@Value("${task.type}")
	private String TASK_TYPE;
	
	@Override
	public Integer addPost(JSONObject data) {
		post record = new post();
		record.setCompanyId(data.getInteger("companyId"));
		record.setName(data.getString("name"));
		record.setPermissionList(data.getString("permissionList"));
		record.setPic(data.getString("pic"));
		record.setLeaderPostId(data.getInteger("leaderPostId"));
		record.setNum(0);
		record.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效",data.getInteger("companyId")));
		int count = postMapper.insert(record);
		if(count == 0) {
			throw new BusiException("添加post表失败");
		}
		postTask postTask = new postTask();
		postTask.setPostId(record.getId());
		count = postTaskMapper.insertSelective(postTask);
		if(count == 0) {
			throw new BusiException("添加postTask表失败");
		}
		return record.getId();
	}

	@Override
	public Integer modifyPost(JSONObject data) {
		post test2 = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		test2.setName(data.getString("name"));
		test2.setPic(data.getString("pic"));
		test2.setPermissionList(data.getString("permissionList"));
		test2.setLeaderPostId(data.getInteger("leaderPostId"));
		int count = postMapper.updateByPrimaryKeySelective(test2);
		if(count == 0) {
			throw new BusiException("更新post表失败");
		}
		List<task> taskList = taskMapper.selectByPost(test2.getId());
		for(task t:taskList) {
			t.setPostIdList(t.getPostIdList().replace(test2.getId()+"-", ""));
			count = taskMapper.updateByPrimaryKeySelective(t);
			if(count == 0) {
				throw new BusiException("更新task表失败");
			}
		}
		String taskFN = data.getString("taskFN");
		String taskRWDay = data.getString("taskRWDay");
		String taskRWWeek = data.getString("taskRWWeek");
		String taskRWMon = data.getString("taskRWMon");
		if(!"".equals(taskFN) && taskFN != null) {
			String[] taskId = taskFN.split("-");
			for(int i = 0 ;i<taskId.length;i++) {
				task task = taskMapper.selectByPrimaryKey(Integer.valueOf(taskId[i]));
				if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", test2.getCompanyId())) {
					throw new BusiException("该taskId不存在");
				}
				if(task.getPrevType() != dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "赋能思维", test2.getCompanyId())) {
					throw new BusiException("该task不属于赋能思维");
				}
				task.setPostIdList(test2.getId()+"-"+task.getPostIdList());
				count = taskMapper.updateByPrimaryKeySelective(task);
				if(count == 0) {
					throw new BusiException("更新task表失败");
				}
			}
		}
		if(!"".equals(taskRWDay) && taskRWDay != null) {
			String[] taskId = taskRWDay.split("-");
			for(int i = 0 ;i<taskId.length;i++) {
				task task = taskMapper.selectByPrimaryKey(Integer.valueOf(taskId[i]));
				if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", test2.getCompanyId())) {
					throw new BusiException("该taskId不存在");
				}
				if(task.getPrevType() != dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单", test2.getCompanyId())) {
					throw new BusiException("该task不属于赋能思维");
				}
				if(task.getType() != dictMapper.selectByCodeAndStateName(TASK_TYPE, "日流程", test2.getCompanyId())) {
					throw new BusiException("该task不属于日流程");
				}
				task.setPostIdList(test2.getId()+"-"+task.getPostIdList());
				count = taskMapper.updateByPrimaryKeySelective(task);
				if(count == 0) {
					throw new BusiException("更新task表失败");
				}
			}
		}
		if(!"".equals(taskRWWeek) && taskRWWeek != null) {
			String[] taskId = taskRWWeek.split("-");
			for(int i = 0 ;i<taskId.length;i++) {
				task task = taskMapper.selectByPrimaryKey(Integer.valueOf(taskId[i]));
				if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", test2.getCompanyId())) {
					throw new BusiException("该taskId不存在");
				}
				if(task.getPrevType() != dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单", test2.getCompanyId())) {
					throw new BusiException("该task不属于赋能思维");
				}
				if(task.getType() != dictMapper.selectByCodeAndStateName(TASK_TYPE, "周安排", test2.getCompanyId())) {
					throw new BusiException("该task不属于周安排");
				}
				task.setPostIdList(test2.getId()+"-"+task.getPostIdList());
				count = taskMapper.updateByPrimaryKeySelective(task);
				if(count == 0) {
					throw new BusiException("更新task表失败");
				}
			}
		}
		if(!"".equals(taskRWMon) && taskRWMon != null) {
			String[] taskId = taskRWMon.split("-");
			for(int i = 0 ;i<taskId.length;i++) {
				task task = taskMapper.selectByPrimaryKey(Integer.valueOf(taskId[i]));
				if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", test2.getCompanyId())) {
					throw new BusiException("该taskId不存在");
				}
				if(task.getPrevType() != dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单", test2.getCompanyId())) {
					throw new BusiException("该task不属于赋能思维");
				}
				if(task.getType() != dictMapper.selectByCodeAndStateName(TASK_TYPE, "月计划", test2.getCompanyId())) {
					throw new BusiException("该task不属于月计划");
				}
				task.setPostIdList(test2.getId()+"-"+task.getPostIdList());
				count = taskMapper.updateByPrimaryKeySelective(task);
				if(count == 0) {
					throw new BusiException("更新task表失败");
				}
			}
		}
		postTask postTask = postTaskMapper.selectByPost(test2.getId());
		postTask.setTaskIdListFN(data.getString("taskFN") == null ? null:data.getString("taskFN")+"-");
		postTask.setTaskIdListRWDay(data.getString("taskRWDay") == null ? null:data.getString("taskRWDay")+"-");
		postTask.setTaskIdListRWWeek(data.getString("taskRWWeek") == null ? null:data.getString("taskRWWeek")+"-");
		postTask.setTaskIdListRWMon(data.getString("taskRWMon") == null ? null:data.getString("taskRWMon")+"-");
		count = postTaskMapper.updateByPrimaryKeySelective(postTask);
		if(count == 0) {
			throw new BusiException("更新postTask表失败");
		}
		return test2.getId();
	}

	@Override
	public Integer deletePost(JSONObject data) {
		post test2 = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		test2.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		if(test2.getNum() != 0) {
			throw new DataException("该职位下有职员存在");
		}
		int count = postMapper.updateByPrimaryKeySelective(test2);
		if(count == 0) {
			throw new BusiException("更新post表失败");
		}
		return test2.getId();
	}

	@Override
	public PostDTO getPost(JSONObject data) {
		post post = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		if(post == null || post.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")) ) {
			throw new BusiException("该公司岗位不存在");
		}
		return this.sePostDTO(post);
	}

	@Override
	public Page<post> getPostByCompany(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<post> posts = postMapper.selectByCompany(companyId,state,(pageNum-1)*PAGESIZE,PAGESIZE);
		int total = postMapper.selectByCompanyCount(companyId,state);
		Page<post> page = new Page<post>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(posts);
		return page;
	}

	@Override
	public List<JSONObject> getPostTypeAndCount(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<post> posts = postMapper.selectByCompany(companyId,state,0,Integer.MAX_VALUE);
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		int allCount = 0;
		for(post p:posts) {
			allCount += p.getNum();
		}
		JSONObject all = new JSONObject();
		all.put("value", "全部");
		all.put("count", allCount);
		all.put("id", 0);
		jsonList.add(all);
		for(post p:posts) {
			JSONObject dataJson = new JSONObject();
			dataJson.put("value", p.getName());
			dataJson.put("count", p.getNum());
			dataJson.put("id", p.getId());
			jsonList.add(dataJson);
		}
		return jsonList;
	}
	
	public PostDTO sePostDTO(post post) {
		PostDTO postDTO = new PostDTO();
		postTask postTask = postTaskMapper.selectByPost(post.getId());
		postDTO.setCompanyId(post.getCompanyId());
		postDTO.setId(post.getId());
		postDTO.setLeaderPostId(post.getLeaderPostId());
		postDTO.setName(post.getName());
		postDTO.setNum(post.getNum());
		postDTO.setPermissionList(post.getPermissionList());
		postDTO.setPic(post.getPic());
		postDTO.setState(dictMapper.selectByCodeAndStateCode(DATA_TYPE, post.getState(), post.getCompanyId()));
		postDTO.setTaskFN(postTask.getTaskIdListFN());
		postDTO.setTaskRWDay(postTask.getTaskIdListRWDay());
		postDTO.setTaskRWWeek(postTask.getTaskIdListRWWeek());
		postDTO.setTaskRWMon(postTask.getTaskIdListRWMon());
		return postDTO;
	}

}
