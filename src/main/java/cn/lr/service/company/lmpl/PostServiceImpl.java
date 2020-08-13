package cn.lr.service.company.lmpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.companyMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.postMapper;
import cn.lr.dao.postTaskMapper;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.exception.DataException;
import cn.lr.po.post;
import cn.lr.po.postTask;
import cn.lr.service.company.PostService;
@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	postMapper postMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	companyMapper companyMapper;
	@Autowired
	postTaskMapper postTaskMapper; 
	
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	
	@Override
	public Integer addPost(JSONObject data) {
		post record = new post();
		record.setCompanyId(data.getInteger("companyId"));
		record.setName(data.getString("name"));
		record.setPermissionList(data.getString("permissionList"));
		record.setPic(data.getString("pic"));
		record.setLeaderPostId(data.getInteger("leaderPostId"));
		record.setNum(0);
		record.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效",data.getInteger("companyId")));
		int count = postMapper.insert(record);
		if(count == 0) {
			throw new BusiException("添加post表失败");
		}
		postTask postTask = new postTask();
		postTask.setPostId(record.getId());
		count = postTaskMapper.insertSelective(postTask);
		if(count == 0) {
			throw new BusiException("添加postTask表失败");
		}
		return record.getId();
	}

	@Override
	public Integer modifyPost(JSONObject data) {
		post test2 = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		test2.setName(data.getString("name"));
		test2.setPic(data.getString("pic"));
		test2.setPermissionList(data.getString("permissionList"));
		test2.setLeaderPostId(data.getInteger("leaderPostId"));
		int count = postMapper.updateByPrimaryKeySelective(test2);
		if(count == 0) {
			throw new BusiException("更新post表失败");
		}
		return test2.getId();
	}

	@Override
	public Integer deletePost(JSONObject data) {
		post test2 = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		test2.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		if(test2.getNum() != 0) {
			throw new DataException("该职位下有职员存在");
		}
		int count = postMapper.updateByPrimaryKeySelective(test2);
		if(count == 0) {
			throw new BusiException("更新post表失败");
		}
		return test2.getId();
	}

	@Override
	public post getPost(JSONObject data) {
		post post = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		if(post == null || post.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")) ) {
			throw new BusiException("该公司岗位不存在");
		}
		return post;
	}

	@Override
	public Page<post> getPostByCompany(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer pageNum = data.getInteger("pageNum");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<post> posts = postMapper.selectByCompany(companyId,state,(pageNum-1)*PAGESIZE,PAGESIZE);
		int total = postMapper.selectByCompanyCount(companyId,state);
		Page<post> page = new Page<post>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(total);
		page.setList(posts);
		return page;
	}

	@Override
	public List<JSONObject> getPostTypeAndCount(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<post> posts = postMapper.selectByCompany(companyId,state,0,Integer.MAX_VALUE);
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		int allCount = 0;
		for(post p:posts) {
			allCount += p.getNum();
		}
		JSONObject all = new JSONObject();
		all.put("value", "全部");
		all.put("count", allCount);
		all.put("id", 0);
		jsonList.add(all);
		for(post p:posts) {
			JSONObject dataJson = new JSONObject();
			dataJson.put("value", p.getName());
			dataJson.put("count", p.getNum());
			dataJson.put("id", p.getId());
			jsonList.add(dataJson);
		}
		return jsonList;
	}

}
