package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.applyCheck;

public interface applyCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(applyCheck record);

    int insertSelective(applyCheck record);

    applyCheck selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(applyCheck record);

    int updateByPrimaryKey(applyCheck record);
    
    List<applyCheck> selectByEmployeeId(@Param("employeeId") Integer employeeId,@Param("page") Integer page,@Param("size") Integer size);

   	int selectByEmployeeCount(@Param("employeeId")Integer employeeId);
}