package cn.lr.dao;

import java.util.List;

import cn.lr.po.employeeRank;

public interface employeeRankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employeeRank record);

    int insertSelective(employeeRank record);

    employeeRank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employeeRank record);

    int updateByPrimaryKey(employeeRank record);
    
    List<employeeRank> selectByEmployeeIdDay(Integer employeeId);
    
    List<employeeRank> selectByEmployeeIdMon(Integer employeeId);
    
    List<employeeRank> selectByEmployeeIdQtr(Integer employeeId);
    
    List<employeeRank> selectByEmployeeIdYear(Integer employeeId);

}