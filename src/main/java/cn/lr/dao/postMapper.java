package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.post;

public interface postMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(post record);

    int insertSelective(post record);

    post selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(post record);

    int updateByPrimaryKey(post record);
    
    post selectByCompanyIdAndPostName(post record);

	List<post> selectByCompany(@Param("companyId") Integer companyId,@Param("state") List<Integer> state, @Param("page") Integer page,
			@Param("size") Integer size);

	int selectByCompanyCount(@Param("companyId")Integer companyId,@Param("state") List<Integer> state);
	
	int selectByNameAndCompany(@Param("name") String name,@Param("companyId")Integer companyId,@Param("state") List<Integer> state);
}