package cn.lr.dao;

import cn.lr.po.employeeLog;

public interface employeeLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employeeLog record);

    int insertSelective(employeeLog record);

    employeeLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employeeLog record);

    int updateByPrimaryKey(employeeLog record);
}