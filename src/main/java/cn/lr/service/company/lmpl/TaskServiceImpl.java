package cn.lr.service.company.lmpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeTaskMapper;
import cn.lr.dao.postTaskMapper;
import cn.lr.dao.taskMapper;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.dynamic;
import cn.lr.po.employeeTask;
import cn.lr.po.postTask;
import cn.lr.po.task;
import cn.lr.service.company.TaskService;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

	@Autowired
	taskMapper taskMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	postTaskMapper postTaskMapper;
	@Autowired
	employeeTaskMapper employeeTaskMapper;
	@Autowired
	dynamicMapper dynamicMapper;

	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	@Value("${check.flow}")
	private String CHECK_FLOW;
	@Value("${prevTask.type}")
	private String PRETASK_TYPE;
	@Value("${task.type}")
	private String TASK_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;

	@Override
	public Integer addTask(JSONObject data) {
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		JSONObject dataJson = this.testType(data);
		task task = new task();
		task.setCompanyId(data.getInteger("companyId"));
		task.setContent(data.getString("content"));
		task.setName(data.getString("name"));
		task.setPrevType(dataJson.getInteger("prevType"));
		task.setType(dataJson.getInteger("type"));
		task.setRank(data.getInteger("rank"));
		List<task> tasks = taskMapper.selectByPostAndStep(data.getInteger("postIdList"),
				dataJson.getInteger("prevType"), dataJson.getInteger("type"), data.getInteger("step"),state);
		for (task t : tasks) {
			t.setStep(t.getStep() + 1);
			int count = taskMapper.updateByPrimaryKeySelective(t);
			if (count == 0) {
				throw new BusiException("修改任务顺序失败");
			}
		}
		task.setStep(data.getInteger("step"));
		task.setPostIdList(String.valueOf(data.getInteger("postIdList")));
		task.setEmployeeIdList(data.getString("employeeIdList"));
		task.setState(state);
		int count = taskMapper.insertSelective(task);
		if (count == 0) {
			throw new BusiException("添加task表失败");
		}
		// 根据职位添加任务 postIdList
		Integer postIdList = data.getInteger("postIdList");
		this.changePostList(postIdList, task.getId(), data.getInteger("step"), true, dataJson.getInteger("prevType"),
				dataJson.getInteger("type"), data.getInteger("companyId"));
		// 根据职员添加任务 employeeIdList
		String employeeIdList = data.getString("employeeIdList");
		this.changeEmployeeList(employeeIdList, task.getId(),
				dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请", data.getInteger("companyId")),
				data.getInteger("step"), true, dataJson.getInteger("prevType"), dataJson.getInteger("type"),
				data.getInteger("companyId"));
		return task.getId();
	}

	@Override
	public Integer modifyTask(JSONObject data) {
		task task = taskMapper.selectByPrimaryKey(data.getInteger("taskId"));
		String oriPostIdList = task.getPostIdList();
		String oriEmployeeIdList = task.getEmployeeIdList();
		Integer oriPrevType = task.getPrevType();
		Integer oriType = task.getType();
		Integer oriStep = task.getStep();
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<dynamic> dynamics = dynamicMapper.selectByTypeAndIdVaild("任务", task.getId(), state);
		if (dynamics.size() != 0) {
			throw new BusiException("该任务已被申请无法修改");
		}
		if (data.getString("prevType") != null) {
			JSONObject dataJson = this.testType(data);
			task.setPrevType(dataJson.getInteger("prevType"));
			if (dataJson.getInteger("type") != 0) {
				task.setType(dataJson.getInteger("type"));
			}
		}
		task.setCompanyId(data.getInteger("companyId"));
		task.setContent(data.getString("content"));
		task.setName(data.getString("name"));
		task.setRank(data.getInteger("rank"));
		task.setPostIdList(String.valueOf(data.getInteger("postIdList")));
		task.setEmployeeIdList(data.getString("employeeIdList"));
		int count = taskMapper.updateByPrimaryKeySelective(task);
		if (count == 0) {
			throw new BusiException("更新task表失败");
		}
		task = taskMapper.selectByPrimaryKey(task.getId());
		// 根据职位添加任务 postIdList
		// 根据职员添加任务 employeeIdList
		// 修改任务优先级
		if (oriStep != task.getStep() || !oriEmployeeIdList.equals(task.getEmployeeIdList())
				|| !oriPostIdList.equals(task.getPostIdList()) || task.getType() != oriType
				|| task.getPrevType() != oriPrevType) {
			List<task> tasks = taskMapper.selectByPostAndStep(Integer.valueOf(oriPostIdList),oriPrevType, oriType, oriStep,state);
			for (task t : tasks) {
				t.setStep(t.getStep() - 1);
				count = taskMapper.updateByPrimaryKeySelective(t);
				if (count == 0) {
					throw new BusiException("修改任务顺序失败");
				}
			}
			this.changePostList(Integer.valueOf(oriPostIdList), task.getId(),null, false, oriPrevType, oriType, data.getInteger("companyId"));
			this.changeEmployeeList(oriEmployeeIdList, task.getId(),null,null, false, oriPrevType, oriType,
					data.getInteger("companyId"));
			
			tasks = taskMapper.selectByPostAndStep(Integer.valueOf(task.getPostIdList()),task.getPrevType(), task.getType(), task.getStep(),state);
			for (task t : tasks) {
				t.setStep(t.getStep() + 1);
				count = taskMapper.updateByPrimaryKeySelective(t);
				if (count == 0) {
					throw new BusiException("修改任务顺序失败");
				}
			}
			task.setStep(data.getInteger("step"));
			this.changePostList(Integer.valueOf(task.getPostIdList()), task.getId(),data.getInteger("step"), true, task.getPrevType(), task.getType(),
					data.getInteger("companyId"));
			this.changeEmployeeList(task.getEmployeeIdList(), task.getId(),dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请", data.getInteger("companyId")),
					data.getInteger("step"),  true, task.getPrevType(), task.getType(),
					data.getInteger("companyId"));
		}
		return task.getId();
	}

	@Override
	public void changeEmployeeList(String idList, Integer taskId, Integer state, Integer step, boolean isAdd,
			Integer prevType, Integer type, Integer companyId) {
		String[] list = idList.split("-");
		System.out.println(idList);
		for (int i = 0; i < list.length; i++) {
			employeeTask employeeTask = employeeTaskMapper.selectByEmployee(Integer.valueOf(list[i]));
			if (employeeTask == null) {
				throw new BusiException("该任务中不存在该职员");
			}
			if (isAdd) {
				if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "赋能思维", companyId)) {
					JSONObject data = this.sortTaskAndState(employeeTask.getTaskIdListFN(),
							employeeTask.getTaskIdListFNState(), step, taskId, state);
					employeeTask.setTaskIdListFN(data.getString("idList"));
					employeeTask.setTaskIdListFNState(data.getString("stateList"));
				} else if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单", companyId)) {
					if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "日流程", companyId)) {
						JSONObject data = this.sortTaskAndState(employeeTask.getTaskIdListRWDay(),
								employeeTask.getTaskIdListRWDayState(), step, taskId, state);
						employeeTask.setTaskIdListRWDay(data.getString("idList"));
						employeeTask.setTaskIdListRWDayState(data.getString("stateList"));
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "周安排", companyId)) {
						JSONObject data = this.sortTaskAndState(employeeTask.getTaskIdListRWWeek(),
								employeeTask.getTaskIdListRWWeekState(), step, taskId, state);
						employeeTask.setTaskIdListRWWeek(data.getString("idList"));
						employeeTask.setTaskIdListRWWeekState(data.getString("stateList"));
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "月计划", companyId)) {
						JSONObject data = this.sortTaskAndState(employeeTask.getTaskIdListRWMon(),
								employeeTask.getTaskIdListRWMonState(), step, taskId, state);
						employeeTask.setTaskIdListRWMon(data.getString("idList"));
						employeeTask.setTaskIdListRWMonState(data.getString("stateList"));
					}
				}
			} else {
				if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "赋能思维", companyId)) {
					String taskIdString = employeeTask.getTaskIdListFN();
					String taskStateString = employeeTask.getTaskIdListFNState();
					String taskIdStringD = "";
					String taskStateStringD = "";
					String[] taskIdList = taskIdString.split("-");
					String[] taskSatetList = taskStateString.split("-");
					for (int j = 0; j < taskIdList.length; j++) {
						if (Integer.valueOf(taskIdList[j]) == taskId) {
							continue;
						}
						taskIdStringD += taskIdList[j] + "-";
						taskStateStringD += taskSatetList[j] + "-";
					}
					employeeTask.setTaskIdListFN(taskIdStringD);
					employeeTask.setTaskIdListFNState(taskStateStringD);
				} else if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单", companyId)) {
					if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "日流程", companyId)) {
						String taskIdString = employeeTask.getTaskIdListRWDay();
						String taskStateString = employeeTask.getTaskIdListRWDayState();
						String taskIdStringD = "";
						String taskStateStringD = "";
						String[] taskIdList = taskIdString.split("-");
						String[] taskSatetList = taskStateString.split("-");
						for (int j = 0; j < taskIdList.length; j++) {
							if (Integer.valueOf(taskIdList[j]) == taskId) {
								continue;
							}
							taskIdStringD += taskIdList[j] + "-";
							taskStateStringD += taskSatetList[j] + "-";
						}
						employeeTask.setTaskIdListRWDay(taskIdStringD);
						employeeTask.setTaskIdListRWDayState(taskStateStringD);
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "周安排", companyId)) {
						String taskIdString = employeeTask.getTaskIdListRWWeek();
						String taskStateString = employeeTask.getTaskIdListRWWeekState();
						String taskIdStringD = "";
						String taskStateStringD = "";
						String[] taskIdList = taskIdString.split("-");
						String[] taskSatetList = taskStateString.split("-");
						for (int j = 0; j < taskIdList.length; j++) {
							if (Integer.valueOf(taskIdList[j]) == taskId) {
								continue;
							}
							taskIdStringD += taskIdList[j] + "-";
							taskStateStringD += taskSatetList[j] + "-";
						}
						employeeTask.setTaskIdListRWWeek(taskIdStringD);
						employeeTask.setTaskIdListRWWeekState(taskStateStringD);
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "月计划", companyId)) {
						String taskIdString = employeeTask.getTaskIdListRWMon();
						String taskStateString = employeeTask.getTaskIdListRWMonState();
						String taskIdStringD = "";
						String taskStateStringD = "";
						String[] taskIdList = taskIdString.split("-");
						String[] taskSatetList = taskStateString.split("-");
						for (int j = 0; j < taskIdList.length; j++) {
							if (Integer.valueOf(taskIdList[j]) == taskId) {
								continue;
							}
							taskIdStringD += taskIdList[j] + "-";
							taskStateStringD += taskSatetList[j] + "-";
						}
						employeeTask.setTaskIdListRWMon(taskIdStringD);
						employeeTask.setTaskIdListRWMonState(taskStateStringD);
					}
				}
			}
			int count = employeeTaskMapper.updateByPrimaryKeySelective(employeeTask);
			if (count == 0) {
				throw new BusiException("更新employeeTask表失败");
			}
		}
	}

	@Override
	public void changePostList(Integer idList, Integer taskId, Integer step, boolean isAdd, Integer prevType,
			Integer type, Integer companyId) {
		System.out.println(idList);
		postTask postTask = postTaskMapper.selectByPost(Integer.valueOf(idList));
		if (isAdd) {
			if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "赋能思维", companyId)) {
				postTask.setTaskIdListFN(this.sortTask(postTask.getTaskIdListFN(), step, taskId));
			} else if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单", companyId)) {
				if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "日流程", companyId)) {
					postTask.setTaskIdListRWDay(this.sortTask(postTask.getTaskIdListRWDay(), step, taskId));
				} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "周安排", companyId)) {
					postTask.setTaskIdListRWWeek(this.sortTask(postTask.getTaskIdListRWWeek(), step, taskId));
				} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "月计划", companyId)) {
					postTask.setTaskIdListRWMon(this.sortTask(postTask.getTaskIdListRWMon(), step, taskId));
				}
			}
		} else {
			if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "赋能思维", companyId)) {
				postTask.setTaskIdListFN(postTask.getTaskIdListFN().replace(taskId + "-", ""));
			} else if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单", companyId)) {
				if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "日流程", companyId)) {
					postTask.setTaskIdListRWDay(postTask.getTaskIdListRWDay().replace(taskId + "-", ""));
				} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "周安排", companyId)) {
					postTask.setTaskIdListRWWeek(postTask.getTaskIdListRWWeek().replace(taskId + "-", ""));
				} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "月计划", companyId)) {
					postTask.setTaskIdListRWMon(postTask.getTaskIdListRWMon().replace(taskId + "-", ""));
				}
			}
		}
		int count_post = postTaskMapper.updateByPrimaryKeySelective(postTask);
		if (count_post == 0) {
			throw new BusiException("更新postTask表失败");
		}
	}

	public String sortTask(String idList, Integer step, Integer taskId) {
		if ("".equals(idList) || idList == null) {
			return taskId + "-";
		}
		String sortTask = "";
		String[] taskIdList = idList.split("-");
		for (int i = 0; i < taskIdList.length; i++) {
			if (i == step) {
				sortTask += taskId + "-";
			}
			sortTask += taskIdList[i] + "-";
		}
		return sortTask;
	}

	public JSONObject sortTaskAndState(String idList, String stateList, Integer step, Integer taskId,
			Integer taskState) {
		if ("".equals(idList) || idList == null) {
			JSONObject data = new JSONObject();
			data.put("idList", taskId + "-");
			data.put("stateList", taskState + "-");
			return data;
		}
		String[] taskIdList = idList.split("-");
		String[] taskStateList = stateList.split("-");
		String sortTask = "";
		String sortTaskState = "";
		for (int i = 0; i < taskIdList.length; i++) {
			if (i == step) {
				sortTask += taskId + "-";
				sortTaskState += taskState + "-";
			}
			sortTask += taskIdList[i] + "-";
			sortTaskState += taskStateList[i] + "-";
		}
		JSONObject data = new JSONObject();
		data.put("idList", sortTask);
		data.put("stateList", sortTaskState);
		return data;
	}

	@Override
	public Integer deleteTask(JSONObject data) {
		task task = taskMapper.selectByPrimaryKey(data.getInteger("taskId"));
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<dynamic> dynamics = dynamicMapper.selectByTypeAndIdVaild("任务", task.getId(), state);
		if (dynamics.size() != 0) {
			throw new BusiException("该任务已被申请无法删除");
		}
		String oriPostIdList = task.getPostIdList();
		String oriEmployeeIdList = task.getEmployeeIdList();
		task.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId")));
		int count = taskMapper.updateByPrimaryKeySelective(task);
		if (count == 0) {
			throw new BusiException("更新task表失败");
		}
		List<task> tasks = taskMapper.selectByPostAndStep(Integer.valueOf(task.getPostIdList()),
				task.getPrevType(), task.getType(), task.getStep(),state);
		for (task t : tasks) {
			t.setStep(t.getStep() - 1);
			count = taskMapper.updateByPrimaryKeySelective(t);
			if (count == 0) {
				throw new BusiException("修改任务顺序失败");
			}
		}
		this.changePostList(Integer.valueOf(oriPostIdList), task.getId(), null, false, task.getPrevType(), task.getType(),
				data.getInteger("companyId"));
		this.changeEmployeeList(oriEmployeeIdList, task.getId(), null, null, false, task.getPrevType(), task.getType(),
				data.getInteger("companyId"));
		return task.getId();
	}

	@Override
	public task getTask(JSONObject data) {
		Integer taskId = data.getInteger("taskId");
		task task = taskMapper.selectByPrimaryKey(taskId);
		if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
				data.getInteger("companyId"))) {
			throw new BusiException("该任务不存在");
		}
		return task;
	}

	@Override
	public Page<task> getTaskByCompany(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<task> tasks = taskMapper.selectByCompanyId(companyId,
				dictMapper.selectByCodeAndStateName(PRETASK_TYPE, data.getString("prevType"),
						data.getInteger("companyId")),
				data.getString("type").equals("") ? null
						: dictMapper.selectByCodeAndStateName(TASK_TYPE, data.getString("type"),
								data.getInteger("companyId")),
				state, (pageNum - 1) * PAGESIZE, PAGESIZE);
		int total = taskMapper.selectByCompanyCount(companyId,
				dictMapper.selectByCodeAndStateName(PRETASK_TYPE, data.getString("prevType"),
						data.getInteger("companyId")),
				data.getString("type").equals("") ? null
						: dictMapper.selectByCodeAndStateName(TASK_TYPE, data.getString("type"),
								data.getInteger("companyId")),
				state);
		Page<task> page = new Page<task>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(tasks);
		return page;
	}

	@Override
	public JSONObject testType(JSONObject data) {
		JSONObject dataJson = new JSONObject();
		String prevType = data.getString("prevType");
		int count1 = dictMapper.selectByCodeAndStateName(PRETASK_TYPE, prevType, data.getInteger("companyId"));
		int count2 = 0;
		if (count1 == 0) {
			throw new BusiException("任务类型错误");
		} else if (prevType.equals("任务清单")) {
			String type = data.getString("type");
			count2 = dictMapper.selectByCodeAndStateName(TASK_TYPE, type, data.getInteger("companyId"));
			if (count2 == 0) {
				throw new BusiException("普通任务分类错误");
			}
		}
		dataJson.put("prevType", count1);
		dataJson.put("type", count2);
		return dataJson;
	}

	@Override
	public Page<task> getTaskByPost(JSONObject data) {
		Integer pageNum = data.getInteger("pageNum");
		postTask postTask = postTaskMapper.selectByPost(data.getInteger("postId"));
		String taskIdList = null;
		if ("赋能思维".equals(data.getString("prevType"))) {
			taskIdList = postTask.getTaskIdListFN();
		} else if ("任务清单".equals(data.getString("prevType"))) {
			if ("日流程".equals(data.getString("type"))) {
				taskIdList = postTask.getTaskIdListRWDay();
			} else if ("周安排".equals(data.getString("type"))) {
				taskIdList = postTask.getTaskIdListRWWeek();
			} else if ("月计划".equals(data.getString("type"))) {
				taskIdList = postTask.getTaskIdListRWMon();
			}
		}
		String[] taskId = taskIdList.split("-");
		System.out.println(taskId.length);
		List<task> tasks = new ArrayList<task>();
		int total = 0;
		if (!taskId[0].equals("")) {
			if (taskId.length > (pageNum - 1) * PAGESIZE) {
				total = PAGESIZE;
			} else {
				total = taskId.length - (pageNum - 2) * PAGESIZE;
			}
			for (int i = (pageNum - 1) * PAGESIZE; i < total; i++) {
				task task = taskMapper.selectByPrimaryKey(Integer.valueOf(taskId[i]));
				if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
						data.getInteger("companyId"))) {
					throw new BusiException("数据有误，有任务不存在");
				}
				tasks.add(task);
			}
		}
		Page<task> page = new Page<task>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(taskId.length);
		page.setList(tasks);
		return page;
	}
	@Override
	public Integer maxStep(JSONObject data) {
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		JSONObject dataJson = this.testType(data);
		return taskMapper.selectByPostAndStepMax(data.getInteger("postId"), dataJson.getInteger("prevType"),
				dataJson.getInteger("type"), state);
	}
}
