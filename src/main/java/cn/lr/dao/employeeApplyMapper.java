package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.employeeApply;

public interface employeeApplyMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(employeeApply record);

	int insertSelective(employeeApply record);

	employeeApply selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(employeeApply record);

	int updateByPrimaryKey(employeeApply record);

	List<employeeApply> selectByEmployeeIdAndType(@Param("employeeId") Integer employeeId,@Param("state") Integer state, @Param("type") Integer type,@Param("page") Integer page,
			@Param("size") Integer size);

	int selectByEmployeeIdAndTypeCount(@Param("employeeId") Integer employeeId,@Param("state") Integer state, @Param("type") Integer type);
	
	List<employeeApply> selectByEmployeeId(@Param("employeeId") Integer employeeId,@Param("stateSX") Integer stateSX,@Param("stateCG") Integer stateCG);
}