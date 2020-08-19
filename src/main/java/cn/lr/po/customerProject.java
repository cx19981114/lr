package cn.lr.po;

public class customerProject {
    private Integer id;

    private Integer customerId;
    
    private Integer employeeId;

    private Integer projectId;

    private Integer count;

    private String dateTime;

    private Integer restCount;
    
    private Integer ingCount;

    private Integer state;

    public Integer getIngCount() {
		return ingCount;
	}

	public void setIngCount(Integer ingCount) {
		this.ingCount = ingCount;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime == null ? null : dateTime.trim();
    }

    public Integer getRestCount() {
        return restCount;
    }

    public void setRestCount(Integer restCount) {
        this.restCount = restCount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}