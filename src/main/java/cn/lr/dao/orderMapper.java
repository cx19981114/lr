package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.order;
import cn.lr.po.statisticType;

public interface orderMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(order record);

	int insertSelective(order record);

	order selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(order record);

	int updateByPrimaryKey(order record);

	List<order> selectByEmployeeCondition(@Param("employeeId") List<Integer> employeeId,
			@Param("stateType") List<Integer> stateType, @Param("date") String date, @Param("startTime") String startTime,
			@Param("state")List<Integer> state, @Param("page") Integer page, @Param("size") Integer size);

	int selectByEmployeeConditionCount(@Param("employeeId") List<Integer> employeeId,
			@Param("stateType") List<Integer> stateType, @Param("date") String date, @Param("startTime") String startTime,
			@Param("state")List<Integer> state);

	int selectByEmployeeConditionCount1(@Param("employeeId") List<Integer> employeeId,
			@Param("stateType") List<Integer> stateType, @Param("date") String date, @Param("state")List<Integer> state);

	List<order> selectByEmployeeIdDate(@Param("employeeId") Integer employeeId, @Param("date") String date,
			@Param("applyState") List<Integer> applyState,@Param("orderState") List<Integer> orderState);

	int selectByEmployeeIdCount(@Param("employeeId") List<Integer> employeeId, @Param("state") List<Integer> state);

	List<order> selectByEmployeeIdHistory(@Param("employeeId") Integer employeeId,@Param("date") String date,
			@Param("applyState") List<Integer> applyState, @Param("orderState") List<Integer> orderState,
			@Param("page") Integer page, @Param("size") Integer size);

	Integer selectByEmployeeIdHistoryCount(@Param("employeeId") Integer employeeId,@Param("date") String date,
			@Param("applyState") List<Integer> applyState, @Param("orderState") List<Integer> orderState);

	List<order> selectByCustomerIdHistory(@Param("customerId") Integer customerId,@Param("date") String date,
			@Param("applyState") List<Integer> applyState, @Param("orderState") List<Integer> orderState,
			@Param("page") Integer page, @Param("size") Integer size);

	Integer selectByCustomerIdHistoryCount(@Param("customerId") Integer customerId,@Param("date") String date,
			@Param("applyState") List<Integer> applyState, @Param("orderState") List<Integer> orderState);

	List<order> selectByProjectIdHistory(@Param("customerProjectId") Integer customerProjectId,
			@Param("applyState") List<Integer> applyState, @Param("orderState") List<Integer> orderState);

	List<order> selectByProjectCount(@Param("employeeId") List<Integer> employeeId,
			@Param("state") List<Integer> state);

	List<order> selectByProjectDayCount(@Param("employeeId") List<Integer> employeeId, @Param("date") String date,
			@Param("state") List<Integer> state);

	List<order> selectByProjectMonCount(@Param("employeeId") List<Integer> employeeId, @Param("date") String date,
			@Param("state") List<Integer> state);

	List<order> selectByProjectYearCount(@Param("employeeId") List<Integer> employeeId, @Param("date") String date,
			@Param("state") List<Integer> state);

	List<order> selectByProjectDay(@Param("employeeId") List<Integer> employeeId, @Param("projectId") Integer projectId,
			@Param("date") String date, @Param("state") List<Integer> state);

	List<order> selectByProjectMon(@Param("employeeId") List<Integer> employeeId, @Param("projectId") Integer projectId,
			@Param("date") String date, @Param("state") List<Integer> state);

	List<order> selectByProjectYear(@Param("employeeId") List<Integer> employeeId,
			@Param("projectId") Integer projectId, @Param("date") String date, @Param("state") List<Integer> state);

	List<order> selectByProjectAllCount(@Param("employeeId") List<Integer> employeeId,
			@Param("state") List<Integer> state);

	List<order> selectByProjectAll(@Param("employeeId") List<Integer> employeeId, @Param("projectId") Integer projectId,
			@Param("state") List<Integer> state);

	List<order> selectByEmployeeOrder(@Param("employeeId") Integer employeeId, @Param("applyState") List<Integer> applyState,
			@Param("orderState") List<Integer> orderState, @Param("date") String date);

	List<order> selectByEmployeeStateApply(@Param("employeeId") Integer employeeId, 
			@Param("state") List<Integer> state,@Param("date") String date);

	List<order> selectByEmployeeCXApply(@Param("employeeId") Integer employeeId,
			@Param("applyState") List<Integer> applyState);

	List<order> selectByEmployeeCXOrder(@Param("employeeId") Integer employeeId,
			@Param("applyState") List<Integer> applyState, @Param("orderState") List<Integer> orderState);

	List<order> selectByEmployeeCXOrderApply(@Param("employeeId") Integer employeeId,
			@Param("applyState") List<Integer> applyState, @Param("orderState") List<Integer> orderState,
			@Param("applyOrderState") List<Integer> applyOrderState);

	List<order> selectByEmployeeFinishState(@Param("employeeId") Integer employeeId, 
			@Param("state") List<Integer> state);

	List<order> selectByProject(@Param("employeeId") Integer employeeId,
			@Param("customerProjectId") Integer customerProjectId, @Param("state") List<Integer> state);

	List<order> selectByEmployee(@Param("employeeId") Integer employeeId, @Param("state") List<Integer> state,
			@Param("page") Integer page, @Param("size") Integer size);

	int selectByEmployeeCount(@Param("employeeId") Integer employeeId, @Param("state") List<Integer> state);
	
	List<statisticType> selectByCustomerHistory(@Param("customerId")Integer customerId,@Param("applyState") List<Integer> applyState,
			@Param("orderState") List<Integer> orderState);
	
	List<statisticType> selectByEmployeeHistory(@Param("employeeId")Integer employeeId,@Param("applyState") List<Integer> applyState,
			@Param("orderState") List<Integer> orderState);
}