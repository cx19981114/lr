package cn.lr.dao;

import cn.lr.po.employee;

public interface employeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employee record);

    int insertSelective(employee record);

    employee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employee record);

    int updateByPrimaryKey(employee record);
}