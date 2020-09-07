package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.company;

public interface companyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(company record);

    int insertSelective(company record);

    company selectByPrimaryKey(Integer id);
    
    company selectByName(company record);

    int updateByPrimaryKeySelective(company record);

    int updateByPrimaryKey(company record);
    
    List<company> listCompanyVaild();
    
    List<company> selectCompanyCondition(@Param("search")String search,@Param("page") Integer page,@Param("size") Integer size);
   
    int selectCompanyConditionCount(@Param("search")String search);
}