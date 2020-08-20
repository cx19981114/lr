package cn.lr.service.company.lmpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.dictMapper;
import cn.lr.dao.permissionMapper;
import cn.lr.dao.postMapper;
import cn.lr.exception.BusiException;
import cn.lr.po.permission;
import cn.lr.po.post;
import cn.lr.service.company.PermissionService;
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	dictMapper dictMapper;
	@Autowired
	permissionMapper permissionMapper;
	@Autowired
	postMapper postMapper;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	@Override
	public List<JSONObject> getPermissionList(JSONObject data) {
		Integer state  = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<permission> permissions = permissionMapper.selectByCompany(data.getInteger("companyId"), state);
		List<JSONObject> permissionJsonList = new ArrayList<JSONObject>();
		for(permission  p :permissions) {
			JSONObject permissionJson = new JSONObject();
			permissionJson.put("id", p.getId());
			permissionJson.put("name", p.getName());
			permissionJsonList.add(permissionJson);
		}
		return permissionJsonList;
	}
	@Override
	public Integer setPermission(JSONObject data) {
		post post = postMapper.selectByPrimaryKey(data.getInteger("postId"));
		post.setPermissionList(data.getString("permissionList"));
		int count  = postMapper.updateByPrimaryKey(post);
		if(count == 0) {
			throw new BusiException("更新post表失败");
		}
		return post.getId();
	}

}
