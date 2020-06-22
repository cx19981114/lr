package cn.lr.dao;

import cn.lr.po.company;

public interface companyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(company record);

    int insertSelective(company record);

    company selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(company record);

    int updateByPrimaryKey(company record);
}