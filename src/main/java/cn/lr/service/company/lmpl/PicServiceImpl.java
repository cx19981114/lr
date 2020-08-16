package cn.lr.service.company.lmpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.companyMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.picMapper;
import cn.lr.exception.BusiException;
import cn.lr.po.pic;
import cn.lr.service.company.PicService;
@Service
@Transactional
public class PicServiceImpl implements PicService {

	@Autowired
	picMapper picMapper;
	@Autowired
	companyMapper companyMapper;
	@Autowired
	dictMapper dictMapper;
	
	@Value("${data.type}")
	private String DATA_TYPE;
	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${picPath}")
	private String PATH;
	@Value("${picActPath}")
	private String ACTPATH;
	@Override
	public JSONObject getPicByCompany(JSONObject data){
		Integer companyId = data.getInteger("companyId");
		Integer state = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<pic> pics = picMapper.selectByCompanyId(companyId,state);
		JSONObject dataJson = new JSONObject();
		List<JSONObject> fileListJSON = new ArrayList<JSONObject>();
		List<String> urlList = new ArrayList<String>();
		for(pic p:pics) {
			File picture = new File(ACTPATH+p.getPic());
			JSONObject files = new JSONObject();
			JSONObject file = new JSONObject();
			file.put("path", PATH+p.getPic());
			file.put("size", picture.length());
			files.put("file", file);
			files.put("url", PATH+p.getPic());
			urlList.add(p.getPic());
			fileListJSON.add(files);
		}
		dataJson.put("files", fileListJSON);
		dataJson.put("uploadImg", urlList);
		System.out.println(dataJson);
		return dataJson;
	}
	@Override
	public Integer addPic(JSONObject data) {
		pic pic = new pic();
		pic.setCompanyId(data.getInteger("companyId"));
		pic.setPic(data.getString("pic"));
		pic.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "未失效",data.getInteger("companyId")));
		int count = picMapper.insertSelective(pic);
		if(count == 0) {
			throw new BusiException("添加pic表失败");
		}
		return pic.getId();
	}
	@Override
	public Integer modifyPic(JSONObject data) {
		pic pic = picMapper.selectByPrimaryKey(data.getInteger("picId"));
		pic.setCompanyId(data.getInteger("companyId"));
		pic.setPic(data.getString("pic"));
		int count = picMapper.updateByPrimaryKeySelective(pic);
		if(count == 0) {
			throw new BusiException("更新pic表失败");
		}
		return pic.getId();
	}
	@Override
	public Integer deletePic(JSONObject data) {
		pic pic = picMapper.selectByPic(data.getString("pic"));
		if(pic == null || pic.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该图片不存在");
		}
		pic.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId")));
		int count = picMapper.updateByPic(pic);
		if(count == 0) {
			throw new BusiException("更新pic表失败");
		}
		return pic.getId();
	}
	@Override
	public pic getPic(JSONObject data) {
		pic pic = picMapper.selectByPrimaryKey(data.getInteger("picId"));
		if(pic == null || pic.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效",data.getInteger("companyId"))) {
			throw new BusiException("该轮播图不存在");
		}
		return pic;
	}

}
