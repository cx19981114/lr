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
    
    List<employeeLogTomorrow> selectByEmployeeId(@Param("employeeId") Integer employeeId,@Param("state") Integer state,@Param("page") Integer page,
			@Param("size") Integer size);

    employeeLogTomorrow selectByEmployeeIdNew(@Param("employeeId")Integer employeeId,@Param("stateSX") Integer stateSX,@Param("stateSB") Integer stateSB);

	int selectByEmployeeIdCount(@Param("employeeId") Integer employeeId,@Param("state") Integer state);
}