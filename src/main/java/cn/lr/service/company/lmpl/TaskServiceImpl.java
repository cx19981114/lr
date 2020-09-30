package cn.lr.service.company.lmpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.companyMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.employeeTaskMapper;
import cn.lr.dao.postMapper;
import cn.lr.dao.postTaskMapper;
import cn.lr.dao.taskMapper;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.company;
import cn.lr.po.dynamic;
import cn.lr.po.employee;
import cn.lr.po.post;
import cn.lr.po.postTask;
import cn.lr.po.task;
import cn.lr.service.company.TaskService;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

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
	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	companyMapper companyMapper;
	@Autowired
	postMapper postMapper;

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
	@Value("${picActPath}")
	private String ACTPATH;

	@Override
	public Integer addTask(JSONObject data) {
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		JSONObject dataJson = this.testType(data);
		task task = new task();
		task.setCompanyId(data.getInteger("companyId"));
		task.setContent(data.getString("content"));
		task.setName(data.getString("name"));
		task.setPrevType(dataJson.getInteger("prevType"));
		task.setType(dataJson.getInteger("type"));
		task.setRank(data.getInteger("rank"));
		stateList.clear();
		stateList.add(stateWSX);
		List<task> tasks = taskMapper.selectByPostAndStep(data.getInteger("postIdList"),
				dataJson.getInteger("prevType"), dataJson.getInteger("type"), data.getInteger("step"), stateList);
		for (task t : tasks) {
			t.setStep(t.getStep() + 1);
			int count = taskMapper.updateByPrimaryKeySelective(t);
			if (count == 0) {
				throw new BusiException("修改任务顺序失败");
			}
		}
		task.setStep(data.getInteger("step"));
		task.setPostIdList(String.valueOf(data.getInteger("postIdList")));
		task.setState(stateWSX);
		int count = taskMapper.insertSelective(task);
		if (count == 0) {
			throw new BusiException("添加task表失败");
		}
		// 根据职位添加任务 postIdList
		Integer postIdList = data.getInteger("postIdList");
		this.changePostList(postIdList, task.getId(), data.getInteger("step"), true, dataJson.getInteger("prevType"),
				dataJson.getInteger("type"), data.getInteger("companyId"));
		return task.getId();
	}

	@Override
	public Integer modifyTask(JSONObject data) {
		task task = taskMapper.selectByPrimaryKey(data.getInteger("taskId"));
		String oriPostIdList = task.getPostIdList();
		Integer oriPrevType = task.getPrevType();
		Integer oriType = task.getType();
		Integer oriStep = task.getStep();
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		List<dynamic> dynamics = dynamicMapper.selectByTypeAndIdVaild("任务", task.getId(), stateList);
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
		if (data.getInteger("postIdList") != null) {
			task.setPostIdList(String.valueOf(data.getInteger("postIdList")));
			int count = taskMapper.updateByPrimaryKeySelective(task);
			if (count == 0) {
				throw new BusiException("更新task表失败");
			}
		}
		// 根据职位添加任务 postIdList
		// 修改任务优先级
		if ((data.getInteger("step") != null && oriStep != data.getInteger("step"))
				|| !oriPostIdList.equals(task.getPostIdList())
				|| task.getType() != oriType || task.getPrevType() != oriPrevType) {
			stateList.clear();
			stateList.add(stateWSX);
			List<task> tasks = taskMapper.selectByPostAndStep(Integer.valueOf(oriPostIdList), oriPrevType, oriType,
					oriStep, stateList);
			for (task t : tasks) {
				t.setStep(t.getStep() - 1);
				int count = taskMapper.updateByPrimaryKeySelective(t);
				if (count == 0) {
					throw new BusiException("修改任务顺序失败");
				}
			}
			this.changePostList(Integer.valueOf(oriPostIdList), task.getId(), null, false, oriPrevType, oriType,
					data.getInteger("companyId"));
			stateList.clear();
			stateList.add(stateWSX);
			tasks = taskMapper.selectByPostAndStep(Integer.valueOf(task.getPostIdList()), task.getPrevType(),
					task.getType(), data.getInteger("step"), stateList);
			for (task t : tasks) {
				t.setStep(t.getStep() + 1);
				int count = taskMapper.updateByPrimaryKeySelective(t);
				if (count == 0) {
					throw new BusiException("修改任务顺序失败");
				}
			}
			task.setStep(data.getInteger("step"));
			int count = taskMapper.updateByPrimaryKeySelective(task);
			if (count == 0) {
				throw new BusiException("修改任务顺序失败");
			}
			this.changePostList(Integer.valueOf(task.getPostIdList()), task.getId(), task.getStep(), true,
					task.getPrevType(), task.getType(), data.getInteger("companyId"));
		}
		return task.getId();
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
		int flag = 0;
		for (int i = 0; i < taskIdList.length; i++) {
			if (i == step - 1) {
				sortTask += taskId + "-";
				flag = 1;
			}
			sortTask += taskIdList[i] + "-";
		}
		if (flag == 0) {
			sortTask += taskId + "-";
		}
		return sortTask;
	}

	@Override
	public Integer deleteTask(JSONObject data) {
		task task = taskMapper.selectByPrimaryKey(data.getInteger("taskId"));
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		List<dynamic> dynamics = dynamicMapper.selectByTypeAndIdVaild("任务", task.getId(), stateList);
		if (dynamics.size() != 0) {
			throw new BusiException("该任务已被申请无法删除");
		}
		String oriPostIdList = task.getPostIdList();
		task.setState(stateYSX);
		int count = taskMapper.updateByPrimaryKeySelective(task);
		if (count == 0) {
			throw new BusiException("更新task表失败");
		}
		stateList.clear();
		stateList.add(stateWSX);
		List<task> tasks = taskMapper.selectByPostAndStep(Integer.valueOf(task.getPostIdList()), task.getPrevType(),
				task.getType(), task.getStep(), stateList);
		for (task t : tasks) {
			t.setStep(t.getStep() - 1);
			count = taskMapper.updateByPrimaryKeySelective(t);
			if (count == 0) {
				throw new BusiException("修改任务顺序失败");
			}
		}
		this.changePostList(Integer.valueOf(oriPostIdList), task.getId(), null, false, task.getPrevType(),
				task.getType(), data.getInteger("companyId"));
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
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateWSX);
		List<task> tasks = taskMapper.selectByCompanyId(companyId,
				dictMapper.selectByCodeAndStateName(PRETASK_TYPE, data.getString("prevType"),
						data.getInteger("companyId")),
				data.getString("type").equals("") ? null
						: dictMapper.selectByCodeAndStateName(TASK_TYPE, data.getString("type"),
								data.getInteger("companyId")),
				stateList, (pageNum - 1) * PAGESIZE, PAGESIZE);
		int total = taskMapper.selectByCompanyCount(companyId,
				dictMapper.selectByCodeAndStateName(PRETASK_TYPE, data.getString("prevType"),
						data.getInteger("companyId")),
				data.getString("type").equals("") ? null
						: dictMapper.selectByCodeAndStateName(TASK_TYPE, data.getString("type"),
								data.getInteger("companyId")),
				stateList);
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
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		System.out.println(taskId.length);
		List<task> tasks = new ArrayList<task>();
		int total = taskId.length;
		if (!taskId[0].equals("")) {
			if (taskId.length > (pageNum - 1) * PAGESIZE)
				total = Integer.min(taskId.length, pageNum * PAGESIZE);
			for (int i = (pageNum - 1) * PAGESIZE; i < total; i++) {
				task task = taskMapper.selectByPrimaryKey(Integer.valueOf(taskId[i]));
				if (task == null || task.getState() == state) {
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
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateWSX);
		JSONObject dataJson = this.testType(data);
		Integer numInteger = taskMapper.selectByPostAndStepMax(data.getInteger("postId"),
				dataJson.getInteger("prevType"), dataJson.getInteger("type"), stateList);
		if (numInteger == null) {
			return 1;
		}
		return numInteger + 1;
	}

	public void AddTaskByExcel(JSONObject data) throws Exception {
		Workbook rwb = Workbook.getWorkbook(new File(ACTPATH + data.getString("file")));
//		Workbook rwb = Workbook.getWorkbook(new File("C:\\Users\\23847\\eclipse-workspace\\lr\\upload"+data.getString("file")));
		Sheet rs = rwb.getSheet("task");// 或者rwb.getSheet(0)
		int clos = rs.getColumns();// 得到所有的列
		int rows = rs.getRows();// 得到所有的行
		System.out.println(clos + " rows:" + rows);
		String companyName = rs.getCell(1, 0).getContents();
		company company = new company();
		company.setName(companyName);
		company = companyMapper.selectByName(company);
		if(company == null) {
			throw new BusiException("公司名称输入错误");
		}
		for (int i = 1; i < rows; i++) {
			for (int j = 0; j < clos; j++) {
				String total = rs.getCell(j++, i).getContents();
				if("岗位名称".equals(total)) {
					String postName = rs.getCell(j++, i).getContents();
					Integer post = postMapper.selectByNameAndCompany(postName, company.getId(), null);
					if(post == null) {
						throw new BusiException("岗位名称输入格式错误");
					}
					i++;
					for (int p = i; p < rows; p++,i++) {
						if(rs.getCell(0, i).getContents().equals("")) {
							break;
						}
						for (int q = 0; q < clos; q++) {
							String step = rs.getCell(q++, p).getContents();
							String prevType = rs.getCell(q++, p).getContents();
							String type = rs.getCell(q++, p).getContents();
							String name = rs.getCell(q++, p).getContents();
							String content = rs.getCell(q++, p).getContents();
							String rank = rs.getCell(q++, p).getContents();
							JSONObject dataJsonObject = new JSONObject();
							dataJsonObject.put("companyId", company.getId());
							dataJsonObject.put("postIdList", post);
							dataJsonObject.put("step", Integer.valueOf(step));
							dataJsonObject.put("prevType", prevType);
							dataJsonObject.put("type", type);
							dataJsonObject.put("name", name);
							dataJsonObject.put("content", content);
							dataJsonObject.put("rank", Integer.valueOf(rank));
							this.addTask(dataJsonObject);
						}
						
					}
				}
			}
		}
	}

}
