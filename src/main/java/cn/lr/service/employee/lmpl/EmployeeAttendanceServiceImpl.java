package cn.lr.service.employee.lmpl;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import cn.lr.dao.employeeAttendanceMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dto.EmployeeAttendanceDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.applyRank;
import cn.lr.po.dynamic;
import cn.lr.po.employeeAttendance;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.service.employee.EmployeeAttendanceService;
import cn.lr.util.MapUtil;
import cn.lr.util.TimeFormatUtil;

@Service
@Transactional
public class EmployeeAttendanceServiceImpl implements EmployeeAttendanceService {
	public static final String Type = "每日打卡";

	@Autowired
	dictMapper dictMapper;
	@Autowired
	employeeAttendanceMapper employeeAttendanceMapper;
	@Autowired
	rankMapper rankMapper;
	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	applyRankMapper applyRankMapper;

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
	@Value("${picPath}")
	private String PATH;
	@Value("${picActPath}")
	private String ACTPATH;

	public String getAddress(JSONObject data) {
		Map<String, String> areaMap = MapUtil.getCityByLonLat(data.getDouble("lng"), data.getDouble("lat"), data.getString("coordtype"));
		return areaMap.get("province")+areaMap.get("city")+areaMap.get("district")+areaMap.get("street");
	}
	@Override
	public Page<EmployeeAttendanceDTO> getEmployeeAttendanceByEmployee(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<employeeAttendance> employeeAttendances = employeeAttendanceMapper.selectByEmployeeId(employeeId,state,
				(pageNum - 1) * PAGESIZE, PAGESIZE);
		List<EmployeeAttendanceDTO> jsonObjects = new ArrayList<EmployeeAttendanceDTO>();
		for (employeeAttendance e : employeeAttendances) {
			EmployeeAttendanceDTO employeeAttendanceDTO = this.sEmployeeAttendanceDTO(e);
			jsonObjects.add(employeeAttendanceDTO);
		}
		int total = employeeAttendanceMapper.selectByEmployeeIdCount(employeeId,state);
		Page<EmployeeAttendanceDTO> page = new Page<EmployeeAttendanceDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}

	// FIXED 今日打卡没有判断今天是否打过卡
	@Override
	public Integer addEmployeeAttendance(JSONObject data) throws ParseException {
		EmployeeAttendanceDTO employeeAttendanceDTO = this.getEmployeeAttendanceByEmployeeNew(data);
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		if (employeeAttendanceDTO != null) {
			throw new BusiException("该用户今日已打卡");
		}
		employeeAttendance employeeAttendance = new employeeAttendance();
		employeeAttendance.setAddress(data.getString("address"));
		employeeAttendance.setDateTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		employeeAttendance.setEmployeeId(data.getInteger("employeeId"));
		employeeAttendance.setNote(data.getString("note"));
		List<Object> pics = data.getJSONArray("pic");
		String picString = "";
		for (Object o : pics) {
			picString += o.toString() + "-";
		}
		employeeAttendance.setPic(picString);
		employeeAttendance
				.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId")));
		int count = employeeAttendanceMapper.insertSelective(employeeAttendance);
		if (count == 0) {
			throw new BusiException("插入employeeAttendance表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("note"));
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeAttendance.getId());
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dataJSonDynamic.put("rank", rankMapper.selectByName(Type, data.getInteger("companyId"),state));
		int dynamicId = DynamicService.writeDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("employeeId", data.getInteger("employeeId"));
		dataJSonApply.put("dynamicId", dynamicId);
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		ApplyRankService.addApplyRank(dataJSonApply);
		return employeeAttendance.getId();
	}

	@Override
	public Integer modifyEmployeeAttendance(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeAttendance employeeAttendance = employeeAttendanceMapper
				.selectByPrimaryKey(data.getInteger("employeeAttendanceId"));
		if (employeeAttendance.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",
				data.getInteger("companyId"))) {
			throw new BusiException("该每日打卡不能修改");
		}
		employeeAttendance.setAddress(data.getString("address"));
		employeeAttendance.setNote(data.getString("note"));
		List<Object> pics = data.getJSONArray("pic");
		String picString = "";
		for (Object o : pics) {
			picString += o.toString() + "-";
		}
		employeeAttendance.setPic(picString);
		employeeAttendance.setDateTime(now);
		int count = employeeAttendanceMapper.updateByPrimaryKeySelective(employeeAttendance);
		if (count == 0) {
			throw new BusiException("更新employeeAttendance表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeAttendance.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("time", now);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		count = DynamicService.modifyDynamic(dataJSonDynamic);
		if (count == 0) {
			throw new BusiException("更新dynamic表失败");
		}
		return employeeAttendance.getId();
	}

	@Override
	public Integer deleteEmployeeAttendance(JSONObject data) {
		employeeAttendance employeeAttendance = employeeAttendanceMapper
				.selectByPrimaryKey(data.getInteger("employeeAttendanceId"));
		if (employeeAttendance.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",
				data.getInteger("companyId"))) {
			throw new BusiException("该打卡已提交无法删除");
		}
		employeeAttendance
				.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId")));
		int count = employeeAttendanceMapper.updateByPrimaryKeySelective(employeeAttendance);
		if (count == 0) {
			throw new BusiException("更新employeeAttendance表失败");
		}

		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeAttendance.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.deleteDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		dataJSonApply.put("dynamicId", dynamic.getId());
		ApplyRankService.deleteApplyRank(dataJSonApply);

		return employeeAttendance.getId();
	}

	@Override
	public Integer annulEmployeeAttendance(JSONObject data) {
		employeeAttendance employeeAttendance = employeeAttendanceMapper
				.selectByPrimaryKey(data.getInteger("employeeAttendanceId"));
		employeeAttendance
				.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败", data.getInteger("companyId")));
		int count = employeeAttendanceMapper.updateByPrimaryKeySelective(employeeAttendance);
		if (count == 0) {
			throw new BusiException("更新employeeAttendance表失败");
		}

		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeAttendance.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.deleteDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		dataJSonApply.put("dynamicId", dynamic.getId());
		ApplyRankService.deleteApplyRank(dataJSonApply);

		return employeeAttendance.getId();
	}

	@Override
	public JSONObject getEmployeeAttendance(JSONObject data) throws ParseException {
		employeeAttendance employeeAttendance = employeeAttendanceMapper
				.selectByPrimaryKey(data.getInteger("employeeAttendanceId"));
		if (employeeAttendance == null || employeeAttendance.getState() == dictMapper
				.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
			throw new BusiException("每日打卡不存在");
		}

		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeAttendance.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());

		JSONObject dataJson = new JSONObject();
		dataJson.put("employeeAttendance", this.sEmployeeAttendanceDTO(employeeAttendance));
		dataJson.put("dynamicId", dynamic.getId());
		dataJson.put("ApplyRankDTO", ApplyRankService.getApplyRankByDynamic(dataJSonDynamic));
		return dataJson;
	}

