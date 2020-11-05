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
import cn.lr.dao.employeeLogTomorrowMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dto.EmployeeLogTomorrowDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.applyRank;
import cn.lr.po.dynamic;
import cn.lr.po.employeeLogTomorrow;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.service.employee.EmployeeLogTomorrowService;
import cn.lr.service.employee.EmployeeRankService;
import cn.lr.util.TimeFormatUtil;

@Service
@Transactional
public class EmployeeLogTomorrowServiceImpl implements EmployeeLogTomorrowService {
	public static final String Type = "明日计划";

	@Autowired
	employeeLogTomorrowMapper employeeLogTomorrowMapper;
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
	public Integer addEmployeeLogTomorrow(JSONObject data) {
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
		employeeLogTomorrow employeeLogTomorrow = employeeLogTomorrowMapper.selectByEmployeeIdNew(data.getInteger("employeeId"),stateList);
		if(employeeLogTomorrow != null) {
			throw new BusiException("职员已填写明日计划");
		}
		employeeLogTomorrow record = new employeeLogTomorrow();
		record.setEmployeeId(data.getInteger("employeeId"));
		record.setCustomer(data.getString("customer"));
		record.setMoney(data.getString("money"));
		record.setPerson(data.getString("person"));
		record.setRun(data.getString("run"));
		record.setDateTime(now);
		record.setState(stateWSH);
		int count = employeeLogTomorrowMapper.insertSelective(record);
		if (count == 0) {
			throw new BusiException("插入employeeLogTomorrow表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("note"));
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", record.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
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
	public Integer modifyEmployeeLogTomorrow(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeLogTomorrow record = employeeLogTomorrowMapper.selectByPrimaryKey(data.getInteger("employeeLogTomorrowId"));
		if (record.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该明日计划不能修改");
		}
		record.setDateTime(now);
		record.setEmployeeId(data.getInteger("employeeId"));
		record.setCustomer(data.getString("customer"));
		record.setMoney(data.getString("money"));
		record.setPerson(data.getString("person"));
		record.setRun(data.getString("run"));
		int count = employeeLogTomorrowMapper.updateByPrimaryKeySelective(record);
		if (count == 0) {
			throw new BusiException("更新employeeLogTomorrow表失败");
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
	public Integer deleteEmployeeLogTomorrow(JSONObject data) {
		employeeLogTomorrow record = employeeLogTomorrowMapper.selectByPrimaryKey(data.getInteger("employeeLogTomorrowId"));
		if(record.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该申请已提交无法删除");
		}
		record.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = employeeLogTomorrowMapper.updateByPrimaryKeySelective(record);
		if (count == 0) {
			throw new BusiException("更新employeeLogTomorrow表失败");
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
	public Integer annulEmployeeLogTomorrow(JSONObject data) {
		employeeLogTomorrow record = employeeLogTomorrowMapper.selectByPrimaryKey(data.getInteger("employeeLogTomorrowId"));
		record.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")));
		int count = employeeLogTomorrowMapper.updateByPrimaryKeySelective(record);
		if (count == 0) {
			throw new BusiException("更新employeeLogTomorrow表失败");
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
	public Page<EmployeeLogTomorrowDTO> getEmployeeLogTomorrowByEmployee(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(state);
		List<employeeLogTomorrow> employeeLogTomorrows = employeeLogTomorrowMapper.selectByEmployeeId(employeeId,stateList, (pageNum - 1) * PAGESIZE,
				PAGESIZE);
		List<EmployeeLogTomorrowDTO> jsonObjects = new ArrayList<EmployeeLogTomorrowDTO>();
		for (employeeLogTomorrow e : employeeLogTomorrows) {
			EmployeeLogTomorrowDTO employeeLogTomorrowDTO = this.sEmployeeLogTomorrowDTO(e);
			jsonObjects.add(employeeLogTomorrowDTO);
		}
		int total = employeeLogTomorrowMapper.selectByEmployeeIdCount(employeeId,stateList);
		Page<EmployeeLogTomorrowDTO> page = new Page<EmployeeLogTomorrowDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}

	@Override
	public JSONObject getEmployeeLogTomorrow(JSONObject data) throws ParseException {
		employeeLogTomorrow employeeLogTomorrow = employeeLogTomorrowMapper.selectByPrimaryKey(data.getInteger("employeeLogTomorrowId"));
		if (employeeLogTomorrow == null || employeeLogTomorrow.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该employeeLogTomorrowId不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeLogTomorrow.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());

		JSONObject dataJson = new JSONObject();
		dataJson.put("employeeLog", this.sEmployeeLogTomorrowDTO(employeeLogTomorrow));
		dataJson.put("dynamicId", dynamic.getId());
		dataJson.put("ApplyRankDTO", ApplyRankService.getApplyRankByDynamic(dataJSonDynamic));
		return dataJson;
	}

	public EmployeeLogTomorrowDTO sEmployeeLogTomorrowDTO(employeeLogTomorrow employeeLogTomorrow) throws ParseException {
		EmployeeLogTomorrowDTO employeeLogTomorrowDTO = new EmployeeLogTomorrowDTO();
		employeeLogTomorrowDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(employeeLogTomorrow.getDateTime()));
		employeeLogTomorrowDTO.setEmployeeId(employeeLogTomorrow.getEmployeeId());
		employeeLogTomorrowDTO.setCustomer(employeeLogTomorrow.getCustomer());
		employeeLogTomorrowDTO.setMoney(employeeLogTomorrow.getMoney());
		employeeLogTomorrowDTO.setPerson(employeeLogTomorrow.getPerson());
		employeeLogTomorrowDTO.setRun(employeeLogTomorrow.getRun());
		employeeLogTomorrowDTO.setId(employeeLogTomorrow.getId());
		employeeLogTomorrowDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, employeeLogTomorrow.getState(),employeeMapper.selectByPrimaryKey(employeeLogTomorrow.getEmployeeId()).getCompanyId()));
		return employeeLogTomorrowDTO;
	}

	public Integer affirmEmployeeLogTomorrow(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeLogTomorrow employeeLogTomorrow = employeeLogTomorrowMapper.selectByPrimaryKey(data.getInteger("employeeLogTomorrowId"));
		if (employeeLogTomorrow.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该明日计划无法确认提交");
		}
		employeeLogTomorrow.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		int count = employeeLogTomorrowMapper.updateByPrimaryKeySelective(employeeLogTomorrow);
		if (count == 0) {
			throw new BusiException("更新employeeLogTomorrow表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeLogTomorrow.getId());
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
		return employeeLogTomorrow.getId();
	}
	@Override
	public EmployeeLogTomorrowDTO getEmployeeLogTomorrowByEmployeeNew(JSONObject data) throws ParseException {
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
		employeeLogTomorrow employeeLogTomorrow = employeeLogTomorrowMapper.selectByEmployeeIdNew(employeeId,stateList);
		if(employeeLogTomorrow != null) {
			return this.sEmployeeLogTomorrowDTO(employeeLogTomorrow);
		}
		return null;
	}
}
