package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.employeeAttendance;

public interface employeeAttendanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employeeAttendance record);

    int insertSelective(employeeAttendance record);

    employeeAttendance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employeeAttendance record);

    int updateByPrimaryKey(employeeAttendance record);
    
    employeeAttendance selectByEmployeeId(Integer employeeId);
    
    List<employeeAttendance> selectByEmployeeId(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);

	int selectByEmployeeIdCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
	
	employeeAttendance selectByEmployeeIdNew(@Param("employeeId")Integer employeeId,@Param("state") List<Integer> state);

}