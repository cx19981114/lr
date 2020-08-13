package cn.lr.po;

public class employeeTask {
    private Integer id;

    private Integer employeeId;

    private String taskIdListFN;

    private String taskIdListRWDay;

    private String taskIdListRWWeek;

    private String taskIdListRWMon;

    private String taskIdListFNState;

    private String taskIdListRWDayState;

    private String taskIdListRWWeekState;

    private String taskIdListRWMonState;

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

    public String getTaskIdListFN() {
        return taskIdListFN;
    }

    public void setTaskIdListFN(String taskIdListFN) {
        this.taskIdListFN = taskIdListFN == null ? null : taskIdListFN.trim();
    }

    public String getTaskIdListRWDay() {
        return taskIdListRWDay;
    }

    public void setTaskIdListRWDay(String taskIdListRWDay) {
        this.taskIdListRWDay = taskIdListRWDay == null ? null : taskIdListRWDay.trim();
    }

    public String getTaskIdListRWWeek() {
        return taskIdListRWWeek;
    }

    public void setTaskIdListRWWeek(String taskIdListRWWeek) {
        this.taskIdListRWWeek = taskIdListRWWeek == null ? null : taskIdListRWWeek.trim();
    }

    public String getTaskIdListRWMon() {
        return taskIdListRWMon;
    }

    public void setTaskIdListRWMon(String taskIdListRWMon) {
        this.taskIdListRWMon = taskIdListRWMon == null ? null : taskIdListRWMon.trim();
    }

    public String getTaskIdListFNState() {
        return taskIdListFNState;
    }

    public void setTaskIdListFNState(String taskIdListFNState) {
        this.taskIdListFNState = taskIdListFNState == null ? null : taskIdListFNState.trim();
    }

    public String getTaskIdListRWDayState() {
        return taskIdListRWDayState;
    }

    public void setTaskIdListRWDayState(String taskIdListRWDayState) {
        this.taskIdListRWDayState = taskIdListRWDayState == null ? null : taskIdListRWDayState.trim();
    }

    public String getTaskIdListRWWeekState() {
        return taskIdListRWWeekState;
    }

    public void setTaskIdListRWWeekState(String taskIdListRWWeekState) {
        this.taskIdListRWWeekState = taskIdListRWWeekState == null ? null : taskIdListRWWeekState.trim();
    }

    public String getTaskIdListRWMonState() {
        return taskIdListRWMonState;
    }

    public void setTaskIdListRWMonState(String taskIdListRWMonState) {
        this.taskIdListRWMonState = taskIdListRWMonState == null ? null : taskIdListRWMonState.trim();
    }
}