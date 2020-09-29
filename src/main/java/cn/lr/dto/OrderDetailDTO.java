package cn.lr.dto;

import java.sql.Timestamp;

import com.alibaba.fastjson.JSONObject;

public class OrderDetailDTO {
	private Integer id;

    private String customerName;
    
    private String employeeName;

    private String projectName;
    
    private Integer rank;

    private String note;
    
    private String applyState;

    private String orderState;
    
    private String applyOrderState;

    private String times;
    
    private String startTime;
    
    private Timestamp actStartTime;
    
    private Timestamp actEndTime;
    
    private JSONObject pics;
    
    private String evaluate;
    
	public String getApplyState() {
		return applyState;
	}

	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Timestamp getActStartTime() {
		return actStartTime;
	}

	public void setActStartTime(Timestamp actStartTime) {
		this.actStartTime = actStartTime;
	}

	public Timestamp getActEndTime() {
		return actEndTime;
	}

	public void setActEndTime(Timestamp actEndTime) {
		this.actEndTime = actEndTime;
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getApplyOrderState() {
		return applyOrderState;
	}

	public void setApplyOrderState(String applyOrderState) {
		this.applyOrderState = applyOrderState;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}
	
	public JSONObject getPics() {
		return pics;
	}

	public void setPics(JSONObject pics) {
		this.pics = pics;
	}

	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
    
}
