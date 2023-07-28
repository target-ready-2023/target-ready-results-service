package com.target.targetreadyresultsservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document (collection = "results")
public class Results {
    @Id
    private String resultsCode;
    private String studentId;
    private String scheduleCode;
    private List<Marks> marksList;


    public Results(String studentId, String scheduleCode, List<Marks> marksList) {
        this.studentId = studentId;
        this.scheduleCode = scheduleCode;
        this.marksList = marksList;
    }


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

    @Override
    public String toString() {
        return "Results{" +
                "resultsCode='" + resultsCode + '\'' +
                ", studentId='" + studentId + '\'' +
                ", scheduleCode='" + scheduleCode + '\'' +
                ", marksList=" + marksList +
                '}';
    }
}
