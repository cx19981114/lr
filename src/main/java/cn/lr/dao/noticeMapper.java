package cn.lr.dao;

import cn.lr.po.notice;

public interface noticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(notice record);

    int insertSelective(notice record);

    notice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(notice record);

    int updateByPrimaryKey(notice record);
}