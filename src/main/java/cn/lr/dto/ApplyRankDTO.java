package cn.lr.dto;

import java.sql.Timestamp;

public class ApplyRankDTO {
	
	private employeeDTO employee;
	
	private Timestamp time;
	
	private String state;
	
	private String note;

	public employeeDTO getEmployee() {
		return employee;
	}

	public void setEmployee(employeeDTO employee) {
		this.employee = employee;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ApplyRankDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApplyRankDTO(employeeDTO employee, Timestamp time, String state, String note) {
		super();
		this.employee = employee;
		this.time = time;
		this.state = state;
		this.note = note;
	}

	@Override
	public String toString() {
		return "ApplyRankDTO [employee=" + employee + ", time=" + time + ", state=" + state + ", note=" + note + "]";
	}

}
