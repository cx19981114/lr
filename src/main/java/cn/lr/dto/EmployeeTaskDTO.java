package cn.lr.dto;

import java.sql.Timestamp;

import com.alibaba.fastjson.JSONObject;

public class EmployeeTaskDTO {
	private Integer id;
	
	private Integer taskId;

	private String name;

	private String content;

	private Integer rank;

	private String prevType;

	private String type;

	private Integer step;

	private Timestamp dateTime;

	private String state;
	
	private String note;
	
	private JSONObject pics;

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public JSONObject getPics() {
		return pics;
	}

	public void setPics(JSONObject pics) {
		this.pics = pics;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getPrevType() {
		return prevType;
	}

	public void setPrevType(String prevType) {
		this.prevType = prevType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
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
