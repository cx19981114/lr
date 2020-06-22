package cn.lr.po;

import java.util.Date;

public class employeeLog {
    private Integer id;

    private Integer employeeId;

    private Date dateTime;

    private String goalAchievement;

    private String taskAchievement;

    private String reason;

    private String feel;

    private String money;

    private String customer;

    private String run;

    private String person;

    private Integer state;

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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getGoalAchievement() {
        return goalAchievement;
    }

    public void setGoalAchievement(String goalAchievement) {
        this.goalAchievement = goalAchievement == null ? null : goalAchievement.trim();
    }

    public String getTaskAchievement() {
        return taskAchievement;
    }

    public void setTaskAchievement(String taskAchievement) {
        this.taskAchievement = taskAchievement == null ? null : taskAchievement.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public String getFeel() {
        return feel;
    }

    public void setFeel(String feel) {
        this.feel = feel == null ? null : feel.trim();
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money == null ? null : money.trim();
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer == null ? null : customer.trim();
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run == null ? null : run.trim();
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person == null ? null : person.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}