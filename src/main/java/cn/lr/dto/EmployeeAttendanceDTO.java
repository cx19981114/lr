package cn.lr.dto;

import java.sql.Timestamp;

import com.alibaba.fastjson.JSONObject;

public class EmployeeAttendanceDTO {
	private Integer id;

    private Integer employeeId;

    private Timestamp dateTime;

    private JSONObject pics;

    private String address;

    private String note;

    private String state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public JSONObject getPics() {
		return pics;
	}

	public void setPics(JSONObject pics) {
		this.pics = pics;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
    
}
