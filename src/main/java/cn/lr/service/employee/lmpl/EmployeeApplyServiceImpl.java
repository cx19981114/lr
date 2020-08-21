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
import cn.lr.dao.employeeApplyMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dto.EmployeeApplyDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.applyRank;
import cn.lr.po.dynamic;
import cn.lr.po.employeeApply;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.service.employee.EmployeeApplyService;
import cn.lr.util.TimeFormatUtil;
@Service
@Transactional
public class EmployeeApplyServiceImpl implements EmployeeApplyService {

	public static final String[] Type = new String[]{"请假","物料","培训"};
	
	@Autowired
	dictMapper dictMapper;
	@Autowired
	employeeApplyMapper employeeApplyMapper;
	@Autowired
	rankMapper rankMapper;
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	applyRankMapper applyRankMapper;
	@Autowired
	employeeMapper employeeMapper;
	
	@Autowired
	ApplyRankService ApplyRankService;
	@Autowired
	DynamicService DynamicService;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${apply.type}")
	private String APPLY_TYPE;
	@Value("${leave.type}")
	private String LEAVE_TYPE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	
	@Override
	public Page<EmployeeApplyDTO> getEmployeeApplyByEmployee(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		Integer type = dictMapper.selectByCodeAndStateName(APPLY_TYPE, data.getString("type"),data.getInteger("companyId"));
		List<employeeApply> employeeApplys = employeeApplyMapper.selectByEmployeeIdAndType(employeeId,state,type,(pageNum-1)*PAGESIZE,PAGESIZE);
		List<EmployeeApplyDTO> jsonObjects = new ArrayList<EmployeeApplyDTO>();
		for(employeeApply e : employeeApplys) {
			EmployeeApplyDTO employeeApplyDTO = this.sEmployeeDTO(e);
			jsonObjects.add(employeeApplyDTO);
		}
		int total = employeeApplyMapper.selectByEmployeeIdAndTypeCount(employeeId,state,type);
		Page<EmployeeApplyDTO> page = new Page<EmployeeApplyDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}

