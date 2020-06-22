package cn.lr.dao;

import cn.lr.po.project;

public interface projectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(project record);

    int insertSelective(project record);

    project selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(project record);

    int updateByPrimaryKey(project record);
}