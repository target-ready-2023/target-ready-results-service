package com.target.targetreadyresultsservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collation = "Schedule")
public class Schedule {

    @Id
    private String scheduleCode;
    private String classCode;
    private List<SubjectSchedule> subjectSchedule;
    private String scheduleType;
    private boolean scheduleStatus; //1 - True(active) and 0 - False(inactive)

    public Schedule(String classCode, List<SubjectSchedule> subjectSchedule,
                    String scheduleType, boolean scheduleStatus) {
        this.classCode = classCode;
        this.subjectSchedule = subjectSchedule;
        this.scheduleType = scheduleType;
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

    public boolean isScheduleStatus() {
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
                ", scheduleStatus=" + scheduleStatus +
                '}';
    }
}
