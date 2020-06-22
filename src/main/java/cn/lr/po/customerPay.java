package cn.lr.po;

import java.util.Date;

public class customerPay {
    private Integer id;

    private Integer customerProjectId;

    private Integer employeeId;

    private Date dateTime;

    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerProjectId() {
        return customerProjectId;
    }

    public void setCustomerProjectId(Integer customerProjectId) {
        this.customerProjectId = customerProjectId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}