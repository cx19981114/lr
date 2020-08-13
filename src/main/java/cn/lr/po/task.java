package cn.lr.po;

public class task {
    private Integer id;

    private Integer companyId;

    private String name;

    private String content;

    private Integer rank;

    private Integer prevType;

    private Integer type;

    private Integer step;

    private String postIdList;

    private String employeeIdList;

    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getPrevType() {
        return prevType;
    }

    public void setPrevType(Integer prevType) {
        this.prevType = prevType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getPostIdList() {
        return postIdList;
    }

    public void setPostIdList(String postIdList) {
        this.postIdList = postIdList == null ? null : postIdList.trim();
    }

    public String getEmployeeIdList() {
        return employeeIdList;
    }

    public void setEmployeeIdList(String employeeIdList) {
        this.employeeIdList = employeeIdList == null ? null : employeeIdList.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}