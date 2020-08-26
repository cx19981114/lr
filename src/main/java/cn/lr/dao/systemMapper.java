package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.system;

public interface systemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(system record);

    int insertSelective(system record);

    system selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(system record);

    int updateByPrimaryKey(system record);
    
    List<system> selectByCompanyId(@Param("companyId")Integer companyId,@Param("state")List<Integer> state,@Param("page")Integer page,@Param("size")Integer size);
    
    int selectByCompanyCount(@Param("companyId")Integer companyId,@Param("state")List<Integer> state);
}