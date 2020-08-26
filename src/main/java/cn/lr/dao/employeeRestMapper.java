package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.employeeRest;

public interface employeeRestMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(employeeRest record);

	int insertSelective(employeeRest record);

	employeeRest selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(employeeRest record);

	int updateByPrimaryKey(employeeRest record);

	List<employeeRest> selectByEmployeeId(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,
			@Param("page") Integer page,@Param("size") Integer size);
	
	int selectByEmployeeIdCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);

	employeeRest selectByEmployeeIdNew(@Param("employeeId")Integer employeeId,
			@Param("state") List<Integer> state,@Param("date") String date);

	List<employeeRest> selectByEmployeeList(@Param("employeeIdList") List<Integer> employeeIdList,
			@Param("state") List<Integer> state,@Param("date") String date);
	
	List<employeeRest> selectByEmployeeTeam(@Param("employeeIdList") List<Integer> employeeIdList,
			@Param("state") List<Integer> state,@Param("date") String date,
			@Param("type") List<Integer> type,@Param("page") Integer page,@Param("size") Integer size);
	
	int selectByEmployeeTeamCount(@Param("employeeIdList") List<Integer> employeeIdList,
			@Param("state") List<Integer> state,@Param("date") String date,
			@Param("type") List<Integer> type);

}