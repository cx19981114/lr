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
import cn.lr.po.post;
import cn.lr.po.postTask;
import cn.lr.po.task;
import cn.lr.service.company.PostService;
import cn.lr.service.company.TaskService;

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

	@Autowired
	TaskService TaskService;
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
		record.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId")));
		int count = postMapper.insert(record);
		if (count == 0) {
			throw new BusiException("添加post表失败");
		}
		postTask postTask = new postTask();
		postTask.setPostId(record.getId());
		count = postTaskMapper.insertSelective(postTask);
		if (count == 0) {
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
		if (count == 0) {
			throw new BusiException("更新post表失败");
		}
//		String prevType = data.getString("prevType");
//		String type = data.getString("type");
//		String taskIdList = data.getString("taskIdList");
//		if (!"".equals(prevType) && prevType != null) {
//			JSONObject taskJsonObject = TaskService.testType(data);
//			List<task> taskList = taskMapper.selectByPost(test2.getId(), taskJsonObject.getInteger("prevType"),
//					taskJsonObject.getInteger("type"),state);
//			for (task t : taskList) {
//				t.setPostIdList(t.getPostIdList().replace(test2.getId() + "-", ""));
//				count = taskMapper.updateByPrimaryKeySelective(t);
//				if (count == 0) {
//					throw new BusiException("更新task表失败");
//				}
//			}
//
//			if (!"".equals(taskIdList) && taskIdList != null) {
//				String[] taskId = taskIdList.split("-");
//				for (int i = 0; i < taskId.length; i++) {
//					System.out.println(Integer.valueOf(taskId[i]));
//					task task = taskMapper.selectByPrimaryKey(Integer.valueOf(taskId[i]));
//					if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
//							test2.getCompanyId())) {
//						throw new BusiException("该任务不存在");
//					}
//					if (task.getPrevType() != dictMapper.selectByCodeAndStateName(PRETASK_TYPE, prevType,
//							test2.getCompanyId())) {
//						throw new BusiException("该任务不属于" + prevType);
//					}
//					if ("任务清单".equals(prevType)) {
//						if (task.getType() != dictMapper.selectByCodeAndStateName(TASK_TYPE, type,
//								test2.getCompanyId())) {
//							throw new BusiException("该任务不属于" + type);
//						}
//					}
//					task.setPostIdList(test2.getId() + "-" + task.getPostIdList());
//					count = taskMapper.updateByPrimaryKeySelective(task);
//					if (count == 0) {
//						throw new BusiException("更新task表失败");
//					}
//				}
//				postTask postTask = postTaskMapper.selectByPost(test2.getId());
//				if ("赋能思维".equals(prevType)) {
//					postTask.setTaskIdListFN(data.getString("taskIdList") + "-");
//				} else if ("任务清单".equals(prevType)) {
//					if ("日流程".equals(type)) {
//						postTask.setTaskIdListRWDay(data.getString("taskIdList") + "-");
//					} else if ("周安排".equals(type)) {
//						postTask.setTaskIdListRWWeek(data.getString("taskIdList") + "-");
//					} else if ("月计划".equals(type)) {
//						postTask.setTaskIdListRWMon(data.getString("taskIdList") + "-");
//					}
//				}
//				count = postTaskMapper.updateByPrimaryKeySelective(postTask);
//				if (count == 0) {
//					throw new BusiException("更新postTask表失败");
//				}
//			}
//		}
		return test2.getId();
	}

	@Override
	public Integer deletePost(JSONObject data) {
		post test2 = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		test2.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId")));
		if (test2.getNum() != 0) {
			throw new BusiException("该职位下有职员存在");
		}
		int count = postMapper.updateByPrimaryKeySelective(test2);
		if (count == 0) {
			throw new BusiException("更新post表失败");
		}
		return test2.getId();
	}

	@Override
	public PostDTO getPost(JSONObject data) {
		post post = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		if (post == null || post.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
				data.getInteger("companyId"))) {
			throw new BusiException("该公司岗位不存在");
		}
		return this.sePostDTO(post);
	}

	@Override
	public Page<post> getPostByCompany(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<post> posts = postMapper.selectByCompany(companyId, state, (pageNum - 1) * PAGESIZE, PAGESIZE);
		int total = postMapper.selectByCompanyCount(companyId, state);
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
		List<post> posts = postMapper.selectByCompany(companyId, state, 0, Integer.MAX_VALUE);
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		int allCount = 0;
		for (post p : posts) {
			allCount += p.getNum();
		}
		JSONObject all = new JSONObject();
		all.put("value", "全部");
		all.put("count", allCount);
		all.put("id", 0);
		jsonList.add(all);
		for (post p : posts) {
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
		post post2 = postMapper.selectByPrimaryKey(post.getLeaderPostId());
		if (!post.getName().contains("店长")) {
			postDTO.setLeaderPostId(post.getLeaderPostId());
			postDTO.setLeaderPostName(post2.getName());
		}
		postTask postTask = postTaskMapper.selectByPost(post.getId());
		postDTO.setCompanyId(post.getCompanyId());
		postDTO.setId(post.getId());
		postDTO.setName(post.getName());
		postDTO.setNum(post.getNum());
		postDTO.setPermissionList(post.getPermissionList());
		postDTO.setPic(post.getPic());
		postDTO.setState(dictMapper.selectByCodeAndStateCode(DATA_TYPE, post.getState(), post.getCompanyId()));
		postDTO.setTaskFN(postTask.getTaskIdListFN() == null || "".equals(postTask.getTaskIdListFN()) ? null
				: postTask.getTaskIdListFN().substring(0, postTask.getTaskIdListFN().length() - 1));
		postDTO.setTaskRWDay(postTask.getTaskIdListRWDay() == null || "".equals(postTask.getTaskIdListRWDay())? null
				: postTask.getTaskIdListRWDay().substring(0, postTask.getTaskIdListRWDay().length() - 1));
		postDTO.setTaskRWWeek(postTask.getTaskIdListRWWeek() == null || "".equals(postTask.getTaskIdListRWWeek())? null
				: postTask.getTaskIdListRWWeek().substring(0, postTask.getTaskIdListRWWeek().length() - 1));
		postDTO.setTaskRWMon(postTask.getTaskIdListRWMon() == null || "".equals(postTask.getTaskIdListRWMon())? null
				: postTask.getTaskIdListRWMon().substring(0, postTask.getTaskIdListRWMon().length() - 1));
		return postDTO;
	}

	@Override
	public List<JSONObject> getTaskByPostList(JSONObject data) {
		postTask postTask = postTaskMapper.selectByPost(data.getInteger("postId"));
		String taskList = null;
		if ("赋能思维".equals(data.getString("prevType"))) {
			taskList = postTask.getTaskIdListFN();
		} else if ("任务清单".equals(data.getString("prevType"))) {
			if ("日流程".equals(data.getString("type"))) {
				taskList = postTask.getTaskIdListRWDay();
			} else if ("周安排".equals(data.getString("type"))) {
				taskList = postTask.getTaskIdListRWWeek();
			} else if ("月计划".equals(data.getString("type"))) {
				taskList = postTask.getTaskIdListRWMon();
			}
		}
		List<task> tasks = new ArrayList<task>();
		if (taskList != null && !("".equals(taskList))) {
			String[] taskId = taskList.split("-");
			for (int i = 0; i < taskId.length; i++) {
				task task = taskMapper.selectByPrimaryKey(Integer.valueOf(taskId[i]));
				tasks.add(task);
			}
		}
		List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
		for (task t : tasks) {
			JSONObject taskJsonObject = new JSONObject();
			taskJsonObject.put("id", t.getId());
			taskJsonObject.put("name", t.getName());
			taskJsonObject.put("content", t.getContent());
			taskJsonList.add(taskJsonObject);
		}
		return taskJsonList;
	}

}
