package cn.lr.service.company.lmpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.companyMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.postMapper;
import cn.lr.po.dict;
import cn.lr.service.company.DictService;

@Service
@Transactional
public class DictServiceImpl implements DictService {

	@Autowired
	dictMapper dictMapper;
	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	companyMapper companyMapper;
	@Autowired
	postMapper postMapper;
	
	@Value("${employee.type}")
	private String EMPLOYEE_TYPE;
	@Value("${data.type}")
	private String DATA_TYPE;

	public HashMap<String, String> EToC = new HashMap<String, String>();

	@PostConstruct
	public void init() {
		EToC.put("task", "任务");
		EToC.put("check", "审核");
		EToC.put("apply", "申请类型");
		EToC.put("leave", "请假类型");
		EToC.put("attendance", "行程安排");
	}

	@Override
	public List<JSONObject> getDictType(JSONObject data) {
		String name = EToC.get(data.getString("type"));
		List<JSONObject> dataJson = new ArrayList<JSONObject>();
		List<dict> prevType = dictMapper.selectByName(name,data.getInteger("companyId"));
		int i = 0;
		for (dict pDict:prevType) {
			JSONObject task = new JSONObject();
			task.put("title", pDict.getStateName());
			task.put("index", i++);
			task.put("id", pDict.getStateCode());
			List<JSONObject> children = new ArrayList<JSONObject>();
			List<dict> type = dictMapper.selectByName(pDict.getStateName(),data.getInteger("companyId"));
			for (dict d:type) {
				JSONObject typeJson = new JSONObject();
				typeJson.put("type", d.getStateName());
				typeJson.put("id", d.getStateCode());
				children.add(typeJson);
			}
			task.put("children", children);
			dataJson.add(task);
		}
		String underList = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId")).getUnderIdList();
		if(data.getString("type").equals("task") &&(underList == null || underList.equals(""))) {
			dataJson.remove(prevType.size() - 1);
		}
		return dataJson;
	}

}
