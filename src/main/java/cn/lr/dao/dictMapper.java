package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.dict;

public interface dictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(dict record);

    int insertSelective(dict record);

    dict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(dict record);

    int updateByPrimaryKey(dict record);
    
    int selectByCodeAndStateName(@Param("code")String code,@Param("stateName")String stateName,@Param("companyId")Integer companyId);

    String selectByCodeAndStateCode(@Param("code")String code,@Param("stateCode")Integer stateCode,@Param("companyId")Integer companyId);
    
    List<dict> selectByName(@Param("name")String name,@Param("companyId")Integer companyId);
}