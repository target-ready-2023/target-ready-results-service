package com.target.targetreadyresultsservice.model;

public class Marks {
    private String subjectCode;
    private float internalMarks;
    private float externalMarks;

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public float getInternalMarks() {
        return internalMarks;
    }

    public void setInternalMarks(float internalMarks) {
        this.internalMarks = internalMarks;
    }

    public float getExternalMarks() {
        return externalMarks;
    }

    public void setExternalMarks(float externalMarks) {
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

