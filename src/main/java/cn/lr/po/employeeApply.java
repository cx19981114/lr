package cn.lr.po;

public class employeeApply {
    private Integer id;

    private Integer employeeId;

    private Integer type;

    private String name;

    private String startDate;

    private String startPhase;

    private String endDate;

    private String endPhase;

    private String note;

    private String dateTime;

    private Integer state;

    public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate == null ? null : startDate.trim();
    }

    public String getStartPhase() {
        return startPhase;
    }

    public void setStartPhase(String startPhase) {
        this.startPhase = startPhase == null ? null : startPhase.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
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