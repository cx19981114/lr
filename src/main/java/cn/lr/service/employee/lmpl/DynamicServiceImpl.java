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

import cn.lr.dao.applyCheckMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dto.DynamicDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.applyCheck;
import cn.lr.po.dynamic;
import cn.lr.po.employee;
import cn.lr.service.employee.DynamicService;
import cn.lr.util.TimeFormatUtil;

@Service
@Transactional
public class DynamicServiceImpl implements DynamicService {
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	rankMapper rankMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	applyCheckMapper applyCheckMapper;

	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${check.flow}")
	private String CHECK_FLOW;

	@Override
	public Integer writeDynamic(JSONObject data) {
		dynamic record = new dynamic();
		record.setNote(data.getString("note"));
		record.setDateTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		record.setTb_name(data.getString("name"));
		record.setTb_id(data.getInteger("id"));
		record.setEmployeeId(data.getInteger("employeeId"));
		record.setPic(data.getString("pic"));
		employee employee = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId"));
		if (employee == null || employee.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该employeeId不存在");
		}
		if (employee.getLeaderIdList() != null && !"".equals(employee.getLeaderIdList())) {
			record.setCheckId(Integer.valueOf(employee.getLeaderIdList().split("-")[0]));
		}
		record.setCompanyId(employee.getCompanyId());
		record.setRank(data.getInteger("rank"));
		record.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId")));
		int count = dynamicMapper.insertSelective(record);
		if (count == 0) {
			throw new BusiException("插入动态表失败");
		}
		return record.getId();
	}

	public Integer modifyDynamic(JSONObject data) {
		dynamic record = dynamicMapper.selectByPrimaryKey(data.getInteger("dynamicId"));
		record.setNote(data.getString("note"));
		record.setTb_name(data.getString("name"));
		record.setTb_id(data.getInteger("id"));
		record.setRank(data.getInteger("rank"));
		record.setDateTime(data.getString("time"));
		if (data.getInteger("employeeId") != null) {
			record.setEmployeeId(data.getInteger("employeeId"));
			employee employee = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId"));
			record.setCompanyId(employee.getCompanyId());
		}
		int count = dynamicMapper.updateByPrimaryKeySelective(record);
		if (count == 0) {
			throw new BusiException("插入动态表失败");
		}
		return record.getId();
	}

	@Override
	public Page<DynamicDTO> getDynamicByEmployee(JSONObject data) throws ParseException{
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<dynamic> dynamics = dynamicMapper.selectByEmployeeId(employeeId,state, (pageNum - 1) * PAGESIZE, PAGESIZE);
		int total = dynamicMapper.selectByEmployeeCount(employeeId,state);
		List<DynamicDTO> dynamicDTOs = new ArrayList<DynamicDTO>();
		for(dynamic d:dynamics) {
			DynamicDTO dynamicDTO = this.sDynamicDTO(d);
			dynamicDTOs.add(dynamicDTO);
		}
		Page<DynamicDTO> page = new Page<DynamicDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(dynamicDTOs);
		return page;
	}

	@Override
	public Page<DynamicDTO> getDynamicByCheck(JSONObject data) throws ParseException{
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<dynamic> dynamics = dynamicMapper.selectByCheckId(employeeId,stateWSH,stateSHZ, (pageNum - 1) * PAGESIZE, PAGESIZE);
		int total = dynamicMapper.selectByCheckCount(employeeId,stateWSH,stateSHZ);
		List<DynamicDTO> dynamicDTOs = new ArrayList<DynamicDTO>();
		for(dynamic d:dynamics) {
			DynamicDTO dynamicDTO = this.sDynamicDTO(d);
			dynamicDTOs.add(dynamicDTO);
		}
		Page<DynamicDTO> page = new Page<DynamicDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(dynamicDTOs);
		return page;
	}

