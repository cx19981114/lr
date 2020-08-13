package cn.lr.service.employee.lmpl;

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
import cn.lr.dao.employeeApplyMapper;
import cn.lr.dao.employeeAttendanceMapper;
import cn.lr.dao.employeeLogDayMapper;
import cn.lr.dao.employeeLogTomorrowMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.employeeRestMapper;
import cn.lr.dao.employeeTaskMapper;
import cn.lr.dao.orderMapper;
import cn.lr.exception.BusiException;
import cn.lr.po.company;
import cn.lr.po.customerPerformance;
import cn.lr.po.customerProject;
import cn.lr.po.employee;
import cn.lr.po.employeeApply;
import cn.lr.po.employeeAttendance;
import cn.lr.po.employeeLogDay;
import cn.lr.po.employeeLogTomorrow;
import cn.lr.po.employeeRest;
import cn.lr.po.employeeTask;
import cn.lr.po.order;
import cn.lr.service.customer.CustomerPerformanceService;
import cn.lr.service.customer.CustomerProjectService;
import cn.lr.service.customer.CustomerService;
import cn.lr.service.customer.OrderService;
import cn.lr.service.employee.ApplyRankService;
import cn.lr.service.employee.DynamicService;
import cn.lr.service.employee.EmployeeApplyService;
import cn.lr.service.employee.EmployeeAttendanceService;
import cn.lr.service.employee.EmployeeLogDayService;
import cn.lr.service.employee.EmployeeLogTomorrowService;
import cn.lr.service.employee.EmployeeRestService;
import cn.lr.service.employee.EmployeeTaskService;
import cn.lr.service.employee.TimedTaskService;
import cn.lr.util.TimeFormatUtil;
@Service("TimedTaskService")
@Transactional
public class TimedTaskServiceImpl implements TimedTaskService{
	
	@Autowired
	companyMapper companyMapper;
	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	employeeTaskMapper employeeTaskMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	employeeApplyMapper employeeApplyMapper;
	@Autowired
	employeeAttendanceMapper employeeAttendanceMapper;
	@Autowired
	employeeLogDayMapper employeeLogDayMapper;
	@Autowired
	employeeLogTomorrowMapper employeeLogTomorrowMapper;
	@Autowired
	employeeRestMapper employeeRestMapper;
	@Autowired
	dynamicMapper dynamicMapper;
	@Autowired
	applyRankMapper applyRankMapper;
	@Autowired
	customerMapper customerMapper;
	@Autowired
	customerPerformanceMapper customerPerformanceMapper;
	@Autowired
	orderMapper orderMapper;
	@Autowired
	customerProjectMapper customerProjectMapper;
	
