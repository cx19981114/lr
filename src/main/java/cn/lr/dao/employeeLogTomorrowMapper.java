package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.employeeLogTomorrow;

public interface employeeLogTomorrowMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employeeLogTomorrow record);

    int insertSelective(employeeLogTomorrow record);

    employeeLogTomorrow selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employeeLogTomorrow record);

    int updateByPrimaryKey(employeeLogTomorrow record);
    
    List<employeeLogTomorrow> selectByEmployeeId(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,
			@Param("size") Integer size);
    
    int selectByEmployeeIdCount(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);

    employeeLogTomorrow selectByEmployeeIdNew(@Param("employeeId")Integer employeeId,@Param("state") List<Integer> state);

}