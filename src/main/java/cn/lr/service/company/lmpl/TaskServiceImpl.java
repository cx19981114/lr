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
import cn.lr.util.SortUtil;

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
		JSONObject dataJson = this.testType(data);
		task task = new task();
		task.setCompanyId(data.getInteger("companyId"));
		task.setContent(data.getString("content"));
		task.setName(data.getString("name"));
		task.setPrevType(dataJson.getInteger("prevType"));
		task.setType(dataJson.getInteger("type"));
		task.setRank(data.getInteger("rank"));
		if (data.getInteger("step") == 1) {
			System.out.println(dataJson.getInteger("type"));
			task task2 = taskMapper.selectByCompanyAndPrevTypeAndTypeAndStep(data.getInteger("companyId"),
					dataJson.getInteger("prevType"), dataJson.getInteger("type"));
			if (task2 != null && task2.getState() != dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
				task2.setStep(0);
				int count = taskMapper.updateByPrimaryKeySelective(task2);
				if (count == 0) {
					throw new BusiException("去除上一个置顶任务失败");
				}
			}
		}
		task.setStep(data.getInteger("step"));
		task.setPostIdList(data.getString("postIdList")+"-");
		task.setEmployeeIdList(data.getString("employeeIdList"));
		task.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效",data.getInteger("companyId")));
		int count = taskMapper.insertSelective(task);
		if (count == 0) {
			throw new BusiException("添加task表失败");
		}
		// 根据职位添加任务 postIdList
		String postIdList = data.getString("postIdList");
		this.changePostList(postIdList, task.getId(), true, dataJson.getInteger("prevType"),
				dataJson.getInteger("type"),data.getInteger("companyId"));
		// 根据职员添加任务 employeeIdList
		String employeeIdList = data.getString("employeeIdList");
		this.changeEmployeeList(employeeIdList, task.getId(), true, dataJson.getInteger("prevType"),
				dataJson.getInteger("type"),data.getInteger("companyId"));
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
		List<dynamic> dynamics = dynamicMapper.selectByTypeAndIdVaild("任务", task.getId(),state);
		if(dynamics.size() != 0) {
			throw new BusiException("该任务已被申请无法修改");
		}
		if (data.getString("prevType") != null) {
			JSONObject dataJson = this.testType(data);
			task.setPrevType(dataJson.getInteger("prevType"));
			if (dataJson.getInteger("type") != 0) {
				task.setType(dataJson.getInteger("type"));
			}
		}
		if(data.getInteger("comapnyId") != null) {
			task .setCompanyId(data.getInteger("comapnyId"));
		}
		task.setContent(data.getString("content"));
		task.setName(data.getString("name"));
		task.setRank(data.getInteger("rank"));
		task.setStep(data.getInteger("step"));
		if(data.getInteger("step") != null) {
			if (oriStep == 0 && data.getInteger("step") == 1) {
				System.out.println("catch");
				task task2 = taskMapper.selectByCompanyAndPrevTypeAndTypeAndStep(task.getCompanyId(),
						task.getPrevType(), task.getType());
				if (task2 != null && task2.getState() != dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
					task2.setStep(0);
					int count = taskMapper.updateByPrimaryKeySelective(task2);
					if (count == 0) {
						throw new BusiException("去除上一个置顶任务失败");
					}
				}
			}
		}
		task.setPostIdList(data.getString("postIdList")+"-");
		task.setEmployeeIdList(data.getString("employeeIdList"));
		int count = taskMapper.updateByPrimaryKeySelective(task);
		if (count == 0) {
			throw new BusiException("更新task表失败");
		}
		task = taskMapper.selectByPrimaryKey(task.getId());
		// 根据职位添加任务 postIdList
		// 根据职员添加任务 employeeIdList
		// 修改任务优先级
		if (oriStep != task.getStep() || !oriEmployeeIdList.equals(task.getEmployeeIdList()) || !oriPostIdList.equals(task.getPostIdList())
				|| task.getType() != oriType || task.getPrevType() != oriPrevType) {
			this.changePostList(oriPostIdList, task.getId(), false, oriPrevType, oriType,data.getInteger("companyId"));
			this.changePostList(task.getPostIdList(), task.getId(), true, task.getPrevType(), task.getType(),data.getInteger("companyId"));
			this.changeEmployeeList(oriEmployeeIdList, task.getId(), false, oriPrevType, oriType,data.getInteger("companyId"));
			this.changeEmployeeList(task.getEmployeeIdList(), task.getId(), true, task.getPrevType(), task.getType(),data.getInteger("companyId"));
		}
		return task.getId();
	}

	@Override
	public void changeEmployeeList(String idList, Integer taskId,boolean isAdd, Integer prevType, Integer type,Integer companyId) {
		String[] list = idList.split("-");
		System.out.println(idList);
		for (int i = 0; i < list.length; i++) {
			employeeTask employeeTask = employeeTaskMapper.selectByEmployee(Integer.valueOf(list[i]));
			if (employeeTask == null) {
				throw new BusiException("employeeTask中不存在该职员");
			}
			if (isAdd) {
				if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "赋能思维",companyId)) {
					String taskListFN = taskId + "-" + (employeeTask.getTaskIdListFN() == null ? "" : employeeTask.getTaskIdListFN());
					String taskStateListFN = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请",companyId) + "-" + (employeeTask.getTaskIdListFNState() == null ? "" : employeeTask.getTaskIdListFNState());
					JSONObject data = this.sortTaskAndState(taskListFN, taskStateListFN);
					employeeTask.setTaskIdListFN(data.getString("idList"));
					employeeTask.setTaskIdListFNState(data.getString("stateList"));
				} else if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单",companyId)) {
					if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "日流程",companyId)) {
						String taskListRWDay = taskId + "-" + (employeeTask.getTaskIdListRWDay() == null ? "" : employeeTask.getTaskIdListRWDay());
						String taskStateListRWDay = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请",companyId) + "-" + (employeeTask.getTaskIdListRWDayState() == null ? "" : employeeTask.getTaskIdListRWDayState());
						JSONObject data = this.sortTaskAndState(taskListRWDay, taskStateListRWDay);
						employeeTask.setTaskIdListRWDay(data.getString("idList"));
						employeeTask.setTaskIdListRWDayState(data.getString("stateList"));
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "周安排",companyId)) {
						String taskListRWWeek = taskId + "-" + (employeeTask.getTaskIdListRWWeek() == null ? "" : employeeTask.getTaskIdListRWWeek());
						String taskStateListRWWeek = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请",companyId) + "-" + (employeeTask.getTaskIdListRWWeekState() == null ? "" : employeeTask.getTaskIdListRWWeekState());
						JSONObject data = this.sortTaskAndState(taskListRWWeek, taskStateListRWWeek);
						employeeTask.setTaskIdListRWWeek(data.getString("idList"));
						employeeTask.setTaskIdListRWWeekState(data.getString("stateList"));
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "月计划",companyId)) {
						String taskListRWMon = taskId + "-" + (employeeTask.getTaskIdListRWMon() == null ? "" : employeeTask.getTaskIdListRWMon());
						String taskStateListRWMon = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请",companyId) + "-" + (employeeTask.getTaskIdListRWMonState() == null ? "" : employeeTask.getTaskIdListRWMonState());
						JSONObject data = this.sortTaskAndState(taskListRWMon, taskStateListRWMon);
						employeeTask.setTaskIdListRWMon(data.getString("idList"));
						employeeTask.setTaskIdListRWMonState(data.getString("stateList"));
					}
				}
			} else {
				if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "赋能思维",companyId)) {
					String taskIdString = employeeTask.getTaskIdListFN();
					String taskStateString = employeeTask.getTaskIdListFNState();
					String taskIdStringD = "";
					String taskStateStringD = "";
					String[] taskIdList = taskIdString.split("-");
					String[] taskSatetList = taskStateString.split("-");
					for(int j = 0;j<taskIdList.length;j++) {
						if(Integer.valueOf(taskIdList[j]) == taskId) {
							continue;
						}
						taskIdStringD += taskIdList[j]+"-";
						taskStateStringD += taskSatetList[j]+"-";
					}
					employeeTask.setTaskIdListFN(taskIdStringD);
					employeeTask.setTaskIdListFNState(taskStateStringD);
				} else if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单",companyId)) {
					if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "日流程",companyId)) {
						String taskIdString = employeeTask.getTaskIdListRWDay();
						String taskStateString = employeeTask.getTaskIdListRWDayState();
						String taskIdStringD = "";
						String taskStateStringD = "";
						String[] taskIdList = taskIdString.split("-");
						String[] taskSatetList = taskStateString.split("-");
						for(int j = 0;j<taskIdList.length;j++) {
							if(Integer.valueOf(taskIdList[j]) == taskId) {
								continue;
							}
							taskIdStringD += taskIdList[j]+"-";
							taskStateStringD += taskSatetList[j]+"-";
						}
						employeeTask.setTaskIdListRWDay(taskIdStringD);
						employeeTask.setTaskIdListRWDayState(taskStateStringD);
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "周安排",companyId)) {
						String taskIdString = employeeTask.getTaskIdListRWWeek();
						String taskStateString = employeeTask.getTaskIdListRWWeekState();
						String taskIdStringD = "";
						String taskStateStringD = "";
						String[] taskIdList = taskIdString.split("-");
						String[] taskSatetList = taskStateString.split("-");
						for(int j = 0;j<taskIdList.length;j++) {
							if(Integer.valueOf(taskIdList[j]) == taskId) {
								continue;
							}
							taskIdStringD += taskIdList[j]+"-";
							taskStateStringD += taskSatetList[j]+"-";
						}
						employeeTask.setTaskIdListRWWeek(taskIdStringD);
						employeeTask.setTaskIdListRWWeekState(taskStateStringD);
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "月计划",companyId)) {
						String taskIdString = employeeTask.getTaskIdListRWMon();
						String taskStateString = employeeTask.getTaskIdListRWMonState();
						String taskIdStringD = "";
						String taskStateStringD = "";
						String[] taskIdList = taskIdString.split("-");
						String[] taskSatetList = taskStateString.split("-");
						for(int j = 0;j<taskIdList.length;j++) {
							if(Integer.valueOf(taskIdList[j]) == taskId) {
								continue;
							}
							taskIdStringD += taskIdList[j]+"-";
							taskStateStringD += taskSatetList[j]+"-";
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
	public void changePostList(String idList, Integer taskId, boolean isAdd, Integer prevType, Integer type,Integer companyId) {
		System.out.println(idList);
		String[] list = idList.split("-");
		for (int i = 0; i < list.length; i++) {
			postTask postTask = postTaskMapper.selectByPost(Integer.valueOf(list[i]));
			if (isAdd) {
				if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "赋能思维",companyId)) {
					postTask.setTaskIdListFN(postTask.getTaskIdListFN() == null ? String.valueOf(taskId + "-")
							: this.sortTask(taskId + "-" + postTask.getTaskIdListFN()));
				} else if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单",companyId)) {
					if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "日流程",companyId)) {
						postTask.setTaskIdListRWDay(postTask.getTaskIdListRWDay() == null ? String.valueOf(taskId + "-")
								: this.sortTask(taskId + "-" + postTask.getTaskIdListRWDay()));
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "周安排",companyId)) {
						postTask.setTaskIdListRWWeek(
								postTask.getTaskIdListRWWeek() == null ? String.valueOf(taskId + "-")
										: this.sortTask(taskId + "-" + postTask.getTaskIdListRWWeek()));
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "月计划",companyId)) {
						postTask.setTaskIdListRWMon(postTask.getTaskIdListRWMon() == null ? String.valueOf(taskId + "-")
								: this.sortTask(taskId + "-" + postTask.getTaskIdListRWMon()));
					}
				}
			} else {
				if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "赋能思维",companyId)) {
					postTask.setTaskIdListFN(postTask.getTaskIdListFN().replace(taskId + "-", ""));
				} else if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单",companyId)) {
					if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "日流程",companyId)) {
						postTask.setTaskIdListRWDay(postTask.getTaskIdListRWDay().replace(taskId + "-", ""));
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "周安排",companyId)) {
						postTask.setTaskIdListRWWeek(postTask.getTaskIdListRWWeek().replace(taskId + "-", ""));
					} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "月计划",companyId)) {
						postTask.setTaskIdListRWMon(postTask.getTaskIdListRWMon().replace(taskId + "-", ""));
					}
				}
			}
			int count_post = postTaskMapper.updateByPrimaryKeySelective(postTask);
			if (count_post == 0) {
				throw new BusiException("更新postTask表失败");
			}
		}
	}

	public String sortTask(String idList) {
		String[] taskId = idList.split("-");
		String sortTask = "";
		String frist = null;
		for (int i = 0; i < taskId.length; i++) {
			task task = taskMapper.selectByPrimaryKey(Integer.valueOf(taskId[i]));
			if (task.getStep() == 1) {
				frist = taskId[i];
				break;
			}
		}
		sortTask = SortUtil.sortFristNum(taskId);
		if(frist != null) {
			sortTask = sortTask.replace(frist + "-", "");
			sortTask = frist + "-" + sortTask;
		}
		return sortTask;
	}
	public JSONObject sortTaskAndState(String idList,String stateList) {
		String[] taskId = idList.split("-");
		String[] taskState = stateList.split("-");
		String sortTask = "";
		String sortTaskState = "";
		String fristId = null;
		String fristState = null;
		for (int i = 0; i < taskId.length; i++) {
			task task = taskMapper.selectByPrimaryKey(Integer.valueOf(taskId[i]));
			if (task.getStep() == 1) {
				fristId = taskId[i];
				fristState = taskState[i];
				break;
			}
		}
		JSONObject data = SortUtil.sortFristNumTogether(taskId,taskState);
		sortTask = data.getString("idList");
		sortTaskState =data.getString("stateList");
		if(fristId != null) {
			String[] sortTaskList = sortTask.split("-");
			String[] sortTaskStateList = sortTaskState.split("-");
			String sortTaskListD = "";
			String sortTaskStateListD = "";
			for(int i =0;i<sortTaskList.length;i++) {
				if(sortTaskList[i].equals(fristId)) {
					continue;
				}
				sortTaskListD += sortTaskList[i]+"-";
				sortTaskStateListD += sortTaskStateList[i]+"-";
			}
			sortTask = fristId + "-" + sortTaskListD;
			sortTaskState = fristState + "-" + sortTaskStateListD;
		}
		data.put("idList", sortTask);
		data.put("stateList", sortTaskState);
		return data;
	}

	@Override
	public Integer deleteTask(JSONObject data) {
		task task = taskMapper.selectByPrimaryKey(data.getInteger("taskId"));
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<dynamic> dynamics = dynamicMapper.selectByTypeAndIdVaild("任务", task.getId(),state);
		if(dynamics.size() != 0) {
			throw new BusiException("该任务已被申请无法删除");
		}
		String oriPostIdList = task.getPostIdList();
		String oriEmployeeIdList = task.getEmployeeIdList();
		task.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = taskMapper.updateByPrimaryKeySelective(task);
		if (count == 0) {
			throw new BusiException("更新task表失败");
		}
		this.changePostList(oriPostIdList, task.getId(), false, task.getPrevType(), task.getType(),data.getInteger("companyId"));
		this.changeEmployeeList(oriEmployeeIdList, task.getId(), false, task.getPrevType(), task.getType(),data.getInteger("companyId"));
		return task.getId();
	}

	@Override
	public task getTask(JSONObject data) {
		Integer taskId = data.getInteger("taskId");
		task task = taskMapper.selectByPrimaryKey(taskId);
		if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
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
				dictMapper.selectByCodeAndStateName(PRETASK_TYPE, data.getString("prevType"),data.getInteger("companyId")),
				data.getString("type").equals("") ? null: dictMapper.selectByCodeAndStateName(TASK_TYPE, data.getString("type"),data.getInteger("companyId")),
				state,(pageNum - 1) * PAGESIZE, PAGESIZE);
		int total = taskMapper.selectByCompanyCount(companyId,
				dictMapper.selectByCodeAndStateName(PRETASK_TYPE, data.getString("prevType"),data.getInteger("companyId")),
				data.getString("type").equals("") ? null
						: dictMapper.selectByCodeAndStateName(TASK_TYPE, data.getString("type"),data.getInteger("companyId")),state);
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
		int count1 = dictMapper.selectByCodeAndStateName(PRETASK_TYPE, prevType,data.getInteger("companyId"));
		int count2 = 0;
		if (count1 == 0) {
			throw new BusiException("任务类型错误");
		} else if (prevType.equals("任务清单")) {
			String type = data.getString("type");
			count2 = dictMapper.selectByCodeAndStateName(TASK_TYPE, type,data.getInteger("companyId"));
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
				if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
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

}
