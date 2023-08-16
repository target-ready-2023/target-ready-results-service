package com.target.targetreadyresultsservice.model;

public class Marks {
    private String subjectCode;
    private double internalMarks;
    private double externalMarks;

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public double getInternalMarks() {
        return internalMarks;
    }

    public void setInternalMarks(double internalMarks) {
        this.internalMarks = internalMarks;
    }

    public double getExternalMarks() {
        return externalMarks;
    }

    public void setExternalMarks(double externalMarks) {
        this.externalMarks = externalMarks;
    }

    public Marks(String subjectCode, double internalMarks, double externalMarks) {
        this.subjectCode = subjectCode;
        this.internalMarks = internalMarks;
        this.externalMarks = externalMarks;
    }

    @Override
    public String toString() {
        return "Marks{" +
                "subjectCode='" + subjectCode + '\'' +
                ", internalMarks=" + internalMarks +
                ", externalMarks=" + externalMarks +
                '}';
    }
}

