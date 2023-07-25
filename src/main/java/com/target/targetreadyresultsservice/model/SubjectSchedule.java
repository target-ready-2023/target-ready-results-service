package com.target.targetreadyresultsservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

public class SubjectSchedule {
    private String subjectCode;
    private LocalDate date;
    private LocalTime time;
    private boolean status; //True(active) and False(inactive)

    public SubjectSchedule(String subjectCode, LocalDate date, LocalTime time, boolean status) {
        this.subjectCode = subjectCode;
        this.date = date;
        this.time = time;
        this.status = status;
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
