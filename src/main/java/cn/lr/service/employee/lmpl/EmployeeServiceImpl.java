package cn.lr.service.employee.lmpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.companyMapper;
import cn.lr.dao.customerMapper;
import cn.lr.dao.customerPerformanceMapper;
import cn.lr.dao.customerProjectMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeLogTomorrowMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.employeeRestMapper;
import cn.lr.dao.employeeTaskMapper;
import cn.lr.dao.orderMapper;
import cn.lr.dao.postMapper;
import cn.lr.dto.EmployeeRankDTO;
import cn.lr.dto.Page;
import cn.lr.dto.employeeDTO;
import cn.lr.exception.BusiException;
import cn.lr.po.company;
import cn.lr.po.customer;
import cn.lr.po.customerPerformance;
import cn.lr.po.customerProject;
import cn.lr.po.employee;
import cn.lr.po.employeeLogTomorrow;
import cn.lr.po.employeeRest;
import cn.lr.po.employeeTask;
import cn.lr.po.order;
import cn.lr.po.post;
import cn.lr.service.company.PostService;
import cn.lr.service.customer.CustomerPerformanceService;
import cn.lr.service.customer.CustomerProjectService;
import cn.lr.service.customer.CustomerService;
import cn.lr.service.customer.OrderService;
import cn.lr.service.employee.EmployeeRankService;
import cn.lr.service.employee.EmployeeService;
import cn.lr.util.DataLengthUtil;
import cn.lr.util.DataVaildCheckUtil;
import cn.lr.util.EncryptionUtil;
import cn.lr.util.TimeFormatUtil;
import jxl.Sheet;
import jxl.Workbook;

