package cn.lr.service.employee.lmpl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.applyRankMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.employeeRestMapper;
import cn.lr.dao.postMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dto.EmployeeRestDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.applyRank;
import cn.lr.po.dict;
import cn.lr.po.dynamic;
import cn.lr.po.employee;
import cn.lr.po.employeeRest;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.service.employee.EmployeeRestService;
import cn.lr.util.TimeFormatUtil;

@Service
@Transactional
public class EmployeeRestServiceImpl implements EmployeeRestService {
	public static final String Type = "每日行程";

	@Autowired
	dictMapper dictMapper;
	@Autowired
	employeeRestMapper employeeRestMapper;
	@Autowired
	rankMapper rankMapper;
	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	applyRankMapper applyRankMapper;
	@Autowired
	postMapper postMapper;

	@Autowired
	ApplyRankService ApplyRankService;
	@Autowired
	DynamicService DynamicService;

	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${attendance.type}")
	private String ATTENDANCE_TYPE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	@Value("${leader}")
	private String LEADER;
	@Value("${admin}")
	private String ADMIN;

	@Override
	public Page<EmployeeRestDTO> getEmployeeRestByEmployee(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		List<employeeRest> employeeRests = employeeRestMapper.selectByEmployeeId(employeeId,stateList, (pageNum - 1) * PAGESIZE,
				PAGESIZE);
		List<EmployeeRestDTO> jsonObjects = new ArrayList<EmployeeRestDTO>();
		for (employeeRest e : employeeRests) {
			EmployeeRestDTO employeeRestDTO = this.sEmployeeRestDTO(e);
			jsonObjects.add(employeeRestDTO);
		}
		int total = employeeRestMapper.selectByEmployeeIdCount(employeeId,stateList);
		Page<EmployeeRestDTO> page = new Page<EmployeeRestDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}
	@Override
	public EmployeeRestDTO getEmployeeRestByEmployeeNew(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		String date = TimeFormatUtil.timeStampToString(data.getTimestamp("date").getTime());
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
		employeeRest employeeRest = employeeRestMapper.selectByEmployeeIdNew(employeeId,stateList,date);
		if(employeeRest != null) {
			return this.sEmployeeRestDTO(employeeRest);
		}
		return null;
	}

