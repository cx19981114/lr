package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.employee;

public interface employeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(employee record);

    int insertSelective(employee record);

    employee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(employee record);

    int updateByPrimaryKey(employee record);
    
    employee selectByPhone(String phone);
    
    List<employee> selectByCompanyId(@Param("companyId") Integer companyId,@Param("state") Integer state,@Param("page") Integer page,@Param("size") Integer size);

	int selectByCompanyCount(@Param("companyId")Integer companyId,@Param("state") Integer state);

	List<employee> selectByPostId(@Param("companyId") Integer companyId,@Param("postId") Integer postId,@Param("state") Integer state,@Param("page") Integer page,@Param("size") Integer size);
	
	int selectByPostCount(@Param("companyId")Integer companyId,@Param("postId") Integer postId,@Param("state") Integer state);
}