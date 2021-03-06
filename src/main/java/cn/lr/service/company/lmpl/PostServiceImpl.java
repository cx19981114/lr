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
import cn.lr.po.leader;
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
	@Value("${noImg}")
	private String NOIMG;

	@Override
	public Integer addPost(JSONObject data) {
		post record = new post();
		record.setCompanyId(data.getInteger("companyId"));
		record.setName(data.getString("name"));
		record.setPermissionList(data.getString("permissionList"));
		if(data.getString("pic") != null) {
			record.setPic(data.getString("pic"));
		}else {
			record.setPic(NOIMG);
		}
		record.setLeaderPostId(data.getString("leaderPostId"));
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
		test2.setLeaderPostId(data.getString("leaderPostId"));
		int count = postMapper.updateByPrimaryKeySelective(test2);
		if (count == 0) {
			throw new BusiException("更新post表失败");
		}
		return test2.getId();
	}

	@Override
	public Integer deletePost(JSONObject data) {
		post test2 = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		if(test2.getLeaderPostId().equals("0")) {
			throw new BusiException("经营者无法删除");
		}
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
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateWSX);
		List<post> posts = postMapper.selectByCompany(companyId, stateList, (pageNum - 1) * PAGESIZE, PAGESIZE);
		int total = postMapper.selectByCompanyCount(companyId, stateList);
		Page<post> page = new Page<post>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(posts);
		return page;
	}
	@Override
	public Page<PostDTO> getPostList(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer pageNum = data.getInteger("pageNum");
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateWSX);
		List<PostDTO> postDTOs = new ArrayList<PostDTO>();
		List<post> posts = postMapper.selectByCompany(companyId, stateList, (pageNum - 1) * PAGESIZE, PAGESIZE);
		for(post p:posts) {
			postDTOs.add(this.sePostDTO(p));
		}
		int total = postMapper.selectByCompanyCount(companyId, stateList);
		Page<PostDTO> page = new Page<PostDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(postDTOs);
		return page;
	}
	@Override
	public List<JSONObject> getPostTypeAndCount(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateWSX);
		List<post> posts = postMapper.selectByCompany(companyId, stateList, 0, Integer.MAX_VALUE);
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
		ArrayList<leader> leaderList = new ArrayList<leader>();
		if(!post.getLeaderPostId().equals("0")) {
			String[] postList = post.getLeaderPostId().split("-");
			for(int i = 0;i<postList.length;i++) {
				post post2 = postMapper.selectByPrimaryKey(Integer.valueOf(postList[i]));
//				if (!post.getName().contains("店长")) {
				leader leader = new leader();
				leader.setLeaderPostId(post2.getId());
				leader.setLeaderPostName(post2.getName());
				leaderList.add(leader);
//				}
			}
		}
		postDTO.setLeaderPost(leaderList);
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
			taskJsonObject.put("step", t.getStep());
			taskJsonList.add(taskJsonObject);
		}
		return taskJsonList;
	}

}
