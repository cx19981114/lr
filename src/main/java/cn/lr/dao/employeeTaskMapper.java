package cn.lr.dao;

import cn.lr.po.employeeTask;

public interface employeeTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employeeTask record);

    int insertSelective(employeeTask record);

    employeeTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employeeTask record);

    int updateByPrimaryKey(employeeTask record);
}