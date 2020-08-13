package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.dynamic;

public interface dynamicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(dynamic record);

    int insertSelective(dynamic record);

    dynamic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(dynamic record);

    int updateByPrimaryKey(dynamic record);
    
    List<dynamic> selectByEmployeeId(@Param("employeeId") Integer employeeId,@Param("state") Integer state,@Param("page") Integer page,@Param("size") Integer size);

   	int selectByEmployeeCount(@Param("employeeId")Integer employeeId,@Param("state") Integer state);
   	
   	List<dynamic> selectByCheckId(@Param("checkId") Integer checkId,@Param("stateWSH") Integer stateWHS,@Param("stateSHZ") Integer stateSHZ,@Param("page") Integer page,@Param("size") Integer size);
   	
   	int selectByCheckCount(@Param("checkId")Integer checkId,@Param("stateWSH") Integer stateWHS,@Param("stateSHZ") Integer stateSHZ);
   	
   	List<dynamic> selectByCompanyId(@Param("companyId") Integer companyId,@Param("state") Integer state,@Param("page") Integer page,@Param("size") Integer size);
   	
   	int selectByCompanyCount(@Param("companyId")Integer companyId,@Param("state") Integer state);
   	
   	dynamic selectByTypeAndId(@Param("name")String name,@Param("id")Integer id,@Param("state") Integer state);
   	
   	List<dynamic> selectByTypeAndIdVaild(@Param("name")String name,@Param("id")Integer id,@Param("state") Integer state);

   	dynamic selectByTypeAndIdAndEmployeeId(@Param("name")String name,@Param("id")Integer id,@Param("employeeId") Integer emlpoyeeId,@Param("stateSX") Integer stateSX,@Param("stateSB") Integer stateSB);
   
   	List<dynamic> selectByTypeAndEmployeeId(@Param("name")String name,@Param("employeeId") Integer employeeId,@Param("page") Integer page,@Param("size") Integer size);
   	
   	int selectByTypeAndEmployeeIdCount(@Param("name")String name,@Param("employeeId") Integer employeeId);
   	
}