package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.rank;

public interface rankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(rank record);

    int insertSelective(rank record);

    rank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(rank record);

    int updateByPrimaryKey(rank record);
    
    int selectByName(@Param("name")String name,@Param("companyId")Integer companyId,@Param("state")List<Integer> state);
}