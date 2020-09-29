package cn.lr.dto;

import java.sql.Timestamp;
import java.util.List;

public class CustomerPerformanceDTO {

	private Integer id;

    private String customerName;
    
    private String operatorName;
    
    private List<String> employeeNameList;

    private String type;
    
    private Integer rank;

    private Integer money;

    private Timestamp dateTime;

    private String note;
    
    private String state;

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

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

	public List<String> getEmployeeNameList() {
		return employeeNameList;
	}

	public void setEmployeeNameList(List<String> employeeNameList) {
		this.employeeNameList = employeeNameList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
