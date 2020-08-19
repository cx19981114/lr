package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.order;

public interface orderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(order record);

    int insertSelective(order record);

    order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(order record);

    int updateByPrimaryKey(order record);
    
	List<order> selectByEmployeeCondition(@Param("employeeId")List<Integer> employeeId,@Param("state")List<Integer> state,
			@Param("date")String date,@Param("startTime")String startTime,@Param("stateCG") Integer stateCG,
			@Param("page") Integer page,@Param("size") Integer size);
	
	int selectByEmployeeConditionCount(@Param("employeeId")List<Integer> employeeId,@Param("state")List<Integer> state,@Param("date")String date,@Param("startTime")String startTime,@Param("stateCG") Integer stateCG);
	
	int selectByEmployeeConditionCount1(@Param("employeeId")List<Integer> employeeId,@Param("state")List<Integer> state,@Param("date")String date,@Param("stateCG") Integer stateCG);
	
	List<order> selectByEmployeeIdDate(@Param("employeeId") Integer employeeId, @Param("date") String date,@Param("state") Integer state);
	
	int selectByEmployeeIdCount(@Param("employeeId") List<Integer> employeeId,@Param("state") Integer state);
	
	List<order> selectByEmployeeIdHistory(@Param("employeeId") Integer employeeId,@Param("applyState") Integer applyState,
			@Param("orderState") Integer orderState,@Param("page") Integer page,@Param("size") Integer size);
	
	Integer selectByEmployeeIdHistoryCount(@Param("employeeId") Integer employeeId,@Param("applyState") Integer applyState,
			@Param("orderState") Integer orderState);
	
	List<order> selectByCustomerIdHistory(@Param("customerId") Integer customerId,@Param("applyState") Integer applyState,
			@Param("orderState") Integer orderState,@Param("page") Integer page,@Param("size") Integer size);
	
	Integer selectByCustomerIdHistoryCount(@Param("customerId") Integer customerId,@Param("applyState") Integer applyState,
			@Param("orderState") Integer orderState);
	
	List<order> selectByProjectIdHistory(@Param("customerProjectId") Integer customerProjectId,@Param("applyState") Integer applyState,
			@Param("orderState") Integer orderState);
	
	List<order> selectByProjectCount(@Param("employeeId") List<Integer> employeeId,@Param("state") Integer state);
	
	List<order> selectByProjectDayCount(@Param("employeeId") List<Integer> employeeId,@Param("date")String date,@Param("state") Integer state);
	
	List<order> selectByProjectMonCount(@Param("employeeId") List<Integer> employeeId,@Param("date")String date,@Param("state") Integer state);
	
	List<order> selectByProjectYearCount(@Param("employeeId") List<Integer> employeeId,@Param("date")String date,@Param("state") Integer state);
	
	List<order> selectByProjectDay(@Param("employeeId") List<Integer> employeeId,@Param("projectId") Integer projectId,@Param("date")String date,@Param("state") Integer state);
	
	List<order> selectByProjectMon(@Param("employeeId") List<Integer> employeeId,@Param("projectId") Integer projectId,@Param("date")String date,@Param("state") Integer state);
	
	List<order> selectByProjectYear(@Param("employeeId") List<Integer> employeeId,@Param("projectId") Integer projectId,@Param("date")String date,@Param("state") Integer state);

	List<order> selectByEmployeeWKS(@Param("employeeId") Integer employeeId,@Param("applyState") Integer applyState,
			@Param("orderState") Integer orderState,@Param("date") String date);
	
	List<order> selectByEmployeeWTJ(@Param("employeeId") Integer employeeId,@Param("state") Integer state,@Param("date") String date);
	
	List<order> selectByEmployeeWSHAndSHZ(@Param("employeeId") Integer employeeId,@Param("stateWSH") Integer stateWSH,
			@Param("stateSHZ") Integer stateSHZ,@Param("date") String date);
	
	List<order> selectByEmployeeCXApply(@Param("employeeId") Integer employeeId,@Param("applyState") List<Integer> applyState);
	
	List<order> selectByEmployeeCXOrder(@Param("employeeId") Integer employeeId,@Param("applyState") Integer applyState
			,@Param("orderState") Integer orderState);
	
	List<order> selectByEmployeeCXOrderApply(@Param("employeeId") Integer employeeId,@Param("applyState") Integer applyState
			,@Param("orderState") Integer orderState,@Param("applyOrderState") List<Integer> applyOrderState);
	
	List<order> selectByEmployeeFinishWTJ(@Param("employeeId") Integer employeeId,@Param("state") Integer state);
	
	List<order> selectByEmployeeFinishWSHAndSHZ(@Param("employeeId") Integer employeeId,@Param("stateWSH") Integer stateWSH,@Param("stateSHZ") Integer stateSHZ);
	
	List<order> selectByProjectAllCount(@Param("employeeId") List<Integer> employeeId,@Param("state") Integer state);
	
	List<order> selectByProjectAll(@Param("employeeId") List<Integer> employeeId,@Param("projectId") Integer projectId,@Param("state") Integer state);
	
	List<order> selectByProject(@Param("employeeId") Integer employeeId,@Param("customerProjectId") Integer customerProjectId,
			@Param("applyState") Integer applyState);

	List<order> selectByEmployee(@Param("employeeId")Integer employeeId,@Param("state") Integer state,@Param("page") Integer page,@Param("size") Integer size);
    
    int selectByEmployeeCount(@Param("employeeId")Integer employeeId,@Param("state") Integer state);
}