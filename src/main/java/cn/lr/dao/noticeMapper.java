package cn.lr.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lr.po.notice;

public interface noticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(notice record);

    int insertSelective(notice record);

    notice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(notice record);

    int updateByPrimaryKey(notice record);
    
    List<notice> selectByCompanyId(@Param("companyId")Integer companyId,@Param("state")Integer state,@Param("page")Integer page,@Param("size")Integer size);
    
    int selectByCompanyCount(@Param("companyId")Integer companyId,@Param("state")Integer state);
}