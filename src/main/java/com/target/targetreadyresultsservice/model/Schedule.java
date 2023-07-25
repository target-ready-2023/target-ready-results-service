package com.target.targetreadyresultsservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "schedule")
public class Schedule {

    @Id
    private String scheduleCode;
    private String classCode;
    private List<SubjectSchedule> subjectSchedule;
    private String scheduleType;
    private String scheduleName;
    private boolean scheduleStatus; // True(active) and  False(inactive)

    public Schedule(String classCode, List<SubjectSchedule> subjectSchedule,
                    String scheduleType, String scheduleName, boolean scheduleStatus) {
        this.classCode = classCode;
        this.subjectSchedule = subjectSchedule;
        this.scheduleType = scheduleType;
        this.scheduleName = scheduleName;
        this.scheduleStatus = scheduleStatus;
    }

    public Schedule(String scheduleCode, String classCode, List<SubjectSchedule> subjectSchedule, String scheduleType, String scheduleName, boolean scheduleStatus) {
        this.scheduleCode = scheduleCode;
        this.classCode = classCode;
        this.subjectSchedule = subjectSchedule;
        this.scheduleType = scheduleType;
        this.scheduleName = scheduleName;
        this.scheduleStatus = scheduleStatus;
    }

    public Schedule() {
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public List<SubjectSchedule> getSubjectSchedule() {
        return subjectSchedule;
    }

    public void setSubjectSchedule(List<SubjectSchedule> subjectSchedule) {
        this.subjectSchedule = subjectSchedule;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public boolean getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(boolean scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleCode='" + scheduleCode + '\'' +
                ", classCode='" + classCode + '\'' +
                ", subjectSchedule=" + subjectSchedule +
                ", scheduleType='" + scheduleType + '\'' +
                ", scheduleName='" + scheduleName + '\'' +
                ", scheduleStatus=" + scheduleStatus +
                '}';
    }
}