	@Autowired
	DynamicService DynamicService;
	@Autowired
	ApplyRankService ApplyRankService;
	@Autowired
	EmployeeTaskService EmployeeTaskService;
	@Autowired
	EmployeeApplyService EmployeeApplyService;
	@Autowired
	EmployeeAttendanceService EmployeeAttendanceService;
	@Autowired
	EmployeeLogDayService EmployeeLogDayService;
	@Autowired
	EmployeeLogTomorrowService EmployeeLogTomorrowService;
	@Autowired
	EmployeeRestService EmployeeRestService;
	@Autowired
	CustomerPerformanceService CustomerPerformanceService;
	@Autowired
	CustomerProjectService CustomerProjectService;
	@Autowired
	CustomerService CustomerService;
	@Autowired
	OrderService OrderService;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${apply.flow}")
	private String APPLY_FLOW;
	@Value("${order.flow}")
	private String ORDER_FLOW;
	@Value("${check.flow}")
	private String CHECK_FLOW;
	@Value("${prevTask.type}")
	private String PRETASK_TYPE;
	@Value("${task.type}")
	private String TASK_TYPE;
	@Override
	public void timedDay() {
		List<company> companies = companyMapper.listCompanyVaild();
		for(company c:companies) {
			Integer stateSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", c.getId());
			Integer stateWSQ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请", c.getId());
			Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", c.getId());
			Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", c.getId());
			Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", c.getId());
			Integer stateCG = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核成功", c.getId());
			Integer stateSB = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核失败", c.getId());
			Integer stateWKS = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", c.getId());
			Integer stateJXZ = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", c.getId());
			String date = TimeFormatUtil.timeStampToString(new Date().getTime());
			List<employee> employees = employeeMapper.selectByCompanyId(c.getId(),stateSX, 0, Integer.MAX_VALUE);
			for(employee e:employees) {
				// 更新今日未审核的任务申请
				employeeTask employeeTask = employeeTaskMapper.selectByEmployee(e.getId());
				String oriFN = employeeTask.getTaskIdListFN();
				String oriState = employeeTask.getTaskIdListFNState();
				if(oriFN != null && !"".equals(oriFN)) {
					String[] oriFNs = oriFN.split("-");
					String[] oriStates = oriState.split("-");
					String stateList = "";
					for(int i = 0;i<oriFNs.length;i++) {
						if(Integer.valueOf(oriStates[i]) != stateWSQ) {
							JSONObject dataTask = new JSONObject();
							dataTask.put("taskId", Integer.valueOf(oriFNs[i]));
							dataTask.put("employeeId", e.getId());
							dataTask.put("companyId", c.getId());
							if(Integer.valueOf(oriStates[i]) == stateWTJ) {
								EmployeeTaskService.deleteEmployeeTask(dataTask);
							}else if(Integer.valueOf(oriStates[i]) == stateWSH || Integer.valueOf(oriStates[i]) == stateSHZ){
								EmployeeTaskService.annulEmployeeTask(dataTask);
							}
						}
						stateList += stateWSQ+"-";
					}
					employeeTask.setTaskIdListFNState(stateList);
				}
				String oriRWDay = employeeTask.getTaskIdListRWDay();
				String oriRWDayState = employeeTask.getTaskIdListRWDayState();
				if(oriRWDay != null && !"".equals(oriRWDay)) {
					String[] oriRWDays = oriRWDay.split("-");
					String[] oriStates = oriRWDayState.split("-");
					String stateList = "";
					for(int i = 0;i<oriRWDays.length;i++) {
						if(Integer.valueOf(oriStates[i]) != stateWSQ) {
							JSONObject dataTask = new JSONObject();
							dataTask.put("taskId", Integer.valueOf(oriRWDays[i]));
							dataTask.put("employeeId", e.getId());
							dataTask.put("companyId", c.getId());
							if(Integer.valueOf(oriStates[i]) == stateWTJ) {
								EmployeeTaskService.deleteEmployeeTask(dataTask);
							}else if(Integer.valueOf(oriStates[i]) == stateWSH || Integer.valueOf(oriStates[i]) == stateSHZ){
								EmployeeTaskService.annulEmployeeTask(dataTask);
							}
						}
						stateList += stateWSQ+"-";
					}
					employeeTask.setTaskIdListRWDayState(stateList);
				}
				int count = employeeTaskMapper.updateByPrimaryKeySelective(employeeTask);
				if(count == 0) {
					throw new BusiException("更新employeeTask表失败");
				}
				// 更新结束日期是今日前一天的申请
				List<employeeApply> employeeApplies = employeeApplyMapper.selectByEmployeeId(e.getId(),stateSX,stateCG);
				for(employeeApply ea:employeeApplies) {
					JSONObject dataApply = new JSONObject();
					dataApply.put("employeeApplyId", ea.getId());
					dataApply.put("employeeId", e.getId());
					dataApply.put("companyId", c.getId());
					if(ea.getState() == stateWTJ) {
						EmployeeApplyService.deleteEmployeeApply(dataApply);
					}else {
						EmployeeApplyService.annulEmployeeApply(dataApply);
					}
				}
				// 更新今日打卡
				employeeAttendance employeeAttendance = employeeAttendanceMapper.selectByEmployeeIdNew(e.getId(),stateSX,stateSB);
				if(employeeAttendance != null) {
					JSONObject dataAttendance = new JSONObject();
					dataAttendance.put("employeeAttendanceId", employeeAttendance.getId());
					dataAttendance.put("employeeId", e.getId());
					dataAttendance.put("companyId", c.getId());
					
					if(employeeAttendance.getState() == stateWTJ) {
						EmployeeAttendanceService.deleteEmployeeAttendance(dataAttendance);
					}else if(employeeAttendance.getState() == stateWSH || employeeAttendance.getState() == stateSHZ){
						EmployeeAttendanceService.annulEmployeeAttendance(dataAttendance);
					}
				}
				//更新今日总结
				employeeLogDay employeeLogDay = employeeLogDayMapper.selectByEmployeeIdNew(e.getId(),stateSX,stateSB);
				if(employeeLogDay != null) {
					JSONObject dataLogDay = new JSONObject();
					dataLogDay.put("employeeLogDayId", employeeLogDay.getId());
					dataLogDay.put("employeeId", e.getId());
					dataLogDay.put("companyId", c.getId());
					
					if(employeeLogDay.getState() == stateWTJ) {
						EmployeeLogDayService.deleteEmployeeLogDay(dataLogDay);
					}else if(employeeLogDay.getState() == stateWSH || employeeLogDay.getState() == stateSHZ){
						EmployeeLogDayService.annulEmployeeLogDay(dataLogDay);
					}
				}
				// 更新明日计划
				employeeLogTomorrow employeeLogTomorrow = employeeLogTomorrowMapper.selectByEmployeeIdNew(e.getId(),stateSX,stateSB);
				if(employeeLogTomorrow != null) {
					JSONObject dataLogTomorrow = new JSONObject();
					dataLogTomorrow.put("employeeLogTomorrowId", employeeLogTomorrow.getId());
					dataLogTomorrow.put("employeeId", e.getId());
					dataLogTomorrow.put("companyId", c.getId());
					
					if(employeeLogTomorrow.getState() == stateWTJ) {
						EmployeeLogTomorrowService.deleteEmployeeLogTomorrow(dataLogTomorrow);
					}else if(employeeLogTomorrow.getState() == stateWSH || employeeLogTomorrow.getState() == stateSHZ){
						EmployeeLogTomorrowService.annulEmployeeLogTomorrow(dataLogTomorrow);
					}
				}
				// 更新每日行程
				
				employeeRest employeeRest = employeeRestMapper.selectByEmployeeIdNew(e.getId(),stateSX,stateSB,date);
				if(employeeRest != null) {
					JSONObject dataRest = new JSONObject();
					dataRest.put("employeeRestId", employeeRest.getId());
					dataRest.put("employeeId", e.getId());
					dataRest.put("companyId", c.getId());
					
					if(employeeRest.getState() == stateWTJ) {
						EmployeeRestService.deleteEmployeeRest(dataRest);
					}else if(employeeRest.getState() == stateWSH || employeeRest.getState() == stateSHZ){
						EmployeeRestService.annulEmployeeRest(dataRest);
					}
				}
				//更新未审核的新增业绩
			
				List<customerPerformance> customerPerformancesWTJ = customerPerformanceMapper.selectByEmployeeWTJ(e.getId(),stateWTJ);
				for(customerPerformance cp:customerPerformancesWTJ) {
					JSONObject cpJsonObject = new JSONObject();
					cpJsonObject.put("customerPerformanceId", cp.getId());
					cpJsonObject.put("companyId", e.getCompanyId());
					CustomerPerformanceService.deleteCustomerPerformance(cpJsonObject);
				}
				List<customerPerformance> customerPerformancesWSHAndSHZ = customerPerformanceMapper.selectByEmployeeWSHAndSHZ(e.getId(),stateWSH,stateSHZ);
				for(customerPerformance cp:customerPerformancesWSHAndSHZ) {
					JSONObject cpJsonObject = new JSONObject();
					cpJsonObject.put("customerPerformanceId", cp.getId());
					cpJsonObject.put("companyId", e.getCompanyId());
					CustomerPerformanceService.annulCustomerPerformance(cpJsonObject);
				}
				//更新未审核的预约
				List<order> ordersWTJ = orderMapper.selectByEmployeeWTJ(e.getId(),stateWTJ,date);
				for(order o:ordersWTJ) {
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("orderId", o.getId());
					oJsonObject.put("companyId", e.getCompanyId());
					OrderService.deleteOrder(oJsonObject);
				}
				List<order> ordersWSHAndSHZ = orderMapper.selectByEmployeeWSHAndSHZ(e.getId(),stateWSH,stateSHZ,date);
				for(order o:ordersWSHAndSHZ) {
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("orderId", o.getId());
					oJsonObject.put("companyId", e.getCompanyId());
					OrderService.annulOrder(oJsonObject);
				}
				//更新未审核的预约完成
				List<order> ordersFinishWTJ = orderMapper.selectByEmployeeFinishWTJ(e.getId(),stateWTJ);
				for(order o:ordersFinishWTJ) {
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("orderId", o.getId());
					oJsonObject.put("companyId", e.getCompanyId());
					OrderService.deleteOrderFinish(oJsonObject);
				}
				List<order> ordersFinishWSHAndSHZ = orderMapper.selectByEmployeeFinishWSHAndSHZ(e.getId(),stateWSH,stateSHZ);
				for(order o:ordersFinishWSHAndSHZ) {
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("orderId", o.getId());
					oJsonObject.put("companyId", e.getCompanyId());
					OrderService.annulOrderFinish(oJsonObject);
				}
				List<order> ordersWKS = orderMapper.selectByEmployeeWKS(e.getId(),stateCG,stateWKS,date);
				for(order o:ordersWKS) {
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("orderId", o.getId());
					oJsonObject.put("companyId", e.getCompanyId());
					oJsonObject.put("evaluate", "预约过期未开始");
					OrderService.cancelOrder(oJsonObject);
				}
				List<order> ordersJXZ = orderMapper.selectByEmployeeWKS(e.getId(),stateCG,stateJXZ,date);
				for(order o:ordersJXZ) {
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("orderId", o.getId());
					oJsonObject.put("companyId", e.getCompanyId());
					oJsonObject.put("evaluate", "预约过期仍在进行中");
					OrderService.cancelOrder(oJsonObject);
				}
				//更新未审核的客户项目
				List<customerProject> customerProjectWTJ = customerProjectMapper.selectByEmployeeWTJ(e.getId(),stateWTJ);
				for(customerProject cp:customerProjectWTJ) {
					JSONObject cpJsonObject = new JSONObject();
					cpJsonObject.put("customerProjectId", cp.getId());
					cpJsonObject.put("companyId", e.getCompanyId());
					CustomerProjectService.deleteCustomerProject(cpJsonObject);
				}
				List<customerProject> customerProjectWSHAndSHZ = customerProjectMapper.selectByEmployeeWSHAndSHZ(e.getId(),stateWSH,stateSHZ);
				for(customerProject cp:customerProjectWSHAndSHZ) {
					JSONObject cpJsonObject = new JSONObject();
					cpJsonObject.put("customerProjectId", cp.getId());
					cpJsonObject.put("companyId", e.getCompanyId());
					CustomerProjectService.annulCustomerProject(cpJsonObject);
				}
			}
		}
		
	}

