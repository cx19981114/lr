package cn.lr.dao;

import cn.lr.po.employeeRank;

public interface employeeRankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employeeRank record);

    int insertSelective(employeeRank record);

    employeeRank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employeeRank record);

    int updateByPrimaryKey(employeeRank record);
}