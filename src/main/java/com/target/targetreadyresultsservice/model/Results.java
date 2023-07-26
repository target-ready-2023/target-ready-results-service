package com.target.targetreadyresultsservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document (collection = "results")
public class Results {
    @Id
    public String resultsCode;

    public String studentId;
    public String scheduleCode;
    public List<Marks> marksList;

    public String getResultsCode() {
        return resultsCode;
    }

    public void setResultsCode(String resultsCode) {
        this.resultsCode = resultsCode;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public List<Marks> getMarksList() {
        return marksList;
    }

    public void setMarksList(List<Marks> marksList) {
        this.marksList = marksList;
    }

    public Results(String resultsCode, String studentId, String scheduleCode, List<Marks> marksList) {
        this.resultsCode = resultsCode;
        this.studentId = studentId;
        this.scheduleCode = scheduleCode;
        this.marksList = marksList;
    }
}