	@Override
	public Page<EmployeeRestDTO> getEmployeeRestByEmployeeTeam(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		Integer pageNum = data.getInteger("pageNum");
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"));
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效",data.getInteger("companyId"));
		Integer stateWSQ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请",data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		String date = TimeFormatUtil.timeStampToString(data.getTimestamp("date").getTime());
		List<Integer> employeeIdList = new ArrayList<>();
		String leader = employee.getId()+"-"+employee.getLeaderIdList();
		if(leader != null && !"".equals(leader)) {
			String[] leaderIdList = leader.split("-");
			stateList.add(stateWSX);
			for(int i = 0;i<leaderIdList.length;i++) {
				employee employee2 = employeeMapper.selectByPrimaryKey(Integer.valueOf(leaderIdList[i]));
				if (employee2 == null || employee2.getState() == stateYSX) {
					throw new BusiException("该职员不存在");
				}
				if(employee2.getPostId() == postMapper.selectByNameAndCompany(LEADER, data.getInteger("companyId"), stateList)
						|| employee2.getPostId() == postMapper.selectByNameAndCompany(ADMIN, data.getInteger("companyId"), stateList)) {
					employeeIdList.add(employee2.getId());
					String under = employee2.getUnderIdList();
					String[] underIdList = null;
					int flag = 1;
					while(!"".equals(under)  && under != null) {
						if(flag == 1) {
							underIdList = under.split("-");
							under = "";
							flag = 0;
						}
						for (int j = 0; j < underIdList.length; j++) {
							employee employee3 = employeeMapper.selectByPrimaryKey(Integer.valueOf(underIdList[j]));
							if (employee3 == null || employee3.getState() == stateYSX) {
								throw new BusiException("该职员不存在");
							}
							if(employee3.getUnderIdList() != null && !"".equals(employee3.getUnderIdList())) {
								under += employee3.getUnderIdList();
								flag = 1;
							}
							employeeIdList.add(Integer.valueOf(underIdList[j]));
						}
					}
				break;
				}
			}
		}
		List<Integer> types = new ArrayList<>();
		String type = data.getString("type");
		if(type.equals("全部")) {
			List<dict> stings = dictMapper.selectByName("行程安排", data.getInteger("companyId"));
			for(dict d:stings) {
				types.add(dictMapper.selectByCodeAndStateName(ATTENDANCE_TYPE, d.getStateName(), data.getInteger("companyId")));
			}
		}else {
			types.add(dictMapper.selectByCodeAndStateName(ATTENDANCE_TYPE,data.getString("type") , data.getInteger("companyId")));
			
		}
		stateList.clear();
		stateList.add(stateWSQ);
		stateList.add(stateCG);
		stateList.add(stateSHZ);
		stateList.add(stateWSH);
		stateList.add(stateWTJ);
		List<employeeRest> employeeRests = employeeRestMapper.selectByEmployeeTeam(employeeIdList,stateList,date,types,
				(pageNum-1)*PAGESIZE, PAGESIZE);
		List<EmployeeRestDTO> jsonObjects = new ArrayList<EmployeeRestDTO>();
		if(type.equals("全部")) {
			int flag = 0;
			for(int i = 0;i<employeeIdList.size();i++) {
				flag = 0;
				for(employeeRest e:employeeRests) {
					if(e.getOperatorId() == employeeIdList.get(i)) {
						EmployeeRestDTO employeeRestDTO = this.sEmployeeRestDTO(e);
						jsonObjects.add(employeeRestDTO);
						flag = 1;
						break;
					}
				}
				if(flag == 0) {
					EmployeeRestDTO employeeRestDTO = new EmployeeRestDTO();
					employee employee2 = employeeMapper.selectByPrimaryKey(employeeIdList.get(i));
					employeeRestDTO.setDateTime(null);
					employeeRestDTO.setEmployeeId(employee2.getId());
					employeeRestDTO.setId(null);
					employeeRestDTO.setName(employee2.getName());
					employeeRestDTO.setNote(null);
					employeeRestDTO.setPic(employee2.getPic());
					employeeRestDTO.setSchedule(null);
					employeeRestDTO.setState(null);
					jsonObjects.add(employeeRestDTO);
				}
				
			}
		}else {
			for(employeeRest e:employeeRests) {
				EmployeeRestDTO employeeRestDTO = this.sEmployeeRestDTO(e);
				jsonObjects.add(employeeRestDTO);
			}
		}
		int total = employeeRestMapper.selectByEmployeeTeamCount(employeeIdList, stateList, date, types);
		Page<EmployeeRestDTO> page = new Page<EmployeeRestDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}

