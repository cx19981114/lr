package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.employeeTask;

public interface employeeTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employeeTask record);

    int insertSelective(employeeTask record);

    employeeTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employeeTask record);

    int updateByPrimaryKey(employeeTask record);
    
    List<employeeTask> selectByEmployeeId(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);
    
    int selectByEmployeeIdCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
    
    List<employeeTask> selectByState(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
}