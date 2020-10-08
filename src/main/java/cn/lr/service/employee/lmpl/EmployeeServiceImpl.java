package cn.lr.service.employee.lmpl;

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
	@Value("${noImg}")
	private String NOIMG;

	@Override
	public Integer addEmployee(JSONObject data) {
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		employee employee = employeeMapper.selectByPhone(data.getString("phone"),stateList);
		if (employee != null) {
			throw new BusiException("该号码已存在");
		}
		employee record = new employee();
		record.setPassword(EncryptionUtil.encryping("123456"));
		record.setCompanyId(data.getInteger("companyId"));
		record.setName(data.getString("name"));
		record.setPhone(data.getString("phone"));
		if (data.getString("pic") != null && !"".equals(data.getString("pic"))) {
			record.setPic(data.getString("pic"));
		} else {
			record.setPic(NOIMG);
		}
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
	public Integer addEmployeeJYZ(JSONObject data) {
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		employee employee = employeeMapper.selectByPhone(data.getString("phone"),stateList);
		if (employee != null) {
			throw new BusiException("该号码已存在");
		}
		employee record = new employee();
		record.setPassword(EncryptionUtil.encryping("123456"));
		record.setCompanyId(data.getInteger("companyId"));
		record.setName(data.getString("name"));
		record.setPhone(data.getString("phone"));
		if (data.getString("pic") != null && !"".equals(data.getString("pic"))) {
			record.setPic(data.getString("pic"));
		} else {
			record.setPic(NOIMG);
		}
		post post = new post();
		post.setCompanyId(data.getInteger("companyId"));
		post.setName("经营者");
		post = postMapper.selectByCompanyIdAndPostName(post);
		post.setNum(post.getNum() + 1);
		int count = postMapper.updateByPrimaryKeySelective(post);
		if (count == 0) {
			throw new BusiException("更新post表失败");
		}
		record.setPostId(post.getId());
		record.setSex(data.getString("sex"));
		record.setState(dictMapper.selectByCodeAndStateName(EMPLOYEE_TYPE, "未激活", data.getInteger("companyId")));
		count = employeeMapper.insertSelective(record);
		if (count == 0) {
			throw new BusiException("插入employee表失败");
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
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", record.getCompanyId());
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", record.getCompanyId());
		Integer stateSB = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败", record.getCompanyId());
		Integer stateWKS = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", record.getCompanyId());
		Integer stateJXZ = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", record.getCompanyId());
		List<Integer> stateList = new ArrayList<Integer>();
		List<Integer> stateListApply = new ArrayList<Integer>();
		stateListApply.add(stateCG);
		List<Integer> stateListOrder = new ArrayList<Integer>();
		stateListOrder.add(stateJXZ);
		List<order> ordersJZX = orderMapper.selectByEmployeeOrder(record.getId(), stateListApply, stateListOrder, date);
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
			if (employee == null) {
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
			employee.setUnderIdList(
					employeeIdStringD + (record.getUnderIdList() == null ? "" : record.getUnderIdList()));
			count = employeeMapper.updateByPrimaryKeySelective(employee);
			if (count == 0) {
				throw new BusiException("更新leaderList的underIdList失败");
			}
			stateList.clear();
			stateList.add(stateCG);
			stateList.add(stateWTJ);
			stateList.add(stateWSH);
			stateList.add(stateSHZ);
			List<customerPerformance> customerPerformances = customerPerformanceMapper.selectByEmployee(record.getId(),
					stateList, 0, Integer.MAX_VALUE);
			for (customerPerformance c : customerPerformances) {
				if (c.getType() == dictMapper.selectByCodeAndStateName(CUSTOMERPERFORMAN_TYPE, "新增顾客",
						record.getCompanyId())) {
					customer customer = customerMapper.selectByPrimaryKey(c.getCustomerId());
					String employeeIdD = customer.getEmployeeIdList().replace(record.getId()+"-", "");
					customer.setEmployeeIdList("".equals(employeeIdD) == true ? employee.getId()+"-":employeeIdD);
					count = customerMapper.updateByPrimaryKey(customer);
//					dynamic dynamic = dynamicMap
				} 
				customerPerformance customerPerformance = customerPerformanceMapper.selectByPrimaryKey(c.getId());
				String employeeIdD = customerPerformance.getEmployeeIdList().replace(record.getId()+"-", "");
				customerPerformance.setEmployeeIdList("".equals(employeeIdD) == true ? employee.getId()+"-":employeeIdD);
				count = customerPerformanceMapper.updateByPrimaryKey(customerPerformance);
			}
			
			stateList.clear();
			stateList.add(stateSX);
			List<customerProject> customerProjects = customerProjectMapper.selectByEmployee(record.getId(), stateList,
					0, Integer.MAX_VALUE);
			for (customerProject c : customerProjects) {
				if (c.getState() != stateSB) {
					customerProject customerProject = customerProjectMapper.selectByPrimaryKey(c.getId());
					customerProject.setEmployeeId(employee.getId());
					count = customerProjectMapper.updateByPrimaryKey(customerProject);
				}
			}
			
			List<Integer> applyState = new ArrayList<>();
			applyState.add(stateCG);
			applyState.add(stateWTJ);
			applyState.add(stateWSH);
			applyState.add(stateSHZ);
			List<order> ordersCX = orderMapper.selectByEmployeeCXApply(record.getId(), applyState);
			
			List<Integer> orderState = new ArrayList<>();
			orderState.add(stateWKS);
			ordersCX.addAll(orderMapper.selectByEmployeeCXOrder(record.getId(), applyState, orderState));
			
			ordersCX.addAll(
					orderMapper.selectByEmployeeCXOrderApply(record.getId(), applyState, orderState, applyState));
			for (order o : ordersCX) {
				order order = orderMapper.selectByPrimaryKey(o.getId());
				order.setEmployeeId(employee.getId());
				count = orderMapper.updateByPrimaryKey(order);
			}
		}
		if (record.getUnderIdList() != null && !"".equals(record.getUnderIdList())) {
			String leader = "";
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
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		List<employee> employees = employeeMapper.selectByCompanyId(companyId, stateList, (pageNum - 1) * PAGESIZE,
				PAGESIZE);
		int total = employeeMapper.selectByCompanyCount(companyId, stateList);
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
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		List<employee> employees = employeeMapper.selectByPostId(companyId, postId, stateList, (pageNum - 1) * PAGESIZE,
				PAGESIZE);
		int total = employeeMapper.selectByPostCount(companyId, postId, stateList);
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
	public Page<employeeDTO> getEmployeeByPostJYZ(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		post post = new post();
		post.setCompanyId(data.getInteger("companyId"));
		post.setName("经营者");
		post = postMapper.selectByCompanyIdAndPostName(post);
		Integer pageNum = data.getInteger("pageNum");
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		List<employee> employees = employeeMapper.selectByPostId(companyId, post.getId(), stateList, (pageNum - 1) * PAGESIZE,
				PAGESIZE);
		int total = employeeMapper.selectByPostCount(companyId, post.getId(), stateList);
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
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		List<employee> employees = employeeMapper.selectByPostId(data.getInteger("companyId"), post.getLeaderPostId(),
				stateList, 0, Integer.MAX_VALUE);
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
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		JSONObject dataJson = new JSONObject();
		dataJson.put("name", employee.getName());
		dataJson.put("id", employee.getId());
		jsonList.add(dataJson);
		if (employee.getUnderIdList() == null || employee.getUnderIdList().equals("")) {
			return jsonList;
		}
		String under = employee.getUnderIdList();
		String[] underIdList = null;
		int flag = 1;
		while (!"".equals(under) && under != null) {
			if (flag == 1) {
				underIdList = under.split("-");
				under = "";
				flag = 0;
			}
			for (int i = 0; i < underIdList.length; i++) {
				employee employee2 = employeeMapper.selectByPrimaryKey(Integer.valueOf(underIdList[i]));
				if (employee2 == null || employee2.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
						data.getInteger("companyId"))) {
					throw new BusiException("该职员不存在");
				}
				if (employee2.getUnderIdList() != null && !"".equals(employee2.getUnderIdList())) {
					under += employee2.getUnderIdList();
					flag = 1;
				}
				dataJson = new JSONObject();
				dataJson.put("name", employee2.getName());
				dataJson.put("id", employee2.getId());
				jsonList.add(dataJson);
			}
		}
		return jsonList;
	}

	@Override
	public List<JSONObject> getEmployeeByPostAll(JSONObject data) {
		String postIdString = data.getString("postIdList");
		String[] postIdList = postIdString.split("-");
		Integer companyId = data.getInteger("companyId");
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		List<JSONObject> dataList = new ArrayList<>();
		for (int i = 0; i < postIdList.length; i++) {
			Integer postId = Integer.valueOf(postIdList[i]);
			JSONObject dataPost = new JSONObject();
			dataPost.put("postId", postId);
			dataPost.put("companyId", companyId);
			PostService.getPost(dataPost);
			List<employee> employees = employeeMapper.selectByPostId(companyId, postId, stateList, 0,
					Integer.MAX_VALUE);
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
		String under = employee.getUnderIdList();
		String[] underIdList = null;
		int flag = 1;
		while (!"".equals(under) && under != null) {
			if (flag == 1) {
				underIdList = under.split("-");
				under = "";
				flag = 0;
			}
			for (int i = 0; i < underIdList.length; i++) {
				employee employee2 = employeeMapper.selectByPrimaryKey(Integer.valueOf(underIdList[i]));
				if (employee2 == null || employee2.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
						data.getInteger("companyId"))) {
					throw new BusiException("该职员不存在");
				}
				if (employee2.getUnderIdList() != null && !"".equals(employee2.getUnderIdList())) {
					under += employee2.getUnderIdList();
					flag = 1;
				}
				employeeIdList.add(Integer.valueOf(underIdList[i]));
			}
		}
		JSONObject businessJson = new JSONObject();
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateCG);
		stateList.add(stateSHZ);
		stateList.add(stateWSH);
		stateList.add(stateWTJ);
		employeeLogTomorrow employeeLogTomorrow = employeeLogTomorrowMapper.selectByEmployeeIdNew(employeeId,
				stateList);
		int customerCount = orderMapper.selectByEmployeeIdCount(employeeIdList, stateList);
		int projectCount = orderMapper.selectByProjectCount(employeeIdList, stateList).size();
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
		Integer stateWSQ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请", data.getInteger("companyId"));
		Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId"));
		Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"));
		Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId"));
		Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		employee employee = employeeMapper.selectByPrimaryKey(employeeId);
		List<Integer> employeeIdList = new ArrayList<>();
		employeeIdList.add(employeeId);
		String under = employee.getUnderIdList();
		String[] underIdList = null;
		int flag = 1;
		while (!"".equals(under) && under != null) {
			if (flag == 1) {
				underIdList = under.split("-");
				under = "";
				flag = 0;
			}
			for (int i = 0; i < underIdList.length; i++) {
				employee employee2 = employeeMapper.selectByPrimaryKey(Integer.valueOf(underIdList[i]));
				if (employee2 == null || employee2.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
						data.getInteger("companyId"))) {
					throw new BusiException("该职员不存在");
				}
				if (employee2.getUnderIdList() != null && !"".equals(employee2.getUnderIdList())) {
					under += employee2.getUnderIdList();
					flag = 1;
				}
				employeeIdList.add(Integer.valueOf(underIdList[i]));
			}
		}
		stateList.clear();
		stateList.add(stateCG);
		int customerCount = orderMapper.selectByEmployeeIdCount(employeeIdList, stateList);
		EmployeeRankDTO employeeRankDTO = EmployeeRankService.getEmployeeRankByEmployee(data);
		String date = TimeFormatUtil.timeStampToString(new Date().getTime());
		stateList.clear();
		stateList.add(stateWSQ);
		stateList.add(stateCG);
		stateList.add(stateSHZ);
		stateList.add(stateWSH);
		stateList.add(stateWTJ);
		employeeRest employeeRest = employeeRestMapper.selectByEmployeeIdNew(employeeId, stateList, date);
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

	public employeeDTO sEmployeeDTO(employee employee) {
		employeeDTO employeeDTO = new employeeDTO();
		System.out.println(employee.getPostId());
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
			System.out.println(employee.getLeaderIdList());
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
