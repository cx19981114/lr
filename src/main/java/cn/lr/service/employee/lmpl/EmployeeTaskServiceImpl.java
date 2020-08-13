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
import cn.lr.dao.employeeTaskMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dao.taskMapper;
import cn.lr.dto.EmployeeTaskDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.applyRank;
import cn.lr.po.dynamic;
import cn.lr.po.employeeTask;
import cn.lr.po.task;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
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
	ApplyRankService ApplyRankService;
	@Autowired
	DynamicService DynamicService;

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
	@Value("${path}")
	private String PATH;

	@Override
	public Page<task> getTaskByEmployee(JSONObject data) {
		employeeTask employeeTask = employeeTaskMapper.selectByEmployee(data.getInteger("employeeId"));
		Integer pageNum = data.getInteger("pageNum");
		String taskList = null;
		String taskStateList = null;
		if ("赋能思维".equals(data.getString("prevType"))) {
			taskList = employeeTask.getTaskIdListFN();
			taskStateList = employeeTask.getTaskIdListFNState();
		} else if ("任务清单".equals(data.getString("prevType"))) {
			if ("日流程".equals(data.getString("type"))) {
				taskList = employeeTask.getTaskIdListRWDay();
				taskStateList = employeeTask.getTaskIdListRWDayState();
			} else if ("周安排".equals(data.getString("type"))) {
				taskList = employeeTask.getTaskIdListRWWeek();
				taskStateList = employeeTask.getTaskIdListRWWeekState();
			} else if ("月计划".equals(data.getString("type"))) {
				taskList = employeeTask.getTaskIdListRWMon();
				taskStateList = employeeTask.getTaskIdListRWMonState();
			}
		}
		List<task> tasks = new ArrayList<task>();
		if (taskList != null && !("".equals(taskList))) {
			String[] taskId = taskList.split("-");
			String[] taskState = taskStateList.split("-");
			for (int i = 0; i < taskId.length; i++) {
				task task = taskMapper.selectByPrimaryKey(Integer.valueOf(taskId[i]));
				if (Integer.valueOf(taskState[i]) == dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请",data.getInteger("companyId"))) {
					tasks.add(task);
				}
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
	public Integer addEmployeeTask(JSONObject data) {
		task task = taskMapper.selectByPrimaryKey(data.getInteger("taskId"));
		if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该taskId不存在");
		}
		employeeTask employeeTask = employeeTaskMapper.selectByEmployee(data.getInteger("employeeId"));
		String taskList = "";
		String taskStateList = "";
		Integer prevType = task.getPrevType();
		if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "赋能思维",data.getInteger("companyId"))) {
			taskList = employeeTask.getTaskIdListFN();
			taskStateList = employeeTask.getTaskIdListFNState();
		} else if (prevType == dictMapper.selectByCodeAndStateName(PRETASK_TYPE, "任务清单",data.getInteger("companyId"))) {
			Integer type = task.getType();
			if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "日流程",data.getInteger("companyId"))) {
				taskList = employeeTask.getTaskIdListRWDay();
				taskStateList = employeeTask.getTaskIdListRWDayState();
			} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "周安排",data.getInteger("companyId"))) {
				taskList = employeeTask.getTaskIdListRWWeek();
				taskStateList = employeeTask.getTaskIdListRWWeekState();
			} else if (type == dictMapper.selectByCodeAndStateName(TASK_TYPE, "月计划",data.getInteger("companyId"))) {
				taskList = employeeTask.getTaskIdListRWMon();
				taskStateList = employeeTask.getTaskIdListRWMonState();
			}
		}
		System.out.println(taskList);
		if (taskList.indexOf(task.getId() + "-") == -1) {
			throw new BusiException("该任务不属于该职员");
		}
		String[] taskId = taskList.split("-");
		String[] taskState = taskStateList.split("-");
		for (int i = 0; i < taskId.length; i++) {
			if (Integer.valueOf(taskId[i]) == task.getId()) {
				if (Integer.valueOf(taskState[i]) != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请",data.getInteger("companyId"))
						&& Integer.valueOf(taskState[i]) != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId"))
						&& Integer.valueOf(taskState[i]) != dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
					throw new BusiException("该任务无法申请");
				}
			}
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("note"));
		dataJSonDynamic.put("name", Type);
		List<Object> pics = data.getJSONArray("pic");
		if(pics != null) {
			String picString = "";
			for (Object o : pics) {
				picString += o.toString() + "-";
			}
			dataJSonDynamic.put("pic", picString);
		}
		dataJSonDynamic.put("id", data.getInteger("taskId"));
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		dataJSonDynamic.put("rank", task.getRank());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		int dynamicId = DynamicService.writeDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("employeeId", data.getInteger("employeeId"));
		dataJSonApply.put("dynamicId", dynamicId);
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		int applyRank = ApplyRankService.addApplyRank(dataJSonApply);
		ApplyRankService.setEmployeeTask(dynamicId, dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId")),data.getInteger("companyId"));
		this.affirmEmployeeTask(data);
		return applyRank;
	}

	@Override
	public Integer modifyEmployeeTask(JSONObject data) {
		task task = taskMapper.selectByPrimaryKey(data.getInteger("taskId"));
		if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该taskId不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", data.getInteger("taskId"));
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.selectByTypeAndIdAndEmployeeId(dataJSonDynamic);
		if (dynamic.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该任务申请无法修改");
		}
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		dataJSonDynamic.put("note", data.getString("note"));
		List<Object> pics = data.getJSONArray("pic");
		String picString = "";
		for (Object o : pics) {
			picString += o.toString() + "-";
		}
		dataJSonDynamic.put("pic", picString);
		dataJSonDynamic.put("rank", task.getRank());
		dataJSonDynamic.put("time", TimeFormatUtil.timeStampToString(new Date().getTime()));
		int dynamicId = DynamicService.modifyDynamic(dataJSonDynamic);

		return dynamicId;
	}

	@Override
	public Integer deleteEmployeeTask(JSONObject data) {
		task task = taskMapper.selectByPrimaryKey(data.getInteger("taskId"));
		if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该taskId不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", data.getInteger("taskId"));
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.selectByTypeAndIdAndEmployeeId(dataJSonDynamic);
		if(dynamic.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该任务申请已提交无法删除");
		}
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.deleteDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("dynamicId", dynamic.getId());
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		int applyRank = ApplyRankService.deleteApplyRank(dataJSonApply);

		ApplyRankService.setEmployeeTask(dynamic.getId(), dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")),data.getInteger("companyId"));

		return applyRank;
	}
	@Override
	public Integer annulEmployeeTask(JSONObject data) {
		task task = taskMapper.selectByPrimaryKey(data.getInteger("taskId"));
		if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该taskId不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", data.getInteger("taskId"));
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.selectByTypeAndIdAndEmployeeId(dataJSonDynamic);
		if(dynamic.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId"))) {
			throw new BusiException("该任务申请已无法撤回");
		}
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.annulDynamic(dataJSonDynamic);
		
		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("dynamicId", dynamic.getId());
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		int applyRank = ApplyRankService.annulApplyRank(dataJSonApply);
		
		ApplyRankService.setEmployeeTask(dynamic.getId(), dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")),data.getInteger("companyId"));
		
		return applyRank;
	}

	public Integer affirmEmployeeTask(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", data.getInteger("taskId"));
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.selectByTypeAndIdAndEmployeeId(dataJSonDynamic);
		if (dynamic.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该任务申请无法确认提交");
		}
		dynamic.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		dynamic.setDateTime(now);
		int count = dynamicMapper.updateByPrimaryKeySelective(dynamic);
		if (count == 0) {
			throw new BusiException("更新dynamic表失败");
		}
		applyRank applyRank = applyRankMapper.selectByDynamic(dynamic.getId());
		applyRank.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		applyRank.setStartTime(now);
		String[] checkTimeList = applyRank.getCheckTimeList().split("--");
		String checkTime = "";
		for (int i = 0; i < checkTimeList.length; i++) {
			checkTime += now + "--";
		}
		applyRank.setCheckTimeList(checkTime);
		count = applyRankMapper.updateByPrimaryKeySelective(applyRank);
		if (count == 0) {
			throw new BusiException("更新applyRank表失败");
		}
		ApplyRankService.setEmployeeTask(dynamic.getId(), dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")),data.getInteger("companyId"));
		return data.getInteger("taskId");
	}
	
	@Override
	public JSONObject getEmployeeTask(JSONObject data) throws ParseException {
		task task = taskMapper.selectByPrimaryKey(data.getInteger("taskId"));
		if (task == null || task.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该taskId不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", data.getInteger("taskId"));
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.selectByTypeAndIdAndEmployeeId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());

		JSONObject dataJson = new JSONObject();
		dataJson.put("employeeTask", this.sEmployeeTaskDTO(task, data.getInteger("employeeId"),data.getInteger("companyId")));
		dataJson.put("dynamicId", dynamic.getId());
		dataJson.put("ApplyRankDTO", ApplyRankService.getApplyRankByDynamic(dataJSonDynamic));
		return dataJson;
	}
	
	public EmployeeTaskDTO sEmployeeTaskDTO(task task,Integer employeeId,Integer companyId) throws ParseException {
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", task.getId());
		dataJSonDynamic.put("employeeId", employeeId);
		dataJSonDynamic.put("companyId", companyId);
		dynamic dynamic = DynamicService.selectByTypeAndIdAndEmployeeId(dataJSonDynamic);
		EmployeeTaskDTO employeeTaskDTO = new EmployeeTaskDTO();
		employeeTaskDTO.setContent(task.getContent());
		employeeTaskDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(dynamic.getDateTime()));
		employeeTaskDTO.setId(task.getId());
		employeeTaskDTO.setNote(dynamic.getNote());
		String pics = dynamic.getPic();
		JSONObject dataJson = new JSONObject();
		List<JSONObject> fileListJSON = new ArrayList<JSONObject>();
		List<String> urlList = new ArrayList<String>();
		if (pics != null && !"".equals(pics)) {
			String[] picList = pics.split("-");
			for(int i =0 ;i<picList.length;i++) {
				File picture = new File(picList[i]);
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
		employeeTaskDTO.setPrevType(dictMapper.selectByCodeAndStateCode(PRETASK_TYPE, task.getPrevType(), companyId));
		employeeTaskDTO.setType(dictMapper.selectByCodeAndStateCode(TASK_TYPE, task.getType(), companyId));
		employeeTaskDTO.setName(task.getName());
		employeeTaskDTO.setRank(task.getRank());
		employeeTaskDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, dynamic.getState(), companyId));
		employeeTaskDTO.setStep(task.getStep());
		return employeeTaskDTO;
	}

}
