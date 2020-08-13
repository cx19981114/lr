package cn.lr.dto;

import java.sql.Timestamp;
import java.util.Date;

public class EmployeeApplyDTO {
	private Integer id;

	private Integer employeeId;

	private String type;

	private String name;

	private Date startDate;

	private String startPhase;

	private Date endDate;

	private String endPhase;

	private String note;

	private Timestamp dateTime;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStartPhase() {
		return startPhase;
	}

	public void setStartPhase(String startPhase) {
		this.startPhase = startPhase;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEndPhase() {
		return endPhase;
	}

	public void setEndPhase(String endPhase) {
		this.endPhase = endPhase;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
