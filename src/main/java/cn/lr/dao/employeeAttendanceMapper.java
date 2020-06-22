package cn.lr.dao;

import cn.lr.po.employeeAttendance;

public interface employeeAttendanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employeeAttendance record);

    int insertSelective(employeeAttendance record);

    employeeAttendance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employeeAttendance record);

    int updateByPrimaryKey(employeeAttendance record);
}