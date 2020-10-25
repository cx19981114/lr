package cn.lr.service.customer.lmpl;

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
import cn.lr.dao.customerMapper;
import cn.lr.dao.customerPerformanceMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeLogTomorrowMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dto.CustomerPerformanceDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.applyRank;
import cn.lr.po.customer;
import cn.lr.po.customerPerformance;
import cn.lr.po.dynamic;
import cn.lr.po.employee;
import cn.lr.po.employeeLogTomorrow;
import cn.lr.po.statisticType;
import cn.lr.service.customer.CustomerPerformanceService;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.util.DataLengthUtil;
import cn.lr.util.TimeFormatUtil;
@Service
@Transactional
public class CustomerPerformanceServiceImpl implements CustomerPerformanceService {
	@Autowired
	customerPerformanceMapper customerPerformanceMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	rankMapper rankMapper;
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	applyRankMapper applyRankMapper;
	@Autowired
	customerMapper customerMapper;
	@Autowired
	employeeMapper employeeMapper;	
	@Autowired
	employeeLogTomorrowMapper employeeLogTomorrowMapper;
	
	@Autowired
	ApplyRankService ApplyRankService;
	@Autowired
	DynamicService DynamicService;
	
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${check.flow}")
	private String CHECK_FLOW;
	@Value("${customerPerforman.type}")
	private String CUSTOMERPERFORMAN_TYPE;
	@Override
	public Integer addCustomerPerformance(JSONObject data) {
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		customerPerformance customerPerformance = new customerPerformance();
		customerPerformance.setCustomerId(data.getInteger("customerId"));
		customerPerformance.setDateTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		customerPerformance.setEmployeeIdList("-"+data.getInteger("employeeId")+"-");
		customerPerformance.setMoney(data.getInteger("money"));
		customerPerformance.setNote(data.getString("note"));
		customerPerformance.setState(stateWTJ);
		customerPerformance.setOperatorId(data.getInteger("employeeId"));
		customerPerformance.setType(dictMapper.selectByCodeAndStateName(CUSTOMERPERFORMAN_TYPE, data.getString("type"), data.getInteger("companyId")));
		int count = customerPerformanceMapper.insertSelective(customerPerformance);
		if(count == 0) {
			throw new BusiException("插入customerPerfoemance表失败");
		}
		
		customer customer = customerMapper.selectByPrimaryKey(customerPerformance.getCustomerId());
		customer.setMoney(customer.getMoney() + customerPerformance.getMoney());
		customer.setActiveConsumeTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		count = customerMapper.updateByPrimaryKeySelective(customer);
		if (count == 0) {
			throw new BusiException("更新customer表失败");
		}
		
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("note"));
		dataJSonDynamic.put("name", data.getString("type"));
		dataJSonDynamic.put("id", customerPerformance.getId());
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		stateList.clear();
		stateList.add(stateWSX);
		dataJSonDynamic.put("rank", rankMapper.selectByName(data.getString("type"),data.getInteger("companyId"),stateList));
		int dynamicId = DynamicService.writeDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("employeeId", data.getInteger("employeeId"));
		dataJSonApply.put("dynamicId", dynamicId);
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		ApplyRankService.addApplyRank(dataJSonApply);
		
