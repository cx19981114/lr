package cn.lr.dao;

import cn.lr.po.permission;

public interface permissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(permission record);

    int insertSelective(permission record);

    permission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(permission record);

    int updateByPrimaryKey(permission record);
}