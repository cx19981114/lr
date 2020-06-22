package cn.lr.dao;

import cn.lr.po.system;

public interface systemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(system record);

    int insertSelective(system record);

    system selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(system record);

    int updateByPrimaryKey(system record);
}