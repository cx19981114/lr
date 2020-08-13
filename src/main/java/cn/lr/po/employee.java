package cn.lr.po;

public class employee {
    private Integer id;

    private String phone;

    private String name;

    private String password;

    private String sex;

    private Integer companyId;

    private Integer postId;

    private String pic;

    private String leaderIdList;

    private Integer state;

    private String verficationCode;

    private String validTime;
    
    private String underIdList;

	public String getUnderIdList() {
		return underIdList;
	}

	public void setUnderIdList(String underIdList) {
		this.underIdList = underIdList;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    public String getLeaderIdList() {
        return leaderIdList;
    }

    public void setLeaderIdList(String leaderIdList) {
        this.leaderIdList = leaderIdList == null ? null : leaderIdList.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getVerficationCode() {
        return verficationCode;
    }

    public void setVerficationCode(String verficationCode) {
        this.verficationCode = verficationCode == null ? null : verficationCode.trim();
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime == null ? null : validTime.trim();
    }
}