	@Override
	public List<List<JSONObject>> getEmployeeRestByEmployeeTeamList(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"));
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效",data.getInteger("companyId"));
		Integer stateWSQ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请",data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		List<Integer> employeeIdList = new ArrayList<>();
		String leader = employee.getId()+"-"+employee.getLeaderIdList();
		if(leader != null && !"".equals(leader)) {
			String[] leaderIdList = leader.split("-");
			stateList.add(stateWSX);
			for(int i = 0;i<leaderIdList.length;i++) {
				employee employee2 = employeeMapper.selectByPrimaryKey(Integer.valueOf(leaderIdList[i]));
				if (employee2 == null || employee2.getState() == stateYSX) {
					throw new BusiException("该职员不存在");
				}
				if(employee2.getPostId() == postMapper.selectByNameAndCompany(LEADER, data.getInteger("companyId"), stateList)
						|| employee2.getPostId() == postMapper.selectByNameAndCompany(ADMIN, data.getInteger("companyId"), stateList)) {
					employeeIdList.add(employee2.getId());
					String under = employee2.getUnderIdList();
					String[] underIdList = null;
					int flag = 1;
					while(!"".equals(under)  && under != null) {
						if(flag == 1) {
							underIdList = under.split("-");
							under = "";
							flag = 0;
						}
						for (int j = 0; j < underIdList.length; j++) {
							employee employee3 = employeeMapper.selectByPrimaryKey(Integer.valueOf(underIdList[j]));
							if (employee3 == null || employee3.getState() == stateYSX) {
								throw new BusiException("该职员不存在");
							}
							if(employee3.getUnderIdList() != null && !"".equals(employee3.getUnderIdList())) {
								under += employee3.getUnderIdList();
								flag = 1;
							}
							employeeIdList.add(Integer.valueOf(underIdList[j]));
						}
					}
				break;
				}
			}
		}
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		List<Integer> types = new ArrayList<>();
		List<dict> stings = dictMapper.selectByName("行程安排", data.getInteger("companyId"));
		for(dict d:stings) {
			types.add(dictMapper.selectByCodeAndStateName(ATTENDANCE_TYPE, d.getStateName(), data.getInteger("companyId")));
		}
		stateList.clear();
		stateList.add(stateWSQ);
		stateList.add(stateCG);
		stateList.add(stateSHZ);
		stateList.add(stateWSH);
		stateList.add(stateWTJ);
		List<employeeRest> employeeRests = employeeRestMapper.selectByEmployeeTeam(employeeIdList,stateList,now,types, 0,
				Integer.MAX_VALUE);
		List<List<JSONObject>> jsonObjects = new ArrayList<List<JSONObject>>();
		List<JSONObject> jsonPart = new ArrayList<JSONObject>();
		for(int i = 0;i<employeeIdList.size();i++) {
			if (i % 5 == 0 && i != 0) {
				jsonObjects.add(jsonPart);
				jsonPart = new ArrayList<JSONObject>();
			}
			int flag = 0;
			JSONObject rest = new JSONObject();
			for (employeeRest e : employeeRests) {
				if(employeeIdList.get(i) == e.getOperatorId()) {
					EmployeeRestDTO  employeeRestDTO = this.sEmployeeRestDTO(e);
					if(e.getState() != stateCG) {
						rest.put("type", "暂无");
					}else {
						rest.put("type", employeeRestDTO.getSchedule());
					}
					rest.put("employeeRestId", employeeRestDTO.getId());
					rest.put("img", employeeRestDTO.getPic());
					rest.put("name", employeeRestDTO.getName());
					rest.put("employeeId", employeeRestDTO.getEmployeeId());
					rest.put("id", employeeRestDTO.getId());
					flag = 1;
				}
			}
			if(flag == 0) {
				employee employee2 = employeeMapper.selectByPrimaryKey(employeeIdList.get(i));
				rest.put("img", employee2.getPic());
				rest.put("name", employee2.getName());
				rest.put("employeeId", employee2.getId());
				rest.put("type", "暂无");
				rest.put("id", null);
			}
			jsonPart.add(rest);
		}
		jsonObjects.add(jsonPart);
		return jsonObjects;
	}

