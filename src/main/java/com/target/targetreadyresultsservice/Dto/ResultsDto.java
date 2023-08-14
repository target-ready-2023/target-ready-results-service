package com.target.targetreadyresultsservice.Dto;

import com.target.targetreadyresultsservice.model.Marks;

import java.util.List;
//for leaderboard and Toppers List
public class ResultsDto {
    public String resultsCode;

    public String studentId;

    public String scheduleCode;

    public List<Marks> marksList;

    public Double totalMarks;

    public Double totalPercentage;

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

    public Double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Double getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(Double totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    public ResultsDto(String resultsCode, String studentId, String scheduleCode, List<Marks> marksList, Double totalMarks, Double totalPercentage) {
        this.resultsCode = resultsCode;
        this.studentId = studentId;
        this.scheduleCode = scheduleCode;
        this.marksList = marksList;
        this.totalMarks = totalMarks;
        this.totalPercentage = totalPercentage;
    }

    @Override
    public String toString() {
        return "ResultsDto{" +
                "resultsCode='" + resultsCode + '\'' +
                ", studentId='" + studentId + '\'' +
                ", scheduleCode='" + scheduleCode + '\'' +
                ", marksList=" + marksList +
                ", totalMarks=" + totalMarks +
                ", totalPercentage=" + totalPercentage +
                '}';
    }
}
