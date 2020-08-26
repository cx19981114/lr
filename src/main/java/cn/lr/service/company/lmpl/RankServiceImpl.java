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

import cn.lr.dao.dictMapper;
import cn.lr.dao.rankMapper;
import cn.lr.exception.BusiException;
import cn.lr.service.company.RankService;
@Service
@Transactional
public class RankServiceImpl implements RankService {

	@Autowired
	rankMapper rankMapper;
	@Autowired
	dictMapper dictMapper;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	public HashMap<String, String> EToC = new HashMap<String, String>();

	@PostConstruct
	public void init() {
		EToC.put("leave", "请假");
		EToC.put("supply", "物料");
		EToC.put("train", "培训");
		EToC.put("log", "每日打卡");
		EToC.put("rest", "每日行程");
		EToC.put("attendance", "每日一报");
		EToC.put("addCustomer", "新增顾客");
		EToC.put("reNewCustomer", "顾客续费");
		EToC.put("buyCustomer", "顾客购买项目");
		EToC.put("order", "预约项目");
		EToC.put("orderFinish", "预约完成");
	}
	
	@Override
	public Integer getRankByType(JSONObject data) {
		String type = EToC.get(data.get("type"));
		Integer stateWSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateWSX);
		if(type == null) {
			throw new BusiException("该类型不存在");
		}
		Integer score = rankMapper.selectByName(type,data.getInteger("companyId"),stateList);
		return score;
	}
}
