package cn.lr.po;

public class postTask {
    private Integer id;

    private Integer postId;

    private String taskIdListFN;

    private String taskIdListRWDay;

    private String taskIdListRWWeek;

    private String taskIdListRWMon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
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
}