package cn.lr.dao;

import cn.lr.po.customer;

public interface customerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(customer record);

    int insertSelective(customer record);

    customer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(customer record);

    int updateByPrimaryKey(customer record);
}