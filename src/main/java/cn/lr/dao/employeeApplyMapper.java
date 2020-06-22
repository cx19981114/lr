package cn.lr.dao;

import cn.lr.po.employeeApply;

public interface employeeApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employeeApply record);

    int insertSelective(employeeApply record);

    employeeApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employeeApply record);

    int updateByPrimaryKey(employeeApply record);
}