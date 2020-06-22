package cn.lr.dao;

import cn.lr.po.rank;

public interface rankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(rank record);

    int insertSelective(rank record);

    rank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(rank record);

    int updateByPrimaryKey(rank record);
}