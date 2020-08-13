package cn.lr.po;

public class applyRank {
    private Integer id;

    private String startTime;

    private String checkIdList;

    private String checkTimeList;

    private String checkList;

    private String noteList;

    private String endTime;

    private Integer checkNumber;

    private Integer dynamicId;

    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getCheckIdList() {
        return checkIdList;
    }

    public void setCheckIdList(String checkIdList) {
        this.checkIdList = checkIdList == null ? null : checkIdList.trim();
    }

    public String getCheckTimeList() {
        return checkTimeList;
    }

    public void setCheckTimeList(String checkTimeList) {
        this.checkTimeList = checkTimeList == null ? null : checkTimeList.trim();
    }

    public String getCheckList() {
        return checkList;
    }

    public void setCheckList(String checkList) {
        this.checkList = checkList == null ? null : checkList.trim();
    }

    public String getNoteList() {
        return noteList;
    }

    public void setNoteList(String noteList) {
        this.noteList = noteList == null ? null : noteList.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public Integer getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(Integer checkNumber) {
        this.checkNumber = checkNumber;
    }

    public Integer getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}