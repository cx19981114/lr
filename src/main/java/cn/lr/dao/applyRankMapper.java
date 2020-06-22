package cn.lr.dao;

import cn.lr.po.applyRank;

public interface applyRankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(applyRank record);

    int insertSelective(applyRank record);

    applyRank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(applyRank record);

    int updateByPrimaryKey(applyRank record);
}