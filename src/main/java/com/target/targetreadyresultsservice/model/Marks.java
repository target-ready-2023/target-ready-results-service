package com.target.targetreadyresultsservice.model;

public class Marks {
    private String subjectCode;
    private int internalMarks;
    private int externalMarks;

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public int getInternalMarks() {
        return internalMarks;
    }

    public void setInternalMarks(int internalMarks) {
        this.internalMarks = internalMarks;
    }

    public int getExternalMarks() {
        return externalMarks;
    }

    public void setExternalMarks(int externalMarks) {
        this.externalMarks = externalMarks;
    }

    public Marks(String subjectCode, int internalMarks, int externalMarks) {
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

