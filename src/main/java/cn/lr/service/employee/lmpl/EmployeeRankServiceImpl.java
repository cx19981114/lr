package cn.lr.service.employee.lmpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.dictMapper;
import cn.lr.dao.dynamicMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.employeeRankMapper;
import cn.lr.dto.EmployeeRankDTO;
import cn.lr.dto.Page;
import cn.lr.exception.BusiException;
import cn.lr.po.dynamic;
import cn.lr.po.employee;
import cn.lr.po.employeeRank;
import cn.lr.service.employee.EmployeeRankService;

@Service
@Transactional
public class EmployeeRankServiceImpl implements EmployeeRankService {

	@Autowired
	employeeRankMapper employeeRankMapper;
	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	dynamicMapper dynamicMapper;

	@Value("${pageSize}")
	private Integer PAGESIZE;
	@Value("${data.type}")
	private String DATA_TYPE;

	@Override
	public Page<EmployeeRankDTO> getEmployeeRankByCompany(JSONObject data) {
		Integer companyId = data.getInteger("companyId");
		Integer pageNum = data.getInteger("pageNum");
		String type = data.getString("type");
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		List<employee> employees = employeeMapper.selectByCompanyId(companyId,stateList, 0, Integer.MAX_VALUE);
		List<EmployeeRankDTO> employeeRankDTOs = new ArrayList<EmployeeRankDTO>();
		List<EmployeeRankDTO> employeeRankDTOSorts = new ArrayList<EmployeeRankDTO>();
		List<employeeRank> employeeRanks = new ArrayList<>();
		for(employee e:employees) {
			if(type.equals("day")) {
				employeeRanks = employeeRankMapper.selectByEmployeeIdDay(e.getId(),stateList);
			}else if(type.equals("week")) {
				employeeRanks = employeeRankMapper.selectByEmployeeIdWeek(e.getId(),stateList);
			}else if(type.equals("mon")) {
				employeeRanks = employeeRankMapper.selectByEmployeeIdMon(e.getId(),stateList);
			}else if(type.equals("qtr")) {
				employeeRanks = employeeRankMapper.selectByEmployeeIdQtr(e.getId(),stateList);
			}else if(type.equals("year")) {
				employeeRanks = employeeRankMapper.selectByEmployeeIdYear(e.getId(),stateList);
			}
			EmployeeRankDTO employeeRankDTO = this.sEmployeeRankDTO(employeeRanks, e.getId());
			employeeRankDTOs.add(employeeRankDTO);
		}
		for (int i = 0; i < employeeRankDTOs.size() - 1; i++) {
			for (int j = 1; j < employeeRankDTOs.size() - i; j++) {
				EmployeeRankDTO a = new EmployeeRankDTO();
				if (employeeRankDTOs.get(j - 1).getAllScore().compareTo(employeeRankDTOs.get(j).getAllScore()) < 0) { 
					a = employeeRankDTOs.get(j - 1);
					employeeRankDTOs.set((j - 1), employeeRankDTOs.get(j));
					employeeRankDTOs.set(j, a);
				}
			}
		}
		int total = employeeRankDTOs.size();
		if(pageNum*PAGESIZE < total ) {
			total = pageNum*PAGESIZE;
		}
		for (int i = (pageNum-1)*PAGESIZE; i < total; i++) {
			employeeRankDTOs.get(i).setRank(i+1);
			employeeRankDTOSorts.add(employeeRankDTOs.get(i));
			
		}
		Page<EmployeeRankDTO> page = new Page<EmployeeRankDTO>();
		page.setPageNum(pageNum);
		page.setPageSize(PAGESIZE);
		page.setTotal(employeeRankDTOSorts.size());
		page.setList(employeeRankDTOSorts);
		return page;
	}

	public EmployeeRankDTO sEmployeeRankDTO(List<employeeRank> employeeRanks,Integer employeeId) {
		Integer addScore = 0;
		Integer subScore = 0;
		for (employeeRank e : employeeRanks) {
			if (e.getIsAdd() == 1) {
				addScore += e.getRank();
			} else {
				subScore += e.getRank();
			}
		}
		EmployeeRankDTO employeeRankDTO = new EmployeeRankDTO();
		employeeRankDTO.setEmployeeId(employeeId);
		employeeRankDTO.setName(employeeMapper.selectByPrimaryKey(employeeId).getName());
		employeeRankDTO.setAddScore(addScore);
		employeeRankDTO.setSubScore(subScore);
		employeeRankDTO.setAllScore(addScore - subScore);
		return employeeRankDTO;
	}
	
