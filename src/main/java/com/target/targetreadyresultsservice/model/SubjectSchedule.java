package com.target.targetreadyresultsservice.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class SubjectSchedule {
    private String subjectCode;
    private LocalDate date;
    private LocalTime time;
    private int maxMarks;
    private boolean status; //True(active) and False(inactive)

    public SubjectSchedule(String subjectCode, LocalDate date, LocalTime time, int maxMarks, boolean status) {
        this.subjectCode = subjectCode;
        this.date = date;
        this.time = time;
        this.maxMarks = maxMarks;
        this.status = status;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(int maxMarks) {
        this.maxMarks = maxMarks;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "    \nsubjectSchedule{" +"\n" +
                "   subjectCode='" + subjectCode + '\'' +",\n" +
                "   date=" + date +",\n" +
                "   time=" + time +",\n"+
                "   status=" + status +"\n"+
                '}';
    }
}
