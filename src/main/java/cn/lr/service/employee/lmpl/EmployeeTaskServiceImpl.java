package cn.lr.service.employee.lmpl;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.applyRankMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.employeeTaskMapper;
import cn.lr.dao.postTaskMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dao.taskMapper;
import cn.lr.dto.EmployeeTaskDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.applyRank;
import cn.lr.po.dynamic;
import cn.lr.po.employee;
import cn.lr.po.employeeTask;
import cn.lr.po.postTask;
import cn.lr.po.task;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.service.employee.EmployeeRankService;
import cn.lr.service.employee.EmployeeTaskService;
import cn.lr.util.TimeFormatUtil;

@Service
@Transactional
public class EmployeeTaskServiceImpl implements EmployeeTaskService {
	public static final String Type = "任务";

	@Autowired
	dictMapper dictMapper;
	@Autowired
	employeeTaskMapper employeeTaskMapper;
	@Autowired
	taskMapper taskMapper;
	@Autowired
	rankMapper rankMapper;
	@Autowired
	applyRankMapper applyRankMapper;
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	postTaskMapper postTaskMapper;
	@Autowired
	employeeMapper employeeMapper;

	@Autowired
	ApplyRankService ApplyRankService;
	@Autowired
	DynamicService DynamicService;
	@Autowired
	EmployeeRankService EmployeeRankService;

	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	@Value("${check.flow}")
	private String CHECK_FLOW;
	@Value("${prevTask.type}")
	private String PRETASK_TYPE;
	@Value("${task.type}")
	private String TASK_TYPE;
	@Value("${picPath}")
	private String PATH;
	@Value("${picActPath}")
	private String ACTPATH;

	@Override
	public Page<task> getTaskByEmployee(JSONObject data) {
		employee employee = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId"));
		postTask postTask = postTaskMapper.selectByPost(employee.getPostId());
		Integer pageNum = data.getInteger("pageNum");
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
		int total = tasks.size();
		if (total > pageNum * PAGESIZE) {
			total = pageNum * PAGESIZE;
		}
		List<task> taskPart = new ArrayList<task>();
		for (int i = (pageNum - 1) * PAGESIZE; i < total; i++) {
			taskPart.add(tasks.get(i));
		}
		Page<task> page = new Page<task>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(tasks.size());
		page.setList(taskPart);
		return page;
	}

