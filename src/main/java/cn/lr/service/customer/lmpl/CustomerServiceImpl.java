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
	@Override
	public Integer addCustomer(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		if(employee == null || employee.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该职工不存在");
		}
		customer customer = new customer();
		customer.setBirth(data.getString("brith"));
		customer.setDateTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		customer.setEmployeeId(data.getInteger("employeeId"));
		customer.setHabit(data.getString("habit"));
		customer.setName(data.getString("name"));
		customer.setNote(data.getString("note"));
		customer.setPhone(data.getString("phone"));
		customer.setPlan(data.getString("plan"));
		customer.setSex(data.getString("sex"));
		customer.setPic(data.getString("pic"));
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
		if(customer == null || customer.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功",data.getInteger("companyId"))) {
			throw new BusiException("该客户id不存在");
		}
		return this.sCustomerDTO(customer);
	}

	@Override
	public Page<JSONObject> getCustomerByEmployee(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		String searchString = data.getString("search");
		Integer state = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<customer> customers = customerMapper.selectByEmployeeId(employeeId,state,searchString,(pageNum-1)*PAGESIZE,PAGESIZE);
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
		int total = customerMapper.selectByEmployeeIdCount(employeeId,state,searchString);
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
		List<customer> customers = customerMapper.selectByEmployeeIdList(employeeId,state);
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
		Integer state = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		int day = customerMapper.selectByEmployeeIdDayCount(employeeId,state);
		int mon = customerMapper.selectByEmployeeIdMonCount(employeeId,state);
		int qtr = customerMapper.selectByEmployeeIdQtrCount(employeeId,state);
		int year = customerMapper.selectByEmployeeIdYearCount(employeeId,state);
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
		Integer state = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<customer> customers = new ArrayList<>();
		List<JSONObject> jsonObjects = new ArrayList<>();
		int total = 0;
		if(type.equals("day")) {
			customers = customerMapper.selectByEmployeeIdDay(employeeId,state,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdDayCount(employeeId,state);
		}else if(type.equals("mon")) {
			customers = customerMapper.selectByEmployeeIdMon(employeeId,state,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdMonCount(employeeId,state);
		}else if(type.equals("qtr")) {
			customers = customerMapper.selectByEmployeeIdQtr(employeeId,state,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdQtrCount(employeeId,state);
		}else if(type.equals("year")) {
			customers = customerMapper.selectByEmployeeIdYear(employeeId,state,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdYearCount(employeeId,state);
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
	public CustomerDTO sCustomerDTO(customer customer) throws ParseException {
		employee employee = employeeMapper.selectByPrimaryKey(customer.getEmployeeId());
		Integer stateInteger = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", employee.getCompanyId());
		List<customerProject> projectList = customerProjectMapper.selectByCustomer(employee.getId(), stateInteger);
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
}
