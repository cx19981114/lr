package cn.lr.service.employee.lmpl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.applyCheckMapper;
import cn.lr.dao.applyRankMapper;
import cn.lr.dao.companyMapper;
import cn.lr.dao.customerMapper;
import cn.lr.dao.customerPerformanceMapper;
import cn.lr.dao.customerProjectMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeApplyMapper;
import cn.lr.dao.employeeAttendanceMapper;
import cn.lr.dao.employeeLogDayMapper;
import cn.lr.dao.employeeLogTomorrowMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.employeeRankMapper;
import cn.lr.dao.employeeRestMapper;
import cn.lr.dao.employeeTaskMapper;
import cn.lr.dao.orderMapper;
import cn.lr.dao.postMapper;
import cn.lr.dao.projectMapper;
import cn.lr.dao.taskMapper;
import cn.lr.dto.ApplyRankDTO;
import cn.lr.dto.employeeDTO;
import cn.lr.exception.BusiException;
import cn.lr.po.applyCheck;
import cn.lr.po.applyRank;
import cn.lr.po.customer;
import cn.lr.po.customerPerformance;
import cn.lr.po.customerProject;
import cn.lr.po.dynamic;
import cn.lr.po.employee;
import cn.lr.po.employeeApply;
import cn.lr.po.employeeAttendance;
import cn.lr.po.employeeLogDay;
import cn.lr.po.employeeLogTomorrow;
import cn.lr.po.employeeRank;
import cn.lr.po.employeeRest;
import cn.lr.po.employeeTask;
import cn.lr.po.order;
import cn.lr.po.project;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.EmployeeService;
import cn.lr.util.TimeFormatUtil;

@Service
@Transactional
public class ApplyRankServiceImpl implements ApplyRankService {
	@Autowired
	EmployeeService EmployeeService;

	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	applyRankMapper applyRankMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	employeeApplyMapper employeeApplyMapper;
	@Autowired
	employeeAttendanceMapper employeeAttendanceMapper;
	@Autowired
	employeeLogDayMapper employeeLogDayMapper;
	@Autowired
	employeeLogTomorrowMapper employeeLogTomorrowMapper;
	@Autowired
	employeeRankMapper employeeRankMapper;
	@Autowired
	employeeTaskMapper employeeTaskMapper;
	@Autowired
	employeeRestMapper employeeRestMapper;
	@Autowired
	taskMapper taskMapper;
	@Autowired
	companyMapper companyMapper;
	@Autowired
	postMapper postMapper;
	@Autowired
	applyCheckMapper applyCheckMapper;
	@Autowired
	customerProjectMapper customerProjectMapper;
	@Autowired
	customerMapper customerMapper;
	@Autowired
	orderMapper orderMapper;
	@Autowired
	customerPerformanceMapper customerPerformanceMapper;
	@Autowired
	projectMapper projectMapper;

	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	@Value("${check.flow}")
	private String CHECK_FLOW;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${prevTask.type}")
	private String PRETASK_TYPE;
	@Value("${task.type}")
	private String TASK_TYPE;
	@Value("${employee.type}")
	private String EMPLOYEE_TYPE;
	@Value("${order.flow}")
	private String ORDER_FLOW;
	@Value("${picPath}")
	private String PATH;
	@Value("${picActPath}")
	private String ACTPATH;
	@Value("${admin}")
	private String ADMIN;

