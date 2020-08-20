package cn.lr.dto;

public class employeeDTO {
	private Integer employeeId;
	
	private Integer companyId;
	
	private Integer postId;
	
	private String leaderName;
	
	private Integer leaderId;

    private String phone;

    private String name;

    private String sex;

    private String company;

    private String post;

    private String pic;

    private String state;
    
    private Integer rank;
    
    private Integer score;
    
    private String permissionList;
    
    
	public String getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(String permissionList) {
		this.permissionList = permissionList;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public employeeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public employeeDTO(Integer employeeId, Integer companyId, Integer postId, String phone, String name, String sex,
			String company, String post, String pic, String state) {
		super();
		this.employeeId = employeeId;
		this.companyId = companyId;
		this.postId = postId;
		this.phone = phone;
		this.name = name;
		this.sex = sex;
		this.company = company;
		this.post = post;
		this.pic = pic;
		this.state = state;
	}

	@Override
	public String toString() {
		return "employeeDTO [employeeId=" + employeeId + ", companyId=" + companyId + ", postId=" + postId + ", phone="
				+ phone + ", name=" + name + ", sex=" + sex + ", company=" + company + ", post=" + post + ", pic=" + pic
				+ ", state=" + state + "]";
	}

	

}
