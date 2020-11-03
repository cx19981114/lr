package cn.lr.po;

public class employeeRank {
    private Integer id;

    private Integer employeeId;

    private String dateTime;

    private Integer rank;

    private Integer dynamicId;
    
    private Integer companyId;

    private Integer isAdd;
    
    private Integer state;
    
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Integer isAdd) {
		this.isAdd = isAdd;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime == null ? null : dateTime.trim();
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
    }
}