	@Override
	public List<JSONObject> getTaskByEmployeeList(JSONObject data) {
		employee employee = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId"));
		postTask postTask = postTaskMapper.selectByPost(employee.getPostId());
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

	@Override
	public Integer addEmployeeTask(JSONObject data) {
		task task = taskMapper.selectByPrimaryKey(data.getInteger("taskId"));
		if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
				data.getInteger("companyId"))) {
			throw new BusiException("该任务不存在");
		}
//		if(!task.getEmployeeIdList().contains(data.getInteger("employeeId")+"-")){
//			throw new BusiException("该任务不属于该职员");
//		}
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		Integer stateWSQ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请",data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateWSQ);
		stateList.add(stateCG);
		stateList.add(stateSHZ);
		stateList.add(stateWSH);
		stateList.add(stateWTJ);
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		
		employeeTask employeeTask = new employeeTask();
		employeeTask.setDateTime(now);
		employeeTask.setEmployeeId(data.getInteger("employeeId"));
		employeeTask.setNote(data.getString("note"));
		employeeTask.setState(stateWSH);
		employeeTask.setTaskId(data.getInteger("taskId"));
		List<Object> pics = data.getJSONArray("pic");
		if (pics != null) {
			String picString = "";
			for (Object o : pics) {
				picString += o.toString() + "-";
			}
			employeeTask.setPics(picString);
		}
		int count = employeeTaskMapper.insertSelective(employeeTask);
		if(count == 0) {
			throw new BusiException("插入employeeTask表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("note"));
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeTask.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		stateList.clear();
		stateList.add(stateWSX);
		dataJSonDynamic.put("rank",task.getRank());
		int dynamicId = DynamicService.writeDynamic(dataJSonDynamic);
		
		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("employeeId", data.getInteger("employeeId"));
		dataJSonApply.put("dynamicId", dynamicId);
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		ApplyRankService.addApplyRank(dataJSonApply);
		
		
		JSONObject dataJSonRank = new JSONObject();
		dataJSonRank.put("companyId", data.getInteger("companyId"));
		dataJSonRank.put("dynamicId", dynamicId);
		dataJSonRank.put("time", now);
		EmployeeRankService.addEmployeeRank(dataJSonRank);
		
		employeeTask = employeeTaskMapper.selectByPrimaryKey(employeeTask.getId());
		if(employeeTask.getState() != stateCG) {
			data.put("employeeTaskId", employeeTask.getId());
			this.affirmEmployeeTask(data);
		}
		return employeeTask.getId();
	}

	@Override
	public Integer modifyEmployeeTask(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeTask employeeTask = employeeTaskMapper.selectByPrimaryKey(data.getInteger("employeeTaskId"));
		if (employeeTask.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该任务不能修改");
		}
		employeeTask.setNote(data.getString("note"));
		List<Object> pics = data.getJSONArray("pic");
		if (pics != null) {
			String picString = "";
			for (Object o : pics) {
				picString += o.toString() + "-";
			}
			employeeTask.setPics(picString);
		}
		int count = employeeTaskMapper.updateByPrimaryKeySelective(employeeTask);
		if(count == 0) {
			throw new BusiException("更新employeeTask表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeTask.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("time", now);
		dataJSonDynamic.put("dynamicId",  dynamic.getId());
		count = DynamicService.modifyDynamic(dataJSonDynamic);
		if(count == 0) {
			throw new BusiException("更新dynamic表失败");
		}
		return employeeTask.getId();
	}

	@Override
	public Integer deleteEmployeeTask(JSONObject data) {
		employeeTask employeeTask = employeeTaskMapper.selectByPrimaryKey(data.getInteger("employeeTaskId"));
		if(employeeTask.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该任务已提交无法删除");
		}
		employeeTask.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = employeeTaskMapper.updateByPrimaryKeySelective(employeeTask);
		if (count == 0) {
			throw new BusiException("更新employeeLogTomorrow表失败");
		}

		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeTask.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.deleteDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		dataJSonApply.put("dynamicId", dynamic.getId());
		ApplyRankService.deleteApplyRank(dataJSonApply);

		JSONObject dataJSonRank = new JSONObject();
		dataJSonRank.put("companyId", data.getInteger("companyId"));
		dataJSonRank.put("dynamicId", dynamic.getId());
		EmployeeRankService.deleteEmployeeRank(dataJSonRank);
		return employeeTask.getId();
	}

	@Override
	public Integer annulEmployeeTask(JSONObject data) {
		employeeTask employeeTask = employeeTaskMapper.selectByPrimaryKey(data.getInteger("employeeTaskId"));
		employeeTask.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")));
		int count = employeeTaskMapper.updateByPrimaryKeySelective(employeeTask);
		if (count == 0) {
			throw new BusiException("更新employeeTask表失败");
		}
		
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeTask.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.annulDynamic(dataJSonDynamic);
		
		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		dataJSonApply.put("dynamicId", dynamic.getId());
		ApplyRankService.annulApplyRank(dataJSonApply);
		
		JSONObject dataJSonRank = new JSONObject();
		dataJSonRank.put("companyId", data.getInteger("companyId"));
		dataJSonRank.put("dynamicId", dynamic.getId());
		EmployeeRankService.deleteEmployeeRank(dataJSonRank);
		return employeeTask.getId();
	}

	public Integer affirmEmployeeTask(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeTask employeeTask = employeeTaskMapper.selectByPrimaryKey(data.getInteger("employeeTaskId"));
		if (employeeTask.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该任务无法确认提交");
		}
		employeeTask.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		int count = employeeTaskMapper.updateByPrimaryKeySelective(employeeTask);
		if (count == 0) {
			throw new BusiException("更新employeeTask表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeTask.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dynamic.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		dynamic.setDateTime(now);
		count = dynamicMapper.updateByPrimaryKeySelective(dynamic);
		if (count == 0) {
			throw new BusiException("更新dynamic表失败");
		}
		applyRank applyRank = applyRankMapper.selectByDynamic(dynamic.getId());
		applyRank.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		applyRank.setStartTime(now);
		String[] checkTimeList = applyRank.getCheckTimeList().split("--");
		String checkTime = "";
		for(int i=0;i<checkTimeList.length;i++) {
			checkTime += now+"--";
		}
		applyRank.setCheckTimeList(checkTime);
		count = applyRankMapper.updateByPrimaryKeySelective(applyRank);
		if (count == 0) {
			throw new BusiException("更新applyRank表失败");
		}
		return employeeTask.getId();
	}

	@Override
	public JSONObject getEmployeeTask(JSONObject data) throws ParseException {
		employeeTask employeeTask = employeeTaskMapper.selectByPrimaryKey(data.getInteger("employeeTaskId"));
		if (employeeTask == null || employeeTask.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该employeeTaskId不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeTask.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());

		JSONObject dataJson = new JSONObject();
		dataJson.put("employeeTask", this.sEmployeeTaskDTO(employeeTask));
		dataJson.put("dynamicId", dynamic.getId());
		dataJson.put("ApplyRankDTO", ApplyRankService.getApplyRankByDynamic(dataJSonDynamic));
		return dataJson;
	}

	public EmployeeTaskDTO sEmployeeTaskDTO(employeeTask employeeTask) throws ParseException {
		task task = taskMapper.selectByPrimaryKey(employeeTask.getTaskId());
		employee employee = employeeMapper.selectByPrimaryKey(employeeTask.getEmployeeId());
		EmployeeTaskDTO employeeTaskDTO = new EmployeeTaskDTO();
		employeeTaskDTO.setContent(task.getContent());
		employeeTaskDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(employeeTask.getDateTime()));
		employeeTaskDTO.setId(employeeTask.getId());
		employeeTaskDTO.setNote(employeeTask.getNote());
		employeeTaskDTO.setTaskId(employeeTask.getTaskId());
		String pics = employeeTask.getPics();
		JSONObject dataJson = new JSONObject();
		List<JSONObject> fileListJSON = new ArrayList<JSONObject>();
		List<String> urlList = new ArrayList<String>();
		if (pics != null && !"".equals(pics)) {
			String[] picList = pics.split("-");
			for (int i = 0; i < picList.length; i++) {
				File picture = new File(ACTPATH + picList[i]);
				JSONObject files = new JSONObject();
				JSONObject file = new JSONObject();
				file.put("path", PATH + picList[i]);
				file.put("size", picture.length());
				files.put("file", file);
				files.put("url", PATH + picList[i]);
				urlList.add(picList[i]);
				fileListJSON.add(files);
			}
			dataJson.put("files", fileListJSON);
			dataJson.put("uploadImg", urlList);
			employeeTaskDTO.setPics(dataJson);
		}
		employeeTaskDTO.setPrevType(dictMapper.selectByCodeAndStateCode(PRETASK_TYPE, task.getPrevType(), employee.getCompanyId()));
		employeeTaskDTO.setType(dictMapper.selectByCodeAndStateCode(TASK_TYPE, task.getType(), employee.getCompanyId()));
		employeeTaskDTO.setName(task.getName());
		employeeTaskDTO.setRank(task.getRank());
		employeeTaskDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, employeeTask.getState(), employee.getCompanyId()));
		employeeTaskDTO.setStep(task.getStep());
		return employeeTaskDTO;
	}

	public Page<EmployeeTaskDTO> getEmployeeTaskByEmployee(JSONObject data) throws ParseException {
		Integer pageNum = data.getInteger("pageNum");
		Integer employeeId = data.getInteger("employeeId");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(state);
		List<employeeTask> employeeTasks = employeeTaskMapper.selectByEmployeeId(employeeId,stateList, (pageNum - 1) * PAGESIZE,
				PAGESIZE);
		List<EmployeeTaskDTO> jsonObjects = new ArrayList<EmployeeTaskDTO>();
		for (employeeTask e : employeeTasks) {
			EmployeeTaskDTO employeeTaskDTO = this.sEmployeeTaskDTO(e);
			jsonObjects.add(employeeTaskDTO);
		}
		int total = employeeTaskMapper.selectByEmployeeIdCount(employeeId,stateList);
		Page<EmployeeTaskDTO> page = new Page<EmployeeTaskDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}
}
