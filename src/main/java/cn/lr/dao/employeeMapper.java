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
    
    employee selectByPhone(@Param("phone")String phone,@Param("state")List<Integer> state);
    
    List<employee> selectByCompanyId(@Param("companyId") Integer companyId,@Param("state") List<Integer> state,@Param("page") Integer page,@Param("size") Integer size);

	int selectByCompanyCount(@Param("companyId")Integer companyId,@Param("state") List<Integer> state);

	List<employee> selectByPostId(@Param("companyId") Integer companyId,@Param("postId") List<Integer> postId,
			@Param("state") List<Integer> state,@Param("page") Integer page,@Param("size") Integer size);
	
	int selectByPostCount(@Param("companyId")Integer companyId,@Param("postId")  List<Integer> postId,
			@Param("state") List<Integer> state);
}