package cn.lr.service.customer.lmpl;

import java.io.File;
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
import cn.lr.dao.orderMapper;
import cn.lr.dao.projectMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dto.CustomerProjectDTO;
import cn.lr.dto.CustomerProjectDetailDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.applyRank;
import cn.lr.po.customer;
import cn.lr.po.customerProject;
import cn.lr.po.dynamic;
import cn.lr.po.employee;
import cn.lr.po.order;
import cn.lr.po.project;
import cn.lr.service.customer.CustomerProjectService;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.util.TimeFormatUtil;
@Service
@Transactional
public class CustomerProjectServiceImpl implements CustomerProjectService {
	public static final String Type = "顾客购买项目";
	
	@Autowired
	projectMapper projectMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	customerProjectMapper customerProjectMapper;
	@Autowired
	customerMapper customerMapper;
	@Autowired
	rankMapper rankMapper;
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	applyRankMapper applyRankMapper;
	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	orderMapper orderMapper;
	
	@Autowired
	ApplyRankService ApplyRankService;
	@Autowired
	DynamicService DynamicService;

	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	@Value("${order.flow}")
	private String ORDER_FLOW;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${picPath}")
	private String PATH;
	@Value("${picActPath}")
	private String ACTPATH;
	
	@Override
	public Integer addCustomerProject(JSONObject data) {
		customer customer = customerMapper.selectByPrimaryKey(data.getInteger("customerId"));
		project project = projectMapper.selectByPrimaryKey(data.getInteger("projectId"));
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		if(customer.getMoney() < project.getMoney()) {
			throw new BusiException("该顾客余额不足");
		}
		customerProject customerProject = new customerProject();
		customerProject.setCount(project.getNum());
		customerProject.setCustomerId(data.getInteger("customerId"));
		customerProject.setDateTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		customerProject.setProjectId(project.getId());
		customerProject.setEmployeeId(customer.getEmployeeId());
		customerProject.setRestCount(project.getNum());
		customerProject.setState(stateWTJ);
		int count =  customerProjectMapper.insertSelective(customerProject);
		if(count == 0) {
			throw new BusiException("插入customerProject表失败");
		}
		
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("note"));
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", customerProject.getId());
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
		
