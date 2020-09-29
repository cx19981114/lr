package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.customerPerformance;
import cn.lr.po.statisticType;

public interface customerPerformanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(customerPerformance record);

    int insertSelective(customerPerformance record);

    customerPerformance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(customerPerformance record);

    int updateByPrimaryKey(customerPerformance record);
    
    List<customerPerformance> selectByEmployeeState(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state);
    
    List<customerPerformance> selectByEmployeeConditionDay(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state,@Param("date")String date,
    		@Param("page") Integer page,@Param("size") Integer size);
    
	int selectByEmployeeConditionDayCount(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state,@Param("date")String date);
	
	List<customerPerformance> selectByEmployeeConditionMon(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state,@Param("date")String date,
			@Param("page") Integer page,@Param("size") Integer size);
	
	int selectByEmployeeConditionMonCount(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state,@Param("date")String date);
	
	List<customerPerformance> selectByEmployeeConditionYear(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state,@Param("date")String date,
			@Param("page") Integer page,@Param("size") Integer size);
	
	int selectByEmployeeConditionYearCount(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state,@Param("date")String date);
	
	List<customerPerformance> selectByEmployeeCondition(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state,
			@Param("page") Integer page,@Param("size") Integer size);
	
	int selectByEmployeeConditionCount(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state);
	
	List<customerPerformance> selectByEmployee(@Param("employeeId")Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,@Param("size") Integer size);
    
    int selectByEmployeeCount(@Param("employeeId")Integer employeeId,@Param("state") List<Integer> state);
    
    List<statisticType> selectByCustomerComsumer(@Param("customerId")Integer customerId,@Param("state") List<Integer> state);
    
    List<customerPerformance> selectByCustomerComsumerList(@Param("customerId")Integer customerId,@Param("date")String date,@Param("state") List<Integer> state,@Param("page") Integer page,@Param("size") Integer size);
    
    int selectByCustomerComsumerListCount(@Param("customerId")Integer customerId,@Param("date")String date,@Param("state") List<Integer> state);
    
    customerPerformance selectByCustomerId(Integer customerId);
}