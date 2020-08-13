package cn.lr.po;

public class employeeLogDay {
    private Integer id;

    private Integer employeeId;

    private String dateTime;

    private String goalAchievement;

    private String taskAchievement;

    private String feel;

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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime == null ? null : dateTime.trim();
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

    public String getFeel() {
        return feel;
    }

    public void setFeel(String feel) {
        this.feel = feel == null ? null : feel.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}