	public EmployeeRankDTO getEmployeeRankByEmployee(JSONObject data) {
		Integer employeeId = data.getInteger("employeeId");
		Integer companyId = data.getInteger("companyId");
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		List<employee> employees = employeeMapper.selectByCompanyId(companyId,stateList, 0, Integer.MAX_VALUE);
		List<EmployeeRankDTO> employeeRankDTOs = new ArrayList<EmployeeRankDTO>();
		List<employeeRank> employeeRanks = new ArrayList<>();
		for(employee e:employees) {
			employeeRanks = employeeRankMapper.selectByEmployeeIdDay(e.getId(),stateList);
			EmployeeRankDTO employeeRankDTO = this.sEmployeeRankDTO(employeeRanks, e.getId());
			employeeRankDTOs.add(employeeRankDTO);
		}
		for (int i = 0; i < employeeRankDTOs.size() - 1; i++) {
			for (int j = 1; j < employeeRankDTOs.size() - i; j++) {
				EmployeeRankDTO a = new EmployeeRankDTO();
				if (employeeRankDTOs.get(j - 1).getAllScore().compareTo(employeeRankDTOs.get(j).getAllScore()) < 0) { 
					a = employeeRankDTOs.get(j - 1);
					employeeRankDTOs.set((j - 1), employeeRankDTOs.get(j));
					employeeRankDTOs.set(j, a);
				}
			}
		}
		for(int i = 0; i < employeeRankDTOs.size(); i++) {
			if(employeeRankDTOs.get(i).getEmployeeId() == employeeId) {
				employeeRankDTOs.get(i).setRank(i+1);
				return employeeRankDTOs.get(i);
			}
		}
		return null;
	}
	public String getEmployeeRankFrist(JSONObject data) {
		JSONObject dataJson = new JSONObject();
		dataJson.put("companyId", data.getInteger("companyId"));
		dataJson.put("pageNum", 1);
		dataJson.put("type", "day");
		Page<EmployeeRankDTO> employeeRankDTOs = this.getEmployeeRankByCompany(dataJson);
		List<EmployeeRankDTO> employeeRankDTOs2= employeeRankDTOs.getList();
		if(employeeRankDTOs2.size() != 0) {
			return employeeRankDTOs2.get(0).getName();
		}
		return null;
	}
	public Integer addEmployeeRank(JSONObject data) {
		employeeRank employeeRank = new employeeRank();
		employeeRank.setDateTime(data.getString("time"));
		System.out.println(data.getInteger("dynamicId"));
		dynamic dynamic = dynamicMapper.selectByPrimaryKey(data.getInteger("dynamicId"));
		if (dynamic == null || dynamic.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE,
				"已失效", data.getInteger("companyId"))) {
			throw new BusiException("该动态不存在");
		}
		employeeRank.setDynamicId(dynamic.getId());
		employeeRank.setRank(dynamic.getRank());
		employeeRank.setEmployeeId(dynamic.getEmployeeId());
		employeeRank.setCompanyId(dynamic.getCompanyId());
		employeeRank.setIsAdd(1);
		employeeRank.setState(dictMapper.selectByCodeAndStateName(DATA_TYPE,"未失效", data.getInteger("companyId")));
		int count = employeeRankMapper.insertSelective(employeeRank);
		if (count == 0) {
			throw new BusiException("更新employeeRank表失败");
		}
		return employeeRank.getId();
	}
	public Integer deleteEmployeeRank(JSONObject data) {
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		employeeRank employeeRank = employeeRankMapper.selectByDynamic(data.getInteger("dynamicId"),stateList);
		employeeRank.setState(stateYSX);
		int count = employeeRankMapper.updateByPrimaryKey(employeeRank);
		if (count == 0) {
			throw new BusiException("更新employeeRank表失败");
		}
		return employeeRank.getId();
	}
}