	// FIXED Rest发来数据
	@Override
	public Integer addEmployeeRest(JSONObject data) throws ParseException {
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		JSONObject dataJson = new JSONObject();
		dataJson.put("employeeId", data.getInteger("operatorId"));
		dataJson.put("date", data.getDate("operatorTime"));
		dataJson.put("companyId", data.getInteger("companyId"));
		EmployeeRestDTO employeeRestDTO = this.getEmployeeRestByEmployeeNew(dataJson); 
		if(employeeRestDTO != null) {
			throw new BusiException("该用户今日已添加行程");
		}
		employeeRest employeeRest = new employeeRest();
		employeeRest.setDateTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		employeeRest.setEmployeeId(data.getInteger("employeeId"));
		employeeRest.setOperatorId(data.getInteger("operatorId"));
		employeeRest.setOperatorTime(TimeFormatUtil.dateToString(data.getDate("operatorTime")));
		employeeRest.setSchedule(dictMapper.selectByCodeAndStateName(ATTENDANCE_TYPE, data.getString("schedule"),data.getInteger("companyId")));
		employeeRest.setNote(data.getString("note"));
		employeeRest.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId")));
		int count = employeeRestMapper.insertSelective(employeeRest);
		if (count == 0) {
			throw new BusiException("插入employeeRest表失败");
		}

		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("note"));
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeRest.getId());
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
		return employeeRest.getId();
	}

	@Override
	public Integer modifyEmployeeRest(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeRest employeeRest = employeeRestMapper.selectByPrimaryKey(data.getInteger("employeeRestId"));
		if(employeeRest.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该每日行程无法修改");
		}
		employeeRest.setOperatorTime(data.getDate("operatorTime") == null ? null
				: TimeFormatUtil.dateToString(data.getDate("operatorTime")));
		employeeRest.setOperatorId(data.getInteger("operatorId"));
		employeeRest.setDateTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		employeeRest.setSchedule(data.getString("schedule") == null ? null
				: dictMapper.selectByCodeAndStateName(ATTENDANCE_TYPE, data.getString("schedule"),data.getInteger("companyId")));
		employeeRest.setDateTime(now);
		employeeRest.setNote(data.getString("note"));
		int count = employeeRestMapper.updateByPrimaryKeySelective(employeeRest);
		if (count == 0) {
			throw new BusiException("更新employeeRest表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeRest.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("time", now);
		dataJSonDynamic.put("dynamicId",  dynamic.getId());
		count = DynamicService.modifyDynamic(dataJSonDynamic);
		if(count == 0) {
			throw new BusiException("更新dynamic表失败");
		}
		return employeeRest.getId();
	}

	@Override
	public Integer deleteEmployeeRest(JSONObject data) {
		employeeRest employeeRest = employeeRestMapper.selectByPrimaryKey(data.getInteger("employeeRestId"));
		if(employeeRest.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该行程已提交无法删除");
		}
		employeeRest.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = employeeRestMapper.updateByPrimaryKeySelective(employeeRest);
		if (count == 0) {
			throw new BusiException("更新employeeRest表失败");
		}

		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeRest.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.deleteDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("dynamicId", dynamic.getId());
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		ApplyRankService.deleteApplyRank(dataJSonApply);

		return employeeRest.getId();
	}
	@Override
	public Integer annulEmployeeRest(JSONObject data) {
		employeeRest employeeRest = employeeRestMapper.selectByPrimaryKey(data.getInteger("employeeRestId"));
		employeeRest.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")));
		int count = employeeRestMapper.updateByPrimaryKeySelective(employeeRest);
		if (count == 0) {
			throw new BusiException("更新employeeRest表失败");
		}
		
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeRest.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.annulDynamic(dataJSonDynamic);
		
		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("dynamicId", dynamic.getId());
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		ApplyRankService.annulApplyRank(dataJSonApply);
		
		return employeeRest.getId();
	}

	@Override
	public JSONObject getEmployeeRest(JSONObject data) throws ParseException {
		employeeRest employeeRest = employeeRestMapper.selectByPrimaryKey(data.getInteger("employeeRestId"));
		if (employeeRest == null || employeeRest.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该每日行程不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeRest.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());

		JSONObject dataJson = new JSONObject();
		dataJson.put("employeeRest", this.sEmployeeRestDTO(employeeRest));
		dataJson.put("dynamicId", dynamic.getId());
		dataJson.put("ApplyRankDTO", ApplyRankService.getApplyRankByDynamic(dataJSonDynamic));
		return dataJson;
	}

	public EmployeeRestDTO sEmployeeRestDTO(employeeRest employeeRest) throws ParseException {
		EmployeeRestDTO employeeRestDTO = new EmployeeRestDTO();
		employee employee = employeeMapper.selectByPrimaryKey(employeeRest.getOperatorId());
		employeeRestDTO.setDateTime(TimeFormatUtil.stringTodate(employeeRest.getOperatorTime()));
		employeeRestDTO.setEmployeeId(employeeRest.getOperatorId());
		employeeRestDTO.setName(employee.getName());
		employeeRestDTO.setId(employeeRest.getId());
		employeeRestDTO.setSchedule(dictMapper.selectByCodeAndStateCode(ATTENDANCE_TYPE, employeeRest.getSchedule(),employee.getCompanyId()));
		employeeRestDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, employeeRest.getState(),employee.getCompanyId()));
		employeeRestDTO.setPic(employee.getPic());
		employeeRestDTO.setNote(employeeRest.getNote());
		return employeeRestDTO;
	}

	public List<JSONObject> getEmployeeRestTypeList(JSONObject data) {
		employee employee = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId"));
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
		String date = TimeFormatUtil.timeStampToString(data.getTimestamp("date").getTime());
		List<Integer> employeeIdList = new ArrayList<>();
		employeeIdList.add(employee.getId());
		String underList = employee.getUnderIdList();
		if (underList != null && !"".equals(underList)) {
			String[] underIdList = underList.split("-");
			for (int i = 0; i < underIdList.length; i++) {
				employeeIdList.add(Integer.valueOf(underIdList[i]));
			}
			System.out.println(employeeIdList.size());
		}
		List<employeeRest> employeeRests = employeeRestMapper.selectByEmployeeList(employeeIdList,stateList,date);
		List<dict> dicts = dictMapper.selectByName("行程安排",data.getInteger("companyId"));
		List<JSONObject> employeeRestJson = new ArrayList<>();
		Map<String, Integer> rest = new HashMap<String, Integer>();
		rest.put("内勤", 0);
		rest.put("外勤", 0);
		rest.put("请假", 0);
		rest.put("轮休", 0);
		JSONObject all = new JSONObject();
		all.put("value", "全部");
		all.put("count", employeeIdList.size());
		all.put("id", 0);
		employeeRestJson.add(all);
		for (employeeRest e : employeeRests) {
			if (e.getSchedule() == dictMapper.selectByCodeAndStateName(ATTENDANCE_TYPE, "内勤",data.getInteger("companyId"))) {
				rest.put("内勤", rest.get("内勤") + 1);
			} else if (e.getSchedule() == dictMapper.selectByCodeAndStateName(ATTENDANCE_TYPE, "外勤",data.getInteger("companyId"))) {
				rest.put("外勤", rest.get("外勤") + 1);
			} else if (e.getSchedule() == dictMapper.selectByCodeAndStateName(ATTENDANCE_TYPE, "请假",data.getInteger("companyId"))) {
				rest.put("请假", rest.get("请假") + 1);
			} else if (e.getSchedule() == dictMapper.selectByCodeAndStateName(ATTENDANCE_TYPE, "轮休",data.getInteger("companyId"))) {
				rest.put("轮休", rest.get("轮休") + 1);
			}
		}
		for (dict d : dicts) {
			JSONObject employeeRest = new JSONObject();
			employeeRest.put("value", d.getStateName());
			employeeRest.put("id", d.getStateCode());
			employeeRest.put("count", rest.get(d.getStateName()));
			employeeRestJson.add(employeeRest);
		}
		return employeeRestJson;
	}
	public Integer affirmEmployeeRest(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeRest employeeRest = employeeRestMapper.selectByPrimaryKey(data.getInteger("employeeRestId"));
		if(employeeRest.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该每日行程无法确认提交");
		}
		employeeRest.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		int count = employeeRestMapper.updateByPrimaryKeySelective(employeeRest);
		if(count == 0) {
			throw new BusiException("更新employeeLog表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeRest.getId());
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
		if (count == 0) {
			throw new BusiException("更新applyRank表失败");
		}
		return employeeRest.getId();
	}
}
