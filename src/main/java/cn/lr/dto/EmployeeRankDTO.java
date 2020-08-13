package cn.lr.dto;

public class EmployeeRankDTO {
	private Integer employeeId;

	private String name;
	
	private Integer addScore;
	
	private Integer subScore;
	
	private Integer allScore;
	
	private Integer rank;
	
	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAddScore() {
		return addScore;
	}

	public void setAddScore(Integer addScore) {
		this.addScore = addScore;
	}

	public Integer getSubScore() {
		return subScore;
	}

	public void setSubScore(Integer subScore) {
		this.subScore = subScore;
	}

	public Integer getAllScore() {
		return allScore;
	}

	public void setAllScore(Integer allScore) {
		this.allScore = allScore;
	}

	@Override
	public String toString() {
		return "EmployeeRankDTO [name=" + name + ", addScore=" + addScore + ", subScore=" + subScore + ", allScore="
				+ allScore + ", rank=" + rank + "]";
	}
	
	
}
