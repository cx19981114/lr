package cn.lr.po;

public class customer {
    private Integer id;

    private String name;

    private String sex;

    private String birth;

    private String phone;

    private String employeeIdList;
    
    private Integer operatorId;

    private String habit;

    private String plan;

    private String note;

    private Integer money;

    private String dateTime;

    private Integer state;

    private String pic;
    
    private String activeConsumeTime;
    
    private String activeServiceTime;

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public String getActiveConsumeTime() {
		return activeConsumeTime;
	}

	public void setActiveConsumeTime(String activeConsumeTime) {
		this.activeConsumeTime = activeConsumeTime;
	}

	public String getActiveServiceTime() {
		return activeServiceTime;
	}

	public void setActiveServiceTime(String activeServiceTime) {
		this.activeServiceTime = activeServiceTime;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
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
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmployeeIdList() {
		return employeeIdList;
	}

	public void setEmployeeIdList(String employeeIdList) {
		this.employeeIdList = employeeIdList;
	}

	public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit == null ? null : habit.trim();
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan == null ? null : plan.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime == null ? null : dateTime.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}