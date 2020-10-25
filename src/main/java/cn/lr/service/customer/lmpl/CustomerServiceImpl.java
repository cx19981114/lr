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
import cn.lr.dao.companyMapper;
import cn.lr.dao.customerMapper;
import cn.lr.dao.customerPerformanceMapper;
import cn.lr.dao.customerProjectMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.postMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dto.CustomerDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.customer;
import cn.lr.po.customerPerformance;
import cn.lr.po.customerProject;
import cn.lr.po.employee;
import cn.lr.po.post;
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
	customerPerformanceMapper customerPerformanceMapper;
	@Autowired
	postMapper postMapper;
	@Autowired
	companyMapper companyMapper;
	
	
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
	@Value("${customerPerforman.type}")
	private String CUSTOMERPERFORMAN_TYPE;
	
	@Override
	public Integer addCustomer(JSONObject data) {
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		List<Integer> stateList = new ArrayList<Integer>();
		String[] employeeIdList = data.getString("employeeId").split("-");
		for(int i = 0;i<employeeIdList.length;i++) {
			employee employee = employeeMapper.selectByPrimaryKey(Integer.valueOf(employeeIdList[i]));
			if(employee == null || employee.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
				throw new BusiException("该职工不存在");
			}
		}
		customer customer = new customer();
		customer.setBirth(data.getString("birth"));
		customer.setDateTime(now);
		customer.setEmployeeIdList("-"+data.getString("employeeId")+"-");
		customer.setHabit(data.getString("habit"));
		customer.setName(data.getString("name"));
		customer.setNote(data.getString("note"));
		customer.setPhone(data.getString("phone"));
		customer.setPlan(data.getString("plan"));
		customer.setSex(data.getString("sex"));
		customer.setOperatorId(data.getInteger("operatorId"));
		if(data.getString("pic") != null && ! "".equals(data.getString("pic"))) {
			customer.setPic(data.getString("pic"));
		}else {
			customer.setPic(NOIMG);
		}
		customer.setMoney(data.getInteger("money"));
		customer.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId")));
		customer.setActiveConsumeTime(now);
		customer.setActiveServiceTime(now);
		int count = customerMapper.insertSelective(customer);
		if(count == 0) {
			throw new BusiException("插入customer表失败");
		}
		
		customerPerformance customerPerformance = new customerPerformance();
		customerPerformance.setCustomerId(customer.getId());
		customerPerformance.setDateTime(now);
		customerPerformance.setEmployeeIdList("-"+data.getString("employeeId")+"-");
		customerPerformance.setMoney(data.getInteger("money"));
		customerPerformance.setNote(data.getString("note"));
		customerPerformance.setState(stateWTJ);
		customerPerformance.setOperatorId(data.getInteger("operatorId"));
		customerPerformance.setType(dictMapper.selectByCodeAndStateName(CUSTOMERPERFORMAN_TYPE, "新增顾客", data.getInteger("companyId")));
		count = customerPerformanceMapper.insertSelective(customerPerformance);
		if(count == 0) {
			throw new BusiException("插入customerPerfoemance表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("note"));
		dataJSonDynamic.put("name", "新增顾客");
		dataJSonDynamic.put("id", customerPerformance.getId());
		dataJSonDynamic.put("employeeId", data.getInteger("operatorId"));
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		stateList.clear();
		stateList.add(stateWSX);
		dataJSonDynamic.put("rank", rankMapper.selectByName("新增顾客",data.getInteger("companyId"),stateList));
		int dynamicId = DynamicService.writeDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("employeeId", data.getInteger("operatorId"));
		dataJSonApply.put("dynamicId", dynamicId);
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		ApplyRankService.addApplyRank(dataJSonApply);
		
		customerPerformance = customerPerformanceMapper.selectByPrimaryKey(customerPerformance.getId());
		if(customerPerformance.getState() != stateCG) {
			data.put("customerPerformanceId", customerPerformance.getId());
			CustomerPerformanceService.affirmCustomerPerformance(data);
		}
		return customer.getId();
	}

	@Override
	public Integer modifyCustomer(JSONObject data) {
		customer customer = customerMapper.selectByPrimaryKey(data.getInteger("customerId"));
		if(customer.getOperatorId() != data.getInteger("operatorId") && !customer.getEmployeeIdList().contains("-"+data.getInteger("operatorId")+"-")) {
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
		customer.setEmployeeIdList("-"+data.getString("employeeId")+"-");
		int count = customerMapper.updateByPrimaryKeySelective(customer);
		if(count == 0) {
			throw new BusiException("更新customer表失败");
		}
		customerPerformance customerPerformance = customerPerformanceMapper.selectByCustomerId(customer.getId());
		customerPerformance.setEmployeeIdList("-"+data.getString("employeeId")+"-");
		count = customerPerformanceMapper.updateByPrimaryKeySelective(customerPerformance);
		if(count == 0) {
			throw new BusiException("更新customerPerformance表失败");
		}
		return customer.getId();
	}
	@Override
	public Integer modifyCustomerPrincipal(JSONObject data) {
		customer customer = customerMapper.selectByPrimaryKey(data.getInteger("customerId"));
		if(!customer.getEmployeeIdList().contains("-"+String.valueOf(data.getInteger("operatorId")+"-"))) {
			throw new BusiException("该客户不属于该员工");
		}
		customer.setEmployeeIdList("-"+data.getString("employeeId")+"-");
		int count = customerMapper.updateByPrimaryKeySelective(customer);
		if(count == 0) {
			throw new BusiException("更新customer表失败");
		}
		customerPerformance customerPerformance = customerPerformanceMapper.selectByCustomerId(customer.getId());
		customerPerformance.setEmployeeIdList("-"+data.getString("employeeId")+"-");
		count = customerPerformanceMapper.updateByPrimaryKeySelective(customerPerformance);
		if(count == 0) {
			throw new BusiException("更新customerPerformance表失败");
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

	public List<Integer> getEmployeeeList(Integer employeeId){
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		post post = postMapper.selectByPrimaryKey(employee.getPostId());
		List<Integer> employeeList = new ArrayList<Integer>();
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", employee.getCompanyId());
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		if(post.getName().equals("经营者")) {
			List<employee> employees = employeeMapper.selectByCompanyId(employee.getCompanyId(), stateList, 0, Integer.MAX_VALUE);
			for(employee e:employees) {
				employeeList.add(e.getId());
			}
		}else if(post.getName().equals("店长") || post.getName().equals("顾问")) {
			employeeList.add(employeeId);
			String under = employee.getUnderIdList();
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
					if (employee3 == null) {
						throw new BusiException("该职员不存在");
					}
					if(employee3.getUnderIdList() != null && !"".equals(employee3.getUnderIdList())) {
						under += employee3.getUnderIdList();
						flag = 1;
					}
					employeeList.add(Integer.valueOf(underIdList[j]));
				}
			}
		}else {
			employeeList.add(employeeId);
		}
		return employeeList;
	}
	@Override
	public Page<JSONObject> getCustomerByEmployee(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		List<Integer> employeeIdList = this.getEmployeeeList(employeeId);
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
		customerMapper.selectByEmployeeId(employeeIdList,stateList,searchString,(pageNum-1)*PAGESIZE,PAGESIZE);
		System.out.println(employeeIdList.toString());
		List<customer> customers = customerMapper.selectByEmployeeId(employeeIdList,stateList,searchString,(pageNum-1)*PAGESIZE,PAGESIZE);
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		for(customer c : customers) {
			JSONObject customer = new JSONObject();
			customer.put("id", c.getId());
			customer.put("pic", c.getPic());
			customer.put("name", c.getName());
			customer.put("phone", c.getPhone());
			customer.put("birth", c.getBirth());
			String employeeIdListByCustomer = c.getEmployeeIdList();
			List<JSONObject> employeeJsonList = new ArrayList<JSONObject>();
			if(employeeIdListByCustomer != null && !"".equals(employeeIdListByCustomer)) {
				String[] emStrings = employeeIdListByCustomer.split("-");
				if(emStrings.length > 1) {
					for(int i = 1;i<emStrings.length;i++) {
						JSONObject employeeJson = new JSONObject();
						String name = employeeMapper.selectByPrimaryKey(Integer.valueOf(emStrings[i])).getName();
						employeeJson.put("name", name);
						employeeJsonList.add(employeeJson);
					}
				}
			}
			customer.put("employeeName", employeeJsonList);
			jsonObjects.add(customer);
		}
		int total = customerMapper.selectByEmployeeIdCount(employeeIdList,stateList,searchString);
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
		List<Integer> employeeIdList = this.getEmployeeeList(employeeId);
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		List<customer> customers = customerMapper.selectByEmployeeIdList(employeeIdList,stateList);
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
		List<Integer> employeeIdList = this.getEmployeeeList(employeeId);
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateWTJ);
		stateList.add(stateWSH);
		stateList.add(stateSHZ);
		int day = customerMapper.selectByEmployeeIdDayCount(employeeIdList,stateList);
		int mon = customerMapper.selectByEmployeeIdMonCount(employeeIdList,stateList);
		int qtr = customerMapper.selectByEmployeeIdQtrCount(employeeIdList,stateList);
		int year = customerMapper.selectByEmployeeIdYearCount(employeeIdList,stateList);
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
		List<Integer> employeeIdList = this.getEmployeeeList(employeeId);
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
			customers = customerMapper.selectByEmployeeIdDay(employeeIdList,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdDayCount(employeeIdList,stateList);
		}else if(type.equals("mon")) {
			customers = customerMapper.selectByEmployeeIdMon(employeeIdList,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdMonCount(employeeIdList,stateList);
		}else if(type.equals("qtr")) {
			customers = customerMapper.selectByEmployeeIdQtr(employeeIdList,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdQtrCount(employeeIdList,stateList);
		}else if(type.equals("year")) {
			customers = customerMapper.selectByEmployeeIdYear(employeeIdList,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByEmployeeIdYearCount(employeeIdList,stateList);
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
					String employeeIdListByCustomer = c.getEmployeeIdList();
					List<JSONObject> employeeJsonList = new ArrayList<JSONObject>();
					if(employeeIdListByCustomer != null && !"".equals(employeeIdListByCustomer)) {
						String[] emStrings = employeeIdListByCustomer.split("-");
						if(emStrings.length > 1) {
							for(int i = 1;i<emStrings.length;i++) {
								JSONObject employeeJson = new JSONObject();
								String name = employeeMapper.selectByPrimaryKey(Integer.valueOf(emStrings[i])).getName();
								employeeJson.put("name", name);
								employeeJsonList.add(employeeJson);
							}
						}
					}
					customer.put("employeeName", employeeJsonList);
					jsonObjects.add(customer);
				}
			}else {
				JSONObject customer = new JSONObject();
				customer.put("id", c.getId());
				customer.put("pic", c.getPic());
				customer.put("name", c.getName());
				customer.put("phone", c.getPhone());
				String employeeIdListByCustomer = c.getEmployeeIdList();
				List<JSONObject> employeeJsonList = new ArrayList<JSONObject>();
				if(employeeIdListByCustomer != null && !"".equals(employeeIdListByCustomer)) {
					String[] emStrings = employeeIdListByCustomer.split("-");
					if(emStrings.length > 1) {
						for(int i = 1;i<emStrings.length;i++) {
							JSONObject employeeJson = new JSONObject();
							String name = employeeMapper.selectByPrimaryKey(Integer.valueOf(emStrings[i])).getName();
							employeeJson.put("name", name);
							employeeJsonList.add(employeeJson);
						}
					}
				}
				customer.put("employeeName", employeeJsonList);
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
		List<Integer> employeeIdList = this.getEmployeeeList(employeeId);
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
			customers = customerMapper.selectByConsumeMon(employeeIdList,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByConsumeMonCount(employeeIdList,stateList);
		}else if(type.equals("qtr")) {
			customers = customerMapper.selectByConsumeQtr(employeeIdList,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByConsumeQtrCount(employeeIdList,stateList);
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
					String employeeIdListByCustomer = c.getEmployeeIdList();
					List<JSONObject> employeeJsonList = new ArrayList<JSONObject>();
					if(employeeIdListByCustomer != null && !"".equals(employeeIdListByCustomer)) {
						String[] emStrings = employeeIdListByCustomer.split("-");
						if(emStrings.length > 1) {
							for(int i = 1;i<emStrings.length;i++) {
								JSONObject employeeJson = new JSONObject();
								String name = employeeMapper.selectByPrimaryKey(Integer.valueOf(emStrings[i])).getName();
								employeeJson.put("name", name);
								employeeJsonList.add(employeeJson);
							}
						}
					}
					customer.put("employeeName", employeeJsonList);
					jsonObjects.add(customer);
				}
			}else {
				JSONObject customer = new JSONObject();
				customer.put("id", c.getId());
				customer.put("pic", c.getPic());
				customer.put("name", c.getName());
				customer.put("phone", c.getPhone());
				String employeeIdListByCustomer = c.getEmployeeIdList();
				List<JSONObject> employeeJsonList = new ArrayList<JSONObject>();
				if(employeeIdListByCustomer != null && !"".equals(employeeIdListByCustomer)) {
					String[] emStrings = employeeIdListByCustomer.split("-");
					if(emStrings.length > 1) {
						for(int i = 1;i<emStrings.length;i++) {
							JSONObject employeeJson = new JSONObject();
							String name = employeeMapper.selectByPrimaryKey(Integer.valueOf(emStrings[i])).getName();
							employeeJson.put("name", name);
							employeeJsonList.add(employeeJson);
						}
					}
				}
				customer.put("employeeName", employeeJsonList);
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
		List<Integer> employeeIdList = this.getEmployeeeList(employeeId);
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
			customers = customerMapper.selectByServiceMon(employeeIdList,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByServiceMonCount(employeeIdList,stateList);
		}else if(type.equals("qtr")) {
			customers = customerMapper.selectByServiceQtr(employeeIdList,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
			total = customerMapper.selectByServiceQtrCount(employeeIdList,stateList);
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
					String employeeIdListByCustomer = c.getEmployeeIdList();
					List<JSONObject> employeeJsonList = new ArrayList<JSONObject>();
					if(employeeIdListByCustomer != null && !"".equals(employeeIdListByCustomer)) {
						String[] emStrings = employeeIdListByCustomer.split("-");
						if(emStrings.length > 1) {
							for(int i = 1;i<emStrings.length;i++) {
								JSONObject employeeJson = new JSONObject();
								String name = employeeMapper.selectByPrimaryKey(Integer.valueOf(emStrings[i])).getName();
								employeeJson.put("name", name);
								employeeJsonList.add(employeeJson);
							}
						}
					}
					customer.put("employeeName", employeeJsonList);
					customer.put("dateTime", TimeFormatUtil.stringToTimeStamp(c.getActiveServiceTime()));
					jsonObjects.add(customer);
				}
			}else {
				JSONObject customer = new JSONObject();
				customer.put("id", c.getId());
				customer.put("pic", c.getPic());
				customer.put("name", c.getName());
				customer.put("phone", c.getPhone());
				String employeeIdListByCustomer = c.getEmployeeIdList();
				List<JSONObject> employeeJsonList = new ArrayList<JSONObject>();
				if(employeeIdListByCustomer != null && !"".equals(employeeIdListByCustomer)) {
					String[] emStrings = employeeIdListByCustomer.split("-");
					if(emStrings.length > 1) {
						for(int i = 1;i<emStrings.length;i++) {
							JSONObject employeeJson = new JSONObject();
							String name = employeeMapper.selectByPrimaryKey(Integer.valueOf(emStrings[i])).getName();
							employeeJson.put("name", name);
							employeeJsonList.add(employeeJson);
						}
					}
				}
				customer.put("employeeName", employeeJsonList);
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
		List<Integer> employeeIdList = this.getEmployeeeList(employeeId);
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
		int total = customerMapper.selectByConsumeMonCount(employeeIdList,stateList);
		serviceTypeJsonObject.put("title", "当月消费");
		serviceTypeJsonObject.put("count", total);
		serviceTypeList.add(serviceTypeJsonObject);
		
		serviceTypeJsonObject = new JSONObject();
		total = customerMapper.selectByConsumeQtrCount(employeeIdList,stateList);
		serviceTypeJsonObject.put("title", "三月消费");
		serviceTypeJsonObject.put("count", total);
		serviceTypeList.add(serviceTypeJsonObject);
		
		serviceTypeJsonObject = new JSONObject();
		total = customerMapper.selectByServiceMonCount(employeeIdList,stateList);
		serviceTypeJsonObject.put("title", "当月服务");
		serviceTypeJsonObject.put("count", total);
		serviceTypeList.add(serviceTypeJsonObject);
		
		serviceTypeJsonObject = new JSONObject();
		total = customerMapper.selectByServiceQtrCount(employeeIdList,stateList);
		serviceTypeJsonObject.put("title", "三月服务");
		serviceTypeJsonObject.put("count", total);
		serviceTypeList.add(serviceTypeJsonObject);
		
		serviceTypeJsonObject = new JSONObject();
		total = customerMapper.selectByUnServiceQtrCount(employeeIdList,stateList);
		serviceTypeJsonObject.put("title", "三月未消费");
		serviceTypeJsonObject.put("count", total);
		serviceTypeList.add(serviceTypeJsonObject);
		
		return serviceTypeList;
		
	}
	@Override
	public Page<JSONObject> getCustomerByUnService(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		List<Integer> employeeIdList = this.getEmployeeeList(employeeId);
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
		customers = customerMapper.selectByUnServiceQtr(employeeIdList,stateList,(pageNum-1)*PAGESIZE,PAGESIZE);
		total = customerMapper.selectByUnServiceQtrCount(employeeIdList,stateList);
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
					String employeeIdListByCustomer = c.getEmployeeIdList();
					List<JSONObject> employeeJsonList = new ArrayList<JSONObject>();
					if(employeeIdListByCustomer != null && !"".equals(employeeIdListByCustomer)) {
						String[] emStrings = employeeIdListByCustomer.split("-");
						if(emStrings.length > 1) {
							for(int i = 1;i<emStrings.length;i++) {
								JSONObject employeeJson = new JSONObject();
								String name = employeeMapper.selectByPrimaryKey(Integer.valueOf(emStrings[i])).getName();
								employeeJson.put("name", name);
								employeeJsonList.add(employeeJson);
							}
						}
					}
					customer.put("employeeName", employeeJsonList);
					customer.put("dateTime", TimeFormatUtil.stringToTimeStamp(c.getActiveServiceTime()));
					jsonObjects.add(customer);
				}
			}else {
				JSONObject customer = new JSONObject();
				customer.put("id", c.getId());
				customer.put("pic", c.getPic());
				customer.put("name", c.getName());
				customer.put("phone", c.getPhone());
				String employeeIdListByCustomer = c.getEmployeeIdList();
				List<JSONObject> employeeJsonList = new ArrayList<JSONObject>();
				if(employeeIdListByCustomer != null && !"".equals(employeeIdListByCustomer)) {
					String[] emStrings = employeeIdListByCustomer.split("-");
					if(emStrings.length > 1) {
						for(int i = 1;i<emStrings.length;i++) {
							JSONObject employeeJson = new JSONObject();
							String name = employeeMapper.selectByPrimaryKey(Integer.valueOf(emStrings[i])).getName();
							employeeJson.put("name", name);
							employeeJsonList.add(employeeJson);
						}
					}
				}
				customer.put("employeeName", employeeJsonList);
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
		employee opEmployee = employeeMapper.selectByPrimaryKey(customer.getOperatorId());
		Integer companyId = opEmployee.getCompanyId();
		List<JSONObject> employeeList = new ArrayList<>();
		String[] employeeIdList = customer.getEmployeeIdList().split("-");
		if(employeeIdList.length > 1) {
			for(int i = 1;i<employeeIdList.length;i++) {
				JSONObject employeeJson = new JSONObject();
				employee employee = employeeMapper.selectByPrimaryKey(Integer.valueOf(employeeIdList[i]));
				employeeJson.put("name", employee.getName());
				employeeJson.put("id", employee.getId());
				employeeList.add(employeeJson);
			}
		}
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",companyId);
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",companyId);
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", companyId);
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", companyId);
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
		customerDTO.setEmployeeList(employeeList);
		customerDTO.setOperatorId(opEmployee.getId());
		customerDTO.setOperatorName(opEmployee.getName());
		customerDTO.setHabit(customer.getHabit());
		customerDTO.setId(customer.getId());
		customerDTO.setMoney(customer.getMoney());
		customerDTO.setName(customer.getName());
		customerDTO.setNote(customer.getNote());
		customerDTO.setPhone(customer.getPhone());
		customerDTO.setPic(customer.getPic());
		customerDTO.setPlan(customer.getPlan());
		customerDTO.setSex(customer.getSex());
		customerDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, customer.getState(),companyId));
		return customerDTO;
	}
	@Override
	public JSONObject getCustomerChart(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		List<Integer> employeeIdList = this.getEmployeeeList(employeeId);
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
			line.add(customerMapper.selectByConsumeDay(employeeIdList, stateList,i));
		}
		series.put("data", line);
		series.put("type", "line");
		seriesList.add(series);
		
		line = new ArrayList<Integer>();
		series = new JSONObject();
		for(int i = 1;i <= 7 ;i++) {
			line.add(customerMapper.selectByServiceDay(employeeIdList, stateList,i));
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