	@Override
	public Page<DynamicDTO> getDynamicByCheckHistory(JSONObject data) throws ParseException{
		Integer employeeId = data.getInteger("employeeId");
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		Integer pageNum = data.getInteger("pageNum");
		List<applyCheck> applyChecks = applyCheckMapper.selectByEmployeeId(employeeId, (pageNum - 1) * PAGESIZE, PAGESIZE);
		int total = applyCheckMapper.selectByEmployeeCount(employeeId);
		List<DynamicDTO> dynamicDTOs = new ArrayList<DynamicDTO>();
		for(applyCheck a:applyChecks) {
			dynamic dynamic = dynamicMapper.selectByPrimaryKey(a.getDynamicId());
			DynamicDTO dynamicDTO = new DynamicDTO();
			dynamicDTO.setCompanyId(employee.getCompanyId());
			dynamicDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(a.getDateTime()));
			dynamicDTO.setEmployeeId(employeeId);
			dynamicDTO.setId(a.getDynamicId());
			dynamicDTO.setNote(a.getNote());
			dynamicDTO.setRank(dynamic.getRank());
			dynamicDTO.setState(dictMapper.selectByCodeAndStateCode(CHECK_FLOW, a.getState(),employee.getCompanyId()));
			dynamicDTO.setTb_id(dynamic.getTb_id());
			dynamicDTO.setTb_name(dynamic.getTb_name());
			dynamicDTO.setPic(employee.getPic());
			dynamicDTOs.add(dynamicDTO);
		}
		Page<DynamicDTO> page = new Page<DynamicDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(dynamicDTOs);
		return page;
	}

	
	@Override
	public dynamic getDynamic(JSONObject data) {
		dynamic dynamic = dynamicMapper.selectByPrimaryKey(data.getInteger("dynamicId"));
		if (dynamic == null) {
			throw new BusiException("该动态不存在");
		}
		return dynamic;
	}

	@Override
	public Page<DynamicDTO> getDynamicByCompany(JSONObject data) throws ParseException {
		Integer companyId = data.getInteger("companyId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<dynamic> dynamics = dynamicMapper.selectByCompanyId(companyId,state, (pageNum - 1) * PAGESIZE, PAGESIZE);
		List<DynamicDTO> dynamicDTOs = new ArrayList<DynamicDTO>();
		for(dynamic d:dynamics) {
			DynamicDTO dynamicDTO = this.sDynamicDTO(d);
			dynamicDTOs.add(dynamicDTO);
		}
		int total = dynamicMapper.selectByCompanyCount(companyId,state);
		Page<DynamicDTO> page = new Page<DynamicDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(dynamicDTOs);
		return page;
	}

	@Override
	public dynamic getDynamicByTypeAndId(JSONObject data) {
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"));
		dynamic dynamic = dynamicMapper.selectByTypeAndId(data.getString("name"), data.getInteger("id"),state);
		if (dynamic == null) {
			throw new BusiException("dynamicId不存在");
		}
		return dynamic;
	}
	
	@Override
	public dynamic selectByTypeAndIdAndEmployeeId(JSONObject data) {
		Integer stateSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"));
		Integer stateSB = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId"));
		dynamic dynamic = dynamicMapper.selectByTypeAndIdAndEmployeeId(data.getString("name"), data.getInteger("id"),
				data.getInteger("employeeId"),stateSX,stateSB);
		if (dynamic == null) {
			throw new BusiException("dynamicId不存在");
		}
		return dynamic;
	}

	@Override
	public Integer deleteDynamic(JSONObject data) {
		dynamic dynamic = dynamicMapper.selectByPrimaryKey(data.getInteger("dynamicId"));
		dynamic.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = dynamicMapper.updateByPrimaryKeySelective(dynamic);
		if (count == 0) {
			throw new BusiException("更新dynamic表失败");
		}
		return dynamic.getId();
	}
	@Override
	public Integer annulDynamic(JSONObject data) {
		dynamic dynamic = dynamicMapper.selectByPrimaryKey(data.getInteger("dynamicId"));
		dynamic.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")));
		int count = dynamicMapper.updateByPrimaryKeySelective(dynamic);
		if (count == 0) {
			throw new BusiException("更新dynamic表失败");
		}
		return dynamic.getId();
	}

	public DynamicDTO sDynamicDTO(dynamic dynamic) throws ParseException {
		DynamicDTO dynamicDTO = new DynamicDTO();
		dynamicDTO.setCompanyId(dynamic.getCompanyId());
		dynamicDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(dynamic.getDateTime()));
		dynamicDTO.setEmployeeId(dynamic.getEmployeeId());
		dynamicDTO.setId(dynamic.getId());
		dynamicDTO.setNote(dynamic.getNote());
		dynamicDTO.setRank(dynamic.getRank());
		dynamicDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, dynamic.getState(),dynamic.getCompanyId()));
		dynamicDTO.setTb_id(dynamic.getTb_id());
		dynamicDTO.setTb_name(dynamic.getTb_name());
		employee employee = employeeMapper.selectByPrimaryKey(dynamic.getEmployeeId());
		dynamicDTO.setEmployeeName(employee.getName());
		dynamicDTO.setPic(employee.getPic());
		return dynamicDTO;
	}
}
