package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.task;

public interface taskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(task record);

    int insertSelective(task record);

    task selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(task record);

    int updateByPrimaryKey(task record);
    
    List<task> selectByCompanyId(@Param("companyId") Integer companyId,@Param("prevType") Integer prevType,
    		@Param("type") Integer type,@Param("state") Integer state,@Param("page") Integer page,@Param("size") Integer size);

	int selectByCompanyCount(@Param("companyId")Integer companyId,@Param("prevType") Integer prevType,
    		@Param("type") Integer type,@Param("state") Integer state);
	
	task selectByCompanyAndPrevTypeAndTypeAndStep(@Param("companyId") Integer companyId,@Param("prevType") Integer prevType,
	    		@Param("type") Integer type);
	
	List<task> selectByPost(@Param("postId")Integer postId,@Param("prevType")Integer prevType,@Param("type")Integer type);
}