	@Override
	public Integer addApplyRank(JSONObject data) {
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效",data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		applyRank applyRank = new applyRank();
		String checkIdList = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId")).getLeaderIdList();
		String[] checkId = checkIdList.split("-");
		String checkList = "";
		String checkTimeList = "";
		String noteList = "";
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		dynamic dynamic = dynamicMapper.selectByPrimaryKey(data.getInteger("dynamicId"));
		if (checkIdList == null || "".equals(checkIdList)) {
			Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId"));
			applyRank.setStartTime(now);
			applyRank.setEndTime(now);
			applyRank.setState(stateCG);
			applyRank.setCheckNumber(0);
			applyRank.setDynamicId(data.getInteger("dynamicId"));
			String name = dynamic.getTb_name();
			Integer id = dynamic.getTb_id();
			if ("每日打卡".equals(name)) {
				employeeAttendance employeeAttendance = employeeAttendanceMapper.selectByPrimaryKey(id);
				if (employeeAttendance == null || employeeAttendance.getState() == dictMapper
						.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
					throw new BusiException("该用户的每日打卡不存在");
				}
				employeeAttendance.setState(stateCG);
				int count = employeeAttendanceMapper.updateByPrimaryKeySelective(employeeAttendance);
				if (count == 0) {
					throw new BusiException("修改每日打卡状态失败");
				}
			} else if ("今日总结".equals(name)) {
				employeeLogDay employeeLogDay = employeeLogDayMapper.selectByPrimaryKey(id);
				if (employeeLogDay == null || employeeLogDay.getState() == dictMapper
						.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
					throw new BusiException("该用户的今日总结不存在");
				}
				employeeLogDay.setState(stateCG);
				int count = employeeLogDayMapper.updateByPrimaryKeySelective(employeeLogDay);
				if (count == 0) {
					throw new BusiException("修改今日总结状态失败");
				}

			} else if ("明日计划".equals(name)) {
				employeeLogTomorrow employeeLogTomorrow = employeeLogTomorrowMapper.selectByPrimaryKey(id);
				if (employeeLogTomorrow == null || employeeLogTomorrow.getState() == dictMapper
						.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
					throw new BusiException("该用户的明日计划不存在");
				}
				employeeLogTomorrow.setState(stateCG);
				int count = employeeLogTomorrowMapper.updateByPrimaryKeySelective(employeeLogTomorrow);
				if (count == 0) {
					throw new BusiException("修改明日计划状态失败");
				}

			} else if ("任务".equals(name)) {
				employeeTask employeeTask = employeeTaskMapper.selectByPrimaryKey(id);
				if (employeeTask == null || employeeTask.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE,
						"已失效", data.getInteger("companyId"))) {
					throw new BusiException("该用户的任务不存在");
				}
				employeeTask.setState(stateCG);
				int count = employeeTaskMapper.updateByPrimaryKeySelective(employeeTask);
				if (count == 0) {
					throw new BusiException("修改任务状态失败");
				}
			} else if ("每日行程".equals(name)) {
				employeeRest employeeRest = employeeRestMapper.selectByPrimaryKey(id);
				if (employeeRest == null || employeeRest.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE,
						"已失效", data.getInteger("companyId"))) {
					throw new BusiException("该用户的每日行程不存在");
				}
				employeeRest.setState(stateCG);
				int count = employeeRestMapper.updateByPrimaryKeySelective(employeeRest);
				if (count == 0) {
					throw new BusiException("修改每日行程状态失败");
				}

			} else if ("请假".equals(name) || "物料".equals(name) || "培训".equals(name)) {
				employeeApply employeeApply = employeeApplyMapper.selectByPrimaryKey(id);
				if (employeeApply == null || employeeApply.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE,
						"已失效", data.getInteger("companyId"))) {
					throw new BusiException("该用户的申请不存在");
				}
				employeeApply.setState(stateCG);
				int count = employeeApplyMapper.updateByPrimaryKeySelective(employeeApply);
				if (count == 0) {
					throw new BusiException("修改申请状态失败");
				}
			} else if ("顾客购买项目".equals(name)) {
				customerProject customerProject = customerProjectMapper.selectByPrimaryKey(id);
				if (customerProject == null || customerProject.getState() == dictMapper
						.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
					throw new BusiException("该客户服务项目不存在");
				}
				customerProject.setState(stateCG);
				int count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
				if (count == 0) {
					throw new BusiException("修改客户服务项目状态失败");
				}
			} else if ("新增顾客".equals(name)) {
				customerPerformance customerPerformance = customerPerformanceMapper.selectByPrimaryKey(id);
				if (customerPerformance == null || customerPerformance.getState() == dictMapper
						.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
					throw new BusiException("该业绩不存在");
				}
				customerPerformance.setState(stateCG);
				int count = customerPerformanceMapper.updateByPrimaryKeySelective(customerPerformance);
				if (count == 0) {
					throw new BusiException("修改业绩状态失败");
				}
				customer customer = customerMapper.selectByPrimaryKey(customerPerformance.getCustomerId());
				if (customer == null
						|| customer.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
					throw new BusiException("该顾客不存在");
				}
				customer.setState(stateCG);
				count = customerMapper.updateByPrimaryKeySelective(customer);
				if (count == 0) {
					throw new BusiException("修改顾客状态失败");
				}
			} else if ("顾客续费".equals(name)) {
				customerPerformance customerPerformance = customerPerformanceMapper.selectByPrimaryKey(id);
				if (customerPerformance == null || customerPerformance.getState() == dictMapper
						.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
					throw new BusiException("该业绩不存在");
				}
				customerPerformance.setState(stateCG);
				int count = customerPerformanceMapper.updateByPrimaryKeySelective(customerPerformance);
				if (count == 0) {
					throw new BusiException("修改业绩状态失败");
				}
			} else if ("预约项目".equals(name)) {
				order order = orderMapper.selectByPrimaryKey(id);
				if (order == null
						|| order.getApplyState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"))) {
					throw new BusiException("该预约不存在");
				}
				order.setApplyState(stateCG);
				order.setOrderState(dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", data.getInteger("companyId")));
				int count = orderMapper.updateByPrimaryKeySelective(order);
				if (count == 0) {
					throw new BusiException("修改预约状态失败");
				}
			} else if ("预约完成".equals(name)) {
				order order = orderMapper.selectByPrimaryKey(id);
				if (order == null || order.getApplyOrderState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
						data.getInteger("companyId"))) {
					throw new BusiException("该预约不存在");
				}
				order.setApplyOrderState(stateCG);
				int count = orderMapper.updateByPrimaryKeySelective(order);
				if (count == 0) {
					throw new BusiException("修改预约状态失败");
				}
			}
		} else if("新增顾客".equals(dynamic.getTb_name())){
			employee employee = employeeMapper.selectByPrimaryKey(dynamic.getEmployeeId());
			stateList.add(stateWSX);
			Integer postId = postMapper.selectByNameAndCompany(ADMIN, data.getInteger("companyId"), stateList);
			List<employee> employees = employeeMapper.selectByPostId(employee.getCompanyId(), postId, stateList, 0, Integer.MAX_VALUE);
			checkIdList = "";
			for (employee employee2:employees) {
				checkIdList += employee2.getId()+"-";
				checkList += dictMapper.selectByCodeAndStateName(CHECK_FLOW, "未审核", data.getInteger("companyId")) + "-";
				checkTimeList += now + "--";
				noteList += "未审核-";
			}
			applyRank.setStartTime(now);
			applyRank.setCheckList(checkList);
			applyRank.setCheckTimeList(checkTimeList);
			applyRank.setNoteList(noteList);
			applyRank.setCheckIdList(checkIdList);
			applyRank.setCheckNumber(0);
			applyRank.setDynamicId(data.getInteger("dynamicId"));
			applyRank.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId")));
		} else{
			for (int i = 0; i < checkId.length; i++) {
				checkList += dictMapper.selectByCodeAndStateName(CHECK_FLOW, "未审核", data.getInteger("companyId")) + "-";
				checkTimeList += now + "--";
				noteList += "未审核-";
			}
			applyRank.setStartTime(now);
			applyRank.setCheckList(checkList);
			applyRank.setCheckTimeList(checkTimeList);
			applyRank.setNoteList(noteList);
			applyRank.setCheckIdList(checkIdList);
			applyRank.setCheckNumber(0);
			applyRank.setDynamicId(data.getInteger("dynamicId"));
			applyRank.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", data.getInteger("companyId")));
		}
		int count = applyRankMapper.insertSelective(applyRank);
		if (count == 0) {
			throw new BusiException("插入applyRank表失败");
		}
		return applyRank.getId();
	}

	@Override
	public Integer modifyApplyRank(JSONObject data) {
		applyRank applyRank = applyRankMapper.selectByDynamic(data.getInteger("dynamicId"));
		if (applyRank.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", data.getInteger("companyId"))
				&& applyRank.getState() != dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中",
						data.getInteger("companyId"))) {
			throw new BusiException("该申请无法审核");
		}
		Integer actCheckId = data.getInteger("checkId");
		Integer actCheck = data.getInteger("check");
		String actNote = data.getString("note");

		String checkIdList = applyRank.getCheckIdList();
		String checkList = applyRank.getCheckList();
		String checkTimeList = applyRank.getCheckTimeList();
		String noteList = applyRank.getNoteList();

		String[] checkId = checkIdList.split("-");
		String[] check = checkList.split("-");
		String[] checkTime = checkTimeList.split("--");
		String[] note = noteList.split("-");

		if (checkId[0].equals("")) {
			throw new BusiException("该员工没有审核列表");
		}
		int i = 0;
		String now = TimeFormatUtil.timeStampToString(new Date().getTime());
		for (; i < checkId.length; i++) {
			if (Integer.valueOf(checkId[i]) == actCheckId) {
				if (i != 0 && Integer.valueOf(check[i - 1]) == dictMapper.selectByCodeAndStateName(CHECK_FLOW, "未审核",
						data.getInteger("companyId"))) {
					throw new BusiException("职员编号:" + checkId[i - 1] + "还未审核");
				}
				check[i] = String.valueOf(actCheck);
				checkTime[i] = now;
				note[i] = actNote;

				String afterCheck = "";
				String afterCheckTime = "";
				String afterNote = "";
				for (int j = 0; j < checkId.length; j++) {
					afterCheck += check[j] + "-";
					afterCheckTime += checkTime[j] + "--";
					afterNote += note[j] + "-";
				}

				applyRank.setCheckList(afterCheck);
				applyRank.setCheckTimeList(afterCheckTime);
				applyRank.setNoteList(afterNote);

				if (actCheck == dictMapper.selectByCodeAndStateName(CHECK_FLOW, "通过", data.getInteger("companyId"))) {
					if (i == checkId.length - 1) {
						applyRank.setEndTime(now);
						applyRank.setState(
								dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId")));
						this.setState(applyRank.getDynamicId(),
								dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", data.getInteger("companyId")),
								null, data.getInteger("companyId"));

						employeeRank employeeRank = new employeeRank();
						employeeRank.setDateTime(now);
						dynamic dynamic = dynamicMapper.selectByPrimaryKey(applyRank.getDynamicId());
						if (dynamic == null || dynamic.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE,
								"已失效", data.getInteger("companyId"))) {
							throw new BusiException("该动态不存在");
						}
						employeeRank.setDynamicId(dynamic.getId());
						employeeRank.setRank(dynamic.getRank());
						employeeRank.setEmployeeId(dynamic.getEmployeeId());
						employeeRank.setCompanyId(dynamic.getCompanyId());
						employeeRank.setIsAdd(1);
						int count = employeeRankMapper.insertSelective(employeeRank);
						if (count == 0) {
							throw new BusiException("更新employeeRank表失败");
						}
					} else {
						applyRank.setCheckNumber(i + 1);
						applyRank.setState(
								dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId")));
						this.setState(applyRank.getDynamicId(),
								dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", data.getInteger("companyId")),
								Integer.valueOf(checkId[i + 1]), data.getInteger("companyId"));
					}
				} else if (actCheck == dictMapper.selectByCodeAndStateName(CHECK_FLOW, "驳回",
						data.getInteger("companyId"))) {
					applyRank.setEndTime(now);
					applyRank.setState(
							dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败", data.getInteger("companyId")));
					this.setState(applyRank.getDynamicId(),
							dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败", data.getInteger("companyId")), null,
							data.getInteger("companyId"));
				}
				break;
			}
		}
		if (i == checkId.length) {
			throw new BusiException("该职员不是该申请的审核员");
		}
		int count = applyRankMapper.updateByPrimaryKeySelective(applyRank);
		if (count == 0) {
			throw new BusiException("更新applyRank表失败");
		}
		applyCheck applyCheck = new applyCheck();
		applyCheck.setDateTime(now);
		applyCheck.setDynamicId(data.getInteger("dynamicId"));
		applyCheck.setEmployeeId(actCheckId);
		applyCheck.setNote(actNote);
		applyCheck.setState(actCheck);
		count = applyCheckMapper.insert(applyCheck);
		if (count == 0) {
			throw new BusiException("插入applyCheck表失败");
		}
		return applyRank.getId();
	}

	public void setState(Integer dynamicId, Integer state, Integer checkId, Integer companyId) {
		dynamic dynamic = dynamicMapper.selectByPrimaryKey(dynamicId);
		if (dynamic == null || dynamic.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
			throw new BusiException("该动态不存在");
		}
		dynamic.setCheckId(checkId);
		dynamic.setState(state);
		int count = dynamicMapper.updateByPrimaryKeySelective(dynamic);
		if (count == 0) {
			throw new BusiException("更新dynamic表失败");
		}
		String name = dynamic.getTb_name();
		Integer id = dynamic.getTb_id();
		if ("每日打卡".equals(name)) {
			employeeAttendance employeeAttendance = employeeAttendanceMapper.selectByPrimaryKey(id);
			if (employeeAttendance == null || employeeAttendance.getState() == dictMapper
					.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该用户的每日打卡不存在");
			}
			employeeAttendance.setState(state);
			count = employeeAttendanceMapper.updateByPrimaryKeySelective(employeeAttendance);
			if (count == 0) {
				throw new BusiException("修改每日打卡状态失败");
			}
		} else if ("今日总结".equals(name)) {
			employeeLogDay employeeLogDay = employeeLogDayMapper.selectByPrimaryKey(id);
			if (employeeLogDay == null
					|| employeeLogDay.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该用户的今日总结不存在");
			}
			employeeLogDay.setState(state);
			count = employeeLogDayMapper.updateByPrimaryKeySelective(employeeLogDay);
			if (count == 0) {
				throw new BusiException("修改今日总结状态失败");
			}

		} else if ("明日计划".equals(name)) {
			employeeLogTomorrow employeeLogTomorrow = employeeLogTomorrowMapper.selectByPrimaryKey(id);
			if (employeeLogTomorrow == null || employeeLogTomorrow.getState() == dictMapper
					.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该用户的明日计划不存在");
			}
			employeeLogTomorrow.setState(state);
			count = employeeLogTomorrowMapper.updateByPrimaryKeySelective(employeeLogTomorrow);
			if (count == 0) {
				throw new BusiException("修改明日计划状态失败");
			}

		} else if ("任务".equals(name)) {
			employeeTask employeeTask = employeeTaskMapper.selectByPrimaryKey(id);
			if (employeeTask == null
					|| employeeTask.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该用户的任务不存在");
			}
			employeeTask.setState(state);
			count = employeeTaskMapper.updateByPrimaryKeySelective(employeeTask);
			if (count == 0) {
				throw new BusiException("修改任务状态失败");
			}
		} else if ("每日行程".equals(name)) {
			employeeRest employeeRest = employeeRestMapper.selectByPrimaryKey(id);
			if (employeeRest == null
					|| employeeRest.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该用户的每日行程不存在");
			}
			employeeRest.setState(state);
			count = employeeRestMapper.updateByPrimaryKeySelective(employeeRest);
			if (count == 0) {
				throw new BusiException("修改每日行程状态失败");
			}

		} else if ("请假".equals(name) || "物料".equals(name) || "培训".equals(name)) {
			employeeApply employeeApply = employeeApplyMapper.selectByPrimaryKey(id);
			if (employeeApply == null
					|| employeeApply.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该用户的申请不存在");
			}
			employeeApply.setState(state);
			count = employeeApplyMapper.updateByPrimaryKeySelective(employeeApply);
			if (count == 0) {
				throw new BusiException("修改申请状态失败");
			}
		} else if ("顾客购买项目".equals(name)) {
			customerProject customerProject = customerProjectMapper.selectByPrimaryKey(id);
			if (customerProject == null
					|| customerProject.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该客户服务项目不存在");
			}
			customerProject.setState(state);
			count = customerProjectMapper.updateByPrimaryKeySelective(customerProject);
			if (count == 0) {
				throw new BusiException("修改客户服务项目状态失败");
			}
		} else if ("新增顾客".equals(name)) {
			customerPerformance customerPerformance = customerPerformanceMapper.selectByPrimaryKey(id);
			if (customerPerformance == null || customerPerformance.getState() == dictMapper
					.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该业绩不存在");
			}
			customerPerformance.setState(state);
			count = customerPerformanceMapper.updateByPrimaryKeySelective(customerPerformance);
			if (count == 0) {
				throw new BusiException("修改业绩状态失败");
			}
			customer customer = customerMapper.selectByPrimaryKey(customerPerformance.getCustomerId());
			if (customer == null
					|| customer.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该顾客不存在");
			}
			customer.setState(state);
			count = customerMapper.updateByPrimaryKeySelective(customer);
			if (count == 0) {
				throw new BusiException("修改顾客状态失败");
			}
		} else if ("顾客续费".equals(name)) {
			customerPerformance customerPerformance = customerPerformanceMapper.selectByPrimaryKey(id);
			if (customerPerformance == null || customerPerformance.getState() == dictMapper
					.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该业绩不存在");
			}
			customerPerformance.setState(state);
			count = customerPerformanceMapper.updateByPrimaryKeySelective(customerPerformance);
			if (count == 0) {
				throw new BusiException("修改业绩状态失败");
			}
		} else if ("预约项目".equals(name)) {
			order order = orderMapper.selectByPrimaryKey(id);
			if (order == null
					|| order.getApplyState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该预约不存在");
			}
			order.setApplyState(state);
			count = orderMapper.updateByPrimaryKeySelective(order);
			if (count == 0) {
				throw new BusiException("修改预约状态失败");
			}
		} else if ("预约完成".equals(name)) {
			order order = orderMapper.selectByPrimaryKey(id);
			if (order == null
					|| order.getApplyOrderState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", companyId)) {
				throw new BusiException("该预约不存在");
			}
			order.setApplyOrderState(state);
			count = orderMapper.updateByPrimaryKeySelective(order);
			if (count == 0) {
				throw new BusiException("修改预约状态失败");
			}
		}
	}

	@Override
	public Integer deleteApplyRank(JSONObject data) {
		applyRank applyRank = applyRankMapper.selectByDynamic(data.getInteger("dynamicId"));
		applyRank.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId")));
		int count = applyRankMapper.updateByPrimaryKeySelective(applyRank);
		if (count == 0) {
			throw new BusiException("更新applyRank表失败");
		}
		return applyRank.getId();
	}

	@Override
	public Integer annulApplyRank(JSONObject data) {
		applyRank applyRank = applyRankMapper.selectByDynamic(data.getInteger("dynamicId"));
		applyRank.setState(dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败", data.getInteger("companyId")));
		applyRank.setEndTime(TimeFormatUtil.timeStampToString(new Date().getTime()));
		int count = applyRankMapper.updateByPrimaryKeySelective(applyRank);
		if (count == 0) {
			throw new BusiException("更新applyRank表失败");
		}
		return applyRank.getId();
	}

	@Override
	public List<ApplyRankDTO> getApplyRank(JSONObject data) throws ParseException {
		applyRank applyRank = applyRankMapper.selectByPrimaryKey(data.getInteger("applyRankId"));
		if (applyRank == null || applyRank.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
				data.getInteger("companyId"))) {
			throw new BusiException("该申请不存在");
		}
		List<ApplyRankDTO> applyRankDTOs = new ArrayList<ApplyRankDTO>();
		String[] checkIdList = applyRank.getCheckIdList().split("-");
		String[] checkTimeList = applyRank.getCheckTimeList().split("--");
		String[] checkList = applyRank.getCheckList().split("-");
		String[] noteList = applyRank.getNoteList().split("-");
		for (int i = 0; i < checkIdList.length; i++) {
			ApplyRankDTO applyRankDTO = new ApplyRankDTO();
			JSONObject dataJson = new JSONObject();
			dataJson.put("employeeId", checkIdList[i]);
			dataJson.put("companyId", data.getInteger("companyId"));
			employeeDTO employeeDTO = EmployeeService.getEmployee(dataJson);
			applyRankDTO.setEmployee(employeeDTO);
			applyRankDTO.setNote(noteList[i]);
			applyRankDTO.setState(dictMapper.selectByCodeAndStateCode(CHECK_FLOW, Integer.valueOf(checkList[i]),
					data.getInteger("companyId")));
			applyRankDTO.setTime(TimeFormatUtil.stringToTimeStamp(checkTimeList[i]));
			applyRankDTOs.add(applyRankDTO);
		}
		return applyRankDTOs;
	}

	@Override
	public List<ApplyRankDTO> getApplyRankByDynamic(JSONObject data) throws ParseException {
		applyRank applyRank = applyRankMapper.selectByDynamic(data.getInteger("dynamicId"));
		if (applyRank == null || applyRank.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",
				data.getInteger("companyId"))) {
			throw new BusiException("该申请不存在");
		}
		List<ApplyRankDTO> applyRankDTOs = new ArrayList<ApplyRankDTO>();
		if (applyRank.getCheckIdList() == null || "".equals(applyRank.getCheckIdList())) {
			return applyRankDTOs;
		}
		String[] checkIdList = applyRank.getCheckIdList().split("-");
		String[] checkTimeList = applyRank.getCheckTimeList().split("--");
		String[] checkList = applyRank.getCheckList().split("-");
		String[] noteList = applyRank.getNoteList().split("-");
		for (int i = 0; i < checkIdList.length; i++) {
			ApplyRankDTO applyRankDTO = new ApplyRankDTO();
			JSONObject dataJson = new JSONObject();
			dataJson.put("employeeId", checkIdList[i]);
			dataJson.put("companyId", data.getInteger("companyId"));
			employeeDTO employeeDTO = EmployeeService.getEmployee(dataJson);
			applyRankDTO.setEmployee(employeeDTO);
			applyRankDTO.setNote(noteList[i]);
			applyRankDTO.setState(dictMapper.selectByCodeAndStateCode(CHECK_FLOW, Integer.valueOf(checkList[i]),
					data.getInteger("companyId")));
			applyRankDTO.setTime(TimeFormatUtil.stringToTimeStamp(checkTimeList[i]));
			applyRankDTOs.add(applyRankDTO);
		}
		return applyRankDTOs;
	}

	@Override
	public List<employeeDTO> getCheckList(JSONObject data) {
		employee employee1 = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId"));
		if (employee1.getLeaderIdList() == null || "".equals(employee1.getLeaderIdList())) {
			throw new BusiException("该职员没有审核列表");
		}
		String[] checkIdList = employee1.getLeaderIdList().split("-");
		List<employeeDTO> employeeDTOs = new ArrayList<>();
		for (int i = 0; i < checkIdList.length; i++) {
			employee employee = employeeMapper.selectByPrimaryKey(Integer.valueOf(checkIdList[i]));
			employeeDTO employeeDTO = new employeeDTO();
			employeeDTO.setCompany(companyMapper.selectByPrimaryKey(employee.getCompanyId()).getName());
			employeeDTO.setEmployeeId(employee.getId());
			employeeDTO.setCompanyId(employee.getCompanyId());
			employeeDTO.setPostId(employee.getPostId());
			employeeDTO.setName(employee.getName());
			employeeDTO.setPhone(employee.getPhone());
			employeeDTO.setPic(employee.getPic());
			employeeDTO.setPost(postMapper.selectByPrimaryKey(employee.getPostId()).getName());
			employeeDTO.setSex(employee.getSex());
			employeeDTO.setState(dictMapper.selectByCodeAndStateCode(EMPLOYEE_TYPE, employee.getState(),
					data.getInteger("companyId")));
			if (employee.getLeaderIdList() != null && !employee.getLeaderIdList().equals("")) {
				String leaderId = employee.getLeaderIdList().split("-")[0];
				employee employee2 = employeeMapper.selectByPrimaryKey(Integer.valueOf(leaderId));
				if (employee2 == null) {
					throw new BusiException("该职员不存在");
				}
				employeeDTO.setLeaderName(employee2.getName());
				employeeDTO.setLeaderId(Integer.valueOf(leaderId));
			}
			employeeDTOs.add(employeeDTO);
		}
		return employeeDTOs;
	}
}