		data.put("customerProjectId", customerProject.getId());
		this.affirmCustomerProject(data);
		return customerProject.getId();
	}

	@Override
	public List<JSONObject> getCustomerProjectByCustomer(JSONObject data) throws ParseException {
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateYWC = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "已完成", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		List<Integer> stateListApply = new ArrayList<Integer>();
		stateListApply.add(stateCG);
		List<Integer> stateListOrder = new ArrayList<Integer>();
		stateListOrder.add(stateYWC);
		List<customerProject> customerProjects = customerProjectMapper.selectByCustomer(data.getInteger("customerId"),stateList);
		List<JSONObject> jsonCustomers  = new ArrayList<JSONObject>();
		for(customerProject c:customerProjects) {
			project project = projectMapper.selectByPrimaryKey(c.getProjectId());
			JSONObject jsonCustomer = new JSONObject();
			jsonCustomer.put("id", c.getId());
			jsonCustomer.put("name",project.getName());
			jsonCustomer.put("count", c.getCount());
			jsonCustomer.put("restCount", c.getRestCount());
			jsonCustomer.put("money", project.getMoney());
			List<order> orders = orderMapper.selectByProjectIdHistory(c.getId(),stateListApply,stateListOrder);
			List<JSONObject> orderJsonList = new ArrayList<>();
			for(order o: orders) {
				JSONObject orderJson = new JSONObject();
				employee employee = employeeMapper.selectByPrimaryKey(o.getEmployeeId());
				orderJson.put("actDateTime", TimeFormatUtil.stringToTimeStamp(o.getActStartTime()));
				orderJson.put("employeeName", employee.getName());
				orderJson.put("id", o.getId());
				orderJsonList.add(orderJson);
			}
			jsonCustomer.put("history", orderJsonList);
			jsonCustomers.add(jsonCustomer);
		}
		return jsonCustomers;
	}
	
	@Override
	public customerProject getCustomerProject(JSONObject data) {
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(data.getInteger("customerProjectId"));
		if(customerProject == null || customerProject.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"))) {
			throw new BusiException("该客户项目不存在");
		}
		return customerProject;
	}

	@Override
	public Integer deleteCustomerProject(JSONObject data) {
		customerProject customerProject = customerProjectMapper
				.selectByPrimaryKey(data.getInteger("customerProjectId"));
		if(customerProject.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该服务顾客项目已提交无法删除");
		}
		customerProject.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
		if (count == 0) {
			throw new BusiException("更新customerProject表失败");
		}

		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", customerProject.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.deleteDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		dataJSonApply.put("dynamicId", dynamic.getId());
		ApplyRankService.deleteApplyRank(dataJSonApply);
		
		return customerProject.getId();
	}
	@Override
	public Integer annulCustomerProject(JSONObject data) {
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(data.getInteger("customerProjectId"));
		customerProject.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")));
		int count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
		if (count == 0) {
			throw new BusiException("更新customerProject表失败");
		}
		
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", customerProject.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		DynamicService.annulDynamic(dataJSonDynamic);
		
		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		dataJSonApply.put("dynamicId", dynamic.getId());
		ApplyRankService.annulApplyRank(dataJSonApply);
		
		return customerProject.getId();
	}
	public Integer affirmCustomerProject(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(data.getInteger("customerProjectId"));
		if (customerProject.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该服务顾客项目无法确认提交");
		}
		customerProject.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		int count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
		if (count == 0) {
			throw new BusiException("更新customerProject表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", customerProject.getId());
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
		return customerProject.getId();
	}
	public Page<CustomerProjectDTO> getCustomerProjectByEmployee(JSONObject data) throws ParseException {
		Integer employeeId = data.getInteger("employeeId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(state);
		List<customerProject> customerProjects = customerProjectMapper.selectByEmployee(employeeId,stateList,(pageNum-1)*PAGESIZE, PAGESIZE);
		List<CustomerProjectDTO> jsonObjects = new ArrayList<CustomerProjectDTO>();
		for(customerProject c:customerProjects) {
			CustomerProjectDTO customerProjectDTO = this.sCustomerProjectDTO(c);
			jsonObjects.add(customerProjectDTO);
		}
		int total = customerProjectMapper.selectByEmployeeCount(employeeId,stateList);
		Page<CustomerProjectDTO> page = new Page<CustomerProjectDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}
	
	public CustomerProjectDTO sCustomerProjectDTO(customerProject customerProject) throws ParseException {
		CustomerProjectDTO customerProjectDTO = new CustomerProjectDTO();
		customer customer = customerMapper.selectByPrimaryKey(customerProject.getCustomerId());
		employee employee = employeeMapper.selectByPrimaryKey(customerProject.getEmployeeId());
		project project = projectMapper.selectByPrimaryKey(customerProject.getProjectId());
		customerProjectDTO.setCount(customerProject.getCount());
		customerProjectDTO.setCustomerName(customer.getName());
		customerProjectDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(customerProject.getDateTime()));
		customerProjectDTO.setEmployeeName(employee.getName());
		customerProjectDTO.setId(customerProject.getId());
		customerProjectDTO.setProjectName(project.getName());
		customerProjectDTO.setRestCount(customerProject.getRestCount());
		customerProjectDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, customerProject.getState(), employee.getCompanyId()));
		return customerProjectDTO;
	}
	
	public CustomerProjectDetailDTO sCustomerProjectDetailDTO(customerProject customerProject) throws ParseException {
		CustomerProjectDetailDTO customerProjectDetailDTO = new CustomerProjectDetailDTO();
		customer customer = customerMapper.selectByPrimaryKey(customerProject.getCustomerId());
		employee employee = employeeMapper.selectByPrimaryKey(customerProject.getEmployeeId());
		project project = projectMapper.selectByPrimaryKey(customerProject.getProjectId());
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", employee.getCompanyId());
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		List<JSONObject> orderJsonList = new ArrayList<>();
		List<order> orders = orderMapper.selectByProject(employee.getId(), customerProject.getId(),stateList);
		customerProjectDetailDTO.setCount(customerProject.getCount());
		customerProjectDetailDTO.setCustomerName(customer.getName());
		customerProjectDetailDTO.setDateTime(TimeFormatUtil.stringToTimeStamp(customerProject.getDateTime()));
		customerProjectDetailDTO.setEmployeeName(employee.getName());
		customerProjectDetailDTO.setId(customerProject.getId());
		customerProjectDetailDTO.setProjectName(project.getName());
		customerProjectDetailDTO.setRestCount(customerProject.getRestCount());
		customerProjectDetailDTO.setState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, customerProject.getState(), employee.getCompanyId()));
		for(order o:orders) {
			JSONObject orderJson = new JSONObject();
			employee e = employeeMapper.selectByPrimaryKey(o.getEmployeeId());
			orderJson.put("employeeName", e.getName());
			orderJson.put("dateTime", TimeFormatUtil.stringToTimeStamp(o.getDateTime()));
			orderJson.put("date", TimeFormatUtil.stringTodate(o.getDate()));
			orderJson.put("startTime", o.getStartTime());
			if(!"".equals(o.getActStartTime()) && o.getActStartTime() != null) {
				orderJson.put("actStartTime", TimeFormatUtil.stringToTimeStamp(o.getActStartTime()));
			}else {
				orderJson.put("actStartTime", null);
			}
			orderJson.put("orderState", dictMapper.selectByCodeAndStateCode(ORDER_FLOW, o.getOrderState(), employee.getCompanyId()));
			if(o.getApplyOrderState() != null) {
				orderJson.put("applyOrderState", dictMapper.selectByCodeAndStateCode(APPLY_FLOW, o.getApplyOrderState(), employee.getCompanyId()));
			}else {
				orderJson.put("applyOrderState", null);
			}
			orderJson.put("times", o.getProjectNum()+"/"+customerProject.getCount());
			orderJson.put("evaluate", o.getEvaluate());
			String pics = o.getPic();
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
				orderJson.put("pic", dataJson);
			}else {
				orderJson.put("pic", null);
			}
			orderJsonList.add(orderJson);
		}
		customerProjectDetailDTO.setUsed(orderJsonList);
		return customerProjectDetailDTO;
	}
	public JSONObject getCustomerProjectDetail(JSONObject data) throws ParseException {
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(data.getInteger("customerProjectId"));
		if(customerProject == null || customerProject.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
			throw new BusiException("该客户项目不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", Type);
		dataJSonDynamic.put("id", customerProject.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		
		JSONObject dataJson = new JSONObject();
//		dataJson.put("customerProject", this.sCustomerProjectDetailDTO(customerProject));
		dataJson.put("order", this.sCustomerProjectDetailDTO(customerProject));
		dataJson.put("dynamicId", dynamic.getId());
		dataJson.put("ApplyRankDTO", ApplyRankService.getApplyRankByDynamic(dataJSonDynamic));
		
		return dataJson;
	}
}
