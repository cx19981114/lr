package cn.lr.dto;

import java.sql.Timestamp;

public class EmployeeLogDayDTO {
	private Integer id;

	private Integer employeeId;

	private Timestamp dateTime;

	private String goalAchievement;

	private String taskAchievement;

	private String feel;

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

	public String getGoalAchievement() {
		return goalAchievement;
	}

	public void setGoalAchievement(String goalAchievement) {
		this.goalAchievement = goalAchievement;
	}

	public String getTaskAchievement() {
		return taskAchievement;
	}

	public void setTaskAchievement(String taskAchievement) {
		this.taskAchievement = taskAchievement;
	}

	public String getFeel() {
		return feel;
	}

	public void setFeel(String feel) {
		this.feel = feel;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
