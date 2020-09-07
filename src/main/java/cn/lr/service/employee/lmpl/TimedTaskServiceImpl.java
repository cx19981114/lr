package cn.lr.service.employee.lmpl;

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
import cn.lr.dao.employeeApplyMapper;
import cn.lr.dao.employeeAttendanceMapper;
import cn.lr.dao.employeeLogDayMapper;
import cn.lr.dao.employeeLogTomorrowMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.employeeRestMapper;
import cn.lr.dao.employeeTaskMapper;
import cn.lr.dao.orderMapper;
import cn.lr.po.company;
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
			Integer stateWKS = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "未开始", c.getId());
			Integer stateJXZ = dictMapper.selectByCodeAndStateName(ORDER_FLOW, "进行中", c.getId());
			List<Integer> stateList = new ArrayList<Integer>();
			String date = TimeFormatUtil.timeStampToString(new Date().getTime());
			stateList.add(stateSX);
			List<employee> employees = employeeMapper.selectByCompanyId(c.getId(),stateList, 0, Integer.MAX_VALUE);
			for(employee e:employees) {
				// 更新今日未审核的任务申请
				stateList.clear();
				stateList.add(stateWTJ);
				List<employeeTask> employeeTaskWTJ = employeeTaskMapper.selectByState(e.getId(),stateList);
				for(employeeTask et:employeeTaskWTJ) {
					JSONObject eJsonObject = new JSONObject();
					eJsonObject.put("employeeTaskId", et.getId());
					eJsonObject.put("companyId", c.getId());
					EmployeeTaskService.deleteEmployeeTask(eJsonObject);
				}
				stateList.clear();
				stateList.add(stateWSH);
				stateList.add(stateSHZ);
				List<employeeTask> employeeTaskWSHAndSHZ = employeeTaskMapper.selectByState(e.getId(),stateList);
				for(employeeTask et:employeeTaskWSHAndSHZ) {
					JSONObject eJsonObject = new JSONObject();
					eJsonObject.put("employeeTaskId", et.getId());
					eJsonObject.put("companyId", c.getId());
					EmployeeTaskService.annulEmployeeTask(eJsonObject);
				}
				// 更新结束日期是今日前一天的申请
				stateList.clear();
				stateList.add(stateWSQ);
				stateList.add(stateCG);
				stateList.add(stateSHZ);
				stateList.add(stateWSH);
				stateList.add(stateWTJ);
				List<employeeApply> employeeApplies = employeeApplyMapper.selectByEmployeeId(e.getId(),stateList);
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
				stateList.clear();
				stateList.add(stateWSQ);
				stateList.add(stateCG);
				stateList.add(stateSHZ);
				stateList.add(stateWSH);
				stateList.add(stateWTJ);
				employeeAttendance employeeAttendance = employeeAttendanceMapper.selectByEmployeeIdNew(e.getId(),stateList);
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
				stateList.clear();
				stateList.add(stateWSQ);
				stateList.add(stateCG);
				stateList.add(stateSHZ);
				stateList.add(stateWSH);
				stateList.add(stateWTJ);
				employeeLogDay employeeLogDay = employeeLogDayMapper.selectByEmployeeIdNew(e.getId(),stateList);
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
				stateList.clear();
				stateList.add(stateWSQ);
				stateList.add(stateCG);
				stateList.add(stateSHZ);
				stateList.add(stateWSH);
				stateList.add(stateWTJ);
				employeeLogTomorrow employeeLogTomorrow = employeeLogTomorrowMapper.selectByEmployeeIdNew(e.getId(),stateList);
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
				stateList.clear();
				stateList.add(stateWSQ);
				stateList.add(stateCG);
				stateList.add(stateSHZ);
				stateList.add(stateWSH);
				stateList.add(stateWTJ);
				employeeRest employeeRest = employeeRestMapper.selectByEmployeeIdNew(e.getId(),stateList,date);
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
//				stateList.clear();
//				stateList.add(stateWTJ);
//				List<customerPerformance> customerPerformancesWTJ = customerPerformanceMapper.selectByEmployeeState(e.getId(),stateList);
//				for(customerPerformance cp:customerPerformancesWTJ) {
//					JSONObject cpJsonObject = new JSONObject();
//					cpJsonObject.put("customerPerformanceId", cp.getId());
//					cpJsonObject.put("companyId", e.getCompanyId());
//					CustomerPerformanceService.deleteCustomerPerformance(cpJsonObject);
//				}
//				stateList.clear();
//				stateList.add(stateWSH);
//				stateList.add(stateSHZ);
//				List<customerPerformance> customerPerformancesWSHAndSHZ = customerPerformanceMapper.selectByEmployeeState(e.getId(),stateList);
//				for(customerPerformance cp:customerPerformancesWSHAndSHZ) {
//					JSONObject cpJsonObject = new JSONObject();
//					cpJsonObject.put("customerPerformanceId", cp.getId());
//					cpJsonObject.put("companyId", e.getCompanyId());
//					CustomerPerformanceService.annulCustomerPerformance(cpJsonObject);
//				}
				//更新未审核的预约
				stateList.clear();
				stateList.add(stateWTJ);
				List<order> ordersWTJ = orderMapper.selectByEmployeeStateApply(e.getId(),stateList,date);
				for(order o:ordersWTJ) {
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("orderId", o.getId());
					oJsonObject.put("companyId", e.getCompanyId());
					OrderService.deleteOrder(oJsonObject);
				}
				stateList.clear();
				stateList.add(stateWSH);
				stateList.add(stateSHZ);
				List<order> ordersWSHAndSHZ = orderMapper.selectByEmployeeStateApply(e.getId(),stateList,date);
				for(order o:ordersWSHAndSHZ) {
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("orderId", o.getId());
					oJsonObject.put("companyId", e.getCompanyId());
					OrderService.annulOrder(oJsonObject);
				}
				//更新未审核的预约完成
//				stateList.clear();
//				stateList.add(stateWTJ);
//				List<order> ordersFinishWTJ = orderMapper.selectByEmployeeFinishState(e.getId(),stateList);
//				for(order o:ordersFinishWTJ) {
//					JSONObject oJsonObject = new JSONObject();
//					oJsonObject.put("orderId", o.getId());
//					oJsonObject.put("companyId", e.getCompanyId());
//					OrderService.deleteOrderFinish(oJsonObject);
//				}
//				stateList.clear();
//				stateList.add(stateWSH);
//				stateList.add(stateSHZ);
//				List<order> ordersFinishWSHAndSHZ = orderMapper.selectByEmployeeFinishState(e.getId(),stateList);
//				for(order o:ordersFinishWSHAndSHZ) {
//					JSONObject oJsonObject = new JSONObject();
//					oJsonObject.put("orderId", o.getId());
//					oJsonObject.put("companyId", e.getCompanyId());
//					OrderService.annulOrderFinish(oJsonObject);
//				}
				List<Integer> stateListAppply = new ArrayList<Integer>();
				List<Integer> stateListOrder = new ArrayList<Integer>();
				stateListAppply.add(stateCG);
				stateListOrder.add(stateWKS);
				List<order> ordersWKS = orderMapper.selectByEmployeeOrder(e.getId(),stateListAppply,stateListOrder,date);
				for(order o:ordersWKS) {
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("orderId", o.getId());
					oJsonObject.put("companyId", e.getCompanyId());
					oJsonObject.put("evaluate", "预约过期未开始");
					OrderService.cancelOrder(oJsonObject);
				}
				stateListAppply.clear();;
				stateListOrder.clear();;
				stateListAppply.add(stateCG);
				stateListOrder.add(stateJXZ);
				List<order> ordersJXZ = orderMapper.selectByEmployeeOrder(e.getId(),stateListAppply,stateListOrder,date);
				for(order o:ordersJXZ) {
					JSONObject oJsonObject = new JSONObject();
					oJsonObject.put("orderId", o.getId());
					oJsonObject.put("companyId", e.getCompanyId());
					oJsonObject.put("evaluate", "预约过期仍在进行中");
					OrderService.cancelOrder(oJsonObject);
				}
				//更新未审核的客户项目
//				stateList.clear();
//				stateList.add(stateWTJ);
//				List<customerProject> customerProjectWTJ = customerProjectMapper.selectByEmployeeState(e.getId(),stateList);
//				for(customerProject cp:customerProjectWTJ) {
//					JSONObject cpJsonObject = new JSONObject();
//					cpJsonObject.put("customerProjectId", cp.getId());
//					cpJsonObject.put("companyId", e.getCompanyId());
//					CustomerProjectService.deleteCustomerProject(cpJsonObject);
//				}
//				stateList.clear();
//				stateList.add(stateWSH);
//				stateList.add(stateSHZ);
//				List<customerProject> customerProjectWSHAndSHZ = customerProjectMapper.selectByEmployeeState(e.getId(),stateList);
//				for(customerProject cp:customerProjectWSHAndSHZ) {
//					JSONObject cpJsonObject = new JSONObject();
//					cpJsonObject.put("customerProjectId", cp.getId());
//					cpJsonObject.put("companyId", e.getCompanyId());
//					CustomerProjectService.annulCustomerProject(cpJsonObject);
//				}
			}
		}
		
	}

}
