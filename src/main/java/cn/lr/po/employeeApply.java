package cn.lr.po;

import java.util.Date;

public class employeeApply {
    private Integer id;

    private Integer employeeId;

    private Integer type;

    private String name;

    private Date startDate;

    private String startPhase;

    private Date endDate;

    private String endPhase;

    private String note;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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
        this.startPhase = startPhase == null ? null : startPhase.trim();
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
        this.endPhase = endPhase == null ? null : endPhase.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}