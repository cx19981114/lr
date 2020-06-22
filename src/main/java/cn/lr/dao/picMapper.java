package cn.lr.dao;

import cn.lr.po.pic;

public interface picMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(pic record);

    int insertSelective(pic record);

    pic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(pic record);

    int updateByPrimaryKey(pic record);
}