package com.target.targetreadyresultsservice.Dto;

public class ResultsDto {
    public String resultsCode;

    public String studentId;

    public String scheduleCode;

    public String markList;

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

    public String getMarkList() {
        return markList;
    }

    public void setMarkList(String markList) {
        this.markList = markList;
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

    public ResultsDto(String resultsCode, String studentId, String scheduleCode, String markList, Double totalMarks, Double totalPercentage) {
        this.resultsCode = resultsCode;
        this.studentId = studentId;
        this.scheduleCode = scheduleCode;
        this.markList = markList;
        this.totalMarks = totalMarks;
        this.totalPercentage = totalPercentage;


    }
}
