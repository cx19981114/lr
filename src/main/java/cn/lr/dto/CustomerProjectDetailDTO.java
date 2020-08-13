package cn.lr.dto;

import java.sql.Timestamp;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class CustomerProjectDetailDTO {
	private Integer id;

	private String customerName;
	
	private String employeeName;
	
	private Timestamp dateTime;
	
	private String projectName;
	
	private Integer count;
	
	private Integer restCount;
	
	private String state;
	
	private List<JSONObject> used;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getRestCount() {
		return restCount;
	}

	public void setRestCount(Integer restCount) {
		this.restCount = restCount;
	}

	public List<JSONObject> getUsed() {
		return used;
	}

	public void setUsed(List<JSONObject> used) {
		this.used = used;
	}

	
	
}
