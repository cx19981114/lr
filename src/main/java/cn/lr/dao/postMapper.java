package cn.lr.dao;

import cn.lr.po.post;

public interface postMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(post record);

    int insertSelective(post record);

    post selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(post record);

    int updateByPrimaryKey(post record);
}