	@Override
	public Integer addEmployeeApply(JSONObject data) {
		employeeApply employeeApply = new employeeApply();
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		employeeApply.setEmployeeId(data.getInteger("employeeId"));
		employeeApply.setName(data.getString("name"));
		employeeApply.setStartDate(TimeFormatUtil.dateToString(data.getDate("startDate")));
		employeeApply.setStartPhase(data.getString("startPhase"));
		employeeApply.setEndDate(TimeFormatUtil.dateToString(data.getDate("endDate")));
		employeeApply.setEndPhase(data.getString("endPhase"));
		employeeApply.setNote(data.getString("note"));
		employeeApply.setDateTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		employeeApply.setType(dictMapper.selectByCodeAndStateName(APPLY_TYPE, data.getString("type"),data.getInteger("companyId")));
		employeeApply.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId")));
		int count = employeeApplyMapper.insertSelective(employeeApply);
		if(count == 0) {
			throw new BusiException("插入employeeApply表失败");
		}
		
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("note"));
		dataJSonDynamic.put("name", Type[employeeApply.getType()-1]);
		dataJSonDynamic.put("id", employeeApply.getId());
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dataJSonDynamic.put("rank",  rankMapper.selectByName(Type[employeeApply.getType()-1],data.getInteger("companyId"),state));
		Integer dynamicId = DynamicService.writeDynamic(dataJSonDynamic);
		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("employeeId", data.getInteger("employeeId"));
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		dataJSonApply.put("dynamicId", dynamicId);
		ApplyRankService.addApplyRank(dataJSonApply);
		return employeeApply.getId();
	}

	@Override
	public Integer modifyEmployeeApply(JSONObject data) {
		employeeApply employeeApply = employeeApplyMapper.selectByPrimaryKey(data.getInteger("employeeApplyId"));
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		if(employeeApply.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该申请不能修改");
		}
		employeeApply.setName(data.getString("name"));
		employeeApply.setStartDate(data.getDate("startDate") == null ? null : TimeFormatUtil.dateToString(data.getDate("startDate")));
		employeeApply.setStartPhase(data.getString("startPhase"));
		employeeApply.setEndDate(data.getDate("endDate") == null ? null : TimeFormatUtil.dateToString(data.getDate("endDate")));
		employeeApply.setEndPhase(data.getString("endPhase"));
		employeeApply.setNote(data.getString("note"));
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeApply.setDateTime(now);
		if(data.getString("type") != null) {
			JSONObject dataJSonDynamic = new JSONObject();
			dataJSonDynamic.put("name", Type[employeeApply.getType()-1]);
			dataJSonDynamic.put("id", employeeApply.getId());
			dataJSonDynamic.put("companyId", data.getInteger("companyId"));
			dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
			employeeApply.setType(dictMapper.selectByCodeAndStateName(APPLY_TYPE, data.getString("type"),data.getInteger("companyId")));
			dataJSonDynamic.put("name", Type[employeeApply.getType()-1]);
			dataJSonDynamic.put("note", data.getString("note"));
			dataJSonDynamic.put("rank",  rankMapper.selectByName(Type[employeeApply.getType()-1],data.getInteger("companyId"),state));
			dataJSonDynamic.put("dynamicId",  dynamic.getId());
			dataJSonDynamic.put("time", now);
			int count = DynamicService.modifyDynamic(dataJSonDynamic);
			if(count == 0) {
				throw new BusiException("更新dynamic表失败");
			}
			
		}
		int count = employeeApplyMapper.updateByPrimaryKeySelective(employeeApply);
		if(count == 0) {
			throw new BusiException("更新employeeApply表失败");
		}
		return employeeApply.getId();
	}

	@Override
	public Integer deleteEmployeeApply(JSONObject data) {
		employeeApply employeeApply = employeeApplyMapper.selectByPrimaryKey(data.getInteger("employeeApplyId"));
		if(employeeApply.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该申请不能删除");
		}
		employeeApply.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = employeeApplyMapper.updateByPrimaryKeySelective(employeeApply);
		if(count == 0) {
			throw new BusiException("更新employeeApply表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type[employeeApply.getType()-1]);
		dataJSonDynamic.put("id", employeeApply.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.deleteDynamic(dataJSonDynamic);
		
		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("dynamicId", dynamic.getId());
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		ApplyRankService.deleteApplyRank(dataJSonApply);
		
		return employeeApply.getId();
	}
	@Override
	public Integer annulEmployeeApply(JSONObject data) {
		employeeApply employeeApply = employeeApplyMapper.selectByPrimaryKey(data.getInteger("employeeApplyId"));
		if(employeeApply.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId"))) {
			throw new BusiException("该申请已无法撤回");
		}
		employeeApply.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")));
		int count = employeeApplyMapper.updateByPrimaryKeySelective(employeeApply);
		if(count == 0) {
			throw new BusiException("更新employeeApply表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type[employeeApply.getType()-1]);
		dataJSonDynamic.put("id", employeeApply.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.annulDynamic(dataJSonDynamic);
		
		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("dynamicId", dynamic.getId());
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		ApplyRankService.annulApplyRank(dataJSonApply);
		
		return employeeApply.getId();
	}

	@Override
	public JSONObject getEmployeeApply(JSONObject data) throws ParseException {
		employeeApply employeeApply = employeeApplyMapper.selectByPrimaryKey(data.getInteger("employeeApplyId"));
		if(employeeApply == null || employeeApply.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该申请不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type[employeeApply.getType()-1]);
		dataJSonDynamic.put("id", employeeApply.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		
		JSONObject dataJson = new JSONObject();
		dataJson.put("employeeApply", this.sEmployeeDTO(employeeApply));
		dataJson.put("dynamicId", dynamic.getId());
		dataJson.put("ApplyRankDTO", ApplyRankService.getApplyRankByDynamic(dataJSonDynamic));
		return dataJson;
	}

	public EmployeeApplyDTO sEmployeeDTO(employeeApply e) throws ParseException {
		EmployeeApplyDTO employeeApplyDTO = new EmployeeApplyDTO();
		employeeApplyDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(e.getDateTime()));
		employeeApplyDTO.setEmployeeId(e.getEmployeeId());
		employeeApplyDTO.setEndDate(TimeFormatUtil.stringTodate(e.getEndDate()));
		employeeApplyDTO.setEndPhase(e.getEndPhase());
		employeeApplyDTO.setId(e.getId());
		employeeApplyDTO.setName(e.getName());
		employeeApplyDTO.setNote(e.getNote());
		employeeApplyDTO.setStartDate(TimeFormatUtil.stringTodate(e.getStartDate()));
		employeeApplyDTO.setStartPhase(e.getStartPhase());
		employeeApplyDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, e.getState(),employeeMapper.selectByPrimaryKey(e.getEmployeeId()).getCompanyId()));
		employeeApplyDTO.setType(dictMapper.selectByCodeAndStateCode(APPLY_TYPE, e.getType(),employeeMapper.selectByPrimaryKey(e.getEmployeeId()).getCompanyId()));
		
		return employeeApplyDTO;
	}
	
	public Integer affirmEmployeeApply(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeApply employeeApply = employeeApplyMapper.selectByPrimaryKey(data.getInteger("employeeApplyId"));
		if(employeeApply.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该申请无法确认提交");
		}
		employeeApply.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		int count = employeeApplyMapper.updateByPrimaryKeySelective(employeeApply);
		if(count == 0) {
			throw new BusiException("更新employeeApply表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type[employeeApply.getType()-1]);
		dataJSonDynamic.put("id", employeeApply.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dynamic.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		dynamic.setDateTime(now);
		count = dynamicMapper.updateByPrimaryKeySelective(dynamic);
		if(count == 0) {
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
		if(count == 0) {
			throw new BusiException("更新applyRank表失败");
		}
		return employeeApply.getId();
	}
}
