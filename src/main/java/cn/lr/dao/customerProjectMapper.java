package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.customerProject;

public interface customerProjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(customerProject record);

    int insertSelective(customerProject record);

    customerProject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(customerProject record);

    int updateByPrimaryKey(customerProject record);
    
    List<customerProject> selectByProject(@Param("projectId")Integer projectId,@Param("state") List<Integer> state);
    
    List<customerProject> selectByCustomer(@Param("customerId")Integer customerId,@Param("state") List<Integer> state);
    
    List<customerProject> selectByEmployee(@Param("employeeId")Integer employeeId,@Param("state") List<Integer> state,@Param("page") Integer page,@Param("size") Integer size);
    
    int selectByEmployeeCount(@Param("employeeId")Integer employeeId,@Param("state") List<Integer> state);
    
    List<customerProject> selectByEmployeeState(@Param("employeeId") Integer employeeId,@Param("state") List<Integer> state);
    
}