		customerPerformance = customerPerformanceMapper.selectByPrimaryKey(customerPerformance.getId());
		if(customerPerformance.getState() != stateCG) {
			data.put("customerPerformanceId", customerPerformance.getId());
			this.affirmCustomerPerformance(data);
		}
		return customerPerformance.getId();
	}

	@Override
	public Integer annulCustomerPerformance(JSONObject data) {
		customerPerformance customerPerformance = customerPerformanceMapper.selectByPrimaryKey(data.getInteger("customerPerformanceId"));
		customerPerformance.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")));
		int count = customerPerformanceMapper.updateByPrimaryKeySelective(customerPerformance);
		if (count == 0) {
			throw new BusiException("更新customerPerformance表失败");
		}
		
		if(customerPerformance.getType() == dictMapper.selectByCodeAndStateName(CUSTOMERPERFORMAN_TYPE, "新增顾客", data.getInteger("companyId"))) {
			customer customer = customerMapper.selectByPrimaryKey(customerPerformance.getCustomerId());
			customer.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")));
			count = customerMapper.updateByPrimaryKeySelective(customer);
			if(count == 0) {
				throw new BusiException("更新customer表失败");
			}
		}else {
			customer customer = customerMapper.selectByPrimaryKey(customerPerformance.getCustomerId());
			customer.setMoney(customer.getMoney() - customerPerformance.getMoney());
//			customer.setActiveConsumeTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
			count = customerMapper.updateByPrimaryKeySelective(customer);
			if (count == 0) {
				throw new BusiException("更新customer表失败");
			}
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", dictMapper.selectByCodeAndStateCode(CUSTOMERPERFORMAN_TYPE, customerPerformance.getType(), data.getInteger("companyId")));
		dataJSonDynamic.put("id", customerPerformance.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.annulDynamic(dataJSonDynamic);
		
		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		dataJSonApply.put("dynamicId", dynamic.getId());
		ApplyRankService.annulApplyRank(dataJSonApply);
		
		return customerPerformance.getId();
	}
	
	@Override
	public Integer affirmCustomerPerformance(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		customerPerformance customerPerformance = customerPerformanceMapper.selectByPrimaryKey(data.getInteger("customerPerformanceId"));
		if (customerPerformance.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该业绩无法确认提交");
		}
		customerPerformance.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		int count = customerPerformanceMapper.updateByPrimaryKeySelective(customerPerformance);
		if (count == 0) {
			throw new BusiException("更新customerPerformance表失败");
		}
		if(customerPerformance.getType() == dictMapper.selectByCodeAndStateName(CUSTOMERPERFORMAN_TYPE, "新增顾客", data.getInteger("companyId"))) {
			customer customer = customerMapper.selectByPrimaryKey(customerPerformance.getCustomerId());
			customer.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
			count = customerMapper.updateByPrimaryKeySelective(customer);
			if(count == 0) {
				throw new BusiException("更新customer表失败");
			}
			
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", dictMapper.selectByCodeAndStateCode(CUSTOMERPERFORMAN_TYPE, customerPerformance.getType(), data.getInteger("companyId")));
		dataJSonDynamic.put("id", customerPerformance.getId());
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
		return customerPerformance.getId();
	}

	@Override
	public Integer deleteCustomerPerformance(JSONObject data) {
		customerPerformance customerPerformance = customerPerformanceMapper.selectByPrimaryKey(data.getInteger("customerPerformanceId"));
		if(customerPerformance.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该业绩已提交无法删除");
		}
		customerPerformance.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = customerPerformanceMapper.updateByPrimaryKeySelective(customerPerformance);
		if (count == 0) {
			throw new BusiException("更新customerPerformance表失败");
		}
		
		customer customer = customerMapper.selectByPrimaryKey(customerPerformance.getCustomerId());
		if(customerPerformance.getType() == dictMapper.selectByCodeAndStateName(CUSTOMERPERFORMAN_TYPE, "新增顾客", data.getInteger("companyId"))) {
			customer.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		}else {
			customer.setMoney(customer.getMoney() - customerPerformance.getMoney());
//			customer.setActiveConsumeTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		}
		count = customerMapper.updateByPrimaryKeySelective(customer);
		if(count == 0) {
			throw new BusiException("更新customer表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", dictMapper.selectByCodeAndStateCode(CUSTOMERPERFORMAN_TYPE, customerPerformance.getType(), data.getInteger("companyId")));
		dataJSonDynamic.put("id", customerPerformance.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.deleteDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		dataJSonApply.put("dynamicId", dynamic.getId());
		ApplyRankService.deleteApplyRank(dataJSonApply);
		return customerPerformance.getId();
	}

	@Override
	public customerPerformance getCustomerPerformance(JSONObject data) {
		customerPerformance customerPerformance = customerPerformanceMapper.selectByPrimaryKey(data.getInteger("customerPerformanceId"));
		if(customerPerformance == null || customerPerformance.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
			throw new BusiException("该客户业绩不存在"); 
		}
		return customerPerformance;
	}

	@Override
	public JSONObject getCustomerPerformanceByEmployeeCondition(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		String date = TimeFormatUtil.dateToString(data.getTimestamp("date"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWSQ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请",data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		// day,mon,year
		String type = data.getString("type");
		Integer pageNum = data.getInteger("pageNum");
		List<customerPerformance> customerPerformances = new ArrayList<>();
		int total = 0;
		if(type.equals("day")) {
			customerPerformances = customerPerformanceMapper.selectByEmployeeConditionDay(employeeId,stateList,date,(pageNum - 1) * PAGESIZE,
					PAGESIZE);
			total = customerPerformanceMapper.selectByEmployeeConditionDayCount(employeeId,stateList,date);
		}else if(type.equals("mon")) {
			customerPerformances = customerPerformanceMapper.selectByEmployeeConditionMon(employeeId,stateList,date,(pageNum - 1) * PAGESIZE,
					PAGESIZE);
			total = customerPerformanceMapper.selectByEmployeeConditionMonCount(employeeId,stateList,date);
		}else if(type.equals("year")) {
			customerPerformances = customerPerformanceMapper.selectByEmployeeConditionYear(employeeId,stateList,date,(pageNum - 1) * PAGESIZE,
					PAGESIZE);
			total = customerPerformanceMapper.selectByEmployeeConditionYearCount(employeeId,stateList,date);
		}else if(type.equals("all")) {
			customerPerformances = customerPerformanceMapper.selectByEmployeeCondition(employeeId,stateList,(pageNum - 1) * PAGESIZE,
					PAGESIZE);
			total = customerPerformanceMapper.selectByEmployeeConditionCount(employeeId,stateList);
		}
		List<CustomerPerformanceDTO> jsonObjects = new ArrayList<CustomerPerformanceDTO>();
		int allMoney = 0;
		for (customerPerformance c : customerPerformances) {
			CustomerPerformanceDTO customerPerformanceDTO = this.sCustomerPerformanceDTO(c);
			allMoney += c.getMoney();
			jsonObjects.add(customerPerformanceDTO);
		}
		Page<CustomerPerformanceDTO> page = new Page<CustomerPerformanceDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		JSONObject cpJsonObject = new JSONObject();
		cpJsonObject.put("page", page);
		JSONObject data1 = new JSONObject();
		if(allMoney == 0) {
			data1.put("complete", "0");
		}else {
			data1.put("complete", DataLengthUtil.CountNum(allMoney));
		}
		stateList.clear();
		stateList.add(stateWSQ);
		stateList.add(stateCG);
		stateList.add(stateSHZ);
		stateList.add(stateWSH);
		stateList.add(stateWTJ);
		employeeLogTomorrow employeeLogTomorrow = employeeLogTomorrowMapper.selectByEmployeeIdNew(employeeId,stateList);
		if(employeeLogTomorrow != null) {
			data1.put("goal", DataLengthUtil.CountNum(Integer.valueOf(employeeLogTomorrow.getMoney())));
		}else {
			data1.put("goal", "0");
		}
		cpJsonObject.put("target", data1);
		return cpJsonObject;
	}
	
	public CustomerPerformanceDTO sCustomerPerformanceDTO(customerPerformance customerPerformance) throws ParseException {
		CustomerPerformanceDTO customerPerformanceDTO = new CustomerPerformanceDTO();
		employee opEmployee = employeeMapper.selectByPrimaryKey(customerPerformance.getOperatorId());
		Integer companyId = opEmployee.getCompanyId();
		String[] employeeIdList = customerPerformance.getEmployeeIdList().split("-");
		List<String> employeeNameList = new ArrayList<>();
		for(int i = 0;i<employeeIdList.length;i++) {
			employee employee = employeeMapper.selectByPrimaryKey(Integer.valueOf(employeeIdList[i]));
			employeeNameList.add(employee.getName());
		}
		String type = dictMapper.selectByCodeAndStateCode(CUSTOMERPERFORMAN_TYPE, customerPerformance.getType(),companyId);
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", companyId);
		List<Integer> stateList = new ArrayList<Integer>();
		customerPerformanceDTO.setCustomerName(customerMapper.selectByPrimaryKey(customerPerformance.getCustomerId()).getName());
		customerPerformanceDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(customerPerformance.getDateTime()));
		customerPerformanceDTO.setEmployeeNameList(employeeNameList);
		customerPerformanceDTO.setId(customerPerformance.getId());
		customerPerformanceDTO.setMoney(customerPerformance.getMoney());
		customerPerformanceDTO.setNote(customerPerformance.getNote());
		customerPerformanceDTO.setOperatorName(opEmployee.getName());
		stateList.add(stateWSX);
		customerPerformanceDTO.setRank(rankMapper.selectByName(type, companyId,stateList));
		customerPerformanceDTO.setType(type);
		customerPerformanceDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, customerPerformance.getState(), companyId));
		return customerPerformanceDTO;
	}
	public Page<CustomerPerformanceDTO> getCustomerPerformanceByEmployee(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		List<customerPerformance> customerPerformances = customerPerformanceMapper.selectByEmployee(employeeId,stateList,(pageNum-1)*PAGESIZE, PAGESIZE);
		List<CustomerPerformanceDTO> jsonObjects = new ArrayList<CustomerPerformanceDTO>();
		for(customerPerformance c:customerPerformances) {
			CustomerPerformanceDTO customerPerformanceDTO = this.sCustomerPerformanceDTO(c);
			jsonObjects.add(customerPerformanceDTO);
		}
		int total = customerPerformanceMapper.selectByEmployeeCount(employeeId,stateList);
		Page<CustomerPerformanceDTO> page = new Page<CustomerPerformanceDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}
	public JSONObject getCustomerPerformanceDetail(JSONObject data) throws ParseException {
		customerPerformance customerPerformance = customerPerformanceMapper.selectByPrimaryKey(data.getInteger("customerPerformanceId"));
		if(customerPerformance == null || customerPerformance.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
			throw new BusiException("该客户业绩不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", dictMapper.selectByCodeAndStateCode(CUSTOMERPERFORMAN_TYPE, customerPerformance.getType(), data.getInteger("companyId")));
		dataJSonDynamic.put("id", customerPerformance.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		
		JSONObject dataJson = new JSONObject();
//		dataJson.put("customerPerformance", this.sCustomerPerformanceDTO(customerPerformance));
		dataJson.put("order", this.sCustomerPerformanceDTO(customerPerformance));
		dataJson.put("dynamicId", dynamic.getId());
		dataJson.put("ApplyRankDTO", ApplyRankService.getApplyRankByDynamic(dataJSonDynamic));
		
		return dataJson;
	}
	public List<JSONObject> getCustomerConsumeMoney(JSONObject data){
		Integer customerId = data.getInteger("customerId");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		List<statisticType> statisticTypes = customerPerformanceMapper.selectByCustomerComsumer(customerId, stateList);
		List<JSONObject> moneyList = new ArrayList<JSONObject>();
		int i = 0;
		for(statisticType s:statisticTypes) {
			JSONObject moneyJsonObject = new JSONObject();
			moneyJsonObject.put("title", s.getMonDate());
			moneyJsonObject.put("money", s.getMoney());
			moneyJsonObject.put("index", i++);
			moneyList.add(moneyJsonObject);
		}
		return moneyList;
	}
	public Page<CustomerPerformanceDTO> getCustomerConsumeMoneyList(JSONObject data) throws ParseException{
		Integer customerId = data.getInteger("customerId");
		Integer pageNum = data.getInteger("pageNum");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		List<customerPerformance> customerPerformances = customerPerformanceMapper.selectByCustomerComsumerList(customerId,data.getString("date"),stateList,(pageNum-1)*PAGESIZE, PAGESIZE);
		List<CustomerPerformanceDTO> jsonObjects = new ArrayList<CustomerPerformanceDTO>();
		for(customerPerformance c:customerPerformances) {
			CustomerPerformanceDTO customerPerformanceDTO = this.sCustomerPerformanceDTO(c);
			jsonObjects.add(customerPerformanceDTO);
		}
		int total = customerPerformanceMapper.selectByCustomerComsumerListCount(customerId,data.getString("date"),stateList);
		Page<CustomerPerformanceDTO> page = new Page<CustomerPerformanceDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}
}
