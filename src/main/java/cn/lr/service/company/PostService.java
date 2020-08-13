package cn.lr.service.company;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dto.Page;
import cn.lr.po.post;

public interface PostService {

	public Integer addPost(JSONObject data);
	
	public Integer modifyPost(JSONObject data);
	
	public Integer deletePost(JSONObject data);
	
	public post getPost(JSONObject data);
	
	public Page<post> getPostByCompany(JSONObject data);
	
	public List<JSONObject> getPostTypeAndCount(JSONObject data);
}
