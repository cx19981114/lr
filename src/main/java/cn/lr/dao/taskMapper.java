package cn.lr.dao;

import cn.lr.po.task;

public interface taskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(task record);

    int insertSelective(task record);

    task selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(task record);

    int updateByPrimaryKey(task record);
}