	public EmployeeAttendanceDTO sEmployeeAttendanceDTO(employeeAttendance employeeAttendance) throws ParseException {
		EmployeeAttendanceDTO employeeAttendanceDTO = new EmployeeAttendanceDTO();
		employeeAttendanceDTO.setAddress(employeeAttendance.getAddress());
		employeeAttendanceDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(employeeAttendance.getDateTime()));
		employeeAttendanceDTO.setEmployeeId(employeeAttendance.getEmployeeId());
		employeeAttendanceDTO.setId(employeeAttendance.getId());
		employeeAttendanceDTO.setNote(employeeAttendance.getNote());
		String pics = employeeAttendance.getPic();
		JSONObject dataJson = new JSONObject();
		List<JSONObject> fileListJSON = new ArrayList<JSONObject>();
		List<String> urlList = new ArrayList<String>();
		if (pics != null && !"".equals(pics)) {
			String[] picList = pics.split("-");
			for(int i =0 ;i<picList.length;i++) {
				File picture = new File(ACTPATH+picList[i]);
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
			employeeAttendanceDTO.setPics(dataJson);
		}
		employeeAttendanceDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, employeeAttendance.getState(),
				employeeMapper.selectByPrimaryKey(employeeAttendance.getEmployeeId()).getCompanyId()));
		return employeeAttendanceDTO;
	}

	public Integer affirmEmployeeAttendance(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeAttendance employeeAttendance = employeeAttendanceMapper
				.selectByPrimaryKey(data.getInteger("employeeAttendanceId"));
		if (employeeAttendance.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",
				data.getInteger("companyId"))) {
			throw new BusiException("该每日打卡无法确认提交");
		}
		employeeAttendance
				.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId")));
		int count = employeeAttendanceMapper.updateByPrimaryKeySelective(employeeAttendance);
		if (count == 0) {
			throw new BusiException("更新employeeAttendance表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", employeeAttendance.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dynamic.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId")));
		dynamic.setDateTime(now);
		count = dynamicMapper.updateByPrimaryKeySelective(dynamic);
		if (count == 0) {
			throw new BusiException("更新dynamic表失败");
		}
		applyRank applyRank = applyRankMapper.selectByDynamic(dynamic.getId());
		applyRank.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId")));
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
		return employeeAttendance.getId();
	}

	@Override
	public EmployeeAttendanceDTO getEmployeeAttendanceByEmployeeNew(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer stateSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		Integer stateSB = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败", data.getInteger("companyId"));
		employeeAttendance employeeAttendance = employeeAttendanceMapper.selectByEmployeeIdNew(employeeId,stateSX,stateSB);
		if (employeeAttendance != null) {
			return this.sEmployeeAttendanceDTO(employeeAttendance);
		}
		return null;
	}
}
