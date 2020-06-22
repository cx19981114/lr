package cn.lr.dao;

import cn.lr.po.dict;

public interface dictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(dict record);

    int insertSelective(dict record);

    dict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(dict record);

    int updateByPrimaryKey(dict record);
}