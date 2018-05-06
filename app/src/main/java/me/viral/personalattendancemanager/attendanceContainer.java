package me.viral.personalattendancemanager;

/**
 * Created by Riv_al on 4/29/2018.
 */

public class attendanceContainer {

    private int subjectId;
    private String subjectName;
    private String semester;
    private int minRequired;
    private String dateTime;
    private String predictLecture;
    private int present;
    private int absent;
    private String percentage;

//    Getters Setters
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getMinRequired() {
        return minRequired;
    }

    public void setMinRequired(int minRequired) {
        this.minRequired = minRequired;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPredictLecture() {
        return predictLecture;
    }

    public void setPredictLecture(String predictLecture) {
        this.predictLecture = predictLecture;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
