package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.pic;

public interface picMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(pic record);

    int insertSelective(pic record);

    pic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(pic record);

    int updateByPrimaryKey(pic record);
    
    List<pic> selectByCompanyId(@Param("companyId")Integer companyId,@Param("state")Integer state);
    
    pic selectByPic(String pic);
    
    int updateByPic(pic record);
}