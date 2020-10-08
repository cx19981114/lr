package cn.lr.service.employee.lmpl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.lr.dao.companyMapper;
import cn.lr.dao.dictMapper;
import cn.lr.dao.employeeMapper;
import cn.lr.dao.postMapper;
import cn.lr.dto.employeeDTO;
import cn.lr.exception.BusiException;
import cn.lr.exception.EmployeeUnactiveException;
import cn.lr.po.company;
import cn.lr.po.employee;
import cn.lr.service.employee.EmployeeRankService;
import cn.lr.service.employee.EmployeeService;
import cn.lr.service.employee.LoginService;
import cn.lr.util.DataVaildCheckUtil;
import cn.lr.util.EncryptionUtil;
import cn.lr.util.LoggerUtil;
import cn.lr.util.SendMessageUtil;
import cn.lr.util.TimeFormatUtil;
@Service
@Transactional
public class LoginServiceImpl implements LoginService {
	@Autowired
	employeeMapper employeeMapper;
	@Autowired
	dictMapper dictMapper;
	@Autowired
	companyMapper companyMapper;
	@Autowired
	postMapper postMapper;
	
	@Autowired
	EmployeeRankService EmployeeRankService;
	@Autowired
	EmployeeService EmployeeService;
	@Value("${employee.type}")
	private String EMPLOYEE_TYPE;
	@Value("${data.type}")
	private String DATA_TYPE;
	@Override
	public employeeDTO checkLogin(JSONObject data) {
		String phone = data.getString("phone");
		Integer postId = data.getInteger("postId");
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		employee employee = employeeMapper.selectByPhone(phone,stateList);
		if(employee == null) {
			throw new BusiException("该手机号不存在");
		}
		if(postId != null && postId == 0) {
			if(employee.getPassword().endsWith(EncryptionUtil.encryping(data.getString("password")))) {
				employeeDTO employeeDTO = new employeeDTO();
				employeeDTO.setName(employee.getName());
				employeeDTO.setPhone(employee.getPhone());
				return employeeDTO;
				
			}
		}
		company company = companyMapper.selectByPrimaryKey(employee.getCompanyId());
		if(employee.getState() == dictMapper.selectByCodeAndStateName(EMPLOYEE_TYPE, "未激活",employee.getCompanyId())) {
			throw new EmployeeUnactiveException("用户未激活");
		}else if(!employee.getPassword().endsWith(EncryptionUtil.encryping(data.getString("password")))){
			throw new BusiException("密码错误");
		}else if(company.getState() == dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", company.getId())) {
			throw new BusiException("该员工不存在");
		}
		return EmployeeService.sEmployeeDTO(employee);
	}
	@Override
	public Integer sendMsg(JSONObject data) throws Exception {
		String phone = data.getString("phone");
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		employee employee = employeeMapper.selectByPhone(phone,stateList);
		if(employee == null) {
			throw new BusiException("该手机号不存在");
		}
		String [] params = SendMessageUtil.sendMsgByTxPlatform(phone);
		LoggerUtil.LOGGER.info("发送验证码成功");
		employee.setVerficationCode(params[0]);
		employee.setValidTime(TimeFormatUtil.timeStampToString(new Date().getTime()+ 60000));
		int count = employeeMapper.updateByPrimaryKeySelective(employee);
		LoggerUtil.LOGGER.info("更新表成功");
		if(count == 0) {
			throw new BusiException("更新employee表失败");
		}
		return employee.getId();
	}
	@Override
	public Integer checkCode(JSONObject data) throws ParseException {
		String phone = data.getString("phone");
		Integer stateYSX = dictMapper.selectByCodeAndStateName(DATA_TYPE, "已失效", data.getInteger("companyId"));
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(stateYSX);
		employee employee = employeeMapper.selectByPhone(phone,stateList);
		if(employee == null) {
			throw new BusiException("该手机号不存在");
		}
		String nowTime = TimeFormatUtil.timeStampToString(new Date().getTime());
		if(TimeFormatUtil.stringToTimeStamp(employee.getValidTime()).before(TimeFormatUtil.stringToTimeStamp(nowTime))) {
			throw new BusiException("已超时");
		}
		if(!employee.getVerficationCode().equals(data.getString("code"))) {
			throw new BusiException("验证码错误");
		}
		return employee.getId();
	}
	
	@Override
	public Integer modifyPasswordFrist(JSONObject data) {
		if(!DataVaildCheckUtil.isValidPassword(data.getString("password"))) {
			throw new BusiException("密码格式不符合规范");
		}
		employee record = employeeMapper.selectByPrimaryKey(data.getInteger("employeeId"));
		record.setPassword(EncryptionUtil.encryping(data.getString("password")));
		record.setState(dictMapper.selectByCodeAndStateName(EMPLOYEE_TYPE, "已激活",data.getInteger("companyId")));
		int count = employeeMapper.updateByPrimaryKeySelective(record);
		if(count == 0) {
			throw new BusiException("更新enployee表失败");
		}
		return record.getId();
	}
}
