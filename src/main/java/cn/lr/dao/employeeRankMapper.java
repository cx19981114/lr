package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.employeeRank;

public interface employeeRankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employeeRank record);

    int insertSelective(employeeRank record);

    employeeRank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employeeRank record);

    int updateByPrimaryKey(employeeRank record);
    
    List<employeeRank> selectByEmployeeIdDay(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state);
    
    List<employeeRank> selectByEmployeeIdWeek(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state);
    
    List<employeeRank> selectByEmployeeIdMon(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state);
    
    List<employeeRank> selectByEmployeeIdQtr(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state);
    
    List<employeeRank> selectByEmployeeIdYear(@Param("employeeId")Integer employeeId,@Param("state")List<Integer> state);
    
    employeeRank selectByDynamic(@Param("dynamicId")Integer dynamicId,@Param("state")List<Integer> state);

}