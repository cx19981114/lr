package cn.lr.service.employee.lmpl;

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
import cn.lr.dao.employeeLogDayMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dto.EmployeeLogDayDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.applyRank;
import cn.lr.po.dynamic;
import cn.lr.po.employeeLogDay;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.service.employee.EmployeeLogDayService;
import cn.lr.service.employee.EmployeeRankService;
import cn.lr.util.TimeFormatUtil;

@Service
@Transactional
public class EmployeeLogDayServiceImpl implements EmployeeLogDayService {
	public static final String Type = "今日总结";

	@Autowired
	employeeLogDayMapper employeeLogDayMapper;
	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	rankMapper rankMapper;
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	applyRankMapper applyRankMapper;

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

	@Override
	public Integer addEmployeeLogDay(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateSHZ);
		stateList.add(stateWSH);
		stateList.add(stateWTJ);
		employeeLogDay employeeLogDay = employeeLogDayMapper.selectByEmployeeIdNew(data.getInteger("employeeId"),stateList);
		if(employeeLogDay != null) {
			throw new BusiException("职员已填写今日总结");
		}
		employeeLogDay record = new employeeLogDay();
		record.setEmployeeId(data.getInteger("employeeId"));
		record.setGoalAchievement(data.getString("goalAchievement"));
		record.setTaskAchievement(data.getString("taskAchievement"));
		record.setFeel(data.getString("feel"));
		record.setDateTime(now);
		record.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId")));
		int count = employeeLogDayMapper.insertSelective(record);
		if (count == 0) {
			throw new BusiException("插入employeeLogDay表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("note"));
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", record.getId());
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		stateList.clear();
		stateList.add(stateWSX);
		dataJSonDynamic.put("rank", rankMapper.selectByName(Type,data.getInteger("companyId"),stateList));
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
		return record.getId();
	}

	@Override
	public Integer modifyEmployeeLogDay(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeLogDay record = employeeLogDayMapper.selectByPrimaryKey(data.getInteger("employeeLogDayId"));
		if (record.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该今日总结不能修改");
		}
		record.setDateTime(now);
		record.setEmployeeId(data.getInteger("employeeId"));
		record.setGoalAchievement(data.getString("goalAchievement"));
		record.setTaskAchievement(data.getString("taskAchievement"));
		record.setFeel(data.getString("feel"));
		int count = employeeLogDayMapper.updateByPrimaryKeySelective(record);
		if (count == 0) {
			throw new BusiException("更新employeeLogDay表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", record.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("time", now);
		dataJSonDynamic.put("dynamicId",  dynamic.getId());
		count = DynamicService.modifyDynamic(dataJSonDynamic);
		if(count == 0) {
			throw new BusiException("更新dynamic表失败");
		}
		return record.getId();
	}

	@Override
	public Integer deleteEmployeeLogDay(JSONObject data) {
		employeeLogDay record = employeeLogDayMapper.selectByPrimaryKey(data.getInteger("employeeLogDayId"));
		if(record.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该申请已提交无法删除");
		}
		record.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = employeeLogDayMapper.updateByPrimaryKeySelective(record);
		if (count == 0) {
			throw new BusiException("更新employeeLogDay表失败");
		}

		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", record.getId());
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
		return record.getId();
	}
	@Override
	public Integer annulEmployeeLogDay(JSONObject data) {
		employeeLogDay record = employeeLogDayMapper.selectByPrimaryKey(data.getInteger("employeeLogDayId"));
		record.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")));
		int count = employeeLogDayMapper.updateByPrimaryKeySelective(record);
		if (count == 0) {
			throw new BusiException("更新employeeLogDay表失败");
		}
		
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", record.getId());
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
		return record.getId();
	}

	@Override
	public Page<EmployeeLogDayDTO> getEmployeeLogDayByEmployee(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(state);
		List<employeeLogDay> employeeLogDays = employeeLogDayMapper.selectByEmployeeId(employeeId,stateList, (pageNum - 1) * PAGESIZE,
				PAGESIZE);
		List<EmployeeLogDayDTO> jsonObjects = new ArrayList<EmployeeLogDayDTO>();
		for (employeeLogDay e : employeeLogDays) {
			EmployeeLogDayDTO employeeLogDayDTO = this.sEmployeeLogDayDTO(e);
			jsonObjects.add(employeeLogDayDTO);
		}
		int total = employeeLogDayMapper.selectByEmployeeIdCount(employeeId,stateList);
		Page<EmployeeLogDayDTO> page = new Page<EmployeeLogDayDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}

	@Override
	public JSONObject getEmployeeLogDay(JSONObject data) throws ParseException {
		employeeLogDay employeeLogDay = employeeLogDayMapper.selectByPrimaryKey(data.getInteger("employeeLogDayId"));
		if (employeeLogDay == null || employeeLogDay.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该今日总结不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeLogDay.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());

		JSONObject dataJson = new JSONObject();
		dataJson.put("employeeLog", this.sEmployeeLogDayDTO(employeeLogDay));
		dataJson.put("dynamicId", dynamic.getId());
		dataJson.put("ApplyRankDTO", ApplyRankService.getApplyRankByDynamic(dataJSonDynamic));
		return dataJson;
	}

	public EmployeeLogDayDTO sEmployeeLogDayDTO(employeeLogDay employeeLogDay) throws ParseException {
		EmployeeLogDayDTO employeeLogDayDTO = new EmployeeLogDayDTO();
		employeeLogDayDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(employeeLogDay.getDateTime()));
		employeeLogDayDTO.setEmployeeId(employeeLogDay.getEmployeeId());
		employeeLogDayDTO.setFeel(employeeLogDay.getFeel());
		employeeLogDayDTO.setGoalAchievement(employeeLogDay.getGoalAchievement());
		employeeLogDayDTO.setId(employeeLogDay.getId());
		employeeLogDayDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, employeeLogDay.getState(),employeeMapper.selectByPrimaryKey(employeeLogDay.getEmployeeId()).getCompanyId()));
		employeeLogDayDTO.setTaskAchievement(employeeLogDay.getTaskAchievement());
		return employeeLogDayDTO;
	}

	public Integer affirmEmployeeLogDay(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeLogDay employeeLogDay = employeeLogDayMapper.selectByPrimaryKey(data.getInteger("employeeLogDayId"));
		if (employeeLogDay.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该今日总结无法确认提交");
		}
		employeeLogDay.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		int count = employeeLogDayMapper.updateByPrimaryKeySelective(employeeLogDay);
		if (count == 0) {
			throw new BusiException("更新employeeLogDay表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeLogDay.getId());
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
		return employeeLogDay.getId();
	}
	@Override
	public EmployeeLogDayDTO getEmployeeLogDayByEmployeeNew(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateSHZ);
		stateList.add(stateWSH);
		stateList.add(stateWTJ);
		employeeLogDay employeeLogDay = employeeLogDayMapper.selectByEmployeeIdNew(employeeId,stateList);
		if(employeeLogDay != null) {
			return this.sEmployeeLogDayDTO(employeeLogDay);
		}
		return null;
	}
}
