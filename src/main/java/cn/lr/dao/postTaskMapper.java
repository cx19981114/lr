package cn.lr.dao;

import cn.lr.po.postTask;

public interface postTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(postTask record);

    int insertSelective(postTask record);

    postTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(postTask record);

    int updateByPrimaryKey(postTask record);
}