@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	postMapper postMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	companyMapper companyMapper;
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	employeeTaskMapper employeeTaskMapper;
	@Autowired
	orderMapper orderMapper;
	@Autowired
	employeeRestMapper employeeRestMapper;
	@Autowired
	employeeLogTomorrowMapper employeeLogTomorrowMapper;
	@Autowired
	customerMapper customerMapper;
	@Autowired
	customerProjectMapper customerProjectMapper;
	@Autowired
	customerPerformanceMapper customerPerformanceMapper;

	@Autowired
	PostService PostService;
	@Autowired
	EmployeeRankService EmployeeRankService;
	@Autowired
	OrderService OrderService;
	@Autowired
	CustomerService CustomerService;
	@Autowired
	CustomerProjectService CustomerProjectService;
	@Autowired
	CustomerPerformanceService CustomerPerformanceService;

	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${employee.type}")
	private String EMPLOYEE_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${attendance.type}")
	private String ATTENDANCE_TYPE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	@Value("${order.flow}")
	private String ORDER_FLOW;
	@Value("${customerPerforman.type}")
	private String CUSTOMERPERFORMAN_TYPE;

	@Override
	public Integer addEmployee(JSONObject data) {
		employee employee = employeeMapper.selectByPhone(data.getString("phone"));
		if (employee != null && employee.getState() != dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
				data.getInteger("companyId"))) {
			throw new BusiException("该号码已存在");
		}
		employee record = new employee();
		record.setPassword(EncryptionUtil.encryping("123456"));
		record.setCompanyId(data.getInteger("companyId"));
		record.setName(data.getString("name"));
		record.setPhone(data.getString("phone"));
		record.setPic(data.getString("pic"));
		post post = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		post.setNum(post.getNum() + 1);
		int count = postMapper.updateByPrimaryKeySelective(post);
		if (count == 0) {
			throw new BusiException("更新post表失败");
		}
		record.setPostId(data.getInteger("postId"));
		record.setSex(data.getString("sex"));
		record.setState(dictMapper.selectByCodeAndStateName(EMPLOYEE_TYPE, "未激活", data.getInteger("companyId")));
		count = employeeMapper.insertSelective(record);
		if (count == 0) {
			throw new BusiException("插入employee表失败");
		}
		// 插入employeeTask表
		employeeTask employeeTask = new employeeTask();
		employeeTask.setEmployeeId(record.getId());
		count = employeeTaskMapper.insertSelective(employeeTask);
		if (count == 0) {
			throw new BusiException("插入employeeTask表失败");
		}
		// 添加领导表的下属List
		if (data.getInteger("leaderId") != null) {
			employee test = employeeMapper.selectByPrimaryKey(data.getInteger("leaderId"));
			if (test == null || test.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
					data.getInteger("companyId"))) {
				throw new BusiException("该领导不存在");
			}
			String underList = test.getUnderIdList() == null ? "" : test.getUnderIdList();
			test.setUnderIdList(record.getId() + "-" + underList);
			count = employeeMapper.updateByPrimaryKey(test);
			if (count == 0) {
				throw new BusiException("更新employee的underIdList失败");
			}
			String leaderList = test.getLeaderIdList() == null ? "" : test.getLeaderIdList();
			record.setLeaderIdList(data.getInteger("leaderId") + "-" + leaderList);
			count = employeeMapper.updateByPrimaryKeySelective(record);
			if (count == 0) {
				throw new BusiException("更新employee的leaderIdList失败");
			}
		}
		return record.getId();
	}

	@Override
	public Integer modifyEmployee(JSONObject data) {
		if (data.getString("password") != null) {
			if (!DataVaildCheckUtil.isValidPassword(data.getString("password"))) {
				throw new BusiException("密码格式不符合规范");
			}
		}
		employee record = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId"));
		if (data.getInteger("companyId") != null) {
			company company = companyMapper.selectByPrimaryKey(data.getInteger("companyId"));
			if (company == null || company.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
					data.getInteger("companyId"))) {
				throw new BusiException("该公司不存在");
			}
			record.setCompanyId(data.getInteger("companyId"));
		}
		if (data.getInteger("leaderId") != null) {
			// 删除旧信息
			if (record.getLeaderIdList() != null && !"".equals(record.getLeaderIdList())) {
				String[] oriLeader = record.getLeaderIdList().split("-");
				employee employee = employeeMapper.selectByPrimaryKey(Integer.valueOf(oriLeader[0]));
				if (employee == null || employee.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
						data.getInteger("companyId"))) {
					throw new BusiException("该职员失败");
				}
				String oriUnder = employee.getUnderIdList();
				Integer place = oriUnder.indexOf(record.getId() + "-");
				StringBuilder ori = new StringBuilder(oriUnder).replace(place,
						place + DataLengthUtil.LengthNum(record.getId()) + 1, "");
				employee.setUnderIdList(ori.toString());
				System.out.println(ori.toString());
				int count = employeeMapper.updateByPrimaryKeySelective(employee);
				if (count == 0) {
					throw new BusiException("更新leaderList的underIdList失败");
				}
			}
			System.out.println(data.getInteger("leaderId"));
			// 添加新的leaderList
			employee test = employeeMapper.selectByPrimaryKey(data.getInteger("leaderId"));
			if (test == null || test.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
					data.getInteger("companyId"))) {
				throw new BusiException("该领导不存在");
			}
			String underList = test.getUnderIdList() == null ? "" : test.getUnderIdList();
			test.setUnderIdList(record.getId() + "-" + underList);
			int count = employeeMapper.updateByPrimaryKey(test);
			if (count == 0) {
				throw new BusiException("更新employee的underIdList失败");
			}
			String leaderList = test.getLeaderIdList() == null ? "" : test.getLeaderIdList();
			record.setLeaderIdList(data.getInteger("leaderId") + "-" + leaderList);
			count = employeeMapper.updateByPrimaryKeySelective(record);
			if (count == 0) {
				throw new BusiException("更新employee的leaderIdList失败");
			}
		}
		record.setName(data.getString("name"));
		record.setPassword(
				data.getString("password") == null ? null : EncryptionUtil.encryping(data.getString("password")));
		record.setPhone(data.getString("phone"));
		record.setPic(data.getString("pic"));
		if (data.getInteger("postId") != null) {
			post post = postMapper.selectByPrimaryKey(data.getInteger("postId"));
			if (post == null || post.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
					data.getInteger("companyId"))) {
				throw new BusiException("该postId不存在");
			}
			// 删除旧post数量
			post = postMapper.selectByPrimaryKey(record.getPostId());
			if (post == null || post.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
					data.getInteger("companyId"))) {
				throw new BusiException("该职位不存在");
			}
			post.setNum(post.getNum() - 1);
			int count = postMapper.updateByPrimaryKeySelective(post);
			if (count == 0) {
				throw new BusiException("删除旧post数量失败");
			}
			// 添加新post数量
			post = postMapper.selectByPrimaryKey(data.getInteger("postId"));
			post.setNum(post.getNum() + 1);
			count = postMapper.updateByPrimaryKeySelective(post);
			if (count == 0) {
				throw new BusiException("添加新post数量失败");
			}
			record.setPostId(data.getInteger("postId"));
		}
		record.setSex(data.getString("sex"));
		int count = employeeMapper.updateByPrimaryKeySelective(record);
		if (count == 0) {
			throw new BusiException("更新enployee表失败");
		}
		return record.getId();
	}

	// FIXED: 删除时用户下面有客户或者预约
	@Override
	public Integer deleteEmployee(JSONObject data) throws Exception {
		employee record = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId"));
		String date = TimeFormatUtil.dateToString(new Date());
		Integer stateSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", record.getCompanyId());
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", record.getCompanyId());
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", record.getCompanyId());
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", record.getCompanyId());
		Integer stateSB = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败", record.getCompanyId());
		Integer stateWKS = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", record.getCompanyId());
		Integer stateJXZ = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", record.getCompanyId());
		List<order> ordersJZX = orderMapper.selectByEmployeeWKS(record.getId(), stateCG, stateJXZ, date);
		if (!ordersJZX.isEmpty()) {
			throw new BusiException("该职员有预约正在进行中，无法删除");
		}
		// 删除职位数量
		post post = postMapper.selectByPrimaryKey(record.getPostId());
		post.setNum(post.getNum() - 1);
		int count = postMapper.updateByPrimaryKeySelective(post);
		if (count == 0) {
			throw new BusiException("删除当前职位数量失败");
		}

		// 删除旧信息
		if (record.getLeaderIdList() != null && !"".equals(record.getLeaderIdList())) {
			String[] oriLeader = record.getLeaderIdList().split("-");
			employee employee = employeeMapper.selectByPrimaryKey(Integer.valueOf(oriLeader[0]));
			if (employee == null || employee.getState() == stateSX) {
				throw new BusiException("该employeeId失败");
			}
			String oriUnder = employee.getUnderIdList();
			String employeeIdStringD = "";
			String[] employeeIdList = oriUnder.split("-");
			for (int j = 0; j < employeeIdList.length; j++) {
				if (Integer.valueOf(employeeIdList[j]) == record.getId()) {
					continue;
				}
				employeeIdStringD += employeeIdList[j] + "-";
			}
			employee.setUnderIdList(employeeIdStringD + record.getUnderIdList());
			count = employeeMapper.updateByPrimaryKeySelective(employee);
			if (count == 0) {
				throw new BusiException("更新leaderList的underIdList失败");
			}
			List<customerProject> customerProjects = customerProjectMapper.selectByEmployee(record.getId(), stateSX, 0,
					Integer.MAX_VALUE);
			for (customerProject c : customerProjects) {
				if (c.getState() == stateCG) {
					c.setEmployeeId(employee.getId());
					count = customerProjectMapper.updateByPrimaryKeySelective(c);
					if (count == 0) {
						throw new BusiException("更新customerProject的负责人");
					}
				} else if (c.getState() != stateSB) {
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("companyId", employee.getCompanyId());
					oJsonObject.put("customerId", c.getCustomerId());
					oJsonObject.put("projectId", c.getProjectId());
					oJsonObject.put("employeeId", c.getEmployeeId());
					CustomerProjectService.addCustomerProject(oJsonObject);
					oJsonObject = new JSONObject();
					oJsonObject.put("customerProjectId", c.getId());
					oJsonObject.put("companyId", record.getCompanyId());
					CustomerProjectService.annulCustomerProject(oJsonObject);
				}
			}
			List<customerPerformance> customerPerformances = customerPerformanceMapper.selectByEmployee(record.getId(),
					stateSX, 0, Integer.MAX_VALUE);
			for (customerPerformance c : customerPerformances) {
				if (c.getState() == stateCG) {
					c.setEmployeeId(employee.getId());
					count = customerPerformanceMapper.updateByPrimaryKeySelective(c);
					if (count == 0) {
						throw new BusiException("更新customerPerformance的负责人");
					}
					if (c.getType() == dictMapper.selectByCodeAndStateName(CUSTOMERPERFORMAN_TYPE, "新增顾客",
							record.getCompanyId())) {
						customer customer = customerMapper.selectByPrimaryKey(c.getCustomerId());
						customer.setEmployeeId(employee.getId());
						count = customerMapper.updateByPrimaryKeySelective(customer);
						if (count == 0) {
							throw new BusiException("更新customer的负责人");
						}
					}
				} else if (c.getState() != stateSB) {
					if (c.getType() == dictMapper.selectByCodeAndStateName(CUSTOMERPERFORMAN_TYPE, "新增顾客",
							record.getCompanyId())) {
						customer customer = customerMapper.selectByPrimaryKey(c.getCustomerId());
						JSONObject oJsonObject = new JSONObject();
						oJsonObject.put("companyId", employee.getCompanyId());
						oJsonObject.put("employeeId", customer.getEmployeeId());
						oJsonObject.put("brith", customer.getBirth());
						oJsonObject.put("habit", customer.getHabit());
						oJsonObject.put("money", customer.getMoney());
						oJsonObject.put("name", customer.getName());
						oJsonObject.put("phone", customer.getPhone());
						oJsonObject.put("plan", customer.getPlan());
						oJsonObject.put("sex", customer.getSex());
						oJsonObject.put("note", customer.getNote());
						oJsonObject.put("pic", customer.getPic());
						CustomerService.addCustomer(oJsonObject);
					} else {
						JSONObject oJsonObject = new JSONObject();
						oJsonObject.put("companyId", employee.getCompanyId());
						oJsonObject.put("customerId", c.getCustomerId());
						oJsonObject.put("money", c.getMoney());
						oJsonObject.put("note", c.getNote());
						oJsonObject.put("type", dictMapper.selectByCodeAndStateCode(CUSTOMERPERFORMAN_TYPE, c.getType(),
								record.getCompanyId()));
						CustomerProjectService.addCustomerProject(oJsonObject);
					}
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("customerProjectId", c.getId());
					oJsonObject.put("companyId", record.getCompanyId());
					CustomerProjectService.annulCustomerProject(oJsonObject);
				}
			}
			List<Integer> applyState = new ArrayList<>();
			applyState.add(stateWSH);
			applyState.add(stateSHZ);
			List<Integer> orderState = new ArrayList<>();
			orderState.add(stateWKS);
			List<order> ordersCX = orderMapper.selectByEmployeeCXApply(record.getId(), applyState);
			ordersCX.addAll(orderMapper.selectByEmployeeCXOrder(record.getId(), stateCG, stateWKS));
			ordersCX.addAll(orderMapper.selectByEmployeeCXOrderApply(record.getId(), stateCG, stateWKS, applyState));
			for (order o : ordersCX) {
				JSONObject oJsonObject = new JSONObject();
				oJsonObject.put("companyId", employee.getCompanyId());
				oJsonObject.put("employeeId", employee.getId());
				oJsonObject.put("customerId", o.getCustomerId());
				oJsonObject.put("customerProjectId", o.getCustomerProjectId());
				oJsonObject.put("date", TimeFormatUtil.stringTodate(o.getDate()));
				oJsonObject.put("startTime", o.getStartTime());
				oJsonObject.put("note", o.getNote());
				OrderService.addOrder(oJsonObject);
				oJsonObject = new JSONObject();
				oJsonObject.put("orderId", o.getId());
				oJsonObject.put("companyId", record.getCompanyId());
				OrderService.annulOrder(oJsonObject);
			}
		}
		if (record.getUnderIdList() != null && "".equals(record.getUnderIdList())) {
			String leader = null;
			if (record.getLeaderIdList() != null && "".equals(record.getLeaderIdList())) {
				leader = record.getLeaderIdList();
			}
			String[] oriUnder = record.getUnderIdList().split("-");
			for (int i = 0; i < oriUnder.length; i++) {
				employee employee = employeeMapper.selectByPrimaryKey(Integer.valueOf(oriUnder[i]));
				employee.setLeaderIdList(leader);
				count = employeeMapper.updateByPrimaryKey(employee);
				if (count == 0) {
					throw new BusiException("更新underList的LeaderList失败");
				}
			}
		}
		// 将职员状态设为已失效
		record.setState(stateSX);
		count = employeeMapper.updateByPrimaryKeySelective(record);
		if (count == 0) {
			throw new BusiException("更新employee表失败");
		}
		return record.getId();
	}

	@Override
	public employeeDTO getEmployee(JSONObject data) {
		employee record = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId"));
		if (record == null) {
			throw new BusiException("该用户不存在");
		}
		return this.sEmployeeDTO(record);
	}

	@Override
	public Page<employeeDTO> getEmployeeByCompany(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<employee> employees = employeeMapper.selectByCompanyId(companyId, state, (pageNum - 1) * PAGESIZE,
				PAGESIZE);
		int total = employeeMapper.selectByCompanyCount(companyId, state);
		List<employeeDTO> employeeDTOs = new ArrayList<employeeDTO>();
		for (employee e : employees) {
			employeeDTO employeeDTO = this.sEmployeeDTO(e);
			employeeDTOs.add(employeeDTO);
		}

		Page<employeeDTO> page = new Page<employeeDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(employeeDTOs);
		return page;
	}

	@Override
	public Page<employeeDTO> getEmployeeByPost(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer postId = data.getInteger("postId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<employee> employees = employeeMapper.selectByPostId(companyId, postId, state, (pageNum - 1) * PAGESIZE,
				PAGESIZE);
		int total = employeeMapper.selectByPostCount(companyId, postId, state);
		List<employeeDTO> employeeDTOs = new ArrayList<employeeDTO>();
		for (employee e : employees) {
			employeeDTO employeeDTO = this.sEmployeeDTO(e);
			employeeDTOs.add(employeeDTO);
		}

		Page<employeeDTO> page = new Page<employeeDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(employeeDTOs);
		return page;
	}

	@Override
	public List<JSONObject> getEmployeeLeader(JSONObject data) {
		post post = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		;
		List<employee> employees = employeeMapper.selectByPostId(data.getInteger("companyId"), post.getLeaderPostId(),
				state, 0, Integer.MAX_VALUE);
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		for (employee e : employees) {
			JSONObject dataJson = new JSONObject();
			dataJson.put("value", e.getName());
			dataJson.put("id", e.getId());
			jsonList.add(dataJson);
		}
		return jsonList;
	}

	@Override
	public List<JSONObject> getEmployeeUnder(JSONObject data) {
		employee employee = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId"));
		if (employee.getUnderIdList() == null || employee.getUnderIdList().equals("")) {
			return new ArrayList<JSONObject>();
		}
		String[] underIdList = employee.getUnderIdList().split("-");
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		JSONObject dataJson = new JSONObject();
		dataJson.put("name", employee.getName());
		dataJson.put("id", employee.getId());
		jsonList.add(dataJson);
		for (int i = 0; i < underIdList.length; i++) {
			employee employee2 = employeeMapper.selectByPrimaryKey(Integer.valueOf(underIdList[i]));
			if (employee2 == null || employee2.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
					data.getInteger("companyId"))) {
				throw new BusiException("该职员不存在");
			}
			dataJson = new JSONObject();
			dataJson.put("name", employee2.getName());
			dataJson.put("id", employee2.getId());
			jsonList.add(dataJson);
		}
		return jsonList;
	}

	@Override
	public List<JSONObject> getEmployeeByPostAll(JSONObject data) {
		String postIdString = data.getString("postIdList");
		String[] postIdList = postIdString.split("-");
		Integer companyId = data.getInteger("companyId");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<JSONObject> dataList = new ArrayList<>();
		for (int i = 0; i < postIdList.length; i++) {
			Integer postId = Integer.valueOf(postIdList[i]);
			JSONObject dataPost = new JSONObject();
			dataPost.put("postId", postId);
			dataPost.put("companyId", companyId);
			PostService.getPost(dataPost);
			List<employee> employees = employeeMapper.selectByPostId(companyId, postId, state, 0, Integer.MAX_VALUE);
			for (employee e : employees) {
				JSONObject dataEmployee = new JSONObject();
				dataEmployee.put("id", e.getId());
				dataEmployee.put("name", e.getName());
				dataList.add(dataEmployee);
			}
		}
		return dataList;
	}

	@Override
	public JSONObject getBusiness(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		List<Integer> employeeIdList = new ArrayList<>();
		employeeIdList.add(employeeId);
		String underList = employee.getUnderIdList();
		if (underList != null && !"".equals(underList)) {
			String[] underIdList = underList.split("-");
			for (int i = 0; i < underIdList.length; i++) {
				employeeIdList.add(Integer.valueOf(underIdList[i]));
			}
		}
		JSONObject businessJson = new JSONObject();
		Integer stateSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		Integer stateSB = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		employeeLogTomorrow employeeLogTomorrow = employeeLogTomorrowMapper.selectByEmployeeIdNew(employeeId, stateSX,
				stateSB);
		int customerCount = orderMapper.selectByEmployeeIdCount(employeeIdList, stateCG);
		int projectCount = orderMapper.selectByProjectCount(employeeIdList, stateCG).size();
		businessJson.put("customer", customerCount);
		if (employeeLogTomorrow != null) {
			businessJson.put("money", DataLengthUtil.CountNum(Integer.valueOf(employeeLogTomorrow.getMoney())));
		} else {
			businessJson.put("money", "0");
		}
		businessJson.put("project", projectCount);
		return businessJson;
	}

	@Override
	public JSONObject getDayMsg(JSONObject data) {
		JSONObject dayJson = new JSONObject();
		Integer employeeId = data.getInteger("employeeId");
		Integer stateSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		Integer stateSB = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		List<Integer> employeeIdList = new ArrayList<>();
		employeeIdList.add(employeeId);
		String underList = employee.getUnderIdList();
		if (underList != null && !"".equals(underList)) {
			String[] underIdList = underList.split("-");
			for (int i = 0; i < underIdList.length; i++) {
				employeeIdList.add(Integer.valueOf(underIdList[i]));
			}
		}
		int customerCount = orderMapper.selectByEmployeeIdCount(employeeIdList, stateCG);
		EmployeeRankDTO employeeRankDTO = EmployeeRankService.getEmployeeRankByEmployee(data);
		String date = TimeFormatUtil.timeStampToString(new Date().getTime());
		employeeRest employeeRest = employeeRestMapper.selectByEmployeeIdNew(employeeId, stateSX, stateSB, date);
		dayJson.put("customerCount", customerCount);
		dayJson.put("rank", employeeRankDTO.getAllScore());
		if (employeeRest != null) {
			dayJson.put("rest", dictMapper.selectByCodeAndStateCode(ATTENDANCE_TYPE, employeeRest.getSchedule(),
					data.getInteger("companyId")));
		} else {
			dayJson.put("rest", "暂无");
		}
		return dayJson;
	}

	public void getAllByExcel(String file) {
		List<employee> list = new ArrayList<employee>();
		try {
			Workbook rwb = Workbook.getWorkbook(new File(file));
			Sheet rs = rwb.getSheet("employee");// 或者rwb.getSheet(0)
			int clos = rs.getColumns();// 得到所有的列
			int rows = rs.getRows();// 得到所有的行
			System.out.println(clos + " rows:" + rows);
			for (int i = 1; i < rows; i++) {
				for (int j = 0; j < clos; j++) {
					// 第一个是列数，第二个是行数
					String userId = rs.getCell(j++, i).getContents();// 默认最左边编号也算一列
					if (userId == null || "".equals(userId))
						userId = "0";
					// 所以这里得j++
					employee employee = new employee();
					String userName = rs.getCell(j++, i).getContents();
					String email = rs.getCell(j++, i).getContents();
					String mobile = rs.getCell(j++, i).getContents();
					String password = rs.getCell(j++, i).getContents();
//	                    employee.setCompanyId(companyId);
//	                    employee.setLeaderIdList(leaderIdList);
//	                    employee.setName(name);
//	                    employee.setPassword(password);
//	                    employee.setPhone(phone);
//	                    employee.setPostId(postId);
//	                    employee.setSex(sex);
//	                    employee.setState(state);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//	        return list;
	}

	public employeeDTO sEmployeeDTO(employee employee) {
		employeeDTO employeeDTO = new employeeDTO();
		post post = postMapper.selectByPrimaryKey(employee.getPostId());
		employeeDTO.setCompany(companyMapper.selectByPrimaryKey(employee.getCompanyId()).getName());
		employeeDTO.setEmployeeId(employee.getId());
		employeeDTO.setCompanyId(employee.getCompanyId());
		employeeDTO.setPostId(employee.getPostId());
		employeeDTO.setName(employee.getName());
		employeeDTO.setPhone(employee.getPhone());
		employeeDTO.setPic(employee.getPic());
		employeeDTO.setPost(post.getName());
		employeeDTO.setSex(employee.getSex());
		employeeDTO.setState(
				dictMapper.selectByCodeAndStateCode(EMPLOYEE_TYPE, employee.getState(), employee.getCompanyId()));
		employeeDTO.setPermissionList(post.getPermissionList());
		if (employee.getLeaderIdList() != null && !employee.getLeaderIdList().equals("")) {
			String leaderId = employee.getLeaderIdList().split("-")[0];
			employee employee1 = employeeMapper.selectByPrimaryKey(Integer.valueOf(leaderId));
			if (employee1 == null) {
				throw new BusiException("该职员不存在");
			}
			employeeDTO.setLeaderName(employee1.getName());
			employeeDTO.setLeaderId(Integer.valueOf(leaderId));
		}
		JSONObject rankJson = new JSONObject();
		rankJson.put("companyId", employee.getCompanyId());
		rankJson.put("employeeId", employee.getId());
		EmployeeRankDTO employeeRankDTO = EmployeeRankService.getEmployeeRankByEmployee(rankJson);
		employeeDTO.setRank(employeeRankDTO.getRank());
		employeeDTO.setScore(employeeRankDTO.getAllScore());
		return employeeDTO;
	}
}
