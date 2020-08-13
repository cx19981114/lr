package cn.lr.service.company;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface DictService {

	public List<JSONObject> getDictType(JSONObject data);
}
