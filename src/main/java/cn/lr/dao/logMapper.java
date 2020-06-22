package cn.lr.dao;

import cn.lr.po.log;

public interface logMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(log record);

    int insertSelective(log record);

    log selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(log record);

    int updateByPrimaryKey(log record);
}