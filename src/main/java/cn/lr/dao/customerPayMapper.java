package cn.lr.dao;

import cn.lr.po.customerPay;

public interface customerPayMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(customerPay record);

    int insertSelective(customerPay record);

    customerPay selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(customerPay record);

    int updateByPrimaryKey(customerPay record);
}