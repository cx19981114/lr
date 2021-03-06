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
import cn.lr.dao.companyMapper;
import cn.lr.dao.customerMapper;
import cn.lr.dao.customerProjectMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.orderMapper;
import cn.lr.dao.projectMapper;
import cn.lr.dao.rankMapper;
import cn.lr.dto.OrderDTO;
import cn.lr.dto.OrderDetailDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.applyRank;
import cn.lr.po.company;
import cn.lr.po.customer;
import cn.lr.po.customerProject;
import cn.lr.po.dynamic;
import cn.lr.po.employee;
import cn.lr.po.order;
import cn.lr.po.project;
import cn.lr.po.statisticType;
import cn.lr.service.customer.OrderService;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.service.employee.EmployeeRankService;
import cn.lr.util.DateUtil;
import cn.lr.util.TimeFormatUtil;
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	public static final String ApplyType = "预约项目";
	
	public static final String OrderType = "预约完成";
	
	@Autowired
	orderMapper orderMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	customerProjectMapper customerProjectMapper;
	@Autowired
	rankMapper rankMapper;
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	applyRankMapper applyRankMapper;
	@Autowired
	customerMapper customerMapper;
	@Autowired
	projectMapper projectMapper;
	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	companyMapper companyMapper;
	
	@Autowired
	ApplyRankService ApplyRankService;
	@Autowired
	DynamicService DynamicService;
	@Autowired
	EmployeeRankService EmployeeRankService;
	
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${check.flow}")
	private String CHECK_FLOW;
	@Value("${order.flow}")
	private String ORDER_FLOW;
	@Value("${picPath}")
	private String PATH;
	@Value("${picActPath}")
	private String ACTPATH;
	
	@Override
	public Page<OrderDTO> getOrderByEmployeeCondition(JSONObject data) throws ParseException {
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
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		List<Integer> employeeIdList = new ArrayList<>();
		employeeIdList.add(employee.getId());
		String under = employee.getUnderIdList();
		String[] underIdList = null;
		int flag = 1;
		while(!"".equals(under)  && under != null) {
			if(flag == 1) {
				underIdList = under.split("-");
				under = "";
				flag = 0;
			}
			for (int i = 0; i < underIdList.length; i++) {
				employee employee2 = employeeMapper.selectByPrimaryKey(Integer.valueOf(underIdList[i]));
				if (employee2 == null) {
					throw new BusiException("该职员不存在");
				}
				if(employee2.getUnderIdList() != null && !"".equals(employee2.getUnderIdList())) {
					under += employee2.getUnderIdList();
					flag = 1;
				}
				employeeIdList.add(Integer.valueOf(underIdList[i]));
			}
		}
		String date = TimeFormatUtil.dateToString(data.getTimestamp("date"));
		String startTime = data.getString("startTime");
		List<Integer> stateTypeList = null;
		if(startTime.equals("全部")) {
			if(!data.getString("type").equals("全部")){
				stateTypeList = new ArrayList<>();
				Integer state = dictMapper.selectByCodeAndStateName(ORDER_FLOW, data.getString("type"), data.getInteger("companyId"));
				stateTypeList.add(state);
			}
			startTime = null;
		}
		Integer pageNum = data.getInteger("pageNum");
		List<order> orders = orderMapper.selectByEmployeeCondition(employeeIdList, stateTypeList,date,startTime,stateList,(pageNum - 1) * PAGESIZE,
				PAGESIZE);
		List<OrderDTO> jsonObjects1 = new ArrayList<OrderDTO>();
		List<OrderDTO> jsonObjects2 = new ArrayList<OrderDTO>();
		for (order o : orders) {
			OrderDTO orderDTO = this.sOrderDTO(o);
			if(o.getEmployeeId() == employeeId) {
				jsonObjects2.add(orderDTO);
				continue;
			}
			jsonObjects1.add(orderDTO);
		}
		jsonObjects2.addAll(jsonObjects1);
		int total = orderMapper.selectByEmployeeConditionCount(employeeIdList,stateTypeList,date,startTime,stateList);
		Page<OrderDTO> page = new Page<OrderDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects2);
		return page;
	}

	@Override
	public Integer addOrder(JSONObject data) {
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(data.getInteger("customerProjectId"));
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		if(customerProject.getRestCount()-customerProject.getIngCount() == 0) {
			throw new BusiException("该项目剩余消费次数为0");
		}
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		order order = new order();
		order.setCustomerId(data.getInteger("customerId"));
		order.setEmployeeId(data.getInteger("employeeId"));
		order.setCustomerProjectId(data.getInteger("customerProjectId"));
		order.setProjectId(customerProject.getProjectId());
		if(data.getTimestamp("date").before(new Date())) {
			throw new BusiException("该预约日期已过期");
		}
		order.setDate(TimeFormatUtil.dateToString(data.getTimestamp("date")));
		order.setStartTime(data.getString("startTime"));
		order.setDateTime(now);
		order.setNote(data.getString("note"));
		order.setApplyState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId")));
		order.setProjectNum(customerProject.getCount()-customerProject.getRestCount()+1);
		int count = orderMapper.insertSelective(order);
		if(count == 0) {
			throw new BusiException("插入order表失败");
		}
		customerProject.setIngCount(customerProject.getIngCount()+1);
		count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
		if(count == 0) {
			throw new BusiException("更新customerProject表失败");
		}
		customer customer = customerMapper.selectByPrimaryKey(order.getCustomerId());
		customer.setActiveServiceTime(now);
		count = customerMapper.updateByPrimaryKeySelective(customer);
		if (count == 0) {
			throw new BusiException("修改顾客活跃时间失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("note"));
		dataJSonDynamic.put("name", ApplyType);
		dataJSonDynamic.put("id", order.getId());
		dataJSonDynamic.put("employeeId", data.getInteger("employeeId"));
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		stateList.clear();
		stateList.add(stateWSX);
		dataJSonDynamic.put("rank", rankMapper.selectByName(ApplyType,data.getInteger("companyId"),stateList));
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
		
		order = orderMapper.selectByPrimaryKey(order.getId());
		if(order.getApplyState() != stateCG) {
			data.put("orderId", order.getId());
			this.affirmOrder(data);
		}
		return order.getId();
	}

	@Override
	public Integer annulOrder(JSONObject data) {
		order order = orderMapper.selectByPrimaryKey(data.getInteger("orderId"));
		if(order.getApplyState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId"))) {
			throw new BusiException("该预约已无法撤回");
		}
		order.setApplyState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")));
		int count = orderMapper.updateByPrimaryKeySelective(order);
		if (count == 0) {
			throw new BusiException("更新order表失败");
		}
		
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", ApplyType);
		dataJSonDynamic.put("id", order.getId());
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
		
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(order.getCustomerProjectId());
		customerProject.setIngCount(customerProject.getIngCount()-1);
		count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
		if(count == 0) {
			throw new BusiException("更新customerProject表失败");
		}
		return order.getId();
	}
	
	@Override
	public Integer affirmOrder(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		order order = orderMapper.selectByPrimaryKey(data.getInteger("orderId"));
		if (order.getApplyState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该预约无法确认提交");
		}
		order.setApplyState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		int count = orderMapper.updateByPrimaryKeySelective(order);
		if (count == 0) {
			throw new BusiException("更新order表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", ApplyType);
		dataJSonDynamic.put("id", order.getId());
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
		return order.getId();
	}

	@Override
	public Integer deleteOrder(JSONObject data) {
		order order = orderMapper.selectByPrimaryKey(data.getInteger("orderId"));
		if(order.getApplyState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该预约已提交无法删除");
		}
		order.setApplyState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = orderMapper.updateByPrimaryKeySelective(order);
		if (count == 0) {
			throw new BusiException("更新order表失败");
		}

		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", ApplyType);
		dataJSonDynamic.put("id", order.getId());
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
		
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(order.getCustomerProjectId());
		customerProject.setIngCount(customerProject.getIngCount()-1);
		count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
		if(count == 0) {
			throw new BusiException("更新customerProject表失败");
		}
		return order.getId();
	}

	@Override
	public List<JSONObject> getOrderByEmployeeType(JSONObject data) {
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
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		List<Integer> employeeIdList = new ArrayList<>();
		employeeIdList.add(employee.getId());
		String under = employee.getUnderIdList();
		String[] underIdList = null;
		int flag = 1;
		while(!"".equals(under)  && under != null) {
			if(flag == 1) {
				underIdList = under.split("-");
				under = "";
				flag = 0;
			}
			for (int i = 0; i < underIdList.length; i++) {
				employee employee2 = employeeMapper.selectByPrimaryKey(Integer.valueOf(underIdList[i]));
				if (employee2 == null) {
					throw new BusiException("该职员不存在");
				}
				if(employee2.getUnderIdList() != null && !"".equals(employee2.getUnderIdList())) {
					under += employee2.getUnderIdList();
					flag = 1;
				}
				employeeIdList.add(Integer.valueOf(underIdList[i]));
			}
		}
		String date = TimeFormatUtil.dateToString(data.getTimestamp("date"));
		List<Integer> stateTypeList = new ArrayList<>();
		stateTypeList.add(dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", data.getInteger("companyId")));
		Integer ordersWKS = orderMapper.selectByEmployeeConditionCount1(employeeIdList,stateTypeList,date,stateList);
		
		stateTypeList = new ArrayList<>();
		stateTypeList.add(dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", data.getInteger("companyId")));
		Integer ordersJXZ = orderMapper.selectByEmployeeConditionCount1(employeeIdList,stateTypeList,date,stateList);
		
		stateTypeList = new ArrayList<>();
		stateTypeList.add(dictMapper.selectByCodeAndStateName(ORDER_FLOW, "已完成", data.getInteger("companyId")));
		Integer ordersYWC = orderMapper.selectByEmployeeConditionCount1(employeeIdList,stateTypeList,date,stateList);
		
		stateTypeList = new ArrayList<>();
		stateTypeList.add(dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未完成", data.getInteger("companyId")));
		Integer ordersWWC = orderMapper.selectByEmployeeConditionCount1(employeeIdList,stateTypeList,date,stateList);
		
		Integer ordersAll = orderMapper.selectByEmployeeConditionCount1(employeeIdList,null,date,stateList);
		List<JSONObject> orderTypeList =  new ArrayList<JSONObject>();
		JSONObject orderJson = new JSONObject();
		orderJson.put("value", "全部");
		orderJson.put("count", ordersAll);
		orderTypeList.add(orderJson);
		orderJson = new JSONObject();
		orderJson.put("value", "未开始");
		orderJson.put("count", ordersWKS);
		orderTypeList.add(orderJson);
		orderJson = new JSONObject();
		orderJson.put("value", "进行中");
		orderJson.put("count", ordersJXZ);
		orderTypeList.add(orderJson);
		orderJson = new JSONObject();
		orderJson.put("value", "已完成");
		orderJson.put("count", ordersYWC);
		orderTypeList.add(orderJson);
		orderJson = new JSONObject();
		orderJson.put("value", "未完成");
		orderJson.put("count", ordersWWC);
		orderTypeList.add(orderJson);
		return orderTypeList;
	}
	
	@Override
	public List<JSONObject> getOrderByProjectType(JSONObject data) throws ParseException{
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
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		List<Integer> employeeIdList = new ArrayList<>();
		employeeIdList.add(employee.getId());
		String under = employee.getUnderIdList();
		String[] underIdList = null;
		int flag = 1;
		while(!"".equals(under)  && under != null) {
			if(flag == 1) {
				underIdList = under.split("-");
				under = "";
				flag = 0;
			}
			for (int i = 0; i < underIdList.length; i++) {
				employee employee2 = employeeMapper.selectByPrimaryKey(Integer.valueOf(underIdList[i]));
				if (employee2 == null ) {
					throw new BusiException("该职员不存在");
				}
				if(employee2.getUnderIdList() != null && !"".equals(employee2.getUnderIdList())) {
					under += employee2.getUnderIdList();
					flag = 1;
				}
				employeeIdList.add(Integer.valueOf(underIdList[i]));
			}
		}
		List<JSONObject> projectJsonList = new ArrayList<>();
		// day,mon,year,all
		String type = data.getString("type");
		List<order> orders = new ArrayList<>();
		String date = null;
		if(!type.equals("all")) {
			date = TimeFormatUtil.dateToString(data.getTimestamp("date"));
		}
		if(type.equals("day")) {
			orders = orderMapper.selectByProjectDayCount(employeeIdList,date,stateList);
		}else if(type.equals("mon")) {
			orders = orderMapper.selectByProjectMonCount(employeeIdList,date,stateList);
		}else if(type.equals("year")) {
			orders = orderMapper.selectByProjectYearCount(employeeIdList,date,stateList);
		}else if(type.equals("all")) {
			orders = orderMapper.selectByProjectAllCount(employeeIdList,stateList);
		}
		for(order o:orders) {
			project project = projectMapper.selectByPrimaryKey(o.getProjectId());
			JSONObject projectJson = new JSONObject();
			projectJson.put("type", project.getName());
			List<order> orderProject = new ArrayList<>();
			if(type.equals("day")) {
				orderProject = orderMapper.selectByProjectDay(employeeIdList, project.getId(),date,stateList);
			}else if(type.equals("mon")) {
				orderProject = orderMapper.selectByProjectMon(employeeIdList, project.getId(),date,stateList);
			}else if(type.equals("year")) {
				orderProject = orderMapper.selectByProjectYear(employeeIdList, project.getId(),date,stateList);
			}else if(type.equals("all")) {
				orderProject = orderMapper.selectByProjectAll(employeeIdList, project.getId(),stateList);
			}
			List<OrderDTO> jsonObjects1 = new ArrayList<OrderDTO>();
			List<OrderDTO> jsonObjects2 = new ArrayList<OrderDTO>();
			for (order op : orderProject) {
				OrderDTO orderDTO = this.sOrderDTO(op);
				if(op.getEmployeeId() == employeeId) {
					jsonObjects2.add(orderDTO);
					continue;
				}
				jsonObjects1.add(orderDTO);
			}
			jsonObjects2.addAll(jsonObjects1);
			projectJson.put("list", jsonObjects2);
			projectJsonList.add(projectJson);
		}
		return projectJsonList;
	}

	@Override
	public order getOrder(JSONObject data) {
		order order = orderMapper.selectByPrimaryKey(data.getInteger("orderId"));
		if(order == null || order.getApplyState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
			throw new BusiException("该预约不存在");
		}
		return order;
	}

	@Override
	public JSONObject getOrderDetail(JSONObject data) throws ParseException {
		order order = orderMapper.selectByPrimaryKey(data.getInteger("orderId"));
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		if(order == null) {
			throw new BusiException("该预约不存在");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", ApplyType);
		dataJSonDynamic.put("id", order.getId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		dynamic dynamic = DynamicService.getDynamicByTypeAndId(dataJSonDynamic);
		dataJSonDynamic.put("dynamicId", dynamic.getId());
		
		JSONObject dataJson = new JSONObject();
		dataJson.put("order", this.sOrderDetailDTO(order));
		dataJson.put("dynamicId", dynamic.getId());
		dataJson.put("ApplyRankDTOApply", ApplyRankService.getApplyRankByDynamic(dataJSonDynamic));
		stateList.add(state);
		dynamic = dynamicMapper.selectByTypeAndId(OrderType, order.getId(),stateList);
		if(dynamic == null) {
			dataJson.put("ApplyRankDTOOrder", null);
		}else {
			dataJSonDynamic = new JSONObject();
			dataJSonDynamic.put("companyId", data.getInteger("companyId"));
			dataJSonDynamic.put("dynamicId", dynamic.getId());
			dataJson.put("ApplyRankDTOOrder", ApplyRankService.getApplyRankByDynamic(dataJSonDynamic));
		}
		return dataJson;
		
	}
	@Override
	public List<JSONObject> getOrderTime(JSONObject data) {
		String date = TimeFormatUtil.dateToString(data.getTimestamp("date"));
		System.out.println(date);
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWKS = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", data.getInteger("companyId"));
		List<Integer> stateApplyList = new ArrayList<Integer>();
		stateApplyList.add(stateCG);
		List<Integer> stateOrderList = new ArrayList<Integer>();
		stateOrderList.add(stateWKS);
		List<order> orders = orderMapper.selectByEmployeeIdDate(data.getInteger("employeeId"), date,stateApplyList,stateOrderList);
		company company = companyMapper.selectByPrimaryKey(data.getInteger("companyId"));
	    List<String> list = DateUtil.cutDate(company.getStartTime(), company.getEndTime());
	    List<JSONObject> timeVaildList = new ArrayList<>();
	    JSONObject timeVaild = new JSONObject();
	    timeVaild.put("title", "全部");
		timeVaild.put("disabled", "false");
		timeVaildList.add(timeVaild);
	    int flag = 0;
	    for(int i = 0;i<list.size();i++) {
	    	timeVaild = new JSONObject();
	    	flag = 0;
	    	for(order o:orders) {
	    		if(o.getStartTime().equals(list.get(i))) {
	    			timeVaild.put("title", list.get(i));
	    			timeVaild.put("disabled", "true");
	    			timeVaildList.add(timeVaild);
	    			flag = 1;
	    			break;
	    		}
	    	}
	    	if(flag == 0) {
	    		timeVaild.put("title", list.get(i));
	    		timeVaild.put("disabled", "false");
	    		timeVaildList.add(timeVaild);
	    	}
	    }
		return timeVaildList;
	}
	@Override
	public OrderDTO sOrderDTO(order order) throws ParseException {
		OrderDTO orderDTO = new OrderDTO();
		employee employee = employeeMapper.selectByPrimaryKey(order.getEmployeeId());
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", employee.getCompanyId());
		List<Integer> stateList = new ArrayList<Integer>();
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(order.getCustomerProjectId());
		orderDTO.setCustomerName(customerMapper.selectByPrimaryKey(order.getCustomerId()).getName());
		orderDTO.setEmployeeName(employee.getName());
		orderDTO.setId(order.getId());
		orderDTO.setNote(order.getNote());
		orderDTO.setStartTime(order.getStartTime());
		orderDTO.setDate(TimeFormatUtil.stringTodate(order.getDate()));
		orderDTO.setEmployeeId(order.getEmployeeId());
		orderDTO.setProjectName(projectMapper.selectByPrimaryKey(customerProject.getProjectId()).getName());
		stateList.add(stateWSX);
		orderDTO.setRank(rankMapper.selectByName(ApplyType, employee.getCompanyId(),stateList));
		orderDTO.setApplyState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, order.getApplyState(), employee.getCompanyId()));
		if( order.getOrderState() != null ) {
			orderDTO.setOrderState(dictMapper.selectByCodeAndStateCode(ORDER_FLOW, order.getOrderState(), employee.getCompanyId()));
		}
		orderDTO.setTimes(order.getProjectNum()+"/"+customerProject.getCount());
		return orderDTO;
	}
	@Override
	public OrderDetailDTO sOrderDetailDTO(order order) throws ParseException {
		OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
		employee employee = employeeMapper.selectByPrimaryKey(order.getEmployeeId());
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(order.getCustomerProjectId());
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", employee.getCompanyId());
		List<Integer> stateList = new ArrayList<Integer>();
		orderDetailDTO.setCustomerName(customerMapper.selectByPrimaryKey(order.getCustomerId()).getName());
		orderDetailDTO.setEmployeeName(employee.getName());
		orderDetailDTO.setId(order.getId());
		orderDetailDTO.setNote(order.getNote());
		orderDetailDTO.setProjectName(projectMapper.selectByPrimaryKey(customerProject.getProjectId()).getName());
		stateList.add(stateWSX);
		orderDetailDTO.setRank(rankMapper.selectByName(ApplyType, employee.getCompanyId(),stateList));
		orderDetailDTO.setApplyState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, order.getApplyState(), employee.getCompanyId()));
		if(order.getOrderState() != null) {
			orderDetailDTO.setOrderState(dictMapper.selectByCodeAndStateCode(ORDER_FLOW, order.getOrderState(), employee.getCompanyId()));
		}
		if(order.getApplyOrderState() != null) {
			orderDetailDTO.setApplyOrderState(dictMapper.selectByCodeAndStateCode(APPLY_FLOW, order.getApplyOrderState(), employee.getCompanyId()));
		}
		orderDetailDTO.setTimes(order.getProjectNum()+"/"+customerProject.getCount());
		orderDetailDTO.setStartTime(order.getStartTime());
		if(order.getActEndTime() != null) {
			orderDetailDTO.setActEndTime(TimeFormatUtil.stringToTimeStamp(order.getActEndTime()));
		}
		if(order.getActStartTime() != null) {
			orderDetailDTO.setActStartTime(TimeFormatUtil.stringToTimeStamp(order.getActStartTime()));
		}
		orderDetailDTO.setEvaluate(order.getEvaluate());
		String pics = order.getPic();
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
			orderDetailDTO.setPics(dataJson);
		}
		return orderDetailDTO;
	}
	@Override
	public Integer startOrder(JSONObject data) {
		Integer orderId = data.getInteger("orderId");
		order order = orderMapper.selectByPrimaryKey(orderId);
		if(order.getApplyState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"))) {
			throw new BusiException("该预约无法开始");
		}
		order.setActStartTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		order.setOrderState(dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", data.getInteger("companyId")));
		int count = orderMapper.updateByPrimaryKeySelective(order);
		if(count == 0) {
			throw new BusiException("更新order表失败");
		}
		return orderId;
	}
	@Override
	public Integer cancelOrder(JSONObject data) {
		Integer orderId = data.getInteger("orderId");
		order order = orderMapper.selectByPrimaryKey(orderId);
		if(order.getApplyState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"))) {
			throw new BusiException("该预约无法开始");
		}
		if(order.getOrderState() != dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", data.getInteger("companyId")) && order.getOrderState() != dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", data.getInteger("companyId"))) {
			throw new BusiException("该预约无法结束");
		}
		order.setOrderState(dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未完成", data.getInteger("companyId")));
		order.setActEndTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		order.setEvaluate(data.getString("evaluate"));
		int count = orderMapper.updateByPrimaryKeySelective(order);
		if(count == 0) {
			throw new BusiException("更新order表失败");
		}
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(order.getCustomerProjectId());
		customerProject.setIngCount(customerProject.getIngCount()-1);
		count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
		if(count == 0) {
			throw new BusiException("更新customerProject表失败");
		}
		return orderId;
	}
	@Override
	public Integer endOrder(JSONObject data) {
		Integer orderId = data.getInteger("orderId");
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		List<Integer> stateList = new ArrayList<Integer>();
		order order = orderMapper.selectByPrimaryKey(orderId);
		if(order.getApplyState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"))) {
			throw new BusiException("该预约无法开始");
		}
		if(order.getOrderState() != dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", data.getInteger("companyId"))) {
			throw new BusiException("该预约无法结束");
		}
		order.setActEndTime(now);
		order.setOrderState(dictMapper.selectByCodeAndStateName(ORDER_FLOW, "已完成", data.getInteger("companyId")));
		order.setEvaluate(data.getString("evaluate"));
		List<Object> pics = data.getJSONArray("pic");
		if(pics != null) {
			String picString = "";
			for (Object o : pics) {
				picString += o.toString() + "-";
			}
			order.setPic(picString);
		}
		order.setApplyOrderState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId")));
		int count = orderMapper.updateByPrimaryKeySelective(order);
		if(count == 0) {
			throw new BusiException("更新order表失败");
		}
		customerProject customerProject = customerProjectMapper
				.selectByPrimaryKey(order.getCustomerProjectId());
		customerProject.setRestCount(customerProject.getRestCount() - 1);
		customerProject.setIngCount(customerProject.getIngCount() + 1);
		count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
		if (count == 0) {
			throw new BusiException("修改项目剩余数量失败");
		}
		customer customer = customerMapper.selectByPrimaryKey(customerProject.getCustomerId());
		customer.setActiveServiceTime(now);
		count = customerMapper.updateByPrimaryKeySelective(customer);
		if (count == 0) {
			throw new BusiException("修改顾客活跃时间失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("note", data.getString("evaluate"));
		dataJSonDynamic.put("name", OrderType);
		dataJSonDynamic.put("id", order.getId());
		dataJSonDynamic.put("employeeId", order.getEmployeeId());
		dataJSonDynamic.put("companyId", data.getInteger("companyId"));
		stateList.clear();
		stateList.add(stateWSX);
		dataJSonDynamic.put("rank", rankMapper.selectByName(OrderType,data.getInteger("companyId"),stateList));
		int dynamicId = DynamicService.writeDynamic(dataJSonDynamic);

		JSONObject dataJSonApply = new JSONObject();
		dataJSonApply.put("employeeId", order.getEmployeeId());
		dataJSonApply.put("dynamicId", dynamicId);
		dataJSonApply.put("companyId", data.getInteger("companyId"));
		ApplyRankService.addApplyRank(dataJSonApply);
		
		JSONObject dataJSonRank = new JSONObject();
		dataJSonRank.put("companyId", data.getInteger("companyId"));
		dataJSonRank.put("dynamicId", dynamicId);
		dataJSonRank.put("time", now);
		EmployeeRankService.addEmployeeRank(dataJSonRank);
		
		order = orderMapper.selectByPrimaryKey(order.getId());
		if(order.getApplyOrderState() != stateCG) {
			this.affirmOrderFinish(data);
		}
		return orderId;
	}
	@Override
	public Integer affirmOrderFinish(JSONObject data) {
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		order order = orderMapper.selectByPrimaryKey(data.getInteger("orderId"));
		if(order.getApplyOrderState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该预约完成无法确认提交");
		}
		order.setApplyOrderState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核",data.getInteger("companyId")));
		int count = orderMapper.updateByPrimaryKeySelective(order);
		if(count == 0) {
			throw new BusiException("更新order表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", OrderType);
		dataJSonDynamic.put("id", order.getId());
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
		return order.getId();
	}
	@Override
	public Integer annulOrderFinish(JSONObject data) {
		order order = orderMapper.selectByPrimaryKey(data.getInteger("orderId"));
		order.setApplyOrderState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败",data.getInteger("companyId")));
		int count = orderMapper.updateByPrimaryKeySelective(order);
		if (count == 0) {
			throw new BusiException("更新order表失败");
		}
		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", OrderType);
		dataJSonDynamic.put("id", order.getId());
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
		
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(order.getCustomerProjectId());
		customerProject.setIngCount(customerProject.getIngCount()-1);
		count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
		if(count == 0) {
			throw new BusiException("更新customerProject表失败");
		}
		return order.getId();
	}
	@Override
	public Integer deleteOrderFinish(JSONObject data) {
		order order = orderMapper.selectByPrimaryKey(data.getInteger("orderId"));
		if(order.getApplyOrderState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交",data.getInteger("companyId"))) {
			throw new BusiException("该预约完成已提交无法删除");
		}
		order.setApplyOrderState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = orderMapper.updateByPrimaryKeySelective(order);
		if (count == 0) {
			throw new BusiException("更新order表失败");
		}

		JSONObject dataJSonDynamic = new JSONObject();
		dataJSonDynamic.put("name", OrderType);
		dataJSonDynamic.put("id", order.getId());
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
		
		customerProject customerProject = customerProjectMapper.selectByPrimaryKey(order.getCustomerProjectId());
		customerProject.setIngCount(customerProject.getIngCount()-1);
		count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
		if(count == 0) {
			throw new BusiException("更新customerProject表失败");
		}
		return order.getId();
	}
	public Page<JSONObject> getOrderHistoryByEmployee(JSONObject data) throws ParseException{
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		Integer stateYWC = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "已完成", data.getInteger("companyId"));
		Integer stateWKS = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", data.getInteger("companyId"));
		Integer stateJXZ = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", data.getInteger("companyId"));
		List<Integer> stateListApply = new ArrayList<Integer>();
		stateListApply.add(stateCG);
		stateListApply.add(stateWTJ);
		stateListApply.add(stateWSH);
		stateListApply.add(stateSHZ);
		List<Integer> stateListOrder = new ArrayList<Integer>();
		stateListOrder.add(stateYWC);
		stateListOrder.add(stateWKS);
		stateListOrder.add(stateJXZ);
		Integer pageNum = data.getInteger("pageNum");
		List<order> orders = orderMapper.selectByEmployeeIdHistory(data.getInteger("employeeId"),data.getString("date"),stateListApply,stateListOrder,(pageNum-1)*PAGESIZE,PAGESIZE);
		List<JSONObject> orderJsonList = new ArrayList<>();
		for(order o: orders) {
			JSONObject orderJson = new JSONObject();
			customer customer = customerMapper.selectByPrimaryKey(o.getCustomerId());
			project project = projectMapper.selectByPrimaryKey(o.getProjectId());
			orderJson.put("evaluate", o.getEvaluate());
			orderJson.put("actDateTime", TimeFormatUtil.stringToTimeStamp(o.getActStartTime()));
			orderJson.put("id", o.getId());
			orderJson.put("customerName", customer.getName());
			orderJson.put("projectName", project.getName());
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
		int total = orderMapper.selectByEmployeeIdHistoryCount(data.getInteger("employeeId"),data.getString("date"),stateListApply,stateListOrder);
		Page<JSONObject> page = new Page<JSONObject>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(orderJsonList);
		return page;
	}
	public Page<JSONObject> getOrderHistoryByCustomer(JSONObject data) throws ParseException{
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		Integer stateYWC = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "已完成", data.getInteger("companyId"));
		Integer stateWKS = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", data.getInteger("companyId"));
		Integer stateJXZ = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", data.getInteger("companyId"));
		List<Integer> stateListApply = new ArrayList<Integer>();
		stateListApply.add(stateCG);
		stateListApply.add(stateWTJ);
		stateListApply.add(stateWSH);
		stateListApply.add(stateSHZ);
		List<Integer> stateListOrder = new ArrayList<Integer>();
		stateListOrder.add(stateYWC);
		stateListOrder.add(stateWKS);
		stateListOrder.add(stateJXZ);
		Integer pageNum = data.getInteger("pageNum");
		List<order> orders = orderMapper.selectByCustomerIdHistory(data.getInteger("customerId"),data.getString("date"),stateListApply,stateListOrder,(pageNum-1)*PAGESIZE,PAGESIZE);
		List<JSONObject> orderJsonList = new ArrayList<>();
		for(order o: orders) {
			JSONObject orderJson = new JSONObject();
			employee employee = employeeMapper.selectByPrimaryKey(o.getEmployeeId());
			project project = projectMapper.selectByPrimaryKey(o.getProjectId());
			orderJson.put("evaluate", o.getEvaluate());
			orderJson.put("actDateTime", TimeFormatUtil.stringToTimeStamp(o.getActStartTime()));
			orderJson.put("id", o.getId());
			orderJson.put("employeeName", employee.getName());
			orderJson.put("projectName", project.getName());
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
		int total = orderMapper.selectByCustomerIdHistoryCount(data.getInteger("customerId"),data.getString("date"),stateListApply,stateListOrder);
		Page<JSONObject> page = new Page<JSONObject>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(orderJsonList);
		return page;
	}
	public List<JSONObject> getOrderHistoryByCustomerType(JSONObject data){
		Integer customerId = data.getInteger("customerId");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		Integer stateYWC = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "已完成", data.getInteger("companyId"));
		Integer stateWKS = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", data.getInteger("companyId"));
		Integer stateJXZ = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", data.getInteger("companyId"));
		List<Integer> stateListApply = new ArrayList<Integer>();
		stateListApply.add(stateCG);
		stateListApply.add(stateWTJ);
		stateListApply.add(stateWSH);
		stateListApply.add(stateSHZ);
		List<Integer> stateListOrder = new ArrayList<Integer>();
		stateListOrder.add(stateYWC);
		stateListOrder.add(stateWKS);
		stateListOrder.add(stateJXZ);
		List<statisticType> statisticTypes = orderMapper.selectByCustomerHistory(customerId,stateListApply,stateListOrder);
		List<JSONObject> moneyList = new ArrayList<JSONObject>();
		int i = 0;
		for(statisticType s:statisticTypes) {
			JSONObject moneyJsonObject = new JSONObject();
			moneyJsonObject.put("title", s.getMonDate());
			moneyJsonObject.put("count", s.getMoney());
			moneyJsonObject.put("index", i++);
			moneyList.add(moneyJsonObject);
		}
		return moneyList;
	}
	public List<JSONObject> getOrderHistoryByEmployeeType(JSONObject data){
		Integer employeeId = data.getInteger("employeeId");
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		Integer stateYWC = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "已完成", data.getInteger("companyId"));
		Integer stateWKS = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", data.getInteger("companyId"));
		Integer stateJXZ = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", data.getInteger("companyId"));
		List<Integer> stateListApply = new ArrayList<Integer>();
		stateListApply.add(stateCG);
		stateListApply.add(stateWTJ);
		stateListApply.add(stateWSH);
		stateListApply.add(stateSHZ);
		List<Integer> stateListOrder = new ArrayList<Integer>();
		stateListOrder.add(stateYWC);
		stateListOrder.add(stateWKS);
		stateListOrder.add(stateJXZ);
		List<statisticType> statisticTypes = orderMapper.selectByEmployeeHistory(employeeId,stateListApply,stateListOrder);
		List<JSONObject> moneyList = new ArrayList<JSONObject>();
		int i = 0;
		for(statisticType s:statisticTypes) {
			JSONObject moneyJsonObject = new JSONObject();
			moneyJsonObject.put("title", s.getMonDate());
			moneyJsonObject.put("count", s.getMoney());
			moneyJsonObject.put("index", i++);
			moneyList.add(moneyJsonObject);
		}
		return moneyList;
	}
	public Page<OrderDTO> getOrderByEmployee(JSONObject data) throws ParseException {
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
		List<order> orders = orderMapper.selectByEmployee(employeeId,stateList,(pageNum-1)*PAGESIZE, PAGESIZE);
		List<OrderDTO> jsonObjects = new ArrayList<OrderDTO>();
		for(order o:orders) {
			OrderDTO orderDTO = this.sOrderDTO(o);
			jsonObjects.add(orderDTO);
		}
		int total = orderMapper.selectByEmployeeCount(employeeId,stateList);
		Page<OrderDTO> page = new Page<OrderDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(jsonObjects);
		return page;
	}
}
