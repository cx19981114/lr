package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.project;

public interface projectMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(project record);

	int insertSelective(project record);

	project selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(project record);

	int updateByPrimaryKey(project record);

	List<project> selectByCompanyId(@Param("companyId") Integer companyId,@Param("state") List<Integer> state, @Param("page") Integer page,
			@Param("size") Integer size);

	int selectByCompanyCount(@Param("companyId") Integer companyId,@Param("state") List<Integer> state);
}