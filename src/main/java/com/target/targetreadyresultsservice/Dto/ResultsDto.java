package com.target.targetreadyresultsservice.Dto;

public class ResultsDto {
    public String resultsCode;

    public String stuentId;

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

    public String getStuentId() {
        return stuentId;
    }

    public void setStuentId(String stuentId) {
        this.stuentId = stuentId;
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

    public ResultsDto(String resultsCode, String stuentId, String scheduleCode, String markList, Double totalMarks, Double totalPercentage) {
        this.resultsCode = resultsCode;
        this.stuentId = stuentId;
        this.scheduleCode = scheduleCode;
        this.markList = markList;
        this.totalMarks = totalMarks;
        this.totalPercentage = totalPercentage;


    }
}
