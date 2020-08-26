package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.permission;

public interface permissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(permission record);

    int insertSelective(permission record);

    permission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(permission record);

    int updateByPrimaryKey(permission record);
    
    List<permission> selectByCompany(@Param("companyId")Integer companyId,@Param("state")List<Integer> state);
}