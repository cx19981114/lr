package cn.lr.dao;

import cn.lr.po.customerProject;

public interface customerProjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(customerProject record);

    int insertSelective(customerProject record);

    customerProject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(customerProject record);

    int updateByPrimaryKey(customerProject record);
}