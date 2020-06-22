package cn.lr.dao;

import cn.lr.po.rest;

public interface restMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(rest record);

    int insertSelective(rest record);

    rest selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(rest record);

    int updateByPrimaryKey(rest record);
}