	@Override
	public void timedWeek() {
		List<company> companies = companyMapper.listCompanyVaild();
		for(company c:companies) {
			Integer stateSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", c.getId());
			Integer stateWSQ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请", c.getId());
			Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", c.getId());
			Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", c.getId());
			Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", c.getId());
			List<employee> employees = employeeMapper.selectByCompanyId(c.getId(),stateSX, 0, Integer.MAX_VALUE);
			for(employee e:employees) {
				employeeTask employeeTask = employeeTaskMapper.selectByEmployee(e.getId());
				String oriRWWeek = employeeTask.getTaskIdListRWWeek();
				String oriRWWeekState = employeeTask.getTaskIdListRWWeekState();
				if(oriRWWeek != null && !"".equals(oriRWWeek)) {
					String[] oriRWWeeks = oriRWWeek.split("-");
					String[] oriStates = oriRWWeekState.split("-");
					String stateList = "";
					for(int i = 0;i<oriRWWeeks.length;i++) {
						if(Integer.valueOf(oriStates[i]) != stateWSQ) {
							JSONObject dataTask = new JSONObject();
							dataTask.put("taskId", Integer.valueOf(oriRWWeeks[i]));
							dataTask.put("employeeId", e.getId());
							dataTask.put("companyId", c.getId());
							if(Integer.valueOf(oriStates[i]) == stateWTJ) {
								EmployeeTaskService.deleteEmployeeTask(dataTask);
							}else if(Integer.valueOf(oriStates[i]) == stateWSH || Integer.valueOf(oriStates[i]) == stateSHZ){
								EmployeeTaskService.annulEmployeeTask(dataTask);
							}
						}
						stateList += stateWSQ+"-";
					}
					employeeTask.setTaskIdListRWWeekState(stateList);
				}
				int count = employeeTaskMapper.updateByPrimaryKeySelective(employeeTask);
				if(count == 0) {
					throw new BusiException("更新employeeTask表失败");
				}
			}
		}
	}

	@Override
	public void timedMon() {
		List<company> companies = companyMapper.listCompanyVaild();
		for(company c:companies) {
			Integer stateSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", c.getId());
			Integer stateWSQ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未申请", c.getId());
			Integer stateWTJ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未提交", c.getId());
			Integer stateWSH = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "未审核", c.getId());
			Integer stateSHZ = dictMapper.selectByCodeAndStateName(APPLY_FLOW, "审核中", c.getId());
			List<employee> employees = employeeMapper.selectByCompanyId(c.getId(),stateSX, 0, Integer.MAX_VALUE);
			for(employee e:employees) {
				employeeTask employeeTask = employeeTaskMapper.selectByEmployee(e.getId());
				String oriRWMon = employeeTask.getTaskIdListRWMon();
				String oriRWMonState = employeeTask.getTaskIdListRWMonState();
				if(oriRWMon != null && !"".equals(oriRWMon)) {
					String[] oriRWMons = oriRWMon.split("-");
					String[] oriStates = oriRWMonState.split("-");
					String stateList = "";
					for(int i = 0;i<oriRWMons.length;i++) {
						if(Integer.valueOf(oriStates[i]) != stateWSQ) {
							JSONObject dataTask = new JSONObject();
							dataTask.put("taskId", Integer.valueOf(oriRWMons[i]));
							dataTask.put("employeeId", e.getId());
							dataTask.put("companyId", c.getId());
							if(Integer.valueOf(oriStates[i]) == stateWTJ) {
								EmployeeTaskService.deleteEmployeeTask(dataTask);
							}else if(Integer.valueOf(oriStates[i]) == stateWSH || Integer.valueOf(oriStates[i]) == stateSHZ){
								EmployeeTaskService.annulEmployeeTask(dataTask);
							}
						}
						stateList += stateWSQ+"-";
					}
					employeeTask.setTaskIdListRWMonState(stateList);
				}
				int count = employeeTaskMapper.updateByPrimaryKeySelective(employeeTask);
				if(count == 0) {
					throw new BusiException("更新employeeTask表失败");
				}
			}
		}
		
	}

}
