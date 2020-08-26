package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.customer;

public interface customerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(customer record);

    int insertSelective(customer record);

    customer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(customer record);

    int updateByPrimaryKey(customer record);
    
    List<customer> selectByEmployeeId(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,
    		@Param("search")String search,@Param("page") Integer page,@Param("size") Integer size);

	int selectByEmployeeIdCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,
			@Param("search")String search);
	
	List<customer> selectByEmployeeIdList(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
	
	List<customer> selectByEmployeeIdDay(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);

	int selectByEmployeeIdDayCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
	
	List<customer> selectByEmployeeIdMon(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);
	
	int selectByEmployeeIdMonCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
	
	List<customer> selectByEmployeeIdQtr(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);
	
	int selectByEmployeeIdQtrCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
	
	List<customer> selectByEmployeeIdYear(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);
	
	int selectByEmployeeIdYearCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
	
	List<customer> selectByConsumeMon(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);
	
	int selectByConsumeMonCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
	
	List<customer> selectByConsumeQtr(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);
	
	int selectByConsumeQtrCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
	
	List<customer> selectByServiceMon(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);
	
	int selectByServiceMonCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
	
	List<customer> selectByServiceQtr(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);
	
	int selectByServiceQtrCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
}