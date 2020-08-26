package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.employeeLogDay;

public interface employeeLogDayMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employeeLogDay record);

    int insertSelective(employeeLogDay record);

    employeeLogDay selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employeeLogDay record);

    int updateByPrimaryKey(employeeLogDay record);
    
    List<employeeLogDay> selectByEmployeeId(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);
    
    int selectByEmployeeIdCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);

    employeeLogDay selectByEmployeeIdNew(@Param("employeeId")Integer employeeId,@Param("state") List<Integer> state);

}