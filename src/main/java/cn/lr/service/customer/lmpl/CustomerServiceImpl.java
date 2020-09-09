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
import cn.lr.dao.customerProjectMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dto.CustomerDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.customer;
import cn.lr.po.customerProject;
import cn.lr.po.employee;
import cn.lr.po.statisticType;
import cn.lr.service.customer.CustomerPerformanceService;
import cn.lr.service.customer.CustomerService;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.util.TimeFormatUtil;
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	public static final String Type = "新增顾客";
	
	@Autowired
	dictMapper dictMapper;
	@Autowired
	customerMapper customerMapper;
	@Autowired
	customerProjectMapper customerProjectMapper;
	@Autowired
	employeeMapper employeeMapper;
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
	CustomerPerformanceService CustomerPerformanceService;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	@Value("${noImg}")
	private String NOIMG;
	
	@Override
	public Integer addCustomer(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		if(employee == null || employee.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该职工不存在");
		}
		customer customer = new customer();
		customer.setBirth(data.getString("birth"));
		customer.setDateTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		customer.setEmployeeId(data.getInteger("employeeId"));
		customer.setHabit(data.getString("habit"));
		customer.setName(data.getString("name"));
		customer.setNote(data.getString("note"));
		customer.setPhone(data.getString("phone"));
		customer.setPlan(data.getString("plan"));
		customer.setSex(data.getString("sex"));
		if(data.getString("pic") != null) {
			customer.setPic(data.getString("pic"));
		}else {
			customer.setPic(NOIMG);
		}
		customer.setMoney(data.getInteger("money"));
		customer.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId")));
		int count = customerMapper.insertSelective(customer);
		if(count == 0) {
			throw new BusiException("插入customer表失败");
		}
		data.put("type", "新增顾客");
		data.put("customerId", customer.getId());
		CustomerPerformanceService.addCustomerPerformance(data);
		return customer.getId();
	}

	@Override
	public Integer modifyCustomer(JSONObject data) {
		customer customer = customerMapper.selectByPrimaryKey(data.getInteger("customerId"));
		if(data.getInteger("employeeId") != customer.getEmployeeId()) {
			throw new BusiException("该客户不属于该员工");
		}
		customer.setBirth(data.getString("brith"));
		customer.setHabit(data.getString("habit"));
		customer.setName(data.getString("name"));
		customer.setNote(data.getString("note"));
		customer.setPhone(data.getString("phone"));
		customer.setPlan(data.getString("plan"));
		customer.setSex(data.getString("sex"));
		customer.setPic(data.getString("pic"));
		int count = customerMapper.updateByPrimaryKeySelective(customer);
		if(count == 0) {
			throw new BusiException("更新customer表失败");
		}
		return customer.getId();
	}

	@Override
	public CustomerDTO getCustomer(JSONObject data) throws ParseException {
		customer customer = customerMapper.selectByPrimaryKey(data.getInteger("customerId"));
		if(customer == null || customer.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
			throw new BusiException("该客户不存在");
		}
		return this.sCustomerDTO(customer);
	}

	@Override
	public Page<JSONObject> getCustomerByEmployee(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		String searchString = data.getString("search");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		List<customer> customers = customerMapper.selectByEmployeeId(employeeId,stateList,searchString,(pageNum-1)*PAGESIZE,PAGESIZE);
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		for(customer c : customers) {
			JSONObject customer = new JSONObject();
			customer.put("id", c.getId());
			customer.put("pic", c.getPic());
			customer.put("name", c.getName());
			customer.put("phone", c.getPhone());
			customer.put("birth", c.getBirth());
			customer.put("employeeName", employeeMapper.selectByPrimaryKey(c.getEmployeeId()).getName());
			jsonObjects.add(customer);
		}
		int total = customerMapper.selectByEmployeeIdCount(employeeId,stateList,searchString);
		Page<JSONObject> page = new Page<JSONObject>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}
	
	@Override
	public List<JSONObject> getCustomerByEmployeeList(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		Integer state = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(state);
		List<customer> customers = customerMapper.selectByEmployeeIdList(employeeId,stateList);
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		for(customer c : customers) {
			JSONObject customerJson = new JSONObject();
			customerJson.put("id", c.getId());
			customerJson.put("name", c.getName());
			jsonObjects.add(customerJson);
		}
		return jsonObjects;
	}
	@Override
	public List<JSONObject> getNewCustomerTypeByEmployee(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		int day = customerMapper.selectByEmployeeIdDayCount(employeeId,stateList);
		int mon = customerMapper.selectByEmployeeIdMonCount(employeeId,stateList);
		int qtr = customerMapper.selectByEmployeeIdQtrCount(employeeId,stateList);
		int year = customerMapper.selectByEmployeeIdYearCount(employeeId,stateList);
		List<JSONObject> customerTypeList = new ArrayList<>();
		JSONObject customerType = new JSONObject();
		customerType.put("value", "本日");
		customerType.put("count", day);
		customerTypeList.add(customerType);
		customerType = new JSONObject();
		customerType.put("value", "本月");
		customerType.put("count", mon);
		customerTypeList.add(customerType);
		customerType = new JSONObject();
		customerType.put("value", "本季");
		customerType.put("count", qtr);
		customerTypeList.add(customerType);
		customerType = new JSONObject();
		customerType.put("value", "本年");
		customerType.put("count", year);
		customerTypeList.add(customerType);
		return customerTypeList;
	}

	@Override
	public Page<JSONObject> getNewCustomerByEmployeeTime(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		String type = data.getString("type");
		String search = data.getString("search");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		List<customer> customers = new ArrayList<>();
		List<JSONObject> jsonObjects = new ArrayList<>();
		int total = 0;
		if(type.equals("day")) {
			customers = customerMapper.selectByEmployeeIdDay(employeeId,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdDayCount(employeeId,stateList);
		}else if(type.equals("mon")) {
			customers = customerMapper.selectByEmployeeIdMon(employeeId,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdMonCount(employeeId,stateList);
		}else if(type.equals("qtr")) {
			customers = customerMapper.selectByEmployeeIdQtr(employeeId,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdQtrCount(employeeId,stateList);
		}else if(type.equals("year")) {
			customers = customerMapper.selectByEmployeeIdYear(employeeId,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdYearCount(employeeId,stateList);
		}
		int count = 0;
		for(customer c:customers) {
			if(!"".equals(search)) {
				if(c.getName().contains(search)) {
					count++;
					JSONObject customer = new JSONObject();
					customer.put("id", c.getId());
					customer.put("pic", c.getPic());
					customer.put("name", c.getName());
					customer.put("phone", c.getPhone());
					customer.put("employeeName", employeeMapper.selectByPrimaryKey(c.getEmployeeId()).getName());
					jsonObjects.add(customer);
				}
			}else {
				JSONObject customer = new JSONObject();
				customer.put("id", c.getId());
				customer.put("pic", c.getPic());
				customer.put("name", c.getName());
				customer.put("phone", c.getPhone());
				customer.put("employeeName", employeeMapper.selectByPrimaryKey(c.getEmployeeId()).getName());
				jsonObjects.add(customer);
			}
		}
		Page<JSONObject> page = new Page<JSONObject>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		if(!"".equals(search)) {
			page.setTotal(count);
		}else {
			page.setTotal(total);
		}
		page.setList(jsonObjects);
		return page;
	}
	@Override
	public Page<JSONObject> getCustomerByConsume(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		String type = data.getString("type");
		String search = data.getString("search");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		List<customer> customers = new ArrayList<>();
		List<JSONObject> jsonObjects = new ArrayList<>();
		int total = 0;
		if(type.equals("mon")) {
			customers = customerMapper.selectByConsumeMon(employeeId,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByConsumeMonCount(employeeId,stateList);
		}else if(type.equals("qtr")) {
			customers = customerMapper.selectByConsumeQtr(employeeId,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByConsumeQtrCount(employeeId,stateList);
		}
		int count = 0;
		for(customer c:customers) {
			if(!"".equals(search)) {
				if(c.getName().contains(search)) {
					count++;
					JSONObject customer = new JSONObject();
					customer.put("id", c.getId());
					customer.put("pic", c.getPic());
					customer.put("name", c.getName());
					customer.put("phone", c.getPhone());
					customer.put("dateTime", TimeFormatUtil.stringToTimeStamp(c.getActiveConsumeTime()));
					customer.put("employeeName", employeeMapper.selectByPrimaryKey(c.getEmployeeId()).getName());
					jsonObjects.add(customer);
				}
			}else {
				JSONObject customer = new JSONObject();
				customer.put("id", c.getId());
				customer.put("pic", c.getPic());
				customer.put("name", c.getName());
				customer.put("phone", c.getPhone());
				customer.put("employeeName", employeeMapper.selectByPrimaryKey(c.getEmployeeId()).getName());
				customer.put("dateTime", TimeFormatUtil.stringToTimeStamp(c.getActiveConsumeTime()));
				jsonObjects.add(customer);
			}
		}
		Page<JSONObject> page = new Page<JSONObject>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		if(!"".equals(search)) {
			page.setTotal(count);
		}else {
			page.setTotal(total);
		}
		page.setList(jsonObjects);
		return page;
	}
	@Override
	public Page<JSONObject> getCustomerByService(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		String type = data.getString("type");
		String search = data.getString("search");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		List<customer> customers = new ArrayList<>();
		List<JSONObject> jsonObjects = new ArrayList<>();
		int total = 0;
		if(type.equals("mon")) {
			customers = customerMapper.selectByServiceMon(employeeId,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByServiceMonCount(employeeId,stateList);
		}else if(type.equals("qtr")) {
			customers = customerMapper.selectByServiceQtr(employeeId,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByServiceQtrCount(employeeId,stateList);
		}
		int count = 0;
		for(customer c:customers) {
			if(!"".equals(search)) {
				if(c.getName().contains(search)) {
					count++;
					JSONObject customer = new JSONObject();
					customer.put("id", c.getId());
					customer.put("pic", c.getPic());
					customer.put("name", c.getName());
					customer.put("phone", c.getPhone());
					customer.put("employeeName", employeeMapper.selectByPrimaryKey(c.getEmployeeId()).getName());
					customer.put("dateTime", TimeFormatUtil.stringToTimeStamp(c.getActiveServiceTime()));
					jsonObjects.add(customer);
				}
			}else {
				JSONObject customer = new JSONObject();
				customer.put("id", c.getId());
				customer.put("pic", c.getPic());
				customer.put("name", c.getName());
				customer.put("phone", c.getPhone());
				customer.put("employeeName", employeeMapper.selectByPrimaryKey(c.getEmployeeId()).getName());
				customer.put("dateTime", TimeFormatUtil.stringToTimeStamp(c.getActiveServiceTime()));
				jsonObjects.add(customer);
			}
		}
		Page<JSONObject> page = new Page<JSONObject>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		if(!"".equals(search)) {
			page.setTotal(count);
		}else {
			page.setTotal(total);
		}
		page.setList(jsonObjects);
		return page;
	}
	public List<JSONObject> getCustomerServiceType(JSONObject data){
		Integer employeeId = data.getInteger("employeeId");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		List<JSONObject> serviceTypeList = new ArrayList<JSONObject>();
		JSONObject serviceTypeJsonObject = new JSONObject();
		int total = customerMapper.selectByConsumeMonCount(employeeId,stateList);
		serviceTypeJsonObject.put("title", "当月消费");
		serviceTypeJsonObject.put("count", total);
		serviceTypeList.add(serviceTypeJsonObject);
		
		serviceTypeJsonObject = new JSONObject();
		total = customerMapper.selectByConsumeQtrCount(employeeId,stateList);
		serviceTypeJsonObject.put("title", "三月消费");
		serviceTypeJsonObject.put("count", total);
		serviceTypeList.add(serviceTypeJsonObject);
		
		serviceTypeJsonObject = new JSONObject();
		total = customerMapper.selectByServiceMonCount(employeeId,stateList);
		serviceTypeJsonObject.put("title", "当月服务");
		serviceTypeJsonObject.put("count", total);
		serviceTypeList.add(serviceTypeJsonObject);
		
		serviceTypeJsonObject = new JSONObject();
		total = customerMapper.selectByServiceQtrCount(employeeId,stateList);
		serviceTypeJsonObject.put("title", "三月服务");
		serviceTypeJsonObject.put("count", total);
		serviceTypeList.add(serviceTypeJsonObject);
		
		return serviceTypeList;
		
	}
	@Override
	public Page<JSONObject> getCustomerByUnService(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		String search = data.getString("search");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		List<customer> customers = new ArrayList<>();
		List<JSONObject> jsonObjects = new ArrayList<>();
		int total = 0;
		customers = customerMapper.selectByUnServiceQtr(employeeId,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
		total = customerMapper.selectByUnServiceQtrCount(employeeId,stateList);
		int count = 0;
		for(customer c:customers) {
			if(!"".equals(search)) {
				if(c.getName().contains(search)) {
					count++;
					JSONObject customer = new JSONObject();
					customer.put("id", c.getId());
					customer.put("pic", c.getPic());
					customer.put("name", c.getName());
					customer.put("phone", c.getPhone());
					customer.put("employeeName", employeeMapper.selectByPrimaryKey(c.getEmployeeId()).getName());
					customer.put("dateTime", TimeFormatUtil.stringToTimeStamp(c.getActiveServiceTime()));
					jsonObjects.add(customer);
				}
			}else {
				JSONObject customer = new JSONObject();
				customer.put("id", c.getId());
				customer.put("pic", c.getPic());
				customer.put("name", c.getName());
				customer.put("phone", c.getPhone());
				customer.put("employeeName", employeeMapper.selectByPrimaryKey(c.getEmployeeId()).getName());
				customer.put("dateTime", TimeFormatUtil.stringToTimeStamp(c.getActiveServiceTime()));
				jsonObjects.add(customer);
			}
		}
		Page<JSONObject> page = new Page<JSONObject>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		if(!"".equals(search)) {
			page.setTotal(count);
		}else {
			page.setTotal(total);
		}
		page.setList(jsonObjects);
		return page;
	}
	public CustomerDTO sCustomerDTO(customer customer) throws ParseException {
		employee employee = employeeMapper.selectByPrimaryKey(customer.getEmployeeId());
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", employee.getCompanyId());
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", employee.getCompanyId());
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", employee.getCompanyId());
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", employee.getCompanyId());
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateSHZ);
		stateList.add(stateWSH);
		stateList.add(stateWTJ);
		List<customerProject> projectList = customerProjectMapper.selectByCustomer(customer.getId(), stateList);
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setProjectNum(projectList.size());
		customerDTO.setBirth(customer.getBirth());
		customerDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(customer.getDateTime()));
		customerDTO.setEmployeeId(customer.getEmployeeId());
		customerDTO.setEmployeeName(employeeMapper.selectByPrimaryKey(customer.getEmployeeId()).getName());
		customerDTO.setHabit(customer.getHabit());
		customerDTO.setId(customer.getId());
		customerDTO.setMoney(customer.getMoney());
		customerDTO.setName(customer.getName());
		customerDTO.setNote(customer.getNote());
		customerDTO.setPhone(customer.getPhone());
		customerDTO.setPic(customer.getPic());
		customerDTO.setPlan(customer.getPlan());
		customerDTO.setSex(customer.getSex());
		customerDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, customer.getState(),employee.getCompanyId()));
		return customerDTO;
	}
	@Override
	public JSONObject getCustomerChart(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		String[] category = new String[] {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
		JSONObject option = new JSONObject();
		JSONObject xAxis = new JSONObject();
		JSONObject yAxis = new JSONObject();
		List<JSONObject> seriesList = new ArrayList<JSONObject>();
		
		List<Integer> line = new ArrayList<Integer>();
		JSONObject series = new JSONObject();
		for(int i = 1;i <= 7 ;i++) {
			line.add(customerMapper.selectByConsumeDay(employeeId, stateList,i));
		}
		series.put("data", line);
		series.put("type", "line");
		seriesList.add(series);
		
		line = new ArrayList<Integer>();
		series = new JSONObject();
		for(int i = 1;i <= 7 ;i++) {
			line.add(customerMapper.selectByServiceDay(employeeId, stateList,i));
		}
		series.put("data", line);
		series.put("type", "line");
		seriesList.add(series);
		
		xAxis.put("type", "category");
		xAxis.put("data", category);
		option.put("xAxis", xAxis);
		
		yAxis.put("type", "value");
		option.put("yAxis", yAxis);
		
		series.put("data", line);
		series.put("type", "line");
		option.put("series", seriesList);
		return option;
	}
	
}
