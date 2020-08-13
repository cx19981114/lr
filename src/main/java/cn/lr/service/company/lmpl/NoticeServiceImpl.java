package cn.lr.service.company.lmpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.companyMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.noticeMapper;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.notice;
import cn.lr.service.company.NoticeService;
@Service
@Transactional
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	noticeMapper noticeMapper;
	@Autowired
	companyMapper companyMapper;
	@Autowired
	dictMapper dictMapper;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Override
	public Page<notice> getNoticeByCompany(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<notice> notices = noticeMapper.selectByCompanyId(companyId,state,(pageNum-1)*PAGESIZE,PAGESIZE);
		int total = noticeMapper.selectByCompanyCount(companyId,state);
		Page<notice> page = new Page<notice>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(notices);
		return page;
	}
	public List<notice> getNoticeByCompanyList(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<notice> notices = noticeMapper.selectByCompanyId(companyId,state,0,Integer.MAX_VALUE);
		return notices;
	}
	@Override
	public Integer addNotice(JSONObject data) {
		notice notice = new notice();
		notice.setCompanyId(data.getInteger("companyId"));
		notice.setContent(data.getString("content"));
		notice.setName(data.getString("name"));
		notice.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效",data.getInteger("companyId")));
		int count = noticeMapper.insertSelective(notice);
		if (count == 0) {
			throw new BusiException("添加notice表失败");
		}
		return notice.getId();
	}
	@Override
	public Integer modifyNotice(JSONObject data) {
		notice notice = noticeMapper.selectByPrimaryKey(data.getInteger("noticeId"));
		notice.setCompanyId(data.getInteger("companyId"));
		notice.setContent(data.getString("content"));
		notice.setName(data.getString("name"));
		int count = noticeMapper.updateByPrimaryKeySelective(notice);
		if (count == 0) {
			throw new BusiException("更新notice表失败");
		}
		return notice.getId();
	}
	@Override
	public Integer deleteNotice(JSONObject data) {
		notice notice = noticeMapper.selectByPrimaryKey(data.getInteger("noticeId"));
		notice.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = noticeMapper.updateByPrimaryKeySelective(notice);
		if (count == 0) {
			throw new BusiException("更新notice表失败");
		}
		return notice.getId();
	}
	@Override
	public notice getNotice(JSONObject data) {
		notice notice = noticeMapper.selectByPrimaryKey(data.getInteger("noticeId"));
		if(notice == null || notice.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该公告不存在");
		}
		return